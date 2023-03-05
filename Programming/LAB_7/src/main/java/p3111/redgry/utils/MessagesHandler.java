package p3111.redgry.utils;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.exceptions.InvalidCountOfArgumentsException;

import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

public class MessagesHandler extends Thread{
    UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private DataBaseManager dataBaseManager;
    private StorageService storageService;
    private ByteBuffer byteBuffer;
    private static String s = null;
    private static AbstractCommand command = null;

    public MessagesHandler(DatagramChannel datagramChannel, SocketAddress socketAddress, DataBaseManager dataBaseManager, StorageService storageService, ByteBuffer byteBuffer){
        this.datagramChannel = datagramChannel;
        this.socketAddress = socketAddress;
        this.dataBaseManager = dataBaseManager;
        this.storageService = storageService;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void run(){
        String login;
        String password;
        Object o;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            login = objectInputStream.readUTF();
            password = objectInputStream.readUTF();
            o = objectInputStream.readObject();
            if (o == null){
                datagramChannel.send(ByteBuffer.wrap("Команда не найдена или имеент неверное количество аргументов. Для просмотра доступных команд введите help".getBytes()), socketAddress);
                datagramChannel.send(ByteBuffer.wrap("I'm fucking seriously, it's fucking EOF!!!".getBytes()), socketAddress);
            } else {
                if (o.getClass().getName().contains(".Login")) authorization("login", login, password);
                else if (o.getClass().getName().contains(".Register")) authorization("register", login, password);
                else if (!o.getClass().getName().contains(".Person")){
                    if (o.getClass().getName().contains(".commands.")) {
                        command = (AbstractCommand) o;
                        dataBaseManager.setUSER(login);
                        dataBaseManager.setPASSWORD(password);
                        CommandsManager.getInstance().executeCommand(userInterface, storageService, command, datagramChannel, socketAddress, dataBaseManager);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | InvalidCountOfArgumentsException e) {
            e.printStackTrace();
        }
    }

    private void authorization(String message, String login, String password) throws IOException{
        if (message.equals("login")){
            if(dataBaseManager.login(login, password)){
                datagramChannel.send(ByteBuffer.wrap("Пользователь успешно вошёл в систему.".getBytes()), socketAddress);
            } else {
                datagramChannel.send(ByteBuffer.wrap("Не удалось войти в систему.".getBytes()), socketAddress);
            }
        }
        if (message.equals("register")){
            if (dataBaseManager.addUser(login, password)){
                datagramChannel.send(ByteBuffer.wrap("Пользователь добавлен. Войдите в систему.".getBytes()), socketAddress);
            } else {
                datagramChannel.send(ByteBuffer.wrap("Не удалось добавить пользователя. Логин занят, содержит недопустимые символы или их последовательность. Мне наплевать, если честно, сами гуглите, какой логин и пароль захавает чёртов postgresql".getBytes()), socketAddress);
            }
        }
    }
}
