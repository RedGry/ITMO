package p3111.redgry.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.commands.commands.Show;
import p3111.redgry.utils.Handlers.ClientMessagesHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilterTableWorker {
    private ClientMessagesHandler updateCollectionClientMessagesHandler;
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    private VisualisationWorker visualisationWorker;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField cordXField;
    @FXML private TextField cordYField;
    @FXML private TextField birthdayField;
    @FXML private TextField heightField;
    @FXML private TextField passportIdField;
    @FXML private TextField hairColorField;
    @FXML private TextField locNameField;
    @FXML private TextField locXField;
    @FXML private TextField locYField;
    @FXML private TextField locZField;
    @FXML private TextField userField;

    @FXML private TableColumn<Person, Long> idColumn;
    @FXML private TableColumn<Person, String> nameColumn;
    @FXML private TableColumn<Person, Long> coordXColumn;
    @FXML private TableColumn<Person, Double> coordYColumn;
    @FXML private TableColumn<Person, String> birthdayColumn;
    @FXML private TableColumn<Person, Long> heightColumn;
    @FXML private TableColumn<Person, String> passportIdColumn;
    @FXML private TableColumn<Person, String> hairColorColumn;
    @FXML private TableColumn<Person, String> locNameColumn;
    @FXML private TableColumn<Person, Double> locXColumn;
    @FXML private TableColumn<Person, Double> locYColumn;
    @FXML private TableColumn<Person, Double> locZColumn;
    @FXML private TableColumn<Person, String> userColumn;
    @FXML private TableView<Person> dbTable;

    public FilterTableWorker(TableView<Person> dbTable, TableColumn<Person, Long> idColumn, TableColumn<Person, String> nameColumn,
                             TableColumn<Person, Long> coordXColumn, TableColumn<Person, Double> coordYColumn,
                             TableColumn<Person, String> birthdayColumn, TableColumn<Person, Long> heightColumn,
                             TableColumn<Person, String> passportIdColumn, TableColumn<Person, String> hairColorColumn,
                             TableColumn<Person, String> locNameColumn, TableColumn<Person, Double> locXColumn,
                             TableColumn<Person, Double> locYColumn,TableColumn<Person, Double> locZColumn,
                             TableColumn<Person, String> userColumn,
                             TextField idField, TextField nameField, TextField cordXField, TextField cordYField,
                             TextField birthdayField, TextField heightField, TextField passportIdField,
                             TextField hairColorField, TextField locNameField, TextField locXField,
                             TextField locYField, TextField locZField, TextField userField,
                             ClientMessagesHandler updateCollectionClientMessagesHandler, VisualisationWorker visualisationWorker){
        this.dbTable = dbTable;
        this.idColumn = idColumn;
        this.nameColumn = nameColumn;
        this.coordXColumn = coordXColumn;
        this.coordYColumn = coordYColumn;
        this.birthdayColumn = birthdayColumn;
        this.heightColumn = heightColumn;
        this.passportIdColumn = passportIdColumn;
        this.hairColorColumn = hairColorColumn;
        this.locNameColumn = locNameColumn;
        this.locXColumn = locXColumn;
        this.locYColumn = locYColumn;
        this.locZColumn = locZColumn;
        this.userColumn = userColumn;
        this.idField = idField;
        this.nameField = nameField;
        this.cordXField = cordXField;
        this.cordYField = cordYField;
        this.birthdayField = birthdayField;
        this.heightField = heightField;
        this.passportIdField = passportIdField;
        this.hairColorField = hairColorField;
        this.locNameField = locNameField;
        this.locXField = locXField;
        this.locYField = locYField;
        this.locZField = locZField;
        this.userField = userField;
        this.updateCollectionClientMessagesHandler = updateCollectionClientMessagesHandler;
        this.visualisationWorker = visualisationWorker;
        try{
            updateCollectionClientMessagesHandler.connect("localhost", 3292);
            setUpTimer();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setUpTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            updateTable();
            visualisationWorker.setupVisualisation();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private  boolean updateRQ = true;

    private void updateTable(){
        try {
            updateCollectionClientMessagesHandler.sendCommand(new Show());
            String answer = "";
            while (!answer.equals("collectionReceived")) answer = updateCollectionClientMessagesHandler.receiveAnswer();
            if (updateRQ){
                personData.clear();
                personData.addAll(updateCollectionClientMessagesHandler.getClientPq());
                visualisationWorker.setupVisualisation();
                updateRQ = false;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void setUpdateRQ(Boolean updateRQ){
        this.updateRQ = updateRQ;
    }

    public ObservableList<Person> getPersonData(){
        return personData;
    }

    public  void setupFilterTable(){
        FilteredList<Person> filteredData = new FilteredList<>(personData, p -> true);
        //System.out.println(filteredData);
        createListeners(filteredData);
        SortedList<Person> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(dbTable.comparatorProperty());
        dbTable.setItems(sortedList);
    }

    private void filterIdColumn(ObservableList<Person> personData){
        boolean firstFilter = false;
        List<Person> addList = new ArrayList<>();
    }

    private void createListeners(FilteredList<Person> personData){
        idField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getId()).contains(value)){
                    return true;
                }else return false;
            });
        });

        nameField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getName()).contains(value)){
                    return true;
                }else return false;
            });
        });

        cordXField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getCoordinates().getX()).contains(value)){
                    return true;
                }else return false;
            });
        });
        cordYField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getCoordinates().getY()).contains(value)){
                    return true;
                }else return false;
            });
        });
        birthdayField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getBirthday()).contains(value)){
                    return true;
                }else return false;
            });
        });
        heightField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getHeight()).contains(value)){
                    return true;
                }else return false;
            });
        });
        passportIdField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getPassportID()).contains(value)){
                    return true;
                }else return false;
            });
        });
        hairColorField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getHairColor().getRus()).contains(value)){
                    return true;
                }else return false;
            });
        });
        locNameField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getLocation().getName()).contains(value)){
                    return true;
                }else return false;
            });
        });
        locXField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getLocation().getX()).contains(value)){
                    return true;
                }else return false;
            });
        });
        locYField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getLocation().getY()).contains(value)){
                    return true;
                }else return false;
            });
        });
        locZField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getLocation().getZ()).contains(value)){
                    return true;
                }else return false;
            });
        });
        userField.textProperty().addListener((observable, oldValue, newValue) ->{
            personData.setPredicate(person -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String value = newValue;
                if (String.valueOf(person.getCreatedByUser()).contains(value)){
                    return true;
                }else return false;
            });
        });
    }

}
