package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class History extends AbstractCommand{

    public History(){
        command = "history";
        helpText = "Вывести последние 15 команд (без аргументов).";
    }
    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (argumentsCount == args.length) {
            CommandsManager.cmdList();
            return null;
        }
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
