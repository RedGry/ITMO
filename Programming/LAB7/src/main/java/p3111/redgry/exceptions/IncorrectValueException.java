package p3111.redgry.exceptions;

/**
 * Исключение, обозначающее некоректно введенное значение.
 */
public class IncorrectValueException extends Exception{

    public IncorrectValueException(String message){
        super(message);
    }
}
