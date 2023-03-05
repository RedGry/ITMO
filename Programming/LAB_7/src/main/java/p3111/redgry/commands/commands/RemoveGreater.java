package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
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
                try {
                    id = Long.parseLong(args[0]);
                } catch (ClassCastException e) {
                    CommandsManager.getInstance().printToClient("Команде был указан аргумент неправильного формата!");
                    return null;
                }
                if (ss.checkKey(id)) {
                    getPerson().setId(id);
                    dataBaseManager.addToDataBase(getPerson());
                    if (ss.removeGreaterKey(id) != null){
                        ss.removeGreaterKey(id).forEach(keys -> {
                            if (dataBaseManager.removeFromDataBase(ss.list().get(Long.parseLong(keys)))){
                                CommandsManager.getInstance().printToClient("Персонаж с ключем: " + keys + " успешно удален!");
                            }
                        });
                        CommandsManager.getInstance().printToClient("Персонажи превышающие ключ: " + id + " и пренадлежащие вам успешно удалены!");
                    } else {
                        CommandsManager.getInstance().printToClient("В коллекции нет персонажей превышающих ключ: " + id + ".");
                    }
                    dataBaseManager.updateCollectionFromDataBase(ss);
                } else CommandsManager.getInstance().printToClient("Персонаж уже существует с данным ID.");
                return null;
            } finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        //logger.warn("Команда не принимает аргументы");
        CommandsManager.getInstance().printToClient("Команда не принимает агрументы!");
        return null;
    }
}
