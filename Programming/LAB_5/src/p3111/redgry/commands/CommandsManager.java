package p3111.redgry.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.commands.*;
import p3111.redgry.exceptions.NoSuchCommandException;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Синглтон, который управляет командами. Хранит в себе реестр всех команд.
 * Через него происходит выполнение команды по строке пользователя.
 */
public class CommandsManager {
    public static CommandsManager instance;
    private Map<String, AbstractCommand> commands = new HashMap<>();
    static ArrayList<String> commandsList = new ArrayList<>();

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
        addCommand(new Save());
        addCommand(new Show());
        addCommand(new Update());

    }

    /**
     * Добавляет команды в реестр.
     * @param cmd обьект команды.
     */
    private void addCommand(AbstractCommand cmd) {
        commands.put(cmd.getCommand(), cmd);
    }

    /**
     * Получает команду из реестра.
     *
     * @param s строка, соответствующая команде.
     * @return обьект команды.
     * @throws NoSuchCommandException в случае, если команды соответсвующей s нет в реестре.
     */
    public AbstractCommand getCommand(String s) throws NoSuchCommandException {
        if (!commands.containsKey(s)){
            throw new NoSuchCommandException();
        }
        return commands.getOrDefault(s, null);
    }

    /**
     * Метод, который выполняет команду, по строке пользователя.
     *
     * @param userInterface  интерфейс обмена данными между пользователем и программой.
     * @param storageService сервис, управляющий коллекцией.
     * @param s              строка, введенная пользователем.
     * @throws IOException Пробрасывается от команды, в случае если команда работает с I/O и произошла ошибка.
     */
    public void executeCommand(UserInterface userInterface, StorageService storageService, String s) throws IOException{
        commandsList.add(s);
        String[] parsedCommand = parseCommand(s);
        AbstractCommand command = getCommand(parsedCommand[0]);
        String[] args = Arrays.copyOfRange(parsedCommand, 1, parsedCommand.length);
        command.execute(userInterface, storageService, args);
    }

    /**
     * Разбивает команду на лексемы (единица слова).
     *
     * @param line строка, введеная пользователем.
     * @return Массив лексем.
     */
    public String[] parseCommand(String line){
        return line.split(" ");
    }

    /**
     * Возвращает все команды из реекстра.
     *
     * @return Список всех команд.
     */
    public List<AbstractCommand> getAllCommands(){
        return commands.keySet().stream().map(x -> (commands.get(x))).collect(Collectors.toList());
    }

    /**
     * Метод, возращающий массив введенных команд пользователем.
     *
     * @return массив команд.
     */
    public static ArrayList<String> getCommandsList(){
        return commandsList;
    }

    /**
     * Метод, которы выводит последние 15 команд без учета введеных аргументов.
     */
    public static void cmdList(){
        if (getCommandsList().size() - 1 < 15){
            int count = 1;
            for (int i = getCommandsList().size() - 1; i != -1; i--) {
                System.out.print(count + ": " + getCommandsList().get(getCommandsList().size() - 1 - i).split(" ")[0] + "\n");
                count++;
            }
        }else {
            int count = 15;
            for (int i = 0; i < 15; i++) {
                System.out.print(count + ": " + getCommandsList().get(getCommandsList().size() - 1 - i).split(" ")[0] + "\n");
                --count;
            }
        }
    }
}
