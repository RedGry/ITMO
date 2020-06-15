package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
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
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (args.length == argumentsCount) {
            try {
                CommandsManager.getInstance().getLock().lock();
                long key;
                try {
                    key = Long.parseLong(args[0]);
                } catch (NumberFormatException e) {
                    CommandsManager.getInstance().printToClient("error_arg");
                    return null;
                }
                Person person = ss.list().get(key);
                if (dataBaseManager.removeFromDataBase(person)){
                    dataBaseManager.updateCollectionFromDataBase(ss);
                    CommandsManager.getInstance().printToClient("suc_remove" + "_" + key);
                } else{
                    CommandsManager.getInstance().printToClient("err_remove" + "_" + key);
                }
                dataBaseManager.updateCollectionFromDataBase(ss);
                CommandsManager.getInstance().sendCollectionToClient(ss.list());
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
