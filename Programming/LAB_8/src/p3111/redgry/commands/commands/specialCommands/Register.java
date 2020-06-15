package p3111.redgry.commands.commands.specialCommands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class Register extends AbstractCommand {
    public Register(){
        command = "register";
    }
    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        // Служебная команда
        return null;
    }
}
