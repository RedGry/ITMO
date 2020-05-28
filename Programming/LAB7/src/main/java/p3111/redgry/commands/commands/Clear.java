package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.DataBaseManager;
import p3111.redgry.utils.UserInterface;

import java.io.IOException;
import java.util.ArrayList;


public class Clear extends AbstractCommand {

    public Clear(){
        command =  "clear";
        helpText = "Очистить коллекцию (удаляет из коллекции элементы принадлежащие вам).";
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args, DataBaseManager dataBaseManager) throws IOException {
        if (argumentsCount == args.length) {
            int count = 0;
            try {
                CommandsManager.getInstance().getLock().lock();
                ss.list().forEach((key, person) -> {
                    dataBaseManager.removeFromDataBase(person);
                });
                dataBaseManager.updateCollectionFromDataBase(ss);
                CommandsManager.getInstance().printToClient("Коллекция успешно очищенна! Все элементы принадлежащие вам были удалены!");
                return null;
            } finally {
                CommandsManager.getInstance().getLock().unlock();
            }
        }
        //logger.warn("Команда не принимает аргументы");
        CommandsManager.getInstance().printToClient("Команда не принимает агрументы!");
        return null;
    }
}
