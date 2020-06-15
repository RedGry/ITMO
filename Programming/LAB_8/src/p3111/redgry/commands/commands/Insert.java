package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class Insert extends AbstractCommand{

    public Insert(){
        command = "insert";
        helpText = "Добавить новый элемент с заданным ключом.";
        needObjectToExecute = true;
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
                } catch (ClassCastException e) {
                    CommandsManager.getInstance().printToClient("error_arg");
                    return null;
                }
                if (ss.checkKey(key)) {
                    if (dataBaseManager.addToDataBase(getPerson())) {
                        dataBaseManager.updateCollectionFromDataBase(ss);
                    }
                    //ss.add(getPerson(), key);
                    CommandsManager.getInstance().printToClient("suc_insert");
                    CommandsManager.getInstance().sendCollectionToClient(ss.list());
                } else {
                    CommandsManager.getInstance().printToClient("err_insert");
                }
                return null;
            } finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
