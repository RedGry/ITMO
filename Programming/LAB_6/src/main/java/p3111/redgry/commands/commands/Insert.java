package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
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
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length == argumentsCount){
            long key;
            try {
                key = Long.parseLong(args[0]);
            } catch (ClassCastException e){
                CommandsManager.getInstance().printToClient("Команде был указан аргумент неправильного формата!");
                return null;
            }
            if (ss.checkKey(key)){
                Person person = ss.returnPerson();
                ss.add(person, key);
                CommandsManager.getInstance().printToClient( "Персонаж с ключом: " + key + " успешно добавлен!");
            }
            else {
                CommandsManager.getInstance().printToClient("Персонаж уже существует с данным ключом.");
            }
            return null;
        }
        //logger.warn("Команда не были переданы аргументы");
        CommandsManager.getInstance().printToClient("Команде не был указан агрумент!");
        return null;
    }
}
