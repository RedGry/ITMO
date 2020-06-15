package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
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
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (args.length == argumentsCount) {
            long key;
            StringBuilder sb = new StringBuilder();
            try {
                key = Long.parseLong(args[0]);
            } catch (ClassCastException e) {
                CommandsManager.getInstance().printToClient("error_arg");
                return null;
            }
            if (ss.checkKey(key)) {
                if (ss.removeGreaterKey(key) != null) {
                    ss.removeGreaterKey(key).forEach(keys -> {
                        if (dataBaseManager.removeFromDataBase(ss.list().get(Long.parseLong(keys)))) {
                            sb.append("ID: ").append(keys).append("\n");
                        }
                        ;
                    });
                    dataBaseManager.updateCollectionFromDataBase(ss);
                    CommandsManager.getInstance().sendCollectionToClient(ss.list());
                    CommandsManager.getInstance().printToClient("suc_greater" + "_" + key);
                } else CommandsManager.getInstance().printToClient("error_greater" + "_" + key + ".");
            }else CommandsManager.getInstance().printToClient("err_greater");
            return null;
        }
        //logger.warn("Команда не были переданы аргументы");
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
