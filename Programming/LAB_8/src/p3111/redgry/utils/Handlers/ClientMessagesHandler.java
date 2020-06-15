package p3111.redgry.utils.Handlers;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.utils.Serialization;
import p3111.redgry.utils.UserInterface;
import p3111.redgry.utils.controllers.MainController;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ClientMessagesHandler extends Thread{
    MainController mainController;
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private Selector selector;
    private String login = "";
    private String password = "";
    private boolean authorized = false;
    UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
    private PriorityQueue<Person> clientPq = new PriorityQueue<>();
    private List<Person> clientList = new LinkedList<>();

    public ClientMessagesHandler(MainController mainController){
        this.mainController = mainController;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void connect(String hostname, int port) throws IOException {
        socketAddress = new InetSocketAddress(hostname, port);
        datagramChannel.connect(socketAddress);
    }

    public String receiveAnswer() throws IOException {
        ByteBuffer byteBuffer0 = ByteBuffer.allocate(10000);
        socketAddress = datagramChannel.receive(byteBuffer0);
        String answer = new String(byteBuffer0.array()).trim();
        if (answer.equals("CollAllrt".trim())) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(100000);
            while (new String(byteBuffer.array()).trim().isEmpty()) {
                datagramChannel.receive(byteBuffer);
            }
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                int size = objectInputStream.readInt();
                Person o;
                clientPq.clear();
                for (int i = 0; i < size; i++) {
                    o = (Person) objectInputStream.readObject();
                    if (o == null) break;
                    clientPq.add(o);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "collectionReceived";
        }else return decodeAnswer(answer);
    }

    private String decodeAnswer(String answer){
        if (answer.isEmpty()) return  "";
        if (answer.contains("I'm fucking seriously, it's fucking EOF!!!")) return answer;
        if (answer.contains("suc_login") || answer.contains("suc_register")) return answer;
        StringBuilder arg = new StringBuilder();
        String[] strings = answer.trim().split("_");
        if(strings.length >= 3) {
            for (int i = 2; i < strings.length; i++) {
                arg.append(strings[i]);
            }
        }
        String command = strings[0] + "_" + strings[1];
        //String command = answer; //strings[0] + "_" + strings[1];
        String normalAnswer = mainController.getCurrentBundle().getString(command);
        //String normalAnswer = command; //mainController.getCurrentBundle().getString(command);
        return normalAnswer + arg;
    }

    public void sendCommand(AbstractCommand command) throws IOException{
        ByteBuffer byteBuffer = ByteBuffer.wrap(Objects.requireNonNull(Serialization.SerializeObject(command, login, password)));
        datagramChannel.send(byteBuffer, socketAddress);
        if (command.getClass().getName().contains("Exit")){
            System.exit(0);
        }
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public PriorityQueue<Person> getClientPq() {
        return clientPq;
    }
    public List<Person> getClientList(){return clientList;}
}
