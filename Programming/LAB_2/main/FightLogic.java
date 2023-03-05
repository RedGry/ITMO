
import ru.ifmo.se.pokemon.*;

class IronHead extends PhysicalMove {
    protected IronHead() {
        super(Type.STEEL, 80, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.3) Effect.flinch(p);
        p.setMod(Stat.ACCURACY, 0);
    }

    @Override
    protected String describe() {
        return "применил Iron Head";
    }
}

class Stomp extends PhysicalMove {
    protected Stomp() {
        super(Type.NORMAL, 65, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.3) Effect.flinch(p);
        p.setMod(Stat.ACCURACY, 0);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        def.setMod(Stat.HP, (int) Math.round(damage) * 2);
    }

    @Override
    protected String describe() {
        return "применил Stomp";
    }
}

class Facade extends PhysicalMove {
    protected Facade() {
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        Status PokCon = def.getCondition();
        if (PokCon.equals(Status.BURN) || PokCon.equals(Status.POISON) || PokCon.equals(Status.PARALYZE)) {
            def.setMod(Stat.HP, (int) Math.round(damage) * 2);
        }
        def.setMod(Stat.HP, (int) Math.round(damage));
    }

    @Override
    protected String describe() {
        return "применил Facade";
    }
}

class Rest extends StatusMove {
    protected Rest() {
        super(Type.PSYCHIC, 0, 25);
    }

    Effect e = new Effect().turns(2);

    @Override
    protected void applySelfEffects(Pokemon p) {
        e.sleep(p);
        p.restore();
    }

    @Override
    protected String describe() {
        return "хочет спать!";
    }


}

class SandAttack extends StatusMove {
    protected SandAttack() {
        super(Type.GROUND, 0, 100);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.ACCURACY, -1);
    }

    @Override
    protected String describe() {
        return "применил Sand Attack";
    }

}

class Barrier extends StatusMove {
    protected Barrier() {
        super(Type.PSYCHIC, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        p.setMod(Stat.DEFENSE, 1);
    }

    @Override
    protected String describe() {
        return "применил Barrier";
    }
}

class WaterGun extends SpecialMove {
    protected WaterGun() {
        super(Type.WATER, 40, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        def.setMod(Stat.HP, (int) Math.round(damage));
    }

    @Override
    protected String describe() {
        return "применил Water Gun";
    }
}

class IceBeam extends SpecialMove {
    protected IceBeam() {
        super(Type.ICE, 90, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        def.setMod(Stat.HP, (int) Math.round(damage));
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        if (Math.random() <= 0.1) Effect.freeze(p);
        p.setMod(Stat.ACCURACY, 0);
    }

    @Override
    protected String describe() {
        return "применил Ice Beam";
    }
}

class MuddyWater extends SpecialMove {
    protected MuddyWater() {
        super(Type.WATER, 90, 30);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        def.setMod(Stat.HP, (int) Math.round(damage));
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        if (Math.random() <= 0.3) p.setMod(Stat.ACCURACY, -1);
    }

    @Override
    protected String describe() {
        return "применил Muddy Water";
    }

}

class Bubble extends SpecialMove {
    protected Bubble() {
        super(Type.WATER, 40, 100);
    }

    @Override
    protected void applyOppDamage(Pokemon def, double damage) {
        def.setMod(Stat.HP, (int) Math.round(damage));
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        if (Math.random() <= 0.1) p.setMod(Stat.SPEED, -1);
    }

    @Override
    protected String describe() {
        return "применил Bubble";
    }

}

