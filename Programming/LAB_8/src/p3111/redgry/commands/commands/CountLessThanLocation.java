package p3111.redgry.commands.commands;

import p3111.redgry.collection.helps.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;

public class CountLessThanLocation extends AbstractCommand {

    public CountLessThanLocation(){
        command = "count_less_than_location";
        helpText = "Вывести количество элементов, значение поля location которых меньше заданного.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (argumentsCount == args.length) {
            try {
                CommandsManager.getInstance().getLock().lock();
                double V;
                try {
                    V = Double.parseDouble(args[0]);
                } catch (NumberFormatException e) {
                    CommandsManager.getInstance().printToClient("error_arg");
                    return null;
                }
                CommandsManager.getInstance().printToClient("count_less" + "_" + ss.CountLessThanLocation(V));
                return null;
            }catch (Exception e){
                CommandsManager.getInstance().printToClient("error_arg");
                e.printStackTrace();
            }
            finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        CommandsManager.getInstance().printToClient("error_arg");
        return null;
    }
}
