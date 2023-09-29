package Entities;

public class Player extends Entity {

    private String name;         // Имя игрока
    private int timesHealed = 0; // Кол-во использованных исцелений

    public Player(int attack, int armor, int health, int damageMin, int damageMax, String name) {
        super(attack, armor, health, damageMin, damageMax);
        setName(name);
    }
    public boolean canHeal() {
        return this.timesHealed < 4;
    }
    public void healing() { // Игрок может себя исцелить до 4-х раз на 30% от максимального здоровья
        if (this.timesHealed < 4) {
            int maxHP = this.getMaxHealth();
            int healedHP = Math.min(maxHP - this.getCurrentHealth(), (int) Math.round(maxHP * 0.3));
            this.setCurrentHealth(this.getCurrentHealth() + (int) Math.round(maxHP * 0.3));
            timesHealed += 1;
            System.out.printf("Игрок %s восстановил %d. Текущий уровень здоровья %d. Осталось %d возможных исцелений.\n",
                    this.name, healedHP, this.getCurrentHealth(), 4 - this.timesHealed);
        }
    }

    public boolean createdCorrectly() {
        return this.getAttack() > 0 & this.getAttack() < 31 & this.getArmor() > 0 & this.getArmor() < 31 &
                this.getMaxHealth() > 0 & this.getDamage()[0] > 0 & this.getDamage()[1] > 0 &
                this.getDamage()[0] <= this.getDamage()[1] & !(this.getName().isEmpty());
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
