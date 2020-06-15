package p3111.redgry.utils.controllers;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.commands.commands.*;
import p3111.redgry.exceptions.InvalidCountOfArgumentException;
import p3111.redgry.utils.Handlers.ClientMessagesHandler;
import p3111.redgry.utils.FilterTableWorker;
import p3111.redgry.utils.VisualisationWorker;

public class MainController implements Initializable{
    private ClientMessagesHandler clientMessagesHandler;
    private VisualisationWorker visualisationWorker;
    private ResourceBundle currentBundle;
    private FilterTableWorker filterTableWorker;
    private ObservableList<Person> masterData = FXCollections.observableArrayList();

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Menu menuFile;
    @FXML private MenuItem exit;
    @FXML private Menu menuHelp;
    @FXML private MenuItem help;
    @FXML private MenuItem info;
    @FXML private Menu selectLanguage;
    @FXML private Label usernameLabel;
    @FXML private Label updateTableLable;
    @FXML private Label addCmdLabel;
    @FXML private Button insert;
    @FXML private Label removeCmdLabel;
    @FXML private Button remove_key;
    @FXML private Button remove_any_by_birthday;
    @FXML private Button remove_greater;
    @FXML private Button remove_greater_key;
    @FXML private Button clear;
    @FXML private Button history;
    @FXML private Label updateCmdLabel;
    @FXML private Button update;
    @FXML private Label printCmdLabel;
    @FXML private Button count_less_then_location;
    @FXML private Button print_ascending;
    @FXML private Tab tabPerson;
    @FXML private Tab tabVisual;
    @FXML private Pane pane;
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


    @FXML void CountLessThanLocationClick() { sendPreparedCommand(getCommandWithSimpleArg(new CountLessThanLocation(), currentBundle.getString("count_less_number"))); }
    @FXML void clearClick() {
        sendPreparedCommand(new Clear());
    }
    @FXML void historyClick(){
        sendPreparedCommand(new History());
    }
    @FXML void exit() {
        System.exit(0);
    }
    @FXML void help() {
        sendPreparedCommand(new Help());
    }
    @FXML void info() {
        sendPreparedCommand(new Info());
    }
    @FXML void insertClick() { sendPreparedCommand(getCommandWithObject(getCommandWithSimpleArg(new Insert(), currentBundle.getString("input_id")), currentBundle.getString("add"), null)); }
    @FXML void printAscendingClick() {
        sendPreparedCommand(new PrintAscending());
    }
    @FXML void removeByBirthdayClick() { sendPreparedCommand(getCommandWithSimpleArg(new RemoveAnyByBirthday(), currentBundle.getString("input_birthday"))); }
    @FXML void removeByKeyClick() { sendPreparedCommand(getCommandWithSimpleArg(new RemoveByKey(), currentBundle.getString("input_id"))); }
    @FXML void removeGreaterClick() { sendPreparedCommand(getCommandWithObject(getCommandWithSimpleArg(new Insert(), currentBundle.getString("input_id")), currentBundle.getString("add"), null)); }
    @FXML void removeGreaterKeyClick() { sendPreparedCommand(getCommandWithObject(getCommandWithSimpleArg(new Insert(), currentBundle.getString("input_id")), currentBundle.getString("add"), null)); }

