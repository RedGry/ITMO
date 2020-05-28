package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Update extends AbstractCommand {

    public Update(){
        command = "update";
        helpText = "Обновить значение элемента коллекции, id которого равен заданному.";
        needObjectToExecute = true;
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (args.length == argumentsCount){
            try {
                CommandsManager.getInstance().getLock().lock();
                long id;
                try {
                    id = Long.parseLong(args[0]);
                } catch (ClassCastException e) {
                    CommandsManager.getInstance().printToClient("Команде был указан аргумент неправильного формата!");
                    return null;
                }
                if (!ss.checkKey(id)) {
                    getPerson().setId(id);
                    if(dataBaseManager.removeFromDataBase(ss.list().get(id))){
                        dataBaseManager.updateElementInDataBase(getPerson());
                        dataBaseManager.updateCollectionFromDataBase(ss);
                        CommandsManager.getInstance().printToClient("Персонаж с ID: " + id + " успешно обновлен.");
                    } else{
                        CommandsManager.getInstance().printToClient("Данный персонаж с ID: " + id + " не пренадлежит вам!");
                    }
                    //ss.update(id, person);
                } else {
                    CommandsManager.getInstance().printToClient("Данного персонажа вы не можете обновить т.к. его нет в коллекции.");
                }
                return null;
            } finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        CommandsManager.getInstance().printToClient("Команде не был указан агрумент!");
        //logger.warn("Команда не принимает аргументы");
        return null;
    }
}
