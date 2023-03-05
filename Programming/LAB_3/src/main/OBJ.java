

public class OBJ extends AOBJ {
    private String name;
    public static TypeOBJ type1;
    public double x;
    public double y;

    OBJ(String name) {
        this.name = name;
        x = 0;
        y = 0;
        this.type1 = TypeOBJ.beautiful;
    }

    OBJ(String name, TypeOBJ _type) {
        this.name = name;
        x = 0;
        y = 0;
        this.type1 = _type;
    }

    public static String getName() {
        String name = "Персик";
        return name;
    }

    @Override
    public double getX(double x) {
        this.x = x;
        return this.x;
    }

    @Override
    public double getY(double y) {
        this.y = y;
        return this.y;
    }

    public static double setY(double y) {
        return y;
    }

    public static double setX(double x) {
        return x;
    }


    public void getCord() {
        System.out.println(this.name + ", координаты X: " + this.x + " | Y: " + this.y);
    }

    @Override
    public void status() {
        System.out.println("Обьект " + name + " неподвижен");
    }

    public void getType() {
        if (this.type1 == TypeOBJ.normal) {
            System.out.println(getName() + " нормальный.");
        } else if (this.type1 == TypeOBJ.beautiful) {
            System.out.println(getName() + " замечательный.");
        } else if (this.type1 == TypeOBJ.compressed) {
            System.out.println(getName() + " сдавленный.");
        } else if (name == "Персик") {
            if (this.type1 == TypeOBJ.rotten) {
                System.out.println(getName() + " гнилой.");
            }
        }
    }
}
