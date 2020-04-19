package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Clear extends AbstractCommand {

    public Clear(){
        command =  "clear";
        helpText = "Очистить коллекцию.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        int count = ss.size();
        ss.clear();
        userInterface.writeln("Коллекция успешно очищенна! Элементов удалено " + count);
        return null;
    }
}
