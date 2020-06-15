package p3111.redgry.utils;

import p3111.redgry.exceptions.InvalidInputException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class UserInterface {
    private final Reader reader;
    private final Writer writer;
    private final Scanner scanner;
    private final boolean interactive;

    /**
     * Конструктор, который создает интерфейс и определяет куда писать и откуда.
     *
     * @param reader      откуда считывать
     * @param writer      куда писать
     * @param interactive флаг, обозначающий режим работы интерфейса.
     */
    public UserInterface(Reader reader, Writer writer, boolean interactive){
        this.reader = reader;
        this.writer = writer;
        this.interactive = interactive;
        this.scanner = new Scanner(reader);
    }

    /**
     * Вывод сообщения на экран пользователя с переносом на новую строку.
     *
     * @param message строка для вывода.
     */
    public void writeln(String message){
        write(message + "\n");
    }

    /**
     * Вывод сообщения.
     *
     * @param message Строка для вывода
     */
    public void write(String message){
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e){
            e.getMessage();
        }
    }

    /**
     * Метод, запрашивающий ввод из стандартного потока ввода. Перед вводом выводит сообщение в стандартный поток вывода.
     *
     * @param message  сообщение для пользователя, будет выведено перед вводом.
     * @param nullable флаг. True - если мы допускаем пустой ввод от пользователя. False - если нам надо добиться от него не пустого ввода.
     * @return введенную строку пользователя, или null если пользователь ввел пустую строку и флаг nullable равен true.
     */
    public String readWithMessage(String message, boolean nullable){
        String result = "";
        do {
            if (result == null){
                writeln("");
            }
            if (interactive){
                writeln(message);
            }
            result = scanner.nextLine();
            result = result.isEmpty() ? null : result;
        } while (interactive && !nullable && result == null);
        if (!interactive && result == null) {
            throw new InvalidInputException("Получена пустая строка в поле, которое не может быть null");
        }
        return  result;
    }

    /**
     * Считывает из потока число и проверяет его на вхождение в промежуток [min; max]. При не корректном вводе, запрашивается повторный ввод.
     * Перед вводом выводит сообщение в стандартный поток вывода.
     *
     * @param message сообщение для пользователя, будет выведено перед вводом.
     * @param min     нижняя граница (-1, если неважна)
     * @param max     вверхняя граница (-1, если не важна)
     * @return введенное пользователем число.
     */
    public String readWithMessage(String message, int min, int max){
        String result;
        do {
            result = readWithMessage(message, false);
        } while (!checkNumber(Double.parseDouble(result), min, max));
        return result;
    }

    /**
     * Метод проверяющий числа которые попадают в промежуток [min, max].
     *
     * @param s   число пользователя.
     * @param min нижняя граница.
     * @param max вверхняя граница.
     * @return ввозвращает True - если число принадлежит промежутку [min, max].
     */
    public static boolean checkNumber(double s, int min, int max){
        return ((min < 0 || s >= min) && (max < 0 || s <= max));
    }

    /**
     * Метож возвращающий есть ли что считывать из входного потока.
     *
     * @return есть ли ещё что считывать.
     */
    public boolean hashNextLine(){
        return scanner.hasNextLine();
    }

    /**
     * Метод для считывания.
     *
     * @return возращает считаную строку.
     */
    public String read(){
        return scanner.nextLine();
    }


}
