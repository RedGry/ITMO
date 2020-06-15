package p3111.redgry;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helps.StackPersonStorage;
import p3111.redgry.collection.helps.StackPersonStorageService;
import p3111.redgry.collection.helps.Storage;
import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.commands.commands.Help;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.Handlers.ServerMessagesHandler;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable{
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private DataBaseManager dataBaseManager;
    private static final int port = 3292;

    UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
    Storage<Long, Person> storage = new StackPersonStorage();
    StorageService storageService = new StackPersonStorageService(storage);

    public Server(DataBaseManager dataBaseManager){
        this.dataBaseManager = dataBaseManager;
    }

    public static void main(String[] args){
        Server server = new Server(new DataBaseManager());
        new Thread(server).start();
    }

    private void receive() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(2000);
        byteBuffer.clear();
        socketAddress = datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        SocketAddress socket = socketAddress;
        DatagramChannel d = datagramChannel;
        if (socketAddress != null && !new String(byteBuffer.array()).trim().isEmpty()){
            ServerMessagesHandler messagesHandler = new ServerMessagesHandler(d, socket, dataBaseManager, storageService, byteBuffer);
            messagesHandler.start();
        }
    }

    @Override
    public void run() {
        dataBaseManager.updateCollectionFromDataBase(storageService);
        socketAddress = new InetSocketAddress("localhost", port);
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(socketAddress);
            datagramChannel.configureBlocking(false);
            while (true){
                receive();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
