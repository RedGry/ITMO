package p3111.redgry.collection.collection;

import lombok.NonNull;
import p3111.redgry.exceptions.IncorrectValueException;

import java.io.Serializable;

/**
 * Модель координат
 */
public class Coordinates implements Serializable {
    private Long x; //Поле не может быть null
    private Double y; //Значение поля должно быть больше -537, Поле не может быть null

    public Coordinates(Long x, Double y){
        this.x = x;
        this.y = y;
    }

    public Coordinates(){
    }

    @Override
    public String toString(){
        return "x = " + x + ", y = " + y;
    }

    public Long getX(){
        return x;
    }

    public void setX(@NonNull Long x){
        this.x = x;
    }

    public Double getY(){
        return y;
    }

    public void setY(@NonNull Double y) throws IncorrectValueException {
        if (y > -537) {
            this.y = y;
        }
        else throw new IncorrectValueException("Число не должно быть меньше меньше или равно -537");
    }
}
