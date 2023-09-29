package Entities;

public class Monster extends Entity{

    private String name;

    public Monster(int attack, int armor, int health, int damageMin, int damageMax, String name) {
        super(attack, armor, health, damageMin, damageMax);
        setName(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
