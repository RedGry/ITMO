package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;
public class PrintAscending extends AbstractCommand {

    public PrintAscending(){
        command = "print_ascending";
        helpText = "Вывести элементы коллекции в порядке возрастания.";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (argumentsCount == args.length) {
            CommandsManager.getInstance().printToClient("print_ascending" + "_" + ss.display());
            return null;
        }
        CommandsManager.getInstance().printToClient("Команда не принимает агрументы!");
        return null;
    }
}
