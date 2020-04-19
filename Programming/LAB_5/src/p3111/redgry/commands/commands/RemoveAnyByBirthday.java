package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class RemoveAnyByBirthday extends AbstractCommand {

    public RemoveAnyByBirthday(){
        command = "remove_any_by_birthday";
        helpText = "Удалить из коллекции один элемент, значение поля birthday которого эквивалентно заданному."  + ANSI_GREEN + " (Формат даты: YYYY-MM-DD)" + ANSI_RESET;
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length < 1) {
            throw new InvalidInputException("А где аргументы?" + "\nФормат даты: YYYY-MM-DD");
        }
        String date;
        try {
            date = args[0];
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Need numerical argument");
        }
        if(ss.removeAnyByBirthday(date)){
            userInterface.writeln("Из коллекции были удалены персонажи с датой рождения: " + date);
        }
        else userInterface.writeln("В коллекции нет персонажей с данной датой рождения.");

        return null;
    }
}
