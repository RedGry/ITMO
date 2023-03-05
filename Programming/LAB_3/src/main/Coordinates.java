

public class Coordinates {
    public String name;
    public Double x;
    public Double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
        this.name = x.toString() + " / " + y.toString();
    }

    public Coordinates(double x, double y, String _name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public String getName() {
        return this.name;
    }

    public double[] getCooridanetes() {
        double[] coord = {this.x, this.y};
        return coord;
    }
}
