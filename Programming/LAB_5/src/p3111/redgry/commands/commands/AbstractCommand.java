package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Абстрактный класс для команд.
 */
public abstract class AbstractCommand {

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
}
