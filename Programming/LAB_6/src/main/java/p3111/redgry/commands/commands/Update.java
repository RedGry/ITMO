package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
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
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length == argumentsCount){
            long key;
            try {
                key = Long.parseLong(args[0]);
            } catch (ClassCastException e){
                CommandsManager.getInstance().printToClient("Команде был указан аргумент неправильного формата!");
                return null;
            }
            if (!ss.checkKey(key)) {
                Person person = ss.returnPerson();
                ss.update(key, person);
                CommandsManager.getInstance().printToClient("Персонаж с ключом: " + key + " успешно обновлен.");
            }else {
                CommandsManager.getInstance().printToClient("Данного персонажа вы не можете обновить т.к. его нет в коллекции.");
            }
            return null;
        }
        CommandsManager.getInstance().printToClient("Команде не был указан агрумент!");
        //logger.warn("Команда не принимает аргументы");
        return null;
    }
}
