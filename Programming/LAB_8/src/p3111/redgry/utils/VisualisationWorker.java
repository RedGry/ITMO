package p3111.redgry.utils;

import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import p3111.redgry.collection.collection.Person;
import p3111.redgry.commands.commands.AbstractCommand;
import p3111.redgry.commands.commands.RemoveByKey;
import p3111.redgry.commands.commands.Update;
import p3111.redgry.exceptions.InvalidCountOfArgumentException;
import p3111.redgry.utils.Handlers.ClientMessagesHandler;
import p3111.redgry.utils.controllers.MainController;

import javafx.util.Duration;
import java.util.*;

public class VisualisationWorker {
    private ResourceBundle currentBundle;
    private MainController mainController;
    private ClientMessagesHandler updateCollectionClientMessagesHandler;
    private Pane pane;
    private HashMap<String, Color> users = new HashMap<>();
    private HashMap<Circle, Person> circlePersonHashMap = new HashMap<>();

    public VisualisationWorker(Pane pane, ClientMessagesHandler updateCollectionClientMessagesHandler, MainController mainController){
        this.pane = pane;
        this.updateCollectionClientMessagesHandler = updateCollectionClientMessagesHandler;
        this.mainController = mainController;
        currentBundle = mainController.getCurrentBundle();
    }

    public void setupVisualisation(){
        setChangeSizeListeners();
        circlePersonHashMap.clear();
        pane.getChildren().clear();
        PriorityQueue<Person> person = updateCollectionClientMessagesHandler.getClientPq();
        for (Person person1 : person){
            setupPerson(person1);
        }
    }

    private void setupPerson(Person person){
        new Random();
        if (!users.containsKey(person.getCreatedByUser())){
            users.put(person.getCreatedByUser(), Color.color(Math.random(), Math.random(), Math.random()));
        }
        float radius = person.getHeight() / 100;
        Circle circle = new Circle(radius * (pane.getHeight() + pane.getWidth() * 0.666) / 100);
        setCoordinates(circle, person.getCoordinates().getX(), person.getCoordinates().getY());

        circle.setStroke(users.get(person.getCreatedByUser()));
        circle.setFill(users.get(person.getCreatedByUser()).deriveColor(1,1,1,0.7));
        circle.setOnMousePressed(circleOnMousePressedEventHandler);
        circlePersonHashMap.put(circle, person);

        pane.getChildren().add(circle);
    }

    private void setChangeSizeListeners(){
        pane.widthProperty().addListener((ChangeListener<? super Number>) (observable, oldValue, newValue) ->{
            pane.getChildren().clear();
            for (Person person : updateCollectionClientMessagesHandler.getClientPq()){
                setupPerson(person);
            }
        });
        pane.heightProperty().addListener((ChangeListener<? super Number>) (observalbe, oldValue, newValue) ->{
            pane.getChildren().clear();
            for (Person person : updateCollectionClientMessagesHandler.getClientPq()){
                setupPerson(person);
            }
        });
    }

    private void setCoordinates(Circle circle, double x, double y){
        double newX = x * pane.getWidth() / 250 + pane.getWidth() / 2;
        double newY = -y * pane.getHeight() / 250 + pane.getWidth() / 2;
        circle.setLayoutX(newX);
        circle.setLayoutY(newY);
    }

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(0),
                new KeyValue (new SimpleDoubleProperty(), 0),
                new KeyValue (new SimpleDoubleProperty(), 0.3)
            ),
            new KeyFrame(Duration.seconds(0),
                new KeyValue (new SimpleDoubleProperty(), 1),
                new KeyValue (new SimpleDoubleProperty(), 1.0)
            )
    );

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
        }
    };

    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>(){
        @Override
        public void handle(MouseEvent t){
            if (t.getSource() instanceof Circle){
                Circle selectedCircle = ((Circle) (t.getSource()));
                Person selectedPerson = circlePersonHashMap.get(selectedCircle);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(currentBundle.getString("info"));
                alert.setHeaderText(currentBundle.getString("uPick"));
                alert.setContentText(mainController.getPersonInLocalLanguage(selectedPerson));

                ButtonType close = new ButtonType(currentBundle.getString("exit"));
                ButtonType edit = new ButtonType(currentBundle.getString("update"));
                ButtonType delete = new ButtonType(currentBundle.getString("removeID"));

                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(delete, edit, close);

                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == close){
                    alert.close();
                } else if (option.get() == edit){
                    AbstractCommand update = new Update();
                    try {
                        update.setArgs(new String[]{String.valueOf(selectedPerson.getId())});
                    } catch (InvalidCountOfArgumentException e) {
                        e.printStackTrace();
                    }
                    mainController.sendPreparedCommand(mainController.getCommandWithObject(update, currentBundle.getString("update"), selectedPerson));
                } else if (option.get() == delete){
                    AbstractCommand removeById = new RemoveByKey();
                    try {
                        removeById.setArgs(new String[]{String.valueOf(selectedPerson.getId())});
                    } catch (InvalidCountOfArgumentException e) {
                        e.printStackTrace();
                    }
                    mainController.sendPreparedCommand(removeById);
                }
            }
        }
    };

    public void setCurrentBundle(ResourceBundle currentBundle){
        this.currentBundle = currentBundle;
    }

}