    @FXML void switchRussian() {
        currentBundle = ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("ru", "RU"));
        changeLanguage();
    }

    @FXML void switchSpanish() {
        currentBundle = ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("es", "DO"));
        changeLanguage();
    }

    @FXML void switchTurkish() {
        currentBundle = ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("tr", "TR"));
        changeLanguage();
    }

    @FXML void switchUkrainian() {
        currentBundle = ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("ua", "UA"));
        changeLanguage();
    }

    @FXML void updateClick() {
        try{
            AbstractCommand command = getCommandWithSimpleArg(new Update(), currentBundle.getString("WriteId"));
            assert command != null;
            final long id = Long.parseLong(command.getArgs()[0]);
            Person selectedPerson = null;

            for (Person person : clientMessagesHandler.getClientPq()){
                if (person.getId() == id){
                    selectedPerson = person;
                }
            }

            if (selectedPerson != null){
                command = getCommandWithObject(command, currentBundle.getString("update"), selectedPerson);
                if (command != null){
                    sendPreparedCommand(command);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("idNotFound"));
            }
        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("IncorrectDataField"));
        }
        catch (Exception e){
            showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("UnexpectedException") + e.getMessage());
        }
    }

    @FXML void updateTableClick() {
        filterTableWorker.setUpdateRQ(true);
    }

    @FXML void visualisatonSelect() {
        visualisationWorker.setupVisualisation();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            currentBundle = ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("ru", "RU"));
            clientMessagesHandler = new ClientMessagesHandler(this);
            ClientMessagesHandler updateCollectionClientMessagesHandler = new ClientMessagesHandler(this);
            visualisationWorker = new VisualisationWorker(pane, updateCollectionClientMessagesHandler, this);
            filterTableWorker = new FilterTableWorker(dbTable, idColumn, nameColumn, coordXColumn, coordYColumn, birthdayColumn, heightColumn, passportIdColumn, hairColorColumn, locNameColumn, locXColumn, locYColumn, locZColumn, userColumn, idField, nameField, cordXField, cordYField, birthdayField, heightField, passportIdField, hairColorField, locNameField, locXField, locYField, locZField, userField, updateCollectionClientMessagesHandler, visualisationWorker);

            clientMessagesHandler.connect("localhost", 3292);

            String fxmFile = "../../fxml/authorization.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmFile));
            Parent root = fxmlLoader.load();
            AuthorizationController authorizationController = fxmlLoader.getController();
            authorizationController.setClientMessagesHandler(clientMessagesHandler);
            authorizationController.setregistrationLoginPos(Pos.CENTER);
            authorizationController.setMainController(this);
            Stage authorizationStage = new Stage();
            authorizationStage.setTitle("Authorization");
            authorizationStage.setResizable(false);
            authorizationStage.setScene(new Scene(root));
            authorizationStage.requestFocus();
            authorizationStage.initModality(Modality.WINDOW_MODAL);
            authorizationStage.showAndWait();
            if (!clientMessagesHandler.isAuthorized()){
                System.exit(0);
            } else {
                usernameLabel.setText(currentBundle.getString("username") + clientMessagesHandler.getLogin());
                changeLanguage();
                fillTable();
                filterTableWorker.setupFilterTable();
                getDataFromTableWithDoubleClick();
            }
        }catch (Exception e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("UnexpectedException") + e.getMessage());
        }
    }

    public void fillTable(){
        try{
            clientMessagesHandler.sendCommand(new Show());
            receiveAnswer();

            filterTableWorker.getPersonData().clear();
            filterTableWorker.getPersonData().addAll(clientMessagesHandler.getClientPq());
            idColumn.setCellValueFactory(personData -> personData.getValue().getIdProperty().asObject());
            nameColumn.setCellValueFactory(personData -> personData.getValue().getNameProperty());
            coordXColumn.setCellValueFactory(personData -> personData.getValue().getCoordinates().getXProperty().asObject());
            coordYColumn.setCellValueFactory(personData -> personData.getValue().getCoordinates().getYProperty().asObject());
            birthdayColumn.setCellValueFactory(personData -> personData.getValue().getBirthdayProperty());
            heightColumn.setCellValueFactory(personData -> personData.getValue().getHeightProperty().asObject());
            passportIdColumn.setCellValueFactory(personData -> personData.getValue().getPassportIDProperty());
            hairColorColumn.setCellValueFactory(personData -> personData.getValue().getHairColorProperty().getRusProperty());
            locNameColumn.setCellValueFactory(personData -> personData.getValue().getLocationProperty().getNameProperty());
            locXColumn.setCellValueFactory(personData -> personData.getValue().getLocationProperty().getXProperty().asObject());
            locYColumn.setCellValueFactory(personData -> personData.getValue().getLocationProperty().getYProperty().asObject());
            locZColumn.setCellValueFactory(personData -> personData.getValue().getLocationProperty().getZProperty().asObject());
            userColumn.setCellValueFactory(personData -> personData.getValue().getCreatedByUserProperty());
            dbTable.setItems(filterTableWorker.getPersonData());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDataFromTableWithDoubleClick(){
        dbTable.setRowFactory(tv ->{
            TableRow<Person> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2){
                    if (!row.isEmpty()){
                        Person selectedPerson = row.getItem();
                        AbstractCommand update = new Update();
                        try {
                            update.setArgs(new String[]{String.valueOf(selectedPerson.getId())});
                        } catch (InvalidCountOfArgumentException e) {
                            e.printStackTrace();
                        }
                        update = getCommandWithObject(update, currentBundle.getString("update"), selectedPerson);
                        if (update != null){
                            sendPreparedCommand(update);
                        }
                    } else{
                        sendPreparedCommand(getCommandWithObject(new Insert(), currentBundle.getString("add"), null));
                    }
                }
            });
            return row;
        });
    }

    public void sendPreparedCommand(AbstractCommand command){
        try{
            if (command != null){
                clientMessagesHandler.sendCommand(command);
                receiveAnswer();
            } else{
                showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("InvalidData"));
            }
        }catch (Exception e){
            showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("UnexpectedException") + e.getMessage());
        }
    }

    public void receiveAnswer(){
        try{
          String answer;
          StringBuilder content = new StringBuilder();
          while (true){
              answer = clientMessagesHandler.receiveAnswer();
              if(answer.contains("I'm fucking seriously, it's fucking EOF!!!")) break;
              if (!answer.isEmpty() && !answer.equals("collectionReceived")){
                  content.append(answer);
              } else if (answer.equals("collectionReceived")){
                  filterTableWorker.setUpdateRQ(true);
              }
          }
          if(!content.toString().isEmpty()){
              showAlert(Alert.AlertType.INFORMATION, getCurrentBundle().getString("Info"), content.toString());
          }
        }catch (Exception e){
            showAlert(Alert.AlertType.ERROR, getCurrentBundle().getString("Error"), getCurrentBundle().getString("UnexpectedException") + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setHeight(1000);
        alert.setWidth(1000);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK){
                System.out.println(currentBundle.getString("windowClose"));
            }
        });
    }

    private void changeLanguage(){
        visualisationWorker.setCurrentBundle(currentBundle);

        //Блок верхнего меню
        menuFile.setText(currentBundle.getString("file"));
        exit.setText(currentBundle.getString("exit"));

        menuHelp.setText(currentBundle.getString("help"));
        help.setText(currentBundle.getString("helpCom"));
        info.setText(currentBundle.getString("info"));

        selectLanguage.setText(currentBundle.getString("language"));

        //Блок нижнего меню (ТулБар)
        usernameLabel.setText(currentBundle.getString("username") + clientMessagesHandler.getLogin());
        updateTableLable.setText(currentBundle.getString("updateTable"));

        //Блок команд:
        addCmdLabel.setText(currentBundle.getString("addCommands"));
        insert.setText(currentBundle.getString("insertCom"));

        removeCmdLabel.setText(currentBundle.getString("removeCommands"));
        remove_key.setText(currentBundle.getString("removeID"));
        remove_any_by_birthday.setText(currentBundle.getString("removeBirthday"));
        remove_greater.setText(currentBundle.getString("removeGreater"));
        remove_greater_key.setText(currentBundle.getString("removeGreaterID"));;
        clear.setText(currentBundle.getString("clearCom"));

        updateCmdLabel.setText(currentBundle.getString("updateCommands"));
        update.setText(currentBundle.getString("updateCom"));

        printCmdLabel.setText(currentBundle.getString("printCommands"));
        count_less_then_location.setText(currentBundle.getString("countLessCom"));
        print_ascending.setText(currentBundle.getString("printCom"));
        history.setText(currentBundle.getString("historyCom"));

        //Блок таблиц и визуализации
        tabPerson.setText(currentBundle.getString("tabPerson"));
        tabVisual.setText(currentBundle.getString("tabVisual"));
    }

    public ResourceBundle getCurrentBundle(){
        return currentBundle;
    }

    public AbstractCommand getCommandWithSimpleArg(AbstractCommand command, String labelText){
        try{
            String fxmFile = "../../fxml/arg.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmFile));
            fxmlLoader.setResources(currentBundle);

            Parent root = fxmlLoader.load();
            ArgDialogController argDialogController = fxmlLoader.getController();
            argDialogController.getArgName().setText(labelText);
            argDialogController.getArgName().setAlignment(Pos.CENTER);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(currentBundle.getString("InputData"));
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));
            dialogStage.requestFocus();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

            String receivedArg = argDialogController.getCommandArg();
            if (receivedArg != null){
                command.setArgs(new String[]{receivedArg});
                return command;
            }
        }catch (Exception e){
            showAlert(Alert.AlertType.ERROR, currentBundle.getString("Error"), currentBundle.getString("UnexpectedException") + e.getMessage());
        }
        return null;
    }

    public AbstractCommand getCommandWithObject(AbstractCommand command, String tittleText, Person person){
        try{
            String fxmFile = "../../fxml/personDialog.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmFile));
            fxmlLoader.setResources(currentBundle);

            Parent root = fxmlLoader.load();
            PersonDialogController personDialogController = fxmlLoader.getController();
            personDialogController.setCurrentBundle(currentBundle);
            personDialogController.setParametersPersonLang();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(tittleText);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(root));
            dialogStage.requestFocus();
            dialogStage.initModality(Modality.WINDOW_MODAL);

            if (person != null){
                personDialogController.getNameField().setText(person.getName());
                personDialogController.getCoordinateXField().setText(String.valueOf(person.getCoordinates().getX()));
                personDialogController.getCoordinateYField().setText(String.valueOf(person.getCoordinates().getY()));
                personDialogController.getHeightField().setText(String.valueOf(person.getHeight()));
                personDialogController.getBirthdayField().setText(String.valueOf(person.getBirthday()));
                personDialogController.getPassportIdField().setText(person.getPassportID());
                personDialogController.getHairColorBox().setValue(person.getHairColor());
                personDialogController.getLocationNameField().setText(person.getLocation().getName());
                personDialogController.getLocationXField().setText(String.valueOf(person.getLocation().getX()));
                personDialogController.getLocationYField().setText(String.valueOf(person.getLocation().getY()));
                personDialogController.getLocationZField().setText(String.valueOf(person.getLocation().getZ()));
            }

            dialogStage.showAndWait();
            person = personDialogController.getPerson();
            if(person != null){
                command.setPerson(person);
                return command;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getPersonInLocalLanguage(Person person){
        return currentBundle.getString("PersonCreatedByUser") + person.getCreatedByUser() + currentBundle.getString("with") +
                "id = " + person.getId() +
                currentBundle.getString("Pname") + person.getName() + '\'' +
                currentBundle.getString("cooridanateX") + person.getCoordinates().getX() + ", y= " + person.getCoordinates().getY() +
                currentBundle.getString("Pheight") + person.getHeight() +
                currentBundle.getString("Pbirthday") + person.getBirthday() +
                currentBundle.getString("PpassportID") + person.getPassportID() +
                currentBundle.getString("PhairColor") + person.getHairColor() +
                currentBundle.getString("LocationName") + person.getLocation().getName() +
                currentBundle.getString("LocationX") + person.getLocation().getX() + ", y= " + person.getLocation().getY() + ", z= " + person.getLocation().getZ() +
                "}}\n";
    }

    public void setCurrentBundle(ResourceBundle currentBundle){
        this.currentBundle = currentBundle;
    }

}
