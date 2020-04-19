package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class PrintAscending extends AbstractCommand {

    public PrintAscending(){
        command = "print_ascending";
        helpText = "Вывести элементы коллекции в порядке возрастания.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        ss.display();
        return null;
    }
}
