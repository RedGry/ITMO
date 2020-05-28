package p3111.redgry;

import p3111.redgry.commands.CommandsManager;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.commands.commands.Login;
import p3111.redgry.commands.commands.Register;
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
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class Client implements Runnable{
    private final DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private final Selector selector;
    private String login = "";
    private String password = "";
    private boolean registered = false;
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
            while (true) {
                client.run();
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка ввода/вывода");
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

    private void authorization(Scanner scanner){
        try{
            String input;
            String answer = "";
            do{
                System.out.println("Если вы уже зарегестрированны, введите login для входа, иначе введите register");
                input = scanner.nextLine().trim().split("\\s+")[0];
            } while (!input.equals("register") & !input.equals("login"));
            if (input.equals("register")){
                register(scanner);
            } else login(scanner);
            while (answer.isEmpty()){
                answer = receiveAnswer();
                if (answer.equals("Пользователь успешно вошёл в систему.")){
                    registered = true;
                }
                System.out.println(answer);
            }
        }catch (Exception e){
            System.err.println("Ошиюка авторизации");
        }
    }

    private void register(Scanner scanner) throws IOException{
        login = "";
        boolean lessThen4 = true;
        boolean withSpaces = true;
        boolean invalidChars = true;
        do {
            System.out.println("Придумайте логин, содержащий не менее 4 символов (допускается использование только английских прописных букв и цифк)");
            login = scanner.nextLine();
            lessThen4 = login.trim().split("\\s+")[0].length() < 4;
            withSpaces = login.trim().split("\\s+").length != 1;
            invalidChars = !login.trim().split("\\s+")[0].matches("[a-z0-9]+");
        }while (lessThen4 || withSpaces || invalidChars);
        password = "";
        lessThen4 = true;
        withSpaces = true;
        invalidChars = true;
        do {
            System.out.println("Придумайте пароль, содержащий не менее 4 (допускается использование только английских прописных букв и цифр)");
            password = scanner.nextLine();
            lessThen4 = login.trim().split("\\s+")[0].length() < 4;
            withSpaces = login.trim().split("\\s+").length != 1;
            invalidChars = !login.trim().split("\\s+")[0].matches("[a-z0-9]+");
        } while (lessThen4 || withSpaces || invalidChars);
        System.out.println("Ваш логин: " + login.trim().split("\\s+")[0] + "\nВаш пароль: " + password.trim().split("\\s+")[0]);
        sendCommand(new Register());
    }

    private void login(Scanner scanner)throws IOException {
        System.out.println("Введите логин: ");
        login = scanner.nextLine();
        System.out.println("Введите пароль: ");
        password = scanner.nextLine();
        sendCommand(new Login());
    }

    public UserInterface getUserInterface(){
        return userInterface;
    }

    private void sendCommand(AbstractCommand command) throws IOException{
        if (command != null){
            if (command.getClass().getName().contains("Exit")) {
                System.out.println("Удачи!");
                System.exit(0);
            }
        }
        ByteBuffer buffer = ByteBuffer.wrap(Serialization.SerializeObject(command, login, password));
        datagramChannel.send(buffer, socketAddress);
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)){
            datagramChannel.register(selector, SelectionKey.OP_WRITE);
            if (!registered) {
                System.out.println("Для работы с коллекцие зарегистрируйтесь (register) или авторизуйтесь (login)");
            }
            while (!registered){
                authorization(scanner);
            }
            while (selector.select() > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                for (Iterator<SelectionKey> it = iterator; it.hasNext(); ){
                    SelectionKey selectionKey = it.next();
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
                            if (command != null && command.isNeedObjectToExecute()){
                                if ((command.getCommand().equals("insert") || command.getCommand().equals("remove_greater")) && command.getArgs().length == 0){
                                    command.setArgs(new String[]{userInterface.readWithMessage("Введите ключ: ", false)});
                                }
                                command.setPerson(CommandsManager.getInstance().GetPerson(userInterface, Long.parseLong(command.getArgs()[0])));
                            }
                            sendCommand(command);
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
            System.err.println("Не удалось получить данные по указанному порту/сервер не доступен");
            run();
        } catch (IOException e) {
            System.err.println("Ошибка ввода/вывода");
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("Произошла непредусмотренная ошибка");
        }
    }
}
