package p3111.redgry.collection.collection;

import lombok.NonNull;
import p3111.redgry.collection.helpers.StackPersonStorage;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Модель персонажей
 */
public class Person implements Comparable{
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private java.time.LocalDateTime birthday; //Поле может быть null
    private String passportID; //Поле может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле не может быть null

    public Person(String name,
                  Coordinates coordinates,
                  Long height,
                  LocalDateTime birthday,
                  String passportID,
                  Color hairColor,
                  Location location) {
        this.id = StackPersonStorage.numberGenerate();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.height = height;
        this.birthday = birthday;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.location = location;
    }

    public Person(long id,
                  String name,
                  Coordinates coordinates,
                  Long height,
                  LocalDateTime birthday,
                  String passportID,
                  Color hairColor,
                  Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.height = height;
        this.birthday = birthday;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.location = location;
    }

    public Person(){}

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

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String  creationDate) {
        if (creationDate != null) {
            this.creationDate = ZonedDateTime.parse(creationDate);
        } else {
            this.creationDate = ZonedDateTime.now();
        }
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
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

    @Override
    public String toString() {
        return "Person{" +
                "\n id: " + id +
                "\n name: '" + name + '\'' +
                "\n coordinates: { x = " + coordinates.getX() + ", y = " + coordinates.getY() + " }" +
                "\n creationDate: " + creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                "\n height: " + height +
                "\n birthday: " + birthday.format(DateTimeFormatter.ISO_LOCAL_DATE) +
                "\n passportID: '" + passportID + '\'' +
                "\n hairColor: " + hairColor.getRus() +
                "\n locationName: " + location.getName() + " { x = " + location.getX() + ", y = " + location.getY() + ", z = " + location.getZ() + " }" +
                "\n }";
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) {
            return -1;
        }
        if (!(o instanceof Person)){
            throw  new ClassCastException();
        }
        Person person = (Person) o;
        return (int) (this.getHeight() - person.getHeight());
    }
}
