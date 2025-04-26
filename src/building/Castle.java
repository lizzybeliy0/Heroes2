package building;

import humans.*;

import java.util.Scanner;

public class Castle {
    private final int COST_OF_PROFSOUZ_UPGRADE = 1000;
    private final int COUNT_OF_PROF_LEVELS = 6;

    private int countOfAvailableUnits = 1;        // на 1 уровне только первый тип
    private int levelOfProf;
    private Unit[] availableUnits;
    private int[] initialCountUnits;            // инициализация, хранение исходных
    private int currentDay = -1;

    public Castle() {
        levelOfProf = 1;
        availableUnits = new Unit[countOfAvailableUnits];
        initialCountUnits = new int[COUNT_OF_PROF_LEVELS];
    }

    public int getLevelOfProf() {
        return levelOfProf;
    }

    public void setLevelOfProf(int levelOfProf) {
        for (; this.levelOfProf < levelOfProf; this.levelOfProf++){
            improvementProf();
        }
    }

    public void upgradeProf(Hero player) {
        if (levelOfProf == 5) {
            System.out.println("Вы настолько известны и деятельны в профсоюзе, что лично вам уже не нужно сдавать деньги, чтобы быть его членом.\n");
            return;
        } else if (player.getGold() >= levelOfProf * COST_OF_PROFSOUZ_UPGRADE) {
            player.addGold(levelOfProf * (-1) * COST_OF_PROFSOUZ_UPGRADE);
            improvementProf();
            levelOfProf++;
            System.out.println("Теперь профсоюз " + levelOfProf + "-го уровня.");
            return;
        }
        System.out.println("В ваших карманах мало золотишка для взноса.\n");
    }

    public void improvementProf() {
        countOfAvailableUnits++;
        availableUnits = new Unit[countOfAvailableUnits];
    }

    public void creationInProf(int day) {
        if (day != currentDay) {
            currentDay = day;
            for (int i = 0; i < countOfAvailableUnits; i++) {
                if (day % 2 == 0) {
                    initialCountUnits[i] = 10;
                } else {
                    initialCountUnits[i] = 12;
                }
            }
        }
        for (int i = 0; i < countOfAvailableUnits; i++) {
            int typeUnit = i + 1;
            int countUnit = initialCountUnits[i];
            availableUnits[i] = new Unit(typeUnit, countUnit);
        }
    }

    public void displayAvailableUnits() {
        for (int i = 0; i < countOfAvailableUnits; i++) {
            if (availableUnits[i].getCount() > 0) {
                System.out.println("Студентoв " + availableUnits[i].getType() +
                        "-го курса доступно к знакомству " + availableUnits[i].getCount() + " штук\nСтоимость: " +
                        availableUnits[i].getCost() + " золота за 1 студента.");
            }
        }
    }

    public void buyUnits(Hero player, Scanner scanner, int day) {
        creationInProf(day);
        displayAvailableUnits();
        System.out.print("\nВыберите, со студентами какого курса вы хотите поговорить " +
                "(номер курса или 0 - если не хотите говорить со студентами): ");
        int availableIndex = scanner.nextInt();
        if (availableIndex < 0 || availableIndex > 5) {
            System.out.print("Вы не нашли студентов с этого курса(\n(Такого курса нет в перечне).");
            return;
        } else if (availableIndex > countOfAvailableUnits) {
            System.out.print("На вашем уровне взаимодействия с профсоюзом вы не можете познакомиться со студентами" +
                    " с этого курса.\nПовысьте уровень профсоюза.\n");
            return;
        }
        if (availableIndex == 0) {
            return;
        }
        availableIndex--;

        System.out.print("\nВыберите со сколькими студентами хотите поговорить: ");
        int availableCount = scanner.nextInt();
        while (availableUnits[availableIndex].getCount() < availableCount) {
            System.out.print("В профсоюзе сейчас нет такого количества студентов. Выбери число, <= количеству студентов: ");
            availableCount = scanner.nextInt();
        }

        while (player.getGold() < availableUnits[availableIndex].getCost() * availableCount) {
            System.out.print("Не хватает золотишка для совершения действия. " +
                    "Если больше не хотите говорить со студентами - нажмите 0: ");
            availableCount = scanner.nextInt();
        }

        player.addGold((-1) * availableUnits[availableIndex].getCost() * availableCount);
        player.getUnits()[availableUnits[availableIndex].getType()].addCount(availableCount);

        initialCountUnits[availableIndex] -= availableCount;                                // уменьшение количества
        availableUnits[availableIndex].addCount(-1 * availableCount);
        System.out.println("Вы подружились с " + availableCount + " студентами.");
    }

    public void buyUnitsForComputer(Hero computerPlayer, int day) {
        int level = day / 4 + 1;
        if (level >= Hero.TYPES_COUNT) {
            if (day % 2 == 0) {
                level = 4;
            } else {
                level = 5;
            }
        }
        if (day >= 3) {
            if (day % 2 == 0) {
                if (level < 4) {
                    computerPlayer.getUnits()[level].addCount(8);
                    computerPlayer.addGold(-800);
                } else {
                    computerPlayer.getUnits()[level].addCount(2);
                    computerPlayer.addGold(-800);
                }
            } else {
                if (level < 4) {
                    computerPlayer.getUnits()[level].addCount(10);
                    computerPlayer.addGold(-1200);
                } else {
                    computerPlayer.getUnits()[level].addCount(1);
                    computerPlayer.addGold(-1200);
                }
            }
        }
    }
}