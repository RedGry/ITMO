package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class RemoveByKey extends AbstractCommand {

    public RemoveByKey(){
        command = "remove_key";
        helpText = "Удалить элемент из коллекции по его ключу.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length == argumentsCount) {
            long key;
            try {
                key = Long.parseLong(args[0]);
            } catch (NumberFormatException e) {
                CommandsManager.getInstance().printToClient("Неправильный формат аргумента");
                return null;
            }
            if (ss.removeByKey(key)) {
                CommandsManager.getInstance().printToClient( "Персонаж с key " + key + " успешно удален!");
            } else {
                CommandsManager.getInstance().printToClient( "Персонаж с key " + key + " не найден.");
            }
            return null;
        }
        //logger.warn("Команда не были переданы аргументы");
        CommandsManager.getInstance().printToClient("Команда ожидала аргументы.");
        return null;
    }
}
