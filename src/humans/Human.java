package humans;

import field.Field;
import field.PartField;
import main.Main;

import java.util.Scanner;

public abstract class Human {
    private int posY;
    private int posX;
    private int step;


    public Human(int posY, int posX, int step) {
        this.posY = posY;
        this.posX = posX;
        this.step = step;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public static boolean isMovePossibleSimple(int todaysMovement, int posY, int posX, int previousPosY, int previousPosX, Field field) {

        if ((posY == 0 || posX == 0) || (posY >= field.getLenY() || posX >= field.getLenX())) {
            System.out.println("Во время обучения у вас нет времени выйти за пределы места вашего обучения.\n");
            return false;
        }
        if (previousPosY == posY && previousPosX == posX) {
            System.out.println("Вы уже тут, можете не тратить силы, чтобы стоять на одном месте, прокрастинируя.\n");
            return false;
        }
        if (posY - previousPosY > todaysMovement || posX - previousPosX > todaysMovement) {
            System.out.println("У вас не такой широкий шаг, вы не можете дойти. Выберите точку поближе.\n");
            return false;
        }
        return true;
    }

    public int move(Hero hero, Hero computer, int todaysMovement, int posY, int posX, int previousPosY, int previousPosX, Field field, Scanner scanner) {
        int was = todaysMovement;
        boolean isSimpleMovePossible = isMovePossibleSimple(todaysMovement, posY, posX, previousPosY, previousPosX, field);
        if (!isSimpleMovePossible) {
            return todaysMovement;
        } else {
            while (todaysMovement >= 0) {
                if (posY == previousPosY && posX == previousPosX) {
                    //moveEnd(posY, posX);
                    return todaysMovement;
                }
                if (previousPosY < posY) {
                    previousPosY++;
                } else if (previousPosY > posY) {
                    previousPosY--;
                }
                if (previousPosX < posX) {
                    previousPosX++;
                } else if (previousPosX > posX) {
                    previousPosX--;
                }

                PartField cell = field.getPartField(previousPosY, previousPosX);

                if (cell.isSwitchStand() && cell.isActive()) {
                    if( Main.interactionWithSwitchStand(hero, cell, scanner)){
                        return 200;
                    }
                }

                if (cell.isTreasure()) {
                    return field.getTreasure(cell, hero, scanner, todaysMovement);
                }
                if (cell.isCastle() && cell.getOwnage() == 2) {
                    return Main.finalBattle(hero, computer, scanner);
                }

                int value = cell.getValue();
                if (value == 8) {
                    System.out.println("Этот маршрут затрагивает непреодолимые препятствия, " +
                            "пожалуйста, измените маршрут.\n");
                    return was;
                } else if (todaysMovement - value < 0) {
                    System.out.println("Из-за сложности пути вам не хватит сил дойти. " +
                            "Попробуйте другую точку назначения.\n");
                    return was;
                } else if (todaysMovement - value >= 0) { //todo (сделано)
                    todaysMovement = todaysMovement - value;
                }
            }
        }
        System.out.println("Что-то пошло не так, проблемы с вами, но не в нас\n");
        return 0;
    }

    public void moveEnd(int newPosY, int newPosX) {
        posX = newPosX;
        posY = newPosY;
    }
}
