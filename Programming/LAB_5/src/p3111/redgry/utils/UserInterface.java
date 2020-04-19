package p3111.redgry.utils;

import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Coordinates;
import p3111.redgry.collection.collection.Location;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.exceptions.InvalidInputException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Класс, отвечающий за обмен информацией с пользователем.
 */
public class UserInterface {
    private Reader reader;
    private Writer writer;
    private Scanner scanner;
    private boolean interactive;

    /**
     * Конструктор, который создает интерфейс и определяет куда писать и откуда.
     *
     * @param reader      откуда считывать
     * @param writer      куда писать
     * @param interactive флаг, обозначающий режим работы интерфейса.
     */
    public UserInterface(Reader reader, Writer writer, boolean interactive){
        this.reader = reader;
        this.writer = writer;
        this.interactive = interactive;
        this.scanner = new Scanner(reader);
    }

    /**
     * Вывод сообщения на экран пользователя с переносом на новую строку.
     *
     * @param message строка для вывода.
     */
    public void writeln(String message){
        write(message + "\n");
    }

    /**
     * Вывод сообщения.
     *
     * @param message Строка для вывода
     */
    public void write(String message){
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e){
            e.getMessage();
        }
    }

    /**
     * Метод, запрашивающий ввод из стандартного потока ввода. Перед вводом выводит сообщение в стандартный поток вывода.
     *
     * @param message  сообщение для пользователя, будет выведено перед вводом.
     * @param nullable флаг. True - если мы допускаем пустой ввод от пользователя. False - если нам надо добиться от него не пустого ввода.
     * @return введенную строку пользователя, или null если пользователь ввел пустую строку и флаг nullable равен true.
     */
    public String readWithMessage(String message, boolean nullable){
        String result = "";
        do {
            if (result == null){
                writeln("");
            }
            if (interactive){
                writeln(message);
            }
            result = scanner.nextLine();
            result = result.isEmpty() ? null : result;
        } while (interactive && !nullable && result == null);
        if (!interactive && result == null) {
            throw new InvalidInputException("Получена пустая строка в поле, которое не может быть null");
        }
        return  result;
    }

    /**
     * Считывает из потока число и проверяет его на вхождение в промежуток [min; max]. При не корректном вводе, запрашивается повторный ввод.
     * Перед вводом выводит сообщение в стандартный поток вывода.
     *
     * @param message сообщение для пользователя, будет выведено перед вводом.
     * @param min     нижняя граница (-1, если неважна)
     * @param max     вверхняя граница (-1, если не важна)
     * @return введенное пользователем число.
     */
    public String readWithMessage(String message, int min, int max){
        String result;
        do {
            result = readWithMessage(message, false);
        } while (!checkNumber(Double.parseDouble(result), min, max));
        return result;
    }

    /**
     * Метод проверяющий числа которые попадают в промежуток [min, max].
     *
     * @param s   число пользователя.
     * @param min нижняя граница.
     * @param max вверхняя граница.
     * @return ввозвращает True - если число принадлежит промежутку [min, max].
     */
    public static boolean checkNumber(double s, int min, int max){
        return ((min < 0 || s >= min) && (max < 0 || s <= max));
    }

    /**
     * Метож возвращающий есть ли что считывать из входного потока.
     *
     * @return есть ли ещё что считывать.
     */
    public boolean hashNextLine(){
        return scanner.hasNextLine();
    }

    /**
     * Метод для считывания.
     *
     * @return возращает считаную строку.
     */
    public String read(){
        return scanner.nextLine();
    }


    public Person readPerson() throws ClassCastException, InvalidInputException, NumberFormatException{
        String name = readWithMessage("Введите имя: ", false);
        Coordinates coordinates = readCoordinates();
        Long height = Long.parseLong(readWithMessage("Введите рост человека (целое число, больше 0): ", 0, -1));
        LocalDateTime birthday = LocalDate.parse(readWithMessage("Введите дату рождения, в формате (YYYY-MM-DD): )", false), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        String passportID = readWithMessage("Введите серию паспорта: ",false);
        Color hairColor = readHairColor();
        Location location = readLocation();
        return new Person(name, coordinates, height, birthday , passportID, hairColor, location);
    }


    public Coordinates readCoordinates() throws NumberFormatException{
        Long x = Long.parseLong(readWithMessage("Введите расположение человека по X (целое число): ", false));
        Double y = Double.parseDouble(readWithMessage("Введите расположение человека по Y (вещесетвенное число): ", false));
        return new Coordinates(x, y);

    }


    public Color readHairColor() {
        StringBuilder sb = new StringBuilder();
        for (Color value : Color.values()){
            sb.append("\n").append(value.ordinal()).append(" - ").append(value.getRus());
        }
        String input = readWithMessage("Какой цвет волос. Введите цвет: ", false);
        if (input == null){
            throw new InvalidInputException("Данного цвета нет в спеске.");
        }
        return Color.byOrdinal(input);

    }


    public Location readLocation() throws NumberFormatException{
        String locationName = readWithMessage("Введите название локации: ", false);
        double x = Double.parseDouble(readWithMessage("Введите расположение человека по X (вещесетвенное число): ", true));
        Double y = Double.parseDouble(readWithMessage("Введите расположение человека по Y (вещесетвенное число): ", false));
        double z = Double.parseDouble(readWithMessage("Введите расположение человека по Z (вещесетвенное число): ", true));
        return new Location(x, y, z, locationName);
    }

}
