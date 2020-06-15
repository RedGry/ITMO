package p3111.redgry.utils.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import p3111.redgry.collection.collection.Color;
import p3111.redgry.collection.collection.Coordinates;
import p3111.redgry.collection.collection.Location;
import p3111.redgry.collection.collection.Person;

public class PersonDialogController {
    private Person person = null;
    private ResourceBundle currentBundle;

    public void initialize(){
        ObservableList<Color> colorObservableList = FXCollections.observableArrayList(Color.GREEN, Color.BLACK, Color.YELLOW, Color.ORANGE, Color.WHITE, Color.RED);
        hairColorBox.getItems().addAll(colorObservableList);
    }

    @FXML private Label parameters_person;
    @FXML private TextField nameField;
    @FXML private TextField coordinateXField;
    @FXML private TextField coordinateYField;
    @FXML private TextField heightField;
    @FXML private TextField birthdayField;
    @FXML private TextField passportIdField;
    @FXML private ComboBox<Color> hairColorBox;
    @FXML private TextField locationNameField;
    @FXML private TextField locationXField;
    @FXML private TextField locationYField;
    @FXML private TextField locationZField;

    @FXML void CancelClick(ActionEvent actionEvent) {
        Stage thisStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        thisStage.close();
    }

    @FXML void OKClick(ActionEvent actionEvent) {
        String name = nameField.getText();
        if(name.trim().isEmpty()){
            showAlert("Name", currentBundle.getString("IncorrectDataField"));
            return;
        }
        long cor_x, height;
        double cor_y, loc_x, loc_y, loc_z;
        java.time.LocalDate birthday;

        try {
            cor_x = Long.parseLong(coordinateXField.getText());
            if (cor_x > 100 || cor_x < -100) {
                showAlert("Coordinate (X)", currentBundle.getString("IncorrectNumber"));
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Coordinate (X)", currentBundle.getString("IncorrectDataField"));
            return;
        }

        try {
            cor_y = Double.parseDouble(coordinateYField.getText());
            if (cor_y > 100 || cor_y < -100) {
                showAlert("Coordinate (Y)", currentBundle.getString("IncorrectNumber"));
                return;
            }
        }catch (Exception e) {
            showAlert("Coordinate (Y)", currentBundle.getString("IncorrectDataField"));
            return;
        }

        try {
            height = Long.parseLong(heightField.getText());
        }catch (Exception e) {
            showAlert("Height", currentBundle.getString("IncorrectDataField"));
            return;
        }

        try {
            birthday = LocalDate.parse(birthdayField.getText());
        } catch (Exception e){
            birthday = LocalDate.now();
        }

        String loc_name = locationNameField.getText();
        String passportID = passportIdField.getText();

        try {
            loc_x = Double.parseDouble(locationZField.getText());
        }catch (Exception e) {
            showAlert("Localation (X)", currentBundle.getString("IncorrectDataField"));
            return;
        }
        try {
            loc_y = Double.parseDouble(locationYField.getText());
        }catch (Exception e) {
            showAlert("Localation (Y)", currentBundle.getString("IncorrectDataField"));
            return;
        }
        try {
            loc_z = Double.parseDouble(locationZField.getText());
        }catch (Exception e) {
            showAlert("Localation (Z)", currentBundle.getString("IncorrectDataField"));
            return;
        }

        Color hairColor = hairColorBox.getValue();
        if (hairColor == null){
            showAlert("HairColor", currentBundle.getString("IncorrectDataField"));
            return;
        }

        try{
            person = new Person(name, new Coordinates(cor_x, cor_y), height, birthday, passportID, hairColor, new Location(loc_x, loc_y, loc_z, loc_name));
            Stage thisStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            thisStage.close();
        }catch (Exception e){
            showAlert(currentBundle.getString("Error"), currentBundle.getString("ErrorCreatingObject"));
        }
    }

    private void showAlert(String fieldName, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(currentBundle.getString("Error"));
        alert.setHeaderText(currentBundle.getString("IncorrectField") + "\"" + fieldName + "\"");
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println(currentBundle.getString("windowClose"));
            }
        });
    }

    public Person getPerson(){return person;}
    public TextField getNameField(){
        return nameField;
    }

    public ResourceBundle getCurrentBundle() {
        return currentBundle;
    }

    public TextField getCoordinateXField() {
        return coordinateXField;
    }

    public TextField getCoordinateYField() {
        return coordinateYField;
    }

    public TextField getHeightField() {
        return heightField;
    }

    public TextField getBirthdayField() {
        return birthdayField;
    }

    public TextField getPassportIdField() {
        return passportIdField;
    }

    public ComboBox<Color> getHairColorBox() {
        return hairColorBox;
    }

    public TextField getLocationNameField() {
        return locationNameField;
    }

    public TextField getLocationXField() {
        return locationXField;
    }

    public TextField getLocationYField() {
        return locationYField;
    }

    public TextField getLocationZField() {
        return locationZField;
    }

    public void setCurrentBundle(ResourceBundle currentBundle){
        this.currentBundle = currentBundle;
    }

    public void setParametersPersonLang(){
        parameters_person.setText(currentBundle.getString("parameters_person"));
    }
}
