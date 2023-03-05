

import java.util.Objects;

abstract class AHuman implements IHuman {
    private String name;
    private HandType type;
    private Skills skill;
    private String pName;
    private Coordinates location;
    private Double x;
    private Double y;

    @Override
    public void getName() {
        System.out.println(this.name);
    }

    @Override
    public HandType getHandType() {
        return this.type;
    }

    public void getX() {
        System.out.println(this.x);
    }

    public void getY() {
        System.out.println(this.y);
    }

    @Override
    public void jump(String name, double x, double y) {
        System.out.println(location.getCooridanetes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, pName, location, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AHuman aHuman = (AHuman) o;
        return Objects.equals(name, aHuman.name) &&
                type == aHuman.type &&
                Objects.equals(pName, aHuman.pName) &&
                Objects.equals(location, aHuman.location) &&
                Objects.equals(x, aHuman.x) &&
                Objects.equals(y, aHuman.y);
    }

    @Override
    public String toString() {
        return "AHuman{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", pName='" + pName + '\'' +
                ", location=" + location +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public void run(Place place) {
        System.out.println(this.name + " бежит");
    }

}
