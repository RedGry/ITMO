package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class Help extends AbstractCommand{

    public Help(){
        command = "help";
        helpText = "Выводит справку по доступным командам.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (argumentsCount == args.length) {
            CommandsManager.getInstance().printToClient("suc_help");
            return null;
        }
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
