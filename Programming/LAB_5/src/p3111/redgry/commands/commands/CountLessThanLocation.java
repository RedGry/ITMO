package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class CountLessThanLocation extends AbstractCommand {

    public CountLessThanLocation(){
        command = "count_less_than_location";
        helpText = "Вывести количество элементов, значение поля location которых меньше заданного.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        double V;
        try{
            V = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Не был указан аргумент");
        }

        userInterface.writeln(ss.CountLessThanLocation(V));
        return null;
    }
}
