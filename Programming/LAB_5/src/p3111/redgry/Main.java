package p3111.redgry;

import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helpers.StackPersonStorage;
import p3111.redgry.collection.helpers.StackPersonStorageService;
import p3111.redgry.collection.helpers.Storage;
import p3111.redgry.collection.helpers.StorageService;
import p3111.redgry.commands.CommandsManager;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.exceptions.NoSuchCommandException;
import p3111.redgry.utils.CsvReader;
import p3111.redgry.utils.UserInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Главный класс программы.
 *
 * @author Egor Krivonosov (RedGry)
 */
public class Main {
    /**
     * Метод, который позволяет нам запустить программу.
     *
     * @param args агрументы из командной строки.
     */
    public static void main(String[] args) {
        String FilePath = "C:/Users/Egor/Desktop/convertcsv.csv";
        //String FilePath = System.getenv("WORK_FILE_PATH");
        Scanner scan = new Scanner(System.in);
        FileInputStream f = null;
        boolean False = true;
        String delimiter = "";

        while(False) {
            if (!delimiter.equals(";") & !delimiter.equals(",")) {
                System.out.println("Введите разделитель для CSV файла \";\" или \",\" ");
                delimiter = scan.nextLine();
            }else{
                False = false;
            }
        }
        
        if (FilePath == null){
            System.out.println("Вы не задали значение переменной окружения, коллекция не будет загруженна и не будет сохранена" + "\n");
            f = null;
        } else {
            try {
                File workFile = new File(FilePath);
                f = new FileInputStream(workFile);
            } catch (FileNotFoundException e){
                System.out.println("Файл не найден или не хватает прав для его чтения, коллекция не будет загруженна в программу, но вы сможете ее сохранить, если есть права на запись, если файла не существует, он будет создан");
            }
        }
        Storage<Long, Person> storage = new StackPersonStorage();
        StorageService storageService = new StackPersonStorageService(storage);
        UserInterface userInterface = new UserInterface(new InputStreamReader(System.in, StandardCharsets.UTF_8), new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);
        if (f != null){
            boolean success = false;
            try {
                CsvReader.InputLoader(FilePath, storage.getPersons(), delimiter);

                if (storage.size() != 0) success = true;
            } catch (ClassCastException e){
                userInterface.writeln("Ошибка приведения типов.");
                e.getMessage();
            } catch (Exception e){
                userInterface.writeln("Автор не заметил данной ошибки при тесте -_-");
                e.printStackTrace();
            } finally {
                if (!success){
                    userInterface.writeln("Что-то пошло не так при инициализации коллекции. Теперь вы работаете с пустой коллекцией." );
                }
            }
        }
        while (true){
            if (userInterface.hashNextLine()) {
                String cmd = userInterface.read();
                try {
                    CommandsManager.getInstance().executeCommand(userInterface, storageService, cmd.trim());
                } catch (NoSuchCommandException e) {
                    userInterface.writeln("Неизвестная команда, используйте команду help, чтобы посмотреть список всех доступных команд.");
                } catch (ArrayIndexOutOfBoundsException e){
                    userInterface.writeln("Не был указан аргумент команде для комнады");
                } catch (InvalidInputException e) {
                    userInterface.writeln("Ошибка в введённых пользователем данных. Проверьте введённые данные на валидность и попробуйте ещё разок.");
                } catch (NumberFormatException e) {
                    userInterface.writeln("Введено не корректное число, попробуйте запустить команду заново");
                } catch (IOException e){
                    userInterface.writeln("Ошибка ввода/вывода");
                } catch (Exception e){
                    userInterface.writeln("Произошла ошибка, автор не заметил её при тестировании! Извините, я все исправлю. ");
                    e.printStackTrace();
                }
            }
        }
    }
}