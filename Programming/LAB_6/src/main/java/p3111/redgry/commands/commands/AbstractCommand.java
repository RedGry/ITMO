package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StackPersonStorageService;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidCountOfArgumentsException;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Абстрактный класс для команд.
 */
public abstract class AbstractCommand implements Serializable {
    //private static final Logger logger = LoggerFactory.getLogger(Server.class.getName());
    protected boolean needObjectToExecute = false;
    protected static String[] args;

    protected static final String ANSI_RESET = "\u001B[0m";
    protected static final String ANSI_BLACK = "\u001B[30m";
    protected static final String ANSI_RED = "\u001B[31m";
    protected static final String ANSI_GREEN = "\u001B[32m";
    protected static final String ANSI_YELLOW = "\u001B[33m";
    protected static final String ANSI_BLUE = "\u001B[34m";
    protected static final String ANSI_PURPLE = "\u001B[35m";
    protected static final String ANSI_CYAN = "\u001B[36m";
    protected static final String ANSI_WHITE = "\u001B[37m";

    protected String command;
    protected String helpText;
    protected int argumentsCount = 0;

    /**
     * Возвращает команду.
     *
     * @return команда.
     */
    public String getCommand(){
        return command;
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды.
     */
    public String getHelpText(){
        return helpText;
    }

    /**
     * Метод, который описывает логику команды.
     *
     * @param userInterface интерфейс, по которому производится обмен данными между пользователем и программой.
     * @param ss            сервис управления коллекцией.
     * @param args          агрументьы команды.
     * @throws IOException В случае, если команда работает с I/O и произошла ошибка.
     */
    public abstract ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException;{
    }

    public boolean isNeedObjectToExecute() {
        return needObjectToExecute;
    }

    public void setPerson(Person person, StackPersonStorageService storage) {
        storage.setPersons(person);
    }

    public void setArgs(String[] args) throws InvalidCountOfArgumentsException {
        AbstractCommand.args = args;
    }

    public static String[] getArgs(){
        return args;
    }
}
