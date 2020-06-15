package p3111.redgry.collection.collection;

import javafx.beans.property.SimpleStringProperty;
import p3111.redgry.exceptions.InvalidInputException;

import java.io.Serializable;

/**
 * The enum Color.
 */
public enum Color implements Comparable<Color>, Serializable {
    GREEN("Зеленый", 0),
    BLACK("Черный", 1),
    YELLOW("Желтый", 2),
    ORANGE("Оранжевый", 3),
    WHITE("Белый", 4),
    RED ("Красный", 5);

    private String rus;
    private int id;

    Color(String rus, int id){
        this.rus = rus;
        this.id = id;
    }

    public static Color byOrdinal(String s){
        for (Color value : Color.values()){
            int id = -1;
            if (s.equals("0") || s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5")){
                id = Integer.parseInt(s);
            }
            if (s.equals(value.getRus()) || id == value.getId()){
                return value;
            }
        }
        throw new InvalidInputException("Не найден вид, соответствующей строке: " + s);
    }

    public static boolean checkColor(String color){
        for (Color value : Color.values()){
            if (value.getRus().equalsIgnoreCase(color)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkId(String idColor){
        for (Color value : Color.values()){
            if (value.getId() == Integer.parseInt(idColor)){
                return true;
            }
        }
        return false;
    }

    public String getRus(){
        return rus;
    }

    public SimpleStringProperty getRusProperty(){
        return new SimpleStringProperty(rus);
    }

    public int getId(){return id;}
}
