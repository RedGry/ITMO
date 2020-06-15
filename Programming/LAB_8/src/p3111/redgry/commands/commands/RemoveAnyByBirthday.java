package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class RemoveAnyByBirthday extends AbstractCommand {

    public RemoveAnyByBirthday(){
        command = "remove_any_by_birthday";
        helpText = "Удалить из коллекции один элемент, значение поля birthday которого эквивалентно заданному. (Формат даты: YYYY-MM-DD)";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (args.length == argumentsCount) {
            String date;
            StringBuilder sb = new StringBuilder();
            try {
                CommandsManager.getInstance().getLock().lock();
                try {
                    date = args[0];
                    LocalDate.parse(date).format(DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    CommandsManager.getInstance().printToClient("error_argBirthday");
                    return null;
                }
                if (ss.removeAnyByBirthday(date) != null) {
                    ss.removeAnyByBirthday(date).forEach(keys -> {
                        if (dataBaseManager.removeFromDataBase(ss.list().get(keys))) {
                            sb.append("ID:").append(" ").append(keys).append("\n");
                        }
                    });
                    dataBaseManager.updateCollectionFromDataBase(ss);
                    CommandsManager.getInstance().printToClient("suc_removeBirthday" + "_" + sb.toString());
                    CommandsManager.getInstance().sendCollectionToClient(ss.list());
                } else {
                    CommandsManager.getInstance().printToClient("err_removeBirthday");
                }
                return null;
            } finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        //logger.warn("Команда не были переданы аргументы");
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
