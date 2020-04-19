package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class RemoveGreaterKey extends AbstractCommand {

    public RemoveGreaterKey(){
        command = "remove_greater_key";
        helpText = "Удалить из коллекции все элементы, ключ которых превышает заданный.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length < 1) {
            throw new InvalidInputException("А где аргументы?");
        }
        long key;
        try {
            key = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Need numerical argument");
        }
        if (ss.removeGreaterKey(key)) {
            userInterface.writeln("Персонажи превышающие key " + key + " успешно удалены!");
        } else userInterface.writeln("Коллекция пуста( Заполни её, а потом попробуй снова.");
        return null;
    }
}
