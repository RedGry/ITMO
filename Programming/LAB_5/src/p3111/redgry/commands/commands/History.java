package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class History extends AbstractCommand{

    public History(){
        command = "history";
        helpText = "Вывести последние 15 команд (без аргументов).";
    }
    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        CommandsManager.cmdList();
        userInterface.writeln("");
        return null;
    }
}
