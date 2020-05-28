package p3111.redgry.utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvMalformedLineException;
import com.opencsv.exceptions.CsvValidationException;
import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Coordinates;
import p3111.redgry.collection.collection.Location;
import p3111.redgry.collection.collection.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

/**
 * Класс с помощью, которого происходит считывание из файла.
 */
public class CsvReader {
    //private static final Logger logger = LoggerFactory.getLogger(Server.class.getName());
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

    /**
     * Получить значения разделителя, который используется для CSV файла.
     *
     * @return разделитель. delimiter
     */
    public static String getDelimiter() {
        return delimiter;
    }

    /**
     * Установить значение разделителя, которое будет использоваться для CSV файла.
     *
     * @param delimiter разделитель.
     */
    public static void setDelimiter(String delimiter) {
        CsvReader.delimiter = delimiter;
    }

    /**
     * Метод, который считывает по строчно каждый элемент коллекции и загружает его в базу.
     *
     * @param pathToFile    путь к файлу с коллекцией.
     * @param linkedHashMap коллекция.
     * @param delimiter     разделитель, для CSV файла.
     * @throws IOException the io exception
     */
    public static void InputLoader(String pathToFile, LinkedHashMap<Long, Person> linkedHashMap, String delimiter) throws IOException {
        setDelimiter(delimiter);
        CSVParser parser = new CSVParserBuilder().withSeparator(delimiter.charAt(0)).build();
        Path myPath = Paths.get(pathToFile);
        try (BufferedReader br = Files.newBufferedReader(myPath, StandardCharsets.UTF_8);
            CSVReader reader = new CSVReaderBuilder(br).withCSVParser(parser).build()){
            String[] line;
            int lineCount = 1;
            do {
                line = reader.readNext();
                if (line == null) break;
                if (line.length != 11) {
                    //logger.warn("Не удалось загрузить элемент. Строка {} имеет неверный формат", lineCount);
                } else {
                    Person person = TransformArguments(line);
                    if (person != null) {
                        int key = 1;
                        //System.out.print(person + "\n");
                        ++key;
                        linkedHashMap.put(person.getId(), person);
                    }
                }
                lineCount++;
            } while (true);
            if (!linkedHashMap.isEmpty())
            System.out.println("Коллекция загружена и готова к работе.");
        } catch (MalformedInputException e){
            //logger.error("Коллекция неправильно задана.");
        } catch (CsvMalformedLineException e){
            //logger.error("Одна из строк не коректна, исправте файл и попробуйте заново.");
        } catch (NullPointerException | IOException | CsvValidationException e){
            e.printStackTrace();
        }
    }

}
