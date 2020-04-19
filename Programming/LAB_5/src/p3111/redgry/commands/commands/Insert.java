package p3111.redgry.commands.commands;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.utils.CollectionUtils;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Insert extends AbstractCommand{

    public Insert(){
        command = "insert";
        helpText = "Добавить новый элемент с заданным ключом.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length < 1){
            throw new InvalidInputException("Необходимы агрументы");
        }
        long key = Long.parseLong(args [0]);
        if (ss.checkKey(key)){
            Person person = CollectionUtils.readPerson(userInterface, key);
            ss.add(person, key);
            userInterface.writeln("Персонаж успешно добавлен!");
        }
        else userInterface.writeln("Персонаж уже существует с данным ключом.");

        return null;
    }
}
