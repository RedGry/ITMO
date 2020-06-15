package p3111.redgry.utils;

import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Coordinates;
import p3111.redgry.collection.collection.Location;
import p3111.redgry.collection.collection.Person;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LoadCollection {
    public static String[] arguments = null;
    private static String delimiter;

    /**
     * Метод преобразующий строку в экземпляр Person.
     * @param arguments строка из файла.
     * @return экземпляр Person.
     */
    public static Person TransformArguments(String[] arguments) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            for (String argument : arguments) {
                if (argument.isEmpty()) throw new Exception();
                String name = arguments[0];
                Coordinates coordinates = new Coordinates(Long.parseLong(arguments[1]), Double.parseDouble(arguments[2]));
                ZonedDateTime creationDate = ZonedDateTime.now();
                Long height = Long.parseLong(arguments[3]);
                LocalDate birthday = LocalDate.parse(arguments[4], formatter);
                String passportID = arguments[5];
                Color hairColor = Color.byOrdinal(arguments[6]);
                Location location = new Location(Double.parseDouble(arguments[7]), Double.parseDouble(arguments[8]), Double.parseDouble(arguments[9]), arguments[10]);
                return new Person(name, coordinates, height, birthday, passportID, hairColor, location);
            }
        } catch (Exception ex) {
            //logger.warn("Не удалось добавить объект, некоторые данные введены неверно!");
            return null;
        }
        return null;
    }

    public static String getDelimiter() {
        return delimiter;
    }

    public static void setDelimiter(String delimiter) {
        LoadCollection.delimiter = delimiter;
    }


}
