package p3111.redgry.utils.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ArgDialogController {
    String commandArg = null;

    @FXML private TextField inputArg;
    @FXML private Label argName;

    @FXML void CancelClick(ActionEvent actionEvent) {
        Stage thisStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        thisStage.close();
    }

    @FXML void OKClick(ActionEvent actionEvent) {
        commandArg = inputArg.getText();
        Stage thisStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        thisStage.close();
    }

    public Label getArgName(){
        return argName;
    }

    public String getCommandArg(){
        return commandArg;
    }

}
