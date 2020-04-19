package p3111.redgry.collection.collection;

import p3111.redgry.exceptions.InvalidInputException;

/**
 * The enum Color.
 */
public enum Color implements Comparable<Color>{
    GREEN("Зеленый"),
    BLACK("Черный"),
    YELLOW("Желтый"),
    ORANGE("Оранжевый"),
    WHITE("Белый"),
    RED ("Красный");

    private String rus;

    Color(String rus){
        this.rus = rus;
    }

    public static Color byOrdinal(String s){
        for (Color value : Color.values()){
            if (value.getRus().equalsIgnoreCase(s)){
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

    public String getRus(){
        return rus;
    }
}
