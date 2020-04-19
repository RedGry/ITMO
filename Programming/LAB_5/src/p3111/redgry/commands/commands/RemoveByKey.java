package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidInputException;
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
        if (args.length < 1) {
            throw new InvalidInputException("А где аргументы?");
        }
        long key;
        try {
            key = Long.parseLong(args[0]);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Need numerical argument");
        }
        if (ss.removeByKey(key)) {
            userInterface.writeln("Персонаж с key " + key + " успешно удален!");
        } else {
            userInterface.writeln("Персонаж с key " + key + " не найден.");
        }
        return null;
    }
}
