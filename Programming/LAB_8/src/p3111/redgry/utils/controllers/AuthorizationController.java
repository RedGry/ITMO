package p3111.redgry.utils.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import p3111.redgry.commands.commands.specialCommands.Login;
import p3111.redgry.commands.commands.specialCommands.Register;
import p3111.redgry.utils.Handlers.ClientMessagesHandler;

public class AuthorizationController {
    private MainController mainController;
    private ClientMessagesHandler clientMessagesHandler;

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button logining;
    @FXML private Button registration;
    @FXML private Menu selecteLanguage;
    @FXML private Label registrationLogin;

    @FXML void loginClick(ActionEvent event) {
        try {
            String login1 = login.getText();
            String password1 = password.getText();
            clientMessagesHandler.setLogin(login1);
            clientMessagesHandler.setPassword(password1);
            clientMessagesHandler.sendCommand(new Login());
            String answer = "";
            while (answer.isEmpty()){
                answer = clientMessagesHandler.receiveAnswer();
            }
            if (answer.equals("suc_login")){
                showAlert(Alert.AlertType.INFORMATION, mainController.getCurrentBundle().getString("Entry"), null, mainController.getCurrentBundle().getString(answer));
                Stage authorizedStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                clientMessagesHandler.setAuthorized(true);
                clientMessagesHandler.setLogin(login1);
                authorizedStage.close();
            }
            else {
                showAlert(Alert.AlertType.ERROR, mainController.getCurrentBundle().getString("Error"), null, mainController.getCurrentBundle().getString("err_login"));
            }
        } catch (Exception e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, mainController.getCurrentBundle().getString("Error"), null, mainController.getCurrentBundle().getString("UnexpectedException") + e.getMessage());
        }
    }

    @FXML void registrationClick(ActionEvent event) {
        try {
            String login1 = login.getText();
            String password1 = password.getText();

            clientMessagesHandler.setLogin(login.getText());
            clientMessagesHandler.setPassword(password.getText());

            boolean lessThen4 = login1.trim().split("\\s+")[0].length() < 4;
            boolean withSpaces = login1.trim().split("\\s+").length != 1;
            boolean invalidChars = !login1.trim().split("\\s+")[0].matches("[a-z0-9]+");
            if (!lessThen4 && !withSpaces && !invalidChars) {
                lessThen4 = password1.trim().split("\\s+")[0].length() < 4;
                withSpaces = password1.trim().split("\\s+").length != 1;
                invalidChars = !password1.trim().split("\\s+")[0].matches("[a-z0-9]+");
                if (!lessThen4 && !withSpaces && !invalidChars) {
                    clientMessagesHandler.sendCommand(new Register());
                    String answer = "";

                    while (answer.isEmpty()){
                        answer = clientMessagesHandler.receiveAnswer();
                    }
                    if (answer.contains("suc_register")){
                        showAlert(Alert.AlertType.INFORMATION, mainController.getCurrentBundle().getString("info"), null, mainController.getCurrentBundle().getString("suc_register"));
                    } else {
                        showAlert(Alert.AlertType.ERROR, mainController.getCurrentBundle().getString("Error"), null, mainController.getCurrentBundle().getString("err_register"));
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, mainController.getCurrentBundle().getString("Error"), mainController.getCurrentBundle().getString("incorrectLogPas"), mainController.getCurrentBundle().getString("uncorrectLogPas"));
                }
            } else {
                showAlert(Alert.AlertType.ERROR, mainController.getCurrentBundle().getString("Error"), mainController.getCurrentBundle().getString("incorrectLogPas"), mainController.getCurrentBundle().getString("uncorrectLogPas"));
            }

        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR, mainController.getCurrentBundle().getString("Error"), null, mainController.getCurrentBundle().getString("UnexpectedException") + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setClientMessagesHandler(ClientMessagesHandler clientMessagesHandler){
        this.clientMessagesHandler = clientMessagesHandler;
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    @FXML private void switchRussian(){
        mainController.setCurrentBundle(ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("ru", "RU")));
        changeLanguage();
    }

    @FXML void switchSpanish() {
        mainController.setCurrentBundle(ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("es", "DO")));
        changeLanguage();
    }

    @FXML void switchTurkish() {
        mainController.setCurrentBundle(ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("tr", "TR")));
        changeLanguage();
    }

    @FXML void switchUkrainian() {
        mainController.setCurrentBundle(ResourceBundle.getBundle("p3111.redgry.bundles.ClassLanguage", new Locale("ua", "UA")));
        changeLanguage();
    }

    private void changeLanguage(){
        selecteLanguage.setText(mainController.getCurrentBundle().getString("selectLanguage"));
        registrationLogin.setText(mainController.getCurrentBundle().getString("registrationLogin"));
        logining.setText(mainController.getCurrentBundle().getString("loginBut"));
        registration.setText(mainController.getCurrentBundle().getString("regBut"));
        registrationLogin.setAlignment(Pos.CENTER);
    }

    public void setregistrationLoginPos(Pos pos){
        registrationLogin.setAlignment(pos);
    }

}
