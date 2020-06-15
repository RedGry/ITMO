package p3111.redgry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
            primaryStage.setTitle("ClientApplication");
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        System.exit(0);
    }
}
