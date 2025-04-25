package field;

import humans.Hero;

import java.util.Scanner;

import main.Main;

public class Field {
    private int lenY;
    private int lenX;
    private PartField[][] field;

    public Field(int lenY, int lenX) {
        this.lenY = lenY;
        this.lenX = lenX;
        field = new PartField[lenY][lenX];
        initializeField();
    }

    private void initializeField() {
        for (int y = 0; y < lenY; y++) {
            for (int x = 0; x < lenX; x++) {
                if (x == 0 || y == 0) {
                    field[y][x] = new PartField(-1);
                } else {
                    if (x == 1 && y == 1) {                              //castle player
                        field[y][x] = new PartField(0);
                        field[y][x].setOwnage(1);
                        field[y][x].setCastle(true);
                    } else if (x == lenX - 1 && y == lenY - 1) {         // castle computer
                        field[y][x] = new PartField(0);
                        field[y][x].setOwnage(2);
                        field[y][x].setCastle(true);
                    } else if ((x == 2 && y == 4) || (x == 4 && y == 2) ||
                            (x == 8 && y == 6) || (x == 6 && y == 8)
                            || (x == 7 && y == 3) || (x == 3 && y == 7)) {      //treasure
                        field[y][x] = new PartField(0);
                        field[y][x].setTreasure(true);
                    } else if (x == y) {
                        field[y][x] = new PartField(1);            // road (value = 1)
                    } else if (x < lenX - y && y < 5 && x < 5) {
                        field[y][x] = new PartField(2);
                        field[y][x].setOwnage(1);
                    } else if (x > lenX - y && y > 5 && x > 5) {
                        field[y][x] = new PartField(2);
                        field[y][x].setOwnage(2);
                    } else if ((y < 4 || y > 6) && (x == lenY - y || x == lenY - y + 1)) {
                        field[y][x] = new PartField(8);
                    } else if ((x > 6 - y && y < 6) || (x < (lenX - (lenX - y)) && y > 5)) {
                        field[y][x] = new PartField(3);
                    } else {
                        field[y][x] = new PartField(3);           // forest
                    }
                }
            }
        }
    }

    public void displayField(Hero hero) {
        for (int y = 0; y < lenY; y++) {
            for (int x = 0; x < lenX; x++) {
                if (x == 0 && y == 0) {
                    System.out.print(" \t");
                } else if (x == 0) {
                    System.out.print(y + "y\t");
                } else if (y == 0) {
                    System.out.print(x + "x\t");
                } else if (hero.getPosX() == x && hero.getPosY() == y) {
                    System.out.print("Г\t");
                } else {
                    field[y][x].displayPartField();
                }
            }
            System.out.println();
        }
    }

    public PartField getPartField(int y, int x) {
        if (x >= 0 && x < lenX && y >= 0 && y < lenY) {
            return field[y][x];
        }
        return null;
    }


    public int getTreasure(PartField cell, Hero player, Scanner scanner, int was) {
        int treasure = 2000;
        System.out.print("Хотите попытать удачу и найти слитое дз/рк? (1 - да, 2 - нет)\n");
        int treasureChoice = scanner.nextInt();
        if (treasureChoice < 1 || treasureChoice > 2) {
            System.out.println("В поисках информации по интернету вы куда-то нажали и все пропало\n");
        } else if (treasureChoice == 2) {
            System.out.println("Идти по тернистому пути только своими силами - достойно\n");
        } else if (treasureChoice == 1) {
            System.out.println("Чтобы найти слив, и желательно бесплатный, нужно преодолеть очень многое...\nБой!\n");
            cell.setTreasure(false);
            return Main.buttle(player, scanner, treasure, was);
        }
        return was;
    }

    public int getLenY() {
        return lenY;
    }

    public int getLenX() {
        return lenX;
    }
}
