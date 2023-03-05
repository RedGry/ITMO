

public class Human extends AHuman {
    private String name;
    private HandType handType;
    private String place;
    private String locations;
    private double x;
    private double y;
    private Coordinates location;
    private TypeOBJ typeOBJ;
    private Gender gender;

    Human(String name, Gender gender, HandType type, double x, double y) {
        this.name = name;
        this.handType = type;
        this.x = x;
        this.y = y;
        this.gender = gender;
        System.out.println("Персонаж " + this.name + " создан с" + Type(this.handType) + ".");
    }

    Human(String name, Gender gender) {
        this.name = name;
        this.x = 0;
        this.y = 0;
        this.gender = gender;
        this.handType = HandType.normal;
        System.out.println("Персонаж " + this.name + " создан.");
    }

    Human(String name, Gender gender, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.gender = gender;
        this.handType = HandType.normal;
        System.out.println("Персонаж " + this.name + " создан.");
    }


    public void see() {
        System.out.println(name + " смотрит по сторонам!");
    }


    private String Type(HandType type) {
        String _HandType;
        if (type == HandType.podgy) {
            _HandType = " пухленькими ручками";
        } else if (type == HandType.small) {
            _HandType = " маленькими ручками";
        } else if (type == HandType._long) {
            _HandType = " длинными ручкками";
        } else if (type == HandType.thin) {
            _HandType = " тонкими ручкками";
        } else if (type == HandType.normal) {
            _HandType = " обычными ручкками";
        } else
            _HandType = " ручками";
        return (_HandType);
    }

    public void jump(String locations, double x, double y) {
        this.locations = locations;
        this.x = x;
        this.y = y;
        if (this.gender == Gender.female) {
            System.out.println(name + " бросилась на место: " + this.locations);
        } else if (this.gender == Gender.male | this.gender == Gender.shemale) {
            System.out.println(name + " бросился на место: " + this.locations);
        }
        if (this.name == "Карлсон") {
            System.out.println("Словно коршун на добычу");
        }
    }


    public void Compress(String obj) {
        String objname = obj;
        OBJ.type1 = TypeOBJ.compressed;
        System.out.println(name + " жадно сжимает " + objname + Type(this.handType));
        System.out.println(objname + " сдавлен!");
    }

    public void put(double x, double y) {
        this.x = x;
        this.y = y;
        if (this.gender == Gender.female) {
            System.out.println(name + " положила " + OBJ.getName());
        } else System.out.println(name + " положил " + OBJ.getName());
    }

    public void run(double x, double y) {
        this.x = x;
        this.y = y;
        if (this.gender == Gender.male | this.gender == Gender.shemale) {
            System.out.println(name + " побежал на место X: " + setX(this.x) + " | Y: " + setY(this.y));
        } else
            System.out.println(name + " побежала на место X: " + setX(this.x) + " | Y: " + setY(this.y));
    }

    void took(double x, double y) {
        this.x = x;
        this.y = y;
        OBJ.setX(x);
        OBJ.setY(y);
        if (this.gender == Gender.female) {
            System.out.println(name + " взяла " + OBJ.getName());
        } else System.out.println(name + " взял " + OBJ.getName());
    }

    public void Throw(String obj, double x, double y) {
        OBJ.setX(Math.random() * 10);
        OBJ.setY(Math.random() * 10);
        if (this.gender == Gender.female) {
            System.out.println(name + " кинула " + OBJ.getName());
        } else System.out.println(name + " кинул " + OBJ.getName());
    }


    private double setX(double x) {
        this.x = x;
        return x;
    }

    private double setY(double y) {
        this.y = y;
        return y;
    }

    public void getCord() {
        System.out.println(this.name + " координата X: " + this.x + " | Y: " + this.y);
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", type=" + handType +
                ", place='" + place + '\'' +
                ", locations='" + locations + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", location=" + location +
                ", type1=" + typeOBJ +
                ", gender=" + gender +
                '}';
    }
}
