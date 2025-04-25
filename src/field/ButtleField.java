package field;

import humans.*;
import main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;


public class ButtleField extends Field {

    static Logger log = LogManager.getLogger(Main.class);
    private boolean[][] placeUnit;

    public ButtleField(int lenY, int lenX, Unit[] playerUnits, Unit[] enemyUnits) {
        super(lenY, lenX);
        placeUnit = new boolean[lenY][lenX];
        for (int y = 0; y < lenY; y++) {
            for (int x = 0; x < lenX; x++) {
                placeUnit[y][x] = false;
                this.getPartField(y, x).setValue(1);
            }
        }

        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            if (playerUnits[i].getCount() > 0) {
                playerUnits[i].setPosY(playerUnits[i].getType());
                playerUnits[i].setPosX(1);
                placeUnit[playerUnits[i].getType()][0] = true;
            }
        }

        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            if (enemyUnits[i].getCount() > 0) {
                enemyUnits[i].setPosY(enemyUnits[i].getType());
                enemyUnits[i].setPosX(lenX - 1);
                placeUnit[enemyUnits[i].getType()][lenX - 1] = true;
            }
        }
    }

    public void displayButtleField(Unit[] playerUnits, Unit[] enemyUnits) {
        for (int y = 0; y < this.getLenY(); y++) {
            for (int x = 0; x < this.getLenX(); x++) {
                if (x == 0 && y == 0) {
                    System.out.print(" \t");
                } else if (x == 0) {
                    System.out.print(y + "y\t");
                } else if (y == 0) {
                    System.out.print(x + "x\t");
                }
                boolean flag = false;
                for (int i = 1; i < Hero.UNITS_COUNT; i++) {
                    if (playerUnits[i].getCount() > 0 && playerUnits[i].getPosY() == y && playerUnits[i].getPosX() == x) {
                        System.out.print(playerUnits[i].getType() + "\t");
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                for (int i = 1; i < Hero.UNITS_COUNT; i++) {
                    if (enemyUnits[i].getCount() > 0 && enemyUnits[i].getPosY() == y && enemyUnits[i].getPosX() == x) {
                        System.out.print(enemyUnits[i].getType() + "\t");
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    continue;
                }
                if (y != 0 && x != 0) {
                    System.out.print("+\t");
                }
            }
            System.out.println();
        }
    }

    public void displayInfoAboutUnits(Unit[] units, int value) {
        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            if (units[i].getCount() > 0) {
                units[i].getInfAboutUnit(value); //попарно не давать, сначал один, потом другой (- половина) todo (сделано)
            }
        }
        System.out.println();
    }


    private boolean isIntersection(Unit[] units, int newPosY, int newPosX) {

        for (int j = 1; j < Hero.UNITS_COUNT; j++) {
            if (units[j].getCount() > 0 && units[j].getPosY() == newPosY        // на позиции уже кто-то стоит
                    && units[j].getPosX() == newPosX) {                         //попарно не давать todo (сделано)
                return true;
            }
        }
        return false;

    }


    public void moveUnit(Unit[] playerUnits, Unit[] enemyUnits, int i, Scanner scanner) {
        int previousPosY = playerUnits[i].getPosY();
        int previousPosX = playerUnits[i].getPosX();


        if (playerUnits[i].getCount() > 0) {
            System.out.print("\nХотите осуществить передвижение студентом " + playerUnits[i].getType() +
                    "-го курса? (1 - да, 2 -нет)");
            int des = scanner.nextInt();
            if (des != 1) {
                System.out.print("Расценим ответ как нет\n");
                log.info("Вы не осуществили передвижение");
                return;
            }
            //System.out.println("Перемещение студентов " + playerUnits[i].getType() + "-го курса:");
            int todaysMovement = playerUnits[i].getStep();
            System.out.print("Введите новую координату Y: ");
            int newPosY = scanner.nextInt();
            System.out.print("Введите новую координату X: ");
            int newPosX = scanner.nextInt();


            boolean isSimpleMovePossible = Human.isMovePossibleSimple(todaysMovement, newPosY, newPosX, previousPosY, previousPosX, this);

            if (!isSimpleMovePossible) {
                log.error("Вы не осуществили передвижение, т к выбранная клетка была за границей вашего поля или длины шага");
                return;
            }


            if (isIntersection(playerUnits, newPosY, newPosX) ||
                    isIntersection(enemyUnits, newPosY, newPosX)) {
                System.out.println("На новой позиции уже кто-то есть.\n");
                log.error("Вы не осуществили передвижение, т к позиция занята");
                return;
            }

            playerUnits[i].setPosY(newPosY);
            playerUnits[i].setPosX(newPosX);
            placeUnit[previousPosY][previousPosX] = false;
            placeUnit[newPosY][newPosX] = true;

            System.out.println("Студент успешно перемещен на позицию (" + newPosY + ", " + newPosX + ").");
            log.info("Вы осуществили передвижение на клетку(" + newPosY + "," + newPosX + ")");
        }
    }


    //ПЕРЕМЕЩЕНИЕ КОМПЬЮТЕРА

    public void moveEnemyUnits(Unit[] playerUnits, Unit[] computerUnits) {
        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            if (computerUnits[i].getCount() > 0) {
                Unit nearestPlayerUnit = findNearestPlayerUnit(computerUnits[i], playerUnits);
                if (nearestPlayerUnit != null) {
                    int todaysMovement = computerUnits[i].getStep();
                    int posY = computerUnits[i].getPosY();
                    int posX = computerUnits[i].getPosX();
                    int newPosY = nearestPlayerUnit.getPosY();
                    int newPosX = nearestPlayerUnit.getPosX() + 1;

                    int previousPosY = posY;
                    int previousPosX = posX;

                    while (todaysMovement > 0) {
                        int deltaY = Integer.compare(newPosY, posY);     //(если new>pos: 1,0, new<pos: -1)
                        int deltaX = Integer.compare(newPosX, posX);

                        if (deltaY != 0 && isMovePossible(computerUnits, playerUnits, posY + deltaY, posX)) {
                            posY += deltaY;
                            if (deltaX != 0 && isMovePossible(computerUnits, playerUnits, posY, posX + deltaX)) {
                                posX += deltaX;
                            }
                            todaysMovement--;
                        } else if (deltaX != 0 && isMovePossible(computerUnits, playerUnits, posY, posX + deltaX)) {
                            posX += deltaX;
                            todaysMovement--;
                        } else {
                            break;
                        }
                    }

                    if (posY != previousPosY || posX != previousPosX) {
                        computerUnits[i].setPosY(posY);
                        computerUnits[i].setPosX(posX);
                        placeUnit[previousPosY][previousPosX] = false;
                        placeUnit[posY][posX] = true;
                        System.out.println("Враг " + i + " переместился на (" + posY + ", " + posX + ").");
                        log.info("Враг " + i + " переместился на (" + posY + ", " + posX + ").");
                    }
                }
            }
        }
        System.out.println();
    }


    private Unit findNearestPlayerUnit(Unit computerUnit, Unit[] playerUnits) {
        Unit nearestUnit = null;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            if (playerUnits[i].getCount() > 0) {
                int distance = calculateDistance(computerUnit, playerUnits[i]);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestUnit = playerUnits[i];
                }
            }
        }
        return nearestUnit;
    }

    private int calculateDistance(Unit comUnit, Unit playerUnit) {
        int deltaY = Math.abs(comUnit.getPosY() - playerUnit.getPosY());
        int deltaX = Math.abs(comUnit.getPosX() - playerUnit.getPosX());
        return deltaY + deltaX;
    }


    private boolean isMovePossible(Unit[] playerUnits, Unit[] enemyUnits, int newPosY, int newPosX) {

        if (newPosY < 1 || newPosY >= this.getLenY() || newPosX < 1 || newPosX >= this.getLenX()) {
            return false;
        }

        if (isIntersection(playerUnits, newPosY, newPosX) ||
                isIntersection(enemyUnits, newPosY, newPosX)) {
            return false;
        }

        return true;
    }


    public boolean attackDistance(Unit player, Unit enemy) {
        if (Math.abs(player.getPosX() - enemy.getPosX()) <= player.getDistanceAttack() &&
                Math.abs(player.getPosY() - enemy.getPosY()) <= player.getDistanceAttack()) {
            return true;
        }
        return false;
    }


    public void attackUnit(Unit[] player, Unit[] enemy, int i, Scanner scanner) {
        boolean isAttackPossible = false;
        for (int j = 1; j < Hero.UNITS_COUNT; j++) {
            if (enemy[j].getCount() > 0 && attackDistance(player[i], enemy[j])) {
                isAttackPossible = true;
                break;
            }
        }
        if (!isAttackPossible) {
            return;
        }
        System.out.println("Хотите ли вы обратиться за помощью к студенту " + i + "-го курса? " +
                "(1 - да, 2 - нет)");
        int atc = scanner.nextInt();
        if (atc < 1 || atc > 2) {
            System.out.println("Если не выбрали, значит, не хотите");
            log.info("Вы не осуществили атаку на противника (не выбрали пункт атаки)");
            return;
        } else if (atc == 2) {
            log.info("Вы не осуществили атаку на противника");
            return;
        }

        int enemyIndex = 0;
        boolean flag = true;
        while (flag) {
            System.out.print("Выберите противника, которого желаете атаковать (координаты):\n");
            System.out.print("Введите новую координату Y: ");
            int newPosY = scanner.nextInt();
            System.out.print("Введите новую координату X: ");
            int newPosX = scanner.nextInt();

            for (int j = 1; j < Hero.UNITS_COUNT; j++) {
                if (enemy[j].getPosY() == newPosY && enemy[j].getPosX() == newPosX &&
                        attackDistance(player[i], enemy[j])) {
                    enemyIndex = j;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println("\nВ этой точке либо нет источника информации, либо он слишком далек\n");
                log.warn("Вы не можете атаковать пустую или далекую клетку");
            }
        }
        int killedUnits = enemy[enemyIndex].attack(player[i].getCount() * player[i].getAttack());
        log.info("Вы осуществили атаку на противника и убили " + killedUnits + " противников " + enemyIndex + " типа");
        //System.out.println("\nВы успешно добились информации у " + killedUnits +
        //" противников " + enemyIndex + " типа\n");
    }


    //АТАКА КОМПЬЮТЕРА

    public void computerAttack(Unit[] playerUnits, Unit[] computerUnits) {
        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            if (computerUnits[i].getCount() > 0) {

                Unit nearestAttackUnit = findNearestPlayerUnit(computerUnits[i], playerUnits);

                if (nearestAttackUnit != null && attackDistance(computerUnits[i], nearestAttackUnit)) {
                    int killedUnits = nearestAttackUnit.attack(computerUnits[i].getCount() * computerUnits[i].getAttack());
                    System.out.println("Противник " + i + " атаковал студента на позиции (" +
                            nearestAttackUnit.getPosY() + ", " + nearestAttackUnit.getPosX() + ") и теперь " +
                            killedUnits + " студентов вам не помогают.");
                    log.info("Противник " + i + " атаковал студента на позиции (" +
                            nearestAttackUnit.getPosY() + ", " + nearestAttackUnit.getPosX() + ") и теперь " +
                            killedUnits + " студентов вам не помогают.");
                } else {
                    System.out.println("Противник " + i + " пока не атаковал.");
                }
            }
        }
        System.out.println();
    }


}
