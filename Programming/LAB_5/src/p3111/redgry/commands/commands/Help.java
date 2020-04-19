package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Help extends AbstractCommand{

    public Help(){
        command = "help";
        helpText = "Выводит справку по доступным командам.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        for (AbstractCommand command : CommandsManager.getInstance().getAllCommands()){
            userInterface.writeln(ANSI_YELLOW + command.getCommand() + ANSI_RESET + ": " + command.getHelpText());
        }
        return null;
    }
}
