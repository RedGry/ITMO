package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.utils.CollectionUtils;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class RemoveGreater extends AbstractCommand {

    public RemoveGreater(){
        command = "remove_greater";
        helpText = "Удалить из коллекции все элементы, превышающие заданный.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        long key = Long.parseLong(userInterface.readWithMessage("Введите Key: ",false));
        if (ss.checkKey(key)){
            Person person = CollectionUtils.readPerson(userInterface, key);
            ss.add(person, key);
            userInterface.writeln("Персонаж успешно добавлен!");
            if (ss.removeGreaterKey(key)) {
                userInterface.writeln("Персонажи превышающие key " + key + " успешно удалены!");
            } else userInterface.writeln("Коллекция пуста( Заполни её, а потом попробуй снова.");
        }
        else userInterface.writeln("Персонаж уже существует с данным ключом.");
        return null;
    }
}
