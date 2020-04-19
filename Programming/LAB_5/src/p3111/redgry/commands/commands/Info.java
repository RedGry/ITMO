package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Info extends AbstractCommand{

    public Info(){
        command = "info";
        helpText = "Выводит в стандартный потор информацию о коллекции.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        userInterface.writeln(ss.info());
        return null;
    }
}
