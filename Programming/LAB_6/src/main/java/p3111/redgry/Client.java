package p3111.redgry;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.exceptions.NoSuchCommandException;
import p3111.redgry.utils.Serialization;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class Client implements Runnable{
    private final DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private final Selector selector;
    UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

    public Client() throws IOException{
        selector = Selector.open();
        datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
    }

    public static void main(String[] args){
        try {
            Client client = new Client();
            client.connect("localhost", 3292);
            client.run();
        } catch (IOException | UnresolvedAddressException e) {
            UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
            if (userInterface.readWithMessage("Хотите попробывать снова подключиться? Введите: Да/Нет.", false).equals("Да")){
                try {
                    Client client = new Client();
                    client.connect("localhost", 3292);
                    client.run();
                } catch (IOException | UnresolvedAddressException ioException) {
                    System.exit(-1);
                }
            } else {System.exit(-1);}
        }
    }

    private void connect(String hostname, int port) throws IOException{
        socketAddress = new InetSocketAddress(hostname, port);
        datagramChannel.connect(socketAddress);
        System.out.println("Устанавливаем соединение с " + hostname + " по порту " + port);
    }

    private String receiveAnswer() throws IOException {
        byte[] bytes = new byte[1000000];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        socketAddress = datagramChannel.receive(buffer);
        return new String(buffer.array()).split("�")[0].trim();
    }

    private void sendCommand(AbstractCommand command) throws IOException{
        if (command != null){
            if (command.getCommand().contains("exit")) {
                System.out.println("Удачи!");
                System.exit(0);
            }
        }
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
        datagramChannel.send(buffer, socketAddress);
    }

    public UserInterface getUserInterface(){
        return userInterface;
    }

    private void sendCommand(AbstractCommand command, String parameter) throws IOException{
        if (command != null){
            if (command.getCommand().contains("exit")) {
                System.out.println("Удачи!");
                System.exit(0);
            }
        }
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
        ByteBuffer buffer1 = ByteBuffer.wrap(new Serialization().SerializeObject(parameter));
        datagramChannel.send(buffer, socketAddress);
        datagramChannel.send(buffer1, socketAddress);
    }

    private void sendPerson(Person person, String parameter) throws IOException{
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(person));
        ByteBuffer buffer1 = ByteBuffer.wrap(new Serialization().SerializeObject(parameter));
        datagramChannel.send(buffer, socketAddress);
        datagramChannel.send(buffer1, socketAddress);
    }

    private void sendPerson(Person person) throws IOException{
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(person));
        datagramChannel.send(buffer, socketAddress);
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);
            datagramChannel.register(selector, SelectionKey.OP_WRITE);
            while (selector.select() > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()){
                        String answer = receiveAnswer();
                        if (answer.contains("I'm fucking seriously, it's fucking EOF!!!")){
                            datagramChannel.register(selector, SelectionKey.OP_WRITE);
                        }else{
                            System.out.println(answer);
                        }
                    }
                    if (selectionKey.isWritable()){
                        datagramChannel.register(selector, SelectionKey.OP_READ);
                        try {
                            String[] strings = scanner.nextLine().trim().split("\\s+");
                            AbstractCommand command = CommandsManager.getInstance().CommandDeterminator(strings);
                            if (strings.length == 1) {
                                sendCommand(command);
                            } else {
                                sendCommand(command, strings[1]);
                            }
                            if (command != null && command.isNeedObjectToExecute() && !command.getCommand().equals("remove_greater") && strings.length > 1){
                                sendPerson(CommandsManager.getInstance().GetPerson(userInterface, Long.parseLong(strings[1])), strings[1]);
                            } else if (command != null && command.isNeedObjectToExecute()){
                                strings[0] = userInterface.readWithMessage("Введите ключ: ", false);
                                sendPerson(CommandsManager.getInstance().GetPerson(userInterface, Long.parseLong(strings[0])), strings[0]);
                            }
                        }catch (NoSuchCommandException e){
                            System.out.println("Данной команды не существует!");
                            run();
                        }catch (NumberFormatException e){
                            System.out.println("Неправильный формат аргумента");
                            run();
                        }
                    }
                }
            }
        } catch (PortUnreachableException e) {
            System.out.println("Не удалось получить данные по указанному порту/сервер не доступен");
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
