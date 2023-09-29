import Entities.Monster;
import Entities.Player;

import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);
    private static int monstersKilled = 0;
    private static final String[] monsterNames = { "Widłogon", "Alghoul", "Noonwraith"};

    public static void main(String[] args) {
        int difficulty = 2; // 1 - легкая, 2 - нормальная
        //Player player = new Player(10, 5, 100, 5, 12, "Ivan"); // Стандартный игрок для упрощения дебага
        Player player  = createPlayer();
        System.out.printf("Создан игрок %s с параметрами\n", player.getName());
        player.stats();

        System.out.println("Поход начинается");
        boolean continueGame = true;
        while (continueGame) {
            Monster monster = createMonster(player, difficulty);
            System.out.printf("Игрок %s встречает чудище %s с характеристиками:\n", player.getName(), monster.getName());
            monster.stats();
            boolean continueFight = true;
            while(continueFight) {
                char c = inputCommand(player);
                switch (c) { // Совершаем действие в соответствии с вводом
                    case 'q':
                        if (player.dealDamage(monster)) {
                            monstersKilled += 1;
                            continueFight = false;
                        }
                        else
                            if (monster.dealDamage(player)) {
                                continueGame = false;
                                continueFight = false;
                            }
                        break;
                    case 'e':
                        player.healing();
                        if (monster.dealDamage(player)) {
                            continueGame  = false;
                            continueFight = false;
                        }
                        break;
                    case 'x': // Если монстр, показался сильным, меняем его
                        continueFight = false;
                        break;
                    case 'f':
                        continueFight = false;
                        continueGame = false;
                        break;
                }
            }
        }
        endGame(player);
    }

    private static Player createPlayer() {
        boolean correctInput = false, incorrectInput;
        int attackIn = 0, armorIn = 0, healthIn = 0, dmgMinIn = 0, dmgMaxIn = 0;
        String heroName;
        Player player = null;

        while (!(correctInput)) { // Пока пользователь не введет адекватные значения для полей игрока
            System.out.print("Введите через пробел: Имя игрока | Значение атаки | Значение защиты " +
                    "| Количество очков здоровья | Минимальную границу урона | Максимальную границу урона: \n");
            String[] s = in.nextLine().split(" ");
            incorrectInput = false; // Каждую итерацию сбрасываем некоректность ввода
            if (s.length != 6) {
                System.out.print("Недостаточно значений\n");
                incorrectInput = true;
            }
            heroName = s[0];
            try {
                attackIn = Integer.parseInt(s[1]);
            } catch (Exception ex) {
                System.out.print("Атака должна быть представлена целым числом\n");
                incorrectInput = true;
            }
            try {
                armorIn  = Integer.parseInt(s[2]);
            } catch (Exception ex) {
                System.out.print("Защита должна быть представлена целым числом\n");
                incorrectInput = true;
            }
            try {
                healthIn = Integer.parseInt(s[3]);
            } catch (Exception ex) {
                System.out.print("Очки здоровья должны быть представлены целым числом\n");
                incorrectInput = true;
            }
            try {
                dmgMinIn = Integer.parseInt(s[4]);
            } catch (Exception ex) {
                System.out.print("Граница минимального урона должна быть представлена целым числом\n");
                incorrectInput = true;
            }
            try {
                dmgMaxIn = Integer.parseInt(s[5]);
            } catch (Exception ex) {
                System.out.print("Граница максимального урона должна быть представлена целым числом\n");
                incorrectInput = true;
            }
            if (incorrectInput) continue; // Если были поля с нечисленным значением, то создавать экземпляр класса нет смысла
            player = new Player(attackIn, armorIn, healthIn, dmgMinIn, dmgMaxIn, heroName);
            player.stats();
            correctInput = player.createdCorrectly();
            if (!(correctInput))
                System.out.print("Игрок с такими параметрами не может быть создан\n");
        }
        return player;
    }

    private static Monster createMonster(Player player, int diff) {
        Random random = new Random();
        return new Monster(Math.max(1, random.nextInt(2 * diff + 1) + player.getArmor()),
                Math.max(1, random.nextInt(player.getAttack() / 5 * diff + 1) + player.getAttack() / 3),
                Math.max(1, (random.nextInt((player.getDamage()[1] - player.getDamage()[0]) + 1) + player.getDamage()[1]) * diff),
                Math.max(1, random.nextInt(player.getMaxHealth() / 90 + 1)  + player.getMaxHealth() / 10),
                Math.max(1, random.nextInt(player.getMaxHealth() / 9 + 1) * diff + player.getMaxHealth() / 9),
                monsterNames[random.nextInt(3)]);
    }

    private static char inputCommand(Player player) {
        System.out.print("Чтобы ударить нажмите: q\n");
        System.out.print("Чтобы восстановить здоровье нажмите: e\n");
        System.out.print("Чтобы отступить нажмите: x\n");
        System.out.print("Чтобы завершить игру нажмите: f\n");
        char c = 'a';
        while(c != 'q' & c != 'e' & c != 'x' & c != 'f') {
            try {
                c = in.next().charAt(0); // может быть введен символ превосходящий 255
            } catch (Exception e) {
                System.out.print("Недопустимая клавиша. Повторите ввод\n");
            }
            if (c != 'q' & c != 'e' & c != 'x' & c != 'f')
                System.out.printf("Нет действия на клавишу %s. Повторите выбор действия\n", c);
            if (c == 'e' & !(player.canHeal())) { // Даже если команда на исцеление подана верно, может быть превышен лимит
                System.out.printf("Игрок %s потратил все свои зелья лечения. Повторите выбор действия\n", player.getName());
                c = 'a';
            }
        }
        return c;
    }

    private static void endGame(Player p) {
        if (p.getCurrentHealth() > 0)
            System.out.printf("Поход завершён. %s Выжил в походе. Мостров сражено: %d", p.getName(), monstersKilled);
        else
            System.out.printf("Поход завершён. %s помёр в бою. Мостров сражено: %d", p.getName(), monstersKilled);
    }
}
