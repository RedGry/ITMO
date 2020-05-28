package p3111.redgry.collection.collection;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import lombok.NonNull;
import p3111.redgry.exceptions.InvalidInputException;
import p3111.redgry.utils.UserInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

/**
 * Data transfer object для персонажей.
 */
public class PersonDTO implements Comparable<PersonDTO>{
    private Vector<PersonDTO> labWorks = new Vector<>();

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private java.time.LocalDate birthday; //Поле может быть null
    private String passportID; //Поле может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле не может быть null

    public PersonDTO(){
    }

    public PersonDTO(Person person){
        id = person.getId();
        name = person.getName();
        coordinates = person.getCoordinates();
        creationDate = person.getCreationDate();
        height = person.getHeight();
        birthday = person.getBirthday();
        passportID = person.getPassportID();
        hairColor = person.getHairColor();
        location = person.getLocation();
    }

    public PersonDTO(Map.Entry<Long, Person> longPersonEntry) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(@NonNull Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String  creationDate) {
        if (creationDate != null) {
            this.creationDate = LocalDate.parse(creationDate);
        } else {
            this.creationDate = LocalDate.now();
        }
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(@NonNull Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(@NonNull Location location) {
        this.location = location;
    }

    public void CheckFields(){
        if (name.isEmpty()){
            throw new InvalidInputException("Name - пусто.");
        }
        if (!UserInterface.checkNumber(height, 0, -1) || !UserInterface.checkNumber(id, 0, -1)){
            throw new InvalidInputException("Одно из чисел Height или ID во входном файле не подходит под ограничения. (Person)");
        }
    }

    public String[] toArray1() {
        ArrayList<String> array = new ArrayList<>();
        labWorks.forEach(labWork -> array.add(labWork.toCSVline()));

        String[] stringArray = new String[array.size()];
        stringArray = array.toArray(stringArray);

        return stringArray;
    }


    @Override
    public int compareTo(PersonDTO personDTO) {
        return (int) (id - personDTO.getId());
    }

    public String toCSVline() {
        CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).withQuoteChar('"').build();
        return parser.parseToLine(toArray(),false);
    }

    private String[] toArray() {
        ArrayList<String> array = new ArrayList<>(getStringValues(
                getName(),
                getCoordinates().getX(),
                getCoordinates().getY(),
                getHeight(),
                getPassportID(),
                getHairColor(),
                getLocation().getX(),
                getLocation().getY(),
                getLocation().getZ(),
                getLocation().getName(),
                id,
                creationDate
        ));
        String[] result = new String[array.size()];
        result = array.toArray(result);
        return result;
    }

    private ArrayList<String> getStringValues(Object ... objects) {
        ArrayList<String> array = new ArrayList<>();
        for (Object object : objects) {
            if (object == null) array.add("");
            else array.add(String.valueOf(object));
        }
        return array;
    }
}
