package views;

import model.Dot;

//Проверка на попадание точки в область
public class AreaResult {

    public AreaResult() {
    }


    //Проверка по самой точке (В виде объекта)
    public static boolean isItInArea(Dot dot){
        try {
            return isItInArea(dot.getX(), dot.getY(), dot.getR());
        }catch (NullPointerException e){
            return false;
        }
    }

    //Проверка точки по её координатам
    public static boolean isItInArea(double x, double y, double r){
        return ((inSecondQuad(x, y, r) || inFirstQuad(x, y, r) ||
                 inThirdQuad(x, y, r)  || inFourthQuad(x, y, r) ))
                && r>=0;
    }

    //Проверка попадания точки по координатным четвертям
    private static boolean inFirstQuad(double x, double y, double r){
        return false;
    }

    private static boolean inSecondQuad(double x, double y, double r){
        return x <= 0 &&
                y>= 0 &&
                y <= r &&
                x >= -r/2;
    }

    private static boolean inThirdQuad(double x, double y, double r){
        return x <= 0 &&
                y <= 0 &&
                y >= -r/2 &&
                x >= -r/2 &&
                y >= -x - r/2;
    }

    private static boolean inFourthQuad(double x, double y, double r){
        return x >= 0 &&
                y <= 0 &&
                x*x + y*y <= r*r / 4;
    }
}
