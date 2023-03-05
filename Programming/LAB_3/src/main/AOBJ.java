

public abstract class AOBJ implements IOBJ {
    private String name;
    private double x;
    private double y;

    public double getX(double x) {
        this.x = x;
        return this.x;
    }

    public double getY(double y) {
        this.x = x;
        return this.x;
    }

    @Override
    public void status() {
        System.out.println("Обьект " + this.name + " неподвижен");
    }
}
