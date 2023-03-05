
import ru.ifmo.se.pokemon.*;

class Registeel extends Pokemon {
    Registeel(String name, int level) {
        super(name, level);
        setStats(80, 75, 150, 75, 150, 50);
        setType(Type.STEEL);
        setMove(new IronHead(), new Stomp(), new Facade(), new Rest());
    }
}

class Eevee extends Pokemon {
    Eevee(String name, int level) {
        super(name, level);
        setStats(55, 55, 50, 45, 65, 55);
        setType(Type.NORMAL);
        setMove(new SandAttack(), new Facade(), new Rest());
    }
}

class Glaceon extends Pokemon {
    Glaceon(String name, int level) {
        super(name, level);
        setStats(65, 60, 110, 130, 95, 65);
        setType(Type.ICE);
        setMove(new SandAttack(), new Facade(), new Rest(), new Barrier());
    }
}

class Azurill extends Pokemon {
    Azurill(String name, int level) {
        super(name, level);
        setStats(50, 20, 40, 20, 40, 20);
        setType(Type.NORMAL, Type.FAIRY);
        setMove(new WaterGun(), new IceBeam());
    }
}

class Marill extends Azurill {
    Marill(String name, int level) {
        super(name, level);
        setStats(70, 20, 50, 20, 50, 40);
        setType(Type.WATER, Type.FAIRY);
        setMove(new WaterGun(), new IceBeam(), new MuddyWater());
    }
}

class Azumarill extends Marill {
    Azumarill(String name, int level) {
        super(name, level);
        setStats(100, 50, 80, 60, 80, 50);
        setType(Type.WATER, Type.FAIRY);
        setMove(new WaterGun(), new IceBeam(), new MuddyWater(), new Bubble());
    }
}