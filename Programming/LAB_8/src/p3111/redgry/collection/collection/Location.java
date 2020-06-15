package p3111.redgry.collection.collection;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.NonNull;

import java.io.Serializable;

public class Location implements Serializable {
    private double x;
    private Double y; //Поле не может быть null
    private double z;
    private String name; //Поле не может быть null

    public Location( double x, Double y, double z, String name){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(@NonNull Double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public SimpleDoubleProperty getXProperty() {
        return new SimpleDoubleProperty(x);
    }

    public SimpleDoubleProperty getYProperty() {
        return new SimpleDoubleProperty(y);
    }

    public SimpleDoubleProperty getZProperty() {
        return new SimpleDoubleProperty(z);
    }

    public SimpleStringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }
}
