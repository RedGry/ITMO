package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class RemoveGreater extends AbstractCommand {

    public RemoveGreater(){
        command = "remove_greater";
        helpText = "Удалить из коллекции все элементы, превышающие заданный.";
        needObjectToExecute = true;
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if(args.length == argumentsCount) {
            try {
                CommandsManager.getInstance().getLock().lock();
                long id;
                StringBuilder sb = new StringBuilder();
                try {
                    id = Long.parseLong(args[0]);
                } catch (ClassCastException e) {
                    CommandsManager.getInstance().printToClient("error_arg");
                    return null;
                }
                if (ss.checkKey(id)) {
                    getPerson().setId(id);
                    dataBaseManager.addToDataBase(getPerson());
                    if (ss.removeGreaterKey(id) != null){
                        ss.removeGreaterKey(id).forEach(keys -> {
                            if (dataBaseManager.removeFromDataBase(ss.list().get(Long.parseLong(keys)))){
                                sb.append("ID: ").append(keys).append("\n");
                            }
                        });
                        CommandsManager.getInstance().printToClient("suc_greater" + "_" + id + "\n" + sb.toString());
                    } else {
                        CommandsManager.getInstance().printToClient("error_greater" + "_" + id + ".");
                    }
                    dataBaseManager.updateCollectionFromDataBase(ss);
                    CommandsManager.getInstance().sendCollectionToClient(ss.list());
                } else CommandsManager.getInstance().printToClient("err_greater");
                return null;
            } finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        //logger.warn("Команда не принимает аргументы");
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
