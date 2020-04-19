package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Update extends AbstractCommand {

    public Update(){
        command = "update";
        helpText = "Обновить значение элемента коллекции, id которого равен заданному.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length < 1){
            throw new InvalidInputException("Введено " + args.length + " аргументов, ожидалось " + argumentsCount);
        }
        long key;
        try {
            key = Long.parseLong(args[0]);
        } catch (ClassCastException e){
            e.printStackTrace();
            return null;
        }
        if (!ss.checkKey(key)) {
            Person person = userInterface.readPerson();
            ss.update(key, person);
        }else {
            userInterface.writeln("Данного персонажа вы не можете обновить т.к. его нет в коллекции.");
        }
        return null;
    }
}
