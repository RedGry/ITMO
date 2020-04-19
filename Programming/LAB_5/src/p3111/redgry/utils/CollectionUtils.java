package p3111.redgry.utils;

import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Coordinates;
import p3111.redgry.collection.collection.Location;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.exceptions.InvalidInputException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс отвечающий за работы с коллекцией.
 */
public class CollectionUtils {
    private CollectionUtils() {}

    /**
     * Метод, запрашивающий у пользователя ввод всех полей обьекта Person (с соответствубющими сообщениями).
     *
     * @param userInterface пользовательский интерфейс
     * @param key ключ
     * @return экземпляр Person, созданный на основе введенных пользователем данных.
     */
    public static Person readPerson(UserInterface userInterface, Long key) throws ClassCastException, InvalidInputException, NumberFormatException, IOException {
        String name;
        long id = key;
        Coordinates coordinates;
        Long height;
        Location location;
        LocalDateTime birthday = LocalDateTime.now().toLocalDate().atStartOfDay();
        do {
            name = userInterface.readWithMessage("Введите Имя персонажа: ", false);
        } while (name.isEmpty());
        try {
            coordinates = readCoordinates(userInterface);
        }catch (NumberFormatException e){
            coordinates = readCoordinates(userInterface);
        }
        try {
            height = Long.parseLong(userInterface.readWithMessage("Введите рост персонажа (значение больше 0): ", 0, -1));
        } catch (NumberFormatException e){
            height = Long.parseLong(userInterface.readWithMessage("Введите рост персонажа (значение больше 0): ", 0, -1));
        }
        try {
            birthday = LocalDate.parse(userInterface.readWithMessage(("Введите дату рождения, в формате (YYYY-MM-DD): "), false ), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        }catch (DateTimeParseException e1) {
            System.out.println("Неправильно указана дата! Дата будет установлена сегодняшняя.");
        }
        String passportID = (userInterface.readWithMessage("Введите ID пасспорта: ", 0, -1));
        Color hairColor = readColor(userInterface);
        try {
            location = readLocation(userInterface);
        } catch (NumberFormatException e){
            location = readLocation(userInterface);
        }
        return new Person(name, coordinates, height, birthday, passportID, hairColor, location);
    }

    private static Location readLocation(UserInterface userInterface) throws NumberFormatException, IOException {
        String LocationName = userInterface.readWithMessage("Введите название Локации: ", true);
        double X = Double.parseDouble(userInterface.readWithMessage("Расположение по X относительно локации: ", 0, -1));
        Double Y = Double.parseDouble(userInterface.readWithMessage("Расположение по Y относительно локации: ", 0, -1));
        double Z = Double.parseDouble(userInterface.readWithMessage("Расположение по Z относительно локации: ", 0, -1));
        return new Location(X, Y, Z, LocationName);
    }

    private static Coordinates readCoordinates(UserInterface userInterface) throws NumberFormatException, IOException {
        Long x = Long.parseLong(userInterface.readWithMessage("Введите расположение персонажа по X (целое число): ", false));
        Double y = Double.parseDouble(userInterface.readWithMessage("Теперь расположение по Y (вещественное число): ", false));
        return new Coordinates(x, y);
    }

    private static Color readColor(UserInterface userInterface) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (Color value : Color.values()) {
            sb.append("\n").append(value.ordinal()).append(": ").append(value.getRus());
        }
        String inp = userInterface.readWithMessage("Какой цвет волос вы хотите? Введите название на русском: " + sb.toString(), true);
        boolean False = true;
        while(False){
            if(Color.checkColor(inp)){
                False = false;
            }else
            inp = userInterface.readWithMessage("Неправильно введен цвет, попробуйте снова: " + sb.toString(), true);
        }
        if (inp == null) {
            return null;
        }
        return Color.byOrdinal(inp);
    }
}
