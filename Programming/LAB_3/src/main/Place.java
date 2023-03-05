

public class Place {
    private String name;
    private Coordinates location;

    public Place(String name) {
        this.name = name;
        this.location = null;
        System.out.println("Место - " + name + " ( " + location.getName() + " )");
    }

    public Place(String name, Coordinates location) {
        this.name = name;
        this.location = location;
        System.out.println("Место - " + name + " ( " + location.getName() + " )");
    }

    public String getName() {
        return this.name == null ? "..." : this.name;
    }

    public Coordinates getLocation() {
        return this.location;
    }


}
