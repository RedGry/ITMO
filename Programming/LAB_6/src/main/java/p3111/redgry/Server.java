package p3111.redgry;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StackPersonStorage;
import p3111.redgry.collection.helpers.StackPersonStorageService;
import p3111.redgry.collection.helpers.Storage;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.utils.CsvReader;
import p3111.redgry.utils.Serialization;
import p3111.redgry.utils.UserInterface;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;

public class Server implements Runnable{
    //private static final Logger logger = LoggerFactory.getLogger(Server.class.getName());

    Selector selector;
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private AbstractCommand command = null;
    private String[] args;
    private static final int port = 3292;
    UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
    Storage<Long, Person> storage = new StackPersonStorage();
    StorageService storageService = new StackPersonStorageService(storage);

    public static void main(String[] args){
        args = new String[]{"C:/Users/Egor/Desktop/convertcsv.csv", ";"};
        try {
            Server server = new Server();
            server.setArguments(args);
            //logger.info("Запускаем сервер по порту " + port);
            server.run();
        } catch (IllegalStateException e){
            System.exit(-1);
        }
    }

    private void receive() throws IOException{
        StringBuilder sb = new StringBuilder();
        ByteBuffer byteBuffer = ByteBuffer.allocate(100000000);
        byteBuffer.clear();
        socketAddress = datagramChannel.receive(byteBuffer);
        byteBuffer.flip();
        SocketAddress socket = socketAddress;
        DatagramChannel d = datagramChannel;

        if (socketAddress != null) {
            String s = null;
            Object o = new Serialization().DeserializeObject(byteBuffer.array());
            if (o == null){
                //logger.info("Получена несуществующая команда");
                datagramChannel.send(ByteBuffer.wrap("Команда не найдена или имеент неверное количество аргументов. Для просмотра доступных команд введите help".getBytes()), socketAddress);
                datagramChannel.send(ByteBuffer.wrap("I'm fucking seriously, it's fucking EOF!!!".getBytes()), socketAddress);
                return;
            }
            if (!o.getClass().getName().contains(".Person")){
                socketAddress = datagramChannel.receive(byteBuffer);
                if (socketAddress != null)
                s = new Serialization().DeserializeObject(byteBuffer.array());

                command = (AbstractCommand) o;
                //logger.info("Сервер получил команду: " + command.getCommand());
                if (s != null) {
                    sb.append(command.getCommand()).append(" ").append(s);
                    //logger.info("Команде был передан аргумент: " + s);
                } else sb.append(command.getCommand());

                if (!command.isNeedObjectToExecute() && s == null){
                //   logger.info("Команда не требует обьекта для выполнения. Начинаем выполнение.");
                    CommandsManager.getInstance().executeCommand(userInterface, storageService, sb.toString(), d, socket);
                }else if (!command.isNeedObjectToExecute() && s != null){
                //   logger.info("Команда не требует обьекта для выполнения, были переданы аргументы. Начинаем выполнение.");
                    CommandsManager.getInstance().executeCommand(userInterface, storageService, sb.toString(), d, socket);
                }
            }else if (command != null){
                Person person = new Person((Person) o);
                command.setPerson(person, (StackPersonStorageService) storageService);

                socketAddress = datagramChannel.receive(byteBuffer);

                if (socketAddress != null)
                    s = new Serialization().DeserializeObject(byteBuffer.array());

                if (s != null) {
                    sb.append(command.getCommand()).append(" ").append(s);
                } else sb.append(command.getCommand());
                // logger.info("Получен обьект {}, необходимый команде. Начинаем выполнение.", person);
                CommandsManager.getInstance().executeCommand(userInterface, storageService, sb.toString(), d, socket);
            }
        }else{
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Thread.sleep(2);
                    try {
                        //logger.info("Выполнение команды save.");
                        CommandsManager.getInstance().executeCommand(userInterface, storageService, "exit");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (InterruptedException | IllegalStateException | ArrayIndexOutOfBoundsException e) {
                    //logger.error("Было вызвано прерывание процесса.");
                    System.exit(-1);
                }
            }));
        }
    }

    public void setArguments(String[] arguments){
        this.args = arguments;
    }


    @Override
    public void run() {
        //String FilePath = System.getenv("WORK_FILE_PATH");
        FileInputStream f = null;
        boolean False = true;
        String delimiter = "";

        if (args.length < 1 || args.length > 2){
            //logger.error("Неверное количество аргументов: {}, ожидалось: 1 или 2 ", args.length);
            System.exit(-1);
        }
        if (args.length == 1){
            args = new String[]{args[0], ";"};
            args = args;
            //logger.info("Был принят один аргумент, стандартным разделителем выбран: ';'");
        }
        if(args.length == 2 && (!args[1].equals(";") && !args[1].equals(","))){
            //logger.error("2 аргумент может быть только: ',' или ';'. Был указан: {}", args[0]);
            System.exit(-1);
        }
        String FilePath = args[0];
        delimiter = args[1];

        if (FilePath == null){
            //logger.error("Не был указан путь к файлу с коллекцией.");
            System.out.println("Вы не задали значение переменной окружения, коллекция не будет загруженна и не будет сохранена" + "\n");
            f = null;
        } else {
            try {
                File workFile = new File(FilePath);
                f = new FileInputStream(workFile);
            } catch (FileNotFoundException e){
                System.out.println("Файл не найден или не хватает прав для его чтения, коллекция не будет загруженна в программу, но вы сможете ее сохранить, если есть права на запись, если файла не существует, он будет создан");
            }
        }
        if (f != null){
            boolean success = false;
            try {
                CsvReader.InputLoader(FilePath, storage.getPersons(), delimiter);
                //logger.info("Коллекция загружена.");
                if (storage.size() != 0) success = true;
            } catch (ClassCastException e){
                userInterface.writeln("Ошибка приведения типов.");
                e.getMessage();
            } catch (Exception e){
                userInterface.writeln("Автор не заметил данной ошибки при тесте -_-");
                e.printStackTrace();
            } finally {
                if (!success){
                    userInterface.writeln("Что-то пошло не так при инициализации коллекции. Теперь вы работаете с пустой коллекцией." );
                }
            }
        }
        socketAddress = new InetSocketAddress("localhost", port);
        try {
            selector = Selector.open();
            datagramChannel = DatagramChannel.open();
            datagramChannel.bind(socketAddress);
            datagramChannel.configureBlocking(false);
            datagramChannel.register(selector, datagramChannel.validOps());
            //logger.info("Канал открыт и готов для приема сообщений.");
            while (true){
                receive();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                CommandsManager.getInstance().executeCommand(userInterface, storageService, "save");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
