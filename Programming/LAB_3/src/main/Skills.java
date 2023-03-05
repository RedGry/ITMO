

public class Skills {
    private String name;
    private String info;
    private int mana;


    public Skills(String name, String info) {
        this.name = name;
        this.info = info;
        this.mana = 100;
    }

    public Skills(String name, String info, int mana) {
        this.name = name;
        this.info = info;
        this.mana = mana;
    }

    public String getName() {
        return name;
    }

    public int getMana() {
        return mana;
    }

    public String getInfo() {
        return info == null ? "..." : info;
    }
}
