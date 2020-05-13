package p3111.redgry.commands.commands;

import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.utils.UserInterface;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;


public class ExecuteScript extends AbstractCommand {
    private HashSet<String> path = new HashSet<>();


    public ExecuteScript(){
        command = "execute_script";
        helpText = "Считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.";
        argumentsCount = 1;
    }

    @Override
    public ArrayList<String> execute(UserInterface userInterface, StorageService ss, String[] args) throws IOException {
        if (argumentsCount == args.length) {
            String sciptPath = args[0];
            Path pathToScript = Paths.get(sciptPath);
            userInterface.writeln(ANSI_GREEN + "\nНачинаем выполнять скрипт " + ANSI_RESET + pathToScript.getFileName());
            long startTime = System.currentTimeMillis();
            try {
                UserInterface fileInterface = new UserInterface(new FileReader(pathToScript.toFile()), new OutputStreamWriter(System.out), false);
                while (fileInterface.hashNextLine()) {
                    String line = fileInterface.read();
                    String[] listScriptPath = line.split(" ");
                    if (listScriptPath[0].equals("execute_script")) {
                        if (!path.contains(pathToScript.toString())) {
                            path.add(pathToScript.toString());
                            CommandsManager.getInstance().executeCommand(fileInterface, ss, line);
                        } else {
                            CommandsManager.getInstance().printToClient(ANSI_RED + "Вы пытаетесь вызвать скрипт, который вы уже вызывали ранее! Пытаетесь устроить рекурсию?" + ANSI_RESET);
                        }
                    } else {
                        CommandsManager.getInstance().executeCommand(fileInterface, ss, line);
                    }
                }
                path.removeAll(path);
                CommandsManager.getInstance().printToClient("Скрипт выполнен успешно. Его выполнение заняло " + (System.currentTimeMillis() - startTime) + " ms");
            } catch (FileNotFoundException e) {
                CommandsManager.getInstance().printToClient("Файла не существует.");
            } catch (Exception e) {
                CommandsManager.getInstance().printToClient("Ошибка во время выполнения скрипта.");
                throw e;
            }
            return null;
        }
        //logger.warn("Команда не были переданы аргументы");
        CommandsManager.getInstance().printToClient("Команде не был указан агрумент или был неправильного формата!");
        return null;
    }
}
