package p3111.redgry.utils;

import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Coordinates;
import p3111.redgry.collection.collection.Location;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.collection.helps.StorageService;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class DataBaseManager implements Serializable {
    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private String USER = "postgres";
    private String PASSWORD = "Я ВОТ ВАМ скажи)))";
    private String USER1;
    private String PASSWORD1;

    public DataBaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public boolean addUser(String login, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "CREATE ROLE " + login + " WITH\n" +
                    "  LOGIN\n" +
                    "  NOSUPERUSER\n" +
                    "  INHERIT\n" +
                    "  NOCREATEDB\n" +
                    "  NOCREATEROLE\n" +
                    "  NOREPLICATION\n" +
                    "  ENCRYPTED PASSWORD '" + password + "';\n" +
                    "GRANT lab7_user TO " + login + ";\n" +
                    "ALTER ROLE " + login + " SET password_encryption TO 'scram-sha-256';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean login(String login, String password) {
        USER = login;
        PASSWORD = password;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM person");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean login1(String login, String password) {
        String hash = PassEncoder.getHash(password);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE \"user\" = ? AND \"hex\" = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, hash);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addUserToDataBase(String login, String password){
        String hash = PassEncoder.getHash(password);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (\"user\", \"hex\") VALUES (?, ?);");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, hash);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    public boolean addToDataBase(Person person) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person (\"name\", \"Coordinates (X)\", \"Coordinates (Y)\", \"creationDate\", height, \"passportID\", \"hairColor\", \"location name\", \"location (X)\", \"location (Y)\", \"location (Z)\", \"user\", birthday ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            //preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(1, person.getName());
            preparedStatement.setLong(2, person.getCoordinates().getX());
            preparedStatement.setDouble(3, person.getCoordinates().getY());
            preparedStatement.setDate(4, Date.valueOf(person.getCreationDate()));
            preparedStatement.setLong(5, person.getHeight());
            preparedStatement.setString(6, person.getPassportID());
            preparedStatement.setString(7, person.getHairColor().getRus());
            preparedStatement.setString(8, person.getLocation().getName());
            preparedStatement.setDouble(9, person.getLocation().getX());
            preparedStatement.setDouble(10, person.getLocation().getY());
            preparedStatement.setDouble(11, person.getLocation().getZ());
            preparedStatement.setString(12, USER1);
            preparedStatement.setDate(13, Date.valueOf(person.getBirthday()));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addToDataBase1(Person person) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO person (id, \"name\", \"Coordinates (X)\", \"Coordinates (Y)\", \"creationDate\", height, \"passportID\", \"hairColor\", \"location name\", \"location (X)\", \"location (Y)\", \"location (Z)\", \"user\", birthday ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setLong(3, person.getCoordinates().getX());
            preparedStatement.setDouble(4, person.getCoordinates().getY());
            preparedStatement.setDate(5, Date.valueOf(person.getCreationDate()));
            preparedStatement.setLong(6, person.getHeight());
            preparedStatement.setString(7, person.getPassportID());
            preparedStatement.setString(8, person.getHairColor().getRus());
            preparedStatement.setString(9, person.getLocation().getName());
            preparedStatement.setDouble(10, person.getLocation().getX());
            preparedStatement.setDouble(11, person.getLocation().getY());
            preparedStatement.setDouble(12, person.getLocation().getZ());
            preparedStatement.setString(13, USER1);
            preparedStatement.setDate(14, Date.valueOf(person.getBirthday()));
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateElementInDataBase(Person person) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM person");
            long maxId = 0;
            while (resultSet.next()){
                maxId = resultSet.getLong("max");
            }
            if (addToDataBase1(person)){
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE person SET id = " + person.getId() + " WHERE id = (SELECT MAX(id) FROM person);");
                preparedStatement.executeUpdate();
                return true;
            } else {
                System.out.println("Не удалось обновить элемент, обновлённый элемент не может быть добавлен в БД");
            }
        } catch (SQLException e) {
            System.out.println("");
            return true;
        }
        return false;
    }

    public boolean removeFromDataBase(Person person){
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE id = " + person.getId());
            while (resultSet.next()){
                if (!resultSet.getString("user").equals(USER1)){
                    return false;
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id = " + person.getId() + " AND \"user\" = '" + USER1 + "'");
            preparedStatement.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void updateCollectionFromDataBase(StorageService storageService){
        LinkedHashMap<Long, Person> linkedHashMap = new LinkedHashMap<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM person");
            while (resultSet.next()){
                Person person = new Person(resultSet.getString("name"), new Coordinates(resultSet.getLong("Coordinates (X)"), resultSet.getDouble("Coordinates (Y)")), resultSet.getLong("height"), LocalDate.parse(resultSet.getString("birthday"), DateTimeFormatter.ofPattern("yyyy-MM-dd")), resultSet.getString("passportID"), Color.byOrdinal(resultSet.getString("hairColor")), new Location(resultSet.getDouble("location (X)"), resultSet.getDouble("location (Y)"), resultSet.getDouble("location (Z)"), resultSet.getString("location name")));
                person.setId(resultSet.getLong("id"));
                person.setCreationByUser(resultSet.getString("user"));
                person.setCreationDate(String.valueOf(resultSet.getDate("creationDate")));
                linkedHashMap.put(person.getId(), person);
            }
            storageService.clear();
            storageService.addAll(linkedHashMap);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setUSER(String USER){
        this.USER1 = USER;
    }

    public void setPASSWORD(String PASSWORD){
        this.PASSWORD1 = PASSWORD;
    }
}
