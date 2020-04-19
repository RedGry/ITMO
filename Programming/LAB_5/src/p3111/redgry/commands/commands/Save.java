package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;



public class Save extends AbstractCommand {
    private static final String PATH = Paths.get("out.csv").toAbsolutePath().toString();


    public Save(){
        command = "save";
        helpText = "Сохранить коллекцию в файл.";
    }


    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (args.length > 0) System.out.println("Команда не принимает аргументы");
        else {
            ss.save(PATH);
            userInterface.writeln("Коллекция сохранена успешно.");
        }
        return null;
    }
}
