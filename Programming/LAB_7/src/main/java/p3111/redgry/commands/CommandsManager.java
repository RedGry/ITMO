package p3111.redgry.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.commands.*;
import p3111.redgry.exceptions.InvalidCountOfArgumentsException;
import p3111.redgry.exceptions.NoSuchCommandException;
import p3111.redgry.utils.CollectionUtils;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Синглтон, который управляет командами. Хранит в себе реестр всех команд.
 * Через него происходит выполнение команды по строке пользователя.
 */
public class CommandsManager {
    public static CommandsManager instance;
    private static final Map<String, AbstractCommand> commands = new HashMap<>();
    static ArrayList<String> commandsList = new ArrayList<>();
    private DatagramChannel serverDatagramChannel;
    private SocketAddress socketAddress;
    private String scriptFileName;
    private boolean isScript = false;
    private BufferedReader scriptBufferedReader;
    ReentrantLock lock = new ReentrantLock();

    public static CommandsManager getInstance(){
        if (instance == null){
            instance = new CommandsManager();
        }
        return instance;
    }

    /**
     * Стандартный конструктор, в котором добавляются все команды.
     */
    private CommandsManager(){
        addCommand(new Clear());
        addCommand(new CountLessThanLocation());
        addCommand(new ExecuteScript());
        addCommand(new Exit());
        addCommand(new Help());
        addCommand(new History());
        addCommand(new Info());
        addCommand(new Insert());
        addCommand(new PrintAscending());
        addCommand(new RemoveAnyByBirthday());
        addCommand(new RemoveByKey());
        addCommand(new RemoveGreater());
        addCommand(new RemoveGreaterKey());
        addCommand(new Show());
        addCommand(new Update());

    }

    private void addCommand(AbstractCommand cmd) {
        commands.put(cmd.getCommand(), cmd);
    }

    public AbstractCommand getCommand(String s) throws NoSuchCommandException {
        if (!commands.containsKey(s)){
            throw new NoSuchCommandException();
        }
        return commands.getOrDefault(s, null);
    }

    public void executeCommand(UserInterface userInterface, StorageService storageService, String s) throws IOException{
        commandsList.add(s);
        String[] parsedCommand = parseCommand(s);
        AbstractCommand command = getCommand(parsedCommand[0]);
        String[] args = Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length);
        //logger.info("Выполнение команды сервера.");
        //command.execute(userInterface, storageService, args);
    }

    public void executeCommand(UserInterface userInterface, StorageService storageService, AbstractCommand command, DatagramChannel datagramChannel, SocketAddress socketAddress, DataBaseManager dataBaseManager) throws IOException, InvalidCountOfArgumentsException {

        CommandsManager.getInstance().setServerDatagramChannel(datagramChannel);
        CommandsManager.getInstance().setSocketAddress(socketAddress);

        //logger.info("Выполнение команды пользователя.");
        command.execute(userInterface, storageService, command.getArgs(), dataBaseManager);
        int i = 0;
        while (i < 1000000){
            i++;
        }
        if (!CommandsManager.getInstance().isScript){
            //logger.info("Отправка пользователю сообщение о завершении чтения.");
            datagramChannel.send(ByteBuffer.wrap("I'm fucking seriously, it's fucking EOF!!!".getBytes()), socketAddress);
        }
    }

    public String[] parseCommand(String line){
        return line.split(" ");
    }

    public List<AbstractCommand> getAllCommands(){
        return commands.keySet().stream().map(x -> (commands.get(x))).collect(Collectors.toList());
    }

    public static ArrayList<String> getCommandsList(){
        return commandsList;
    }

    public static void cmdList(){
        StringBuilder sb = new StringBuilder();
        if (getCommandsList().size() - 1 < 15){
            int count = 1;
            for (int i = getCommandsList().size() - 1; i != -1; i--) {
                sb.append(count + ": " + getCommandsList().get(getCommandsList().size() - 1 - i).split(" ")[0] + "\n");
                count++;
            }
        }else {
            int count = 15;
            for (int i = 0; i < 15; i++) {
                sb.append(count + ": " + getCommandsList().get(getCommandsList().size() - 1 - i).split(" ")[0] + "\n");
                --count;
            }
        }
        CommandsManager.getInstance().printToClient(sb.toString());
    }

    public void setScript(boolean script) {
        isScript = script;
    }

    public boolean isScript() {
        return isScript;
    }

    public String getScriptFileName() {
        return scriptFileName;
    }

    public void setScriptFileName(String scriptFileName) {
        this.scriptFileName = scriptFileName;
    }

    public BufferedReader getScriptBufferedReader() {
        return scriptBufferedReader;
    }

    public void setScriptBufferedReader(BufferedReader scriptBufferedReader) {
        this.scriptBufferedReader = scriptBufferedReader;
    }

    public void setServerDatagramChannel(DatagramChannel datagramChannel) {
        serverDatagramChannel = datagramChannel;
    }

    public DatagramChannel getServerDatagramChannel() {
        return serverDatagramChannel;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    public SocketAddress getSocketAddress() {
        return socketAddress;
    }

    public void printToClient(String line) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap((line.getBytes()));
            CommandsManager.getInstance().getServerDatagramChannel().send(buffer, CommandsManager.getInstance().getSocketAddress());
            //logger.info("Отправляем ответ клиенту: {} ", new String(buffer.array()));
        } catch (IOException e) {
            //logger.info("Не удалось отправить ответ клиенту {}", e.getMessage());
        }
    }

    public static AbstractCommand CommandDeterminator(String[] args){
        try {
            String cmd = args[0].trim();
            args = Arrays.copyOfRange(args, 1, args.length);
            if (cmd.trim().equals("login") || cmd.trim().equals("register")){
                System.out.println("Вы уже вошли, для повторного входа или регистрации закончите сеанс с помощью команды exit");
                return null;
            }
            AbstractCommand command = commands.get(cmd.trim());
            command.setArgs(args);
            return command;
            /**
            for (AbstractCommand command : commands.values()) {
                if (command.getCommand().equals(cmd.trim())) {
                    command.setArgs(args);
                    return command;
                }
            }
             */
        } catch (Exception e){
            System.out.println("");
        }
        return null;
    }

    public Person GetPerson(UserInterface userInterface, Long key) throws IOException {
        Person person = CollectionUtils.ArgumentsReader(userInterface, key);
        return person;
    }

    public ReentrantLock getLock(){
        return lock;
    }
}
