package Entities;

import java.util.Random;

public abstract class Entity {
    private int attack, armor;
    private int maxHealth, currentHealth;
    private int[] damage = new int[2];
    private boolean isDead;

    public Entity(int attack, int armor, int maxHealth, int minDamage, int maxDamage) {
        setAttack(attack);
        setArmor(armor);
        setMaxHealth(maxHealth);
        this.isDead = false;
        setDamage(minDamage, maxDamage);
    }

    public int getAttack() {
        return attack;
    }

    public int getArmor() {
        return armor;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int[] getDamage() {
        return damage;
    }

    public String getName() { return "Undef"; }

    public boolean getIsDead() { return isDead; }

    private void setAttack(int attack) {
        if (attack < 1 | attack > 30) {
            System.out.printf("Ошибка при изменении параметра атаки на %d. Атака должна быть целым числом от 1 до 30\n", attack);
            return;
        }
        this.attack = attack;
    }

    private void setArmor(int armor) {
        if (armor < 1 | armor > 30) {
            System.out.printf("Ошибка при изменении параметра защиты на %d. Защита должна быть целым числом от 1 до 30\n", armor);
            return;
        }
        this.armor = armor;
    }

    private void setMaxHealth(int health) {
        if (health < 1) {
            System.out.printf("Ошибка при изменении параметра запаса максимального здоровья на %d. Запас здоровья должен быть целым числом больше 0\n", health);
            return;
        }
        this.maxHealth     = health;
        this.currentHealth = health;
    }

    protected boolean setCurrentHealth(int health) {
        if (health <= 0) this.dying();
        this.currentHealth = Math.min(this.maxHealth, Math.max(health, 0));
        return this.getIsDead();
    }

    private void setDamage(int damageMin, int damageMax) {
        boolean fl = true;
        if (damageMin < 1) {
            System.out.println("Ошибка при изменении параметра разброса урона. Граница минимального урона должна быть больше 0");
            fl = false;
        }
        if (damageMax < 1) {
            System.out.println("Ошибка при изменении параметра разброса урона. Граница максимального урона должна быть больше 0");
            fl = false;
        }
        if (fl & damageMin > damageMax) {
            System.out.println("Ошибка при изменении параметра разброса урона. Левая граница разброса урона не может быть больше правой");
            fl =  false;
        }
        if (!fl) return;

        this.damage = new int[] {damageMin, damageMax};
    }

    public boolean dealDamage(Entity defending) {
        int modif = (this.getAttack() > defending.getArmor()) ? this.getAttack() - defending.getArmor() + 1 : 1;
        System.out.printf("Модификатор атаки %d атакующего %s\n", modif, this.getName());
        Random random = new Random();
        int diceRollResult;
        int i = 0; boolean isSuccess = false;
        while (i < modif) {
            diceRollResult = random.nextInt(6) + 1;
            System.out.printf("На кубике выпадает %d\n", diceRollResult);
            if (diceRollResult >= 5) {
                isSuccess = true;
                break;
            }
            ++i;
        }
        boolean isKilled = false;
        if (isSuccess) {
            int dmg = random.nextInt(this.getDamage()[1] - this.getDamage()[0] + 1) + this.getDamage()[0];
            isKilled = defending.setCurrentHealth(defending.getCurrentHealth() - dmg);
            if (isKilled)
                System.out.printf("Атакующий %s, стирает в порошок защищающегося %s\n",
                        this.getName(), defending.getName());
            else
                System.out.printf("Атакующий %s, наносит %d урон(а), оставляя у %s %d очков здоровья\n",
                        this.getName(), dmg, defending.getName(), defending.getCurrentHealth());
        }
        return isKilled;
    }

    private void dying() {
        this.isDead = true;
    }
    public void stats() {
        System.out.printf("Атака: %d\nЗащита: %d\nМаксимальное здоровье: %d\nУрон: %d - %d\n",
                this.getAttack(), this.getArmor(),this.getMaxHealth(), this.getDamage()[0], this.getDamage()[1]);
    }
}
