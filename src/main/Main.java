package main;

import java.util.Collections;
import java.util.ArrayList;
import field.*;
import humans.Hero;
import building.Castle;
import humans.Unit;
import statistics.Statistic;

import java.util.Random;            // интерфейс / бекэнд + арифметика + враг другой цвет!
import java.util.Scanner;           // массив с текстами можно сделать и выводить

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import save.Save;


import java.io.*;
import java.util.*;


public class Main {
    private static final int LEN_FIELD_Y = 11;
    private static final int LEN_FIELD_X = 11;
    private static final int LEN_BATTLE_FIELD_Y = 6;
    private static final int LEN_BATTLE_FIELD_X = 6;
    private static int treasureDayBattle = 1;
    private static final int SWITCH_ACTIVATION_INTERVAL = 3;
    private static final int SWITCH_EXPLOSION_TURNS = 3;
    private static final int SWITCH_EXPLOSION_DAMAGE = 20;
    private static final int SWITCH_ATTEMPTS = 3;

    private static int switchesDestroyed = 0;
    private static String currentMapName = "МГТУ";

    static Logger log = LogManager.getLogger(Main.class);
    

    public static Random random = new Random();

    private static void activateSwitchStands(Field field) {
        ArrayList<PartField> switchStands = new ArrayList<>();

        for (int y = 1; y < field.getLenY(); y++) {
            for (int x = 1; x < field.getLenX(); x++) {
                PartField cell = field.getPartField(y, x);
                if (cell.isSwitchStand() && !cell.isActive()) {
                    switchStands.add(cell);
                }
            }
        }
        if (!switchStands.isEmpty()) {
            Collections.shuffle(switchStands); //перемешивание
            PartField toActivate = switchStands.get(0);
            toActivate.setActive(true);
            toActivate.setTurnsLeft(SWITCH_EXPLOSION_TURNS);
            System.out.println("\nАктивирована стойка с коммутаторами! На деактивацию у вас 3 дня.\n");
        }
    }

    private static boolean boomSwitchStands(Hero player, Field field) {
        for (int y = 1; y < field.getLenY(); y++) {
            for (int x = 1; x < field.getLenX(); x++) {
                PartField cell = field.getPartField(y, x);
                if (cell.isSwitchStand() && cell.isActive()) {
                    cell.setTurnsLeft(cell.getTurnsLeft() - 1);
                    if (cell.getTurnsLeft() <= 0) {
                        System.out.println("\n3 дня уже прошло... Стойка с коммутаторами взорвалась!\n");
                        boolean gameEnd = applySwitchExplosionDamage(player);
                        cell.setActive(false);
                        if(gameEnd){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean interactionWithSwitchStand(Hero player, PartField cell, Scanner scanner) {
        if (!cell.isActive()) return false;

        System.out.println("Вы подошли к активной стойке с коммутаторами!");
        System.out.println("У вас " + SWITCH_ATTEMPTS + " попытки найти главный коммутатор");

        for (int attempt = 1; attempt <= SWITCH_ATTEMPTS; attempt++) {
            System.out.print("Попытка " + attempt + ": введите номер коммутатора (1-" + cell.getSwitchesCount() + "): ");
            int guess = scanner.nextInt();
            if (guess == cell.getMainSwitchIndex()) {
                System.out.println("Успех! Вы нашли главный коммутатор и деактивировали стойку!\n");
                cell.setActive(false);
                switchesDestroyed++;
                return false;
            } else {
                System.out.println("Неверно! Это не главный коммутатор.");
            }
        }
        System.out.println("Вы не успели обезвредить стойку! Произошел взрыв!");
        boolean gameEnd = applySwitchExplosionDamage(player);
        cell.setActive(false);
        if(gameEnd){
            return true;
        }
        return false;
    }

    private static boolean applySwitchExplosionDamage(Hero player) {
        System.out.println("Все студенты получают урон " + SWITCH_EXPLOSION_DAMAGE);

        for (Unit unit : player.getUnits()) {
            if (unit != null && unit.getCount() > 0) {
                unit.attack(SWITCH_EXPLOSION_DAMAGE);
                if (unit.getCount() > 0) {
                    return false; //продолжаем играть
                }
            }
        }
        System.out.println("\nВсе ваши соратники погибли при взрыве коммутационной стойки!\n" +
                "Нужно было следить за оборудованием лучше...");
        return true;//конец игры
    }

    public static int finalfinalBattle(Hero player, Scanner scanner) {
        int randomValue = random.nextInt(2);              //  случайное число 0 или 1
        //System.out.println("Случайное значение: " + randomValue);
        Unit[] enemyUnits;
        if (randomValue == 0) {                                  //созд одного директора или армию копов
            System.out.println("\n\nНа тазах вы наткнулись на полицию, которая ловит всех студентов присутствующих там.\n" +
                    "Но вы же хотите продолжить веселье, юный герой? Возможность веселиться нужно завоевать!\nБой!\n");

            log.info("Вы наткнулись на полицию");
            enemyUnits = player.makeEnemyUnits(3);       //армию копов
        } else {
            System.out.println("\n\nНа тазах вы разговорились с ректором, и он решил вас испытать, после чего предложил вам " +
                    "шуточную битву.\nКак от такой желанного предложения можно отказаться, " +
                    "юный герой? Да начнется противостояние!\nБой!\n");

            log.info("Вы наткнулись на ректора");
            enemyUnits = player.makeEnemyUnits(10);      //директора
        }
        ButtleField battleField = new ButtleField(LEN_BATTLE_FIELD_Y, LEN_BATTLE_FIELD_X, player.getUnits(), enemyUnits);
        int lose = endOfBattle(player.getUnits(), enemyUnits);
        int round = 1;
        while (lose == 0) {
            System.out.println("\nРаунд " + round);
            System.out.println("\n\nИнформация о друзьях-студентах :");
            battleField.displayInfoAboutUnits(player.getUnits(), 2);
            System.out.println("\nИнформация о соперниках:");
            battleField.displayInfoAboutUnits(enemyUnits, randomValue);
            battleField.displayButtleField(player.getUnits(), enemyUnits);

            for (int i = 1; i < 6; i++) {
                if (player.getUnits()[i].getCount() > 0) {
                    battleField.moveUnit(player.getUnits(), enemyUnits, i, scanner);
                }
            }
            battleField.moveEnemyUnits(player.getUnits(), enemyUnits);
            battleField.displayButtleField(player.getUnits(), enemyUnits);
            for (int i = 1; i < 6; i++) {
                if (player.getUnits()[i].getCount() > 0) {
                    battleField.attackUnit(player.getUnits(), enemyUnits, i, scanner);
                }
            }
            battleField.computerAttack(player.getUnits(), enemyUnits);
            round++;
            lose = endOfBattle(player.getUnits(), enemyUnits);
        }
        if (lose == 200) {
            if (randomValue == 0) {
                System.out.println("\n\nВы проиграли полиции... Вас забрали на бобике в участок и вы не имеете ни малейшего представления" +
                        "о том,\nгде может быть ваш диплом...");
            } else {
                System.out.println("\n\nВы проиграли ректору в этой серьезной схватке. Ну что же, это значит, что вам еще есть к чему " +
                        "стремиться,\nмой юный герой! Вы уходите с тазов счастливым, ведь, несмотря на поражение, это был лучший " +
                        "выпуск из всех, что можно представить!\nВы держите в руках синий диплом.");
            }
            return 1000;
        }
        if (randomValue == 0) {
            System.out.println("\n\nПоздравляю, вы победили полицию! Вы с друзьями весело и задорно продолжается праздновать получение " +
                    "диплома и тусить на тазах.");
            if (player.getUnits()[5].getCount() > 0) {
                System.out.println("Все это время в руках вы держите заветный красный диплом");
            } else {
                System.out.println("Вы держите в руках синий диплом");
            }
            return 1000;
        }
        System.out.println("\n\nВы победили ректора! Он вами гордится и очень доволен.\nОн лично вручает вам ваш красный диплом!");
        return 1000;

    }


    public static int buttle(Hero player, Scanner scanner, int treasure, int was) {
        Unit[] enemyUnits = player.makeEnemyUnits(treasureDayBattle);
        ButtleField battleField = new ButtleField(LEN_BATTLE_FIELD_Y, LEN_BATTLE_FIELD_X, player.getUnits(), enemyUnits);
        int lose = endOfBattle(player.getUnits(), enemyUnits);
        int round = 1;
        while (lose == 0) {
            System.out.println("\nРаунд " + round);
            System.out.println("\n\nИнформация о друзьях-студентах :");
            battleField.displayInfoAboutUnits(player.getUnits(), 2);
            System.out.println("\nИнформация о количестве 'противников':");
            battleField.displayInfoAboutUnits(enemyUnits, 2);
            battleField.displayButtleField(player.getUnits(), enemyUnits);

            for (int i = 1; i < 6; i++) {
                if (player.getUnits()[i].getCount() > 0) {
                    battleField.moveUnit(player.getUnits(), enemyUnits, i, scanner);
                }
            }
            battleField.moveEnemyUnits(player.getUnits(), enemyUnits);
            battleField.displayButtleField(player.getUnits(), enemyUnits);

            for (int i = 1; i < 6; i++) {
                if (player.getUnits()[i].getCount() > 0) {
                    battleField.attackUnit(player.getUnits(), enemyUnits, i, scanner);
                }
            }
            battleField.computerAttack(player.getUnits(), enemyUnits);

            round++;
            lose = endOfBattle(player.getUnits(), enemyUnits);
        }
        if (lose == 200) {
            System.out.println("Вы не смогли сдать дз/рк и вас отчислили...");
            return lose;
        }

        player.addGold(treasure);
        System.out.println("Вы сдали рк и вас не отчислили!\n" +
                "А за это полагается награда: " + treasure + " золота.\n");
        treasureDayBattle++;
        return was;
    }


    public static int endOfBattle(Unit[] playerUnits, Unit[] computerUnits) {
        boolean playerLose = true;
        boolean computerLose = true;
        for (int i = 1; i < 6; i++) {
            if (playerUnits[i].getCount() > 0) {
                playerLose = false;
                break;
            }
        }
        for (int i = 1; i < 6; i++) {
            if (computerUnits[i].getCount() > 0) {
                computerLose = false;
                break;
            }
        }
        if (!playerLose && !computerLose) {
            return 0;
        }
        if (playerLose) {
            log.info("Вы проиграли");
            return 200;

        }
        log.info("Вы победили");
        return 100;
    }


    public static int finalBattle(Hero player, Hero computer, Scanner scanner) {
        System.out.print("Вот вы и достигли финального этапа! Битва, в которой станет понятно все!" +
                "Сможете ли вы одолеть напор аттестационной комиссии и стать настоящим героем?");

        ButtleField battleField = new ButtleField(LEN_BATTLE_FIELD_Y, LEN_BATTLE_FIELD_X, player.getUnits(), computer.getUnits());
        int lose = endOfBattle(player.getUnits(), computer.getUnits());
        int round = 1;
        while (lose == 0) {
            System.out.println("\nРаунд " + round);
            System.out.println("\n\nИнформация о друзьях-студентах :");
            battleField.displayInfoAboutUnits(player.getUnits(), 2);
            System.out.println("\nИнформация о количестве 'противников':");
            battleField.displayInfoAboutUnits(computer.getUnits(), 2);
            battleField.displayButtleField(player.getUnits(), computer.getUnits());

            for (int i = 1; i < 6; i++) {
                if (player.getUnits()[i].getCount() > 0) {
                    battleField.moveUnit(player.getUnits(), computer.getUnits(), i, scanner);
                }
            }
            battleField.moveEnemyUnits(player.getUnits(), computer.getUnits());
            battleField.displayButtleField(player.getUnits(), computer.getUnits());

            for (int i = 1; i < 6; i++) {
                if (player.getUnits()[i].getCount() > 0) {
                    battleField.attackUnit(player.getUnits(), computer.getUnits(), i, scanner);
                }
            }
            battleField.computerAttack(player.getUnits(), computer.getUnits());

            round++;
            lose = endOfBattle(player.getUnits(), computer.getUnits());
        }
        if (lose == 200) {
            System.out.println("Вы не смогли защитить диплом и вас отчислили с последнего курса...");
            return lose;
        }


        System.out.println("Вы смогли победить все напасти во время обучения и успешно защитили диплом!\n" +
                "С чем я Вас и поздравляю. Вы истинный герой!\nСпасибо за прохождение игры и до скорых встреч на тазах!\n\n");

        System.out.println("Дорогой выпускник ВУЗа, наш Герой, как ты хочешь провести столь знаменательный вечер?\n" +
                "1 - Отмечать получение диплома на тазах;\n2 - Отметить получение диплома с семьей.");
        int finalDesicion = scanner.nextInt();
        if (finalDesicion != 1) {
            System.out.println("\n\nВы тихо отметили получение диплома с семьей, вспоминая все прекрасные моменты вашего обучения,\n" +
                    "так неожиданно быстро прошедшего в вашей жизни. Вы знаете, что теперь вы истинный герой, и что в грядущей,\n" +
                    "не менее легкой и интересной жизни, вам все будет по плечу!");
            return 1000;
        }
        return finalfinalBattle(player, scanner);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Желаете сыграть в игру? (1 -да, 2 -нет): ");
        int desicion = scanner.nextInt();
        if (desicion != 1) {
            return;
        }
        scanner.nextLine();

        Hero player = null;
        Hero computerPlayer = null;
        Castle playerCastle = null;
        Castle computerCastle = null;
        Field mainField = null;
        int day = 1;
        int startPlayerPosY = 1;
        int startPlayerPosX = 1;

        System.out.print("Хотите увидеть статистику рекордов перед началом? (1 - да, 2 - нет): ");
        int showStats = scanner.nextInt();
        if (showStats == 1) {
            System.out.println("\nТоп-5 по времени прохождения:");
            List<String> timeStats = Statistic.getTopTimeStats();
            if (timeStats.isEmpty()) {
                System.out.println("Статистика пока отсутствует");
            } else {
                for (String stat : timeStats) {
                    System.out.println(stat);
                }
            }

            System.out.println("\nТоп-5 по уничтоженным стойкам:");
            List<String> switchStats = Statistic.getTopSwitchStats();
            if (switchStats.isEmpty()) {
                System.out.println("Статистика пока отсутствует");
            } else {
                for (String stat : switchStats) {
                    System.out.println(stat);
                }
            }
            System.out.println();
        }
        scanner.nextLine();

        System.out.print("Каково твое имя, юный герой? ");
        String playerName = scanner.nextLine();

        File saveFile = new File("saves/" + playerName + ".txt");
        if (saveFile.exists()) {
            System.out.print("Оооо, юный герой, ты уже был здесь, я тебя помню... " + playerName +
                    ", кажется, верно?\nДа, ты ушел в академ в тот раз, точно. " +
                    "Желаешь восстановиться? (1 - да, 2 - нет): ");
            int loadChoice = scanner.nextInt();
            if (loadChoice == 1) {
                Object[] load = Save.loadGame(playerName);
                if (load != null) {
                    player = (Hero) load[0];
                    computerPlayer = (Hero) load[1];
                    mainField = (Field) load[2];
                    day = (int) load[3];
                    playerCastle = (Castle) load[4];
                    computerCastle = (Castle) load[5];
                    startPlayerPosY = player.getPosY();
                    startPlayerPosX = player.getPosX();
                }
            }
        }

        if (player == null) {
            System.out.print("\nЗдравствуй, юный герой и будущий студент!\n" +
                    "У тебя есть возможность выбрать ВУЗ, в котором ты будешь учиться в течение игры," +
                    " или создать свой, со своими правилами и картами местности.\n" +
                    "Желаешь создать свою модель ВУЗа или отредактировать иной ВУЗ (отличный от МГТУ)? (1 -да, 2 -нет): ");
            int editorChoice = scanner.nextInt();
            if (editorChoice == 1) {
                runMapEditor(scanner);
            }

            System.out.print("Желаешь выбрать для игры иной ВУЗ (отличный от МГТУ)? (1 -да, 2 -нет): ");
            int mapDesicion = scanner.nextInt();
            if (mapDesicion == 1) {
                File mapsFolder = new File("savemaps");
                if (mapsFolder.exists() && mapsFolder.listFiles() != null && mapsFolder.listFiles().length > 0) {
                    System.out.println("Доступные карты:");
                    for (File file : mapsFolder.listFiles()) {
                        System.out.println(file.getName().replace(".txt", ""));
                    }
                    scanner.nextLine();

                    System.out.print("Введите название карты: ");
                    currentMapName = scanner.nextLine();
                    mainField = MapEditor.loadMap(currentMapName);

                    if (mainField == null) {
                        System.out.println("Ошибка загрузки карты, создана стандартная");
                        mainField = new Field(LEN_FIELD_Y, LEN_FIELD_X, false);
                        currentMapName = "МГТУ";
                    }
                } else {
                    System.out.println("Нет доступных карт, создана стандартная");
                    mainField = new Field(LEN_FIELD_Y, LEN_FIELD_X, false);
                    currentMapName = "МГТУ";
                }
            } else {
                mainField = new Field(LEN_FIELD_Y, LEN_FIELD_X, false);
                currentMapName = "МГТУ";
            }
            player = new Hero(startPlayerPosY, startPlayerPosX);
            computerPlayer = new Hero(LEN_FIELD_Y - 1, LEN_FIELD_X - 1);
            playerCastle = new Castle();
            computerCastle = new Castle();


            System.out.print("\n\nДобро пожаловать в игру, герой! Поздравляю с зачислением! " +
                    "Теперь ты студент 1-го курса и перед тобой лежит долгий, тернистый, местами\n" +
                    "кажущийся непроходимый путь обучения. Для прохождения игры тебе необходимо " +
                    "выполнить основное задание - сдача диплома.\nНа сдаче ты встретишь главного босса, " +
                    "который покажет, достоин ли ты носить звание истинного Героя.\nУдачи!\n ");
        }

        System.out.println("\n-------------------------------------------------------\n\n");
        System.out.println("Условные обозначения:\n" +
                "+ - дорога (1 шаг)\n* - леса (3 шага)\n^ - горы (непроходимо)\nТ - сокровища (1)\n" +
                "# - территория героя (2 шага)\n& - территория врага (2 шага)\n" +
                "И - ваше место (1)\nК - место обитания приемной комиссии (1)\n" +
                "Г - герой\nA - герой врага\n");

        mainField.displayField(player);

        int gameEnd = 0;
        //int day = 1;
        while (gameEnd == 0) {
            System.out.println("\nДень " + day + ".");
            day++;
            int todaysMovement = player.getStep();
            int dayEnd = 4;
            int choice = 0;

            if(boomSwitchStands(player, mainField)){
                //gameEnd = 1;
                break;
            }
            if ((day-1) % SWITCH_ACTIVATION_INTERVAL == 0) {
                activateSwitchStands(mainField);
            }

            while (choice != dayEnd && gameEnd == 0) {
                System.out.println("Что же ты решишь делать, герой?\n" +
                        "1 - переместиться в новое пространство\n" +
                        "2 - посмотреть информацию о себе и своих соратниках\n" +
                        "3 - сходить в профсоюз, улучшить себя и соратников\n" +
                        "4 - лечь спать до следующего утра\n" +
                        "5 - сохранить прогресс\n" +
                        "6 - отчислиться\n");
                choice = scanner.nextInt();
                int e = 0;
                while (choice < 1 || choice > 6) {
                    e++;
                    if (e == 1 || e % 15 == 0) {
                        System.out.println("Прогуливаясь в свободное время по ВУЗУу, что не входило в ваши планы,\n" +
                                "вы наткнулись на студсовет, который заставил вас поучаствовать в мероприятии.\n");
                        if (todaysMovement >= 4) {
                            System.out.println("Силы, оставшиеся на передвижение: -4\nЗолотишко героя: +1000\n\n");
                            todaysMovement = todaysMovement - 4;
                            player.addGold(1000);
                        } else {
                            System.out.println("У вас было слишком мало сил и вы уснули на мероприятии...\n\n");
                            choice = dayEnd;
                            break;
                        }

                    }
                    System.out.println("Выбери действия, что ты умеешь делать (1-5).");
                    choice = scanner.nextInt();
                }
                switch (choice) {
                    case 1:
                        mainField.displayField(player);
                        System.out.println("Вы можете сделать " + todaysMovement + " шагов.\n");
                        System.out.print("Введите координату Y точки перемещения:");
                        int newPosY = scanner.nextInt();
                        System.out.print("Введите координату X точки перемещения:");
                        int newPosX = scanner.nextInt();

                        int newMovement = player.move(player, computerPlayer, todaysMovement, newPosY, newPosX, startPlayerPosY, startPlayerPosX, mainField, scanner);
                        if (newMovement == 100 || newMovement == 200) {
                            gameEnd = 1;
                            break;
                        }
                        if (newMovement == 1000) {
                            Statistic.saveTimeStat(playerName, day, currentMapName);
                            Statistic.saveSwitchStat(playerName, switchesDestroyed, currentMapName);

                            System.out.println("\nВаша статистика:");
                            System.out.println("Дней игры: " + day);
                            System.out.println("Уничтожено стоек: " + switchesDestroyed);
                            System.out.println("Карта: " + currentMapName);
                            gameEnd = 1;
                            break;
                        }
                        if (newMovement != todaysMovement) {
                            startPlayerPosY = newPosY;
                            startPlayerPosX = newPosX;
                            player.moveEnd(newPosY, newPosX);
                        }
                        todaysMovement = newMovement;
                        mainField.displayField(player);
                        System.out.println("Вы можете сделать " + todaysMovement + " шагов.\n");
                        Save.saveGame(playerName, player, computerPlayer, mainField, day, playerCastle, computerCastle);
                        break;
                    case 2:
                        player.getInfAboutHero();
                        break;
                    case 3:
                        int castleChoice = 0;
                        int exit = 4;
                        while (castleChoice != exit) {
                            System.out.println("Что же ты решишь делать в профсоюзе, герой?\n" +
                                    "1 - посмотреть, кто сейчас в кабинете профсоюза, и поговорить с ними:\n" +
                                    "2 - похлопать по карманам (вдруг там что-нибудь звенит)\n" +
                                    "3 - отдать " + playerCastle.getLevelOfProf() * 1000 + " денег профсоюзу на улучшение всего (повышает уровень студентов, с которыми вы можете познакомиться):\n" +
                                    "4 - выйти из профсоюза, хватит с вас активностей на сегодня\n");
                            castleChoice = scanner.nextInt();
                            switch (castleChoice) {
                                case 1:
                                    playerCastle.buyUnits(player, scanner, day);
                                    break;
                                case 2:
                                    System.out.println("У вас " + player.getGold() + " золотишка в карманах.\n");
                                    break;
                                case 3:
                                    playerCastle.upgradeProf(player);
                                    break;
                                case 4:
                                    System.out.println("Вы вышли из профсоюза.\n");
                                    break;
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Сладких снов, студент!\n");
                        player.addGold(1000);
                        computerPlayer.addGold(1000);
                        computerCastle.buyUnitsForComputer(computerPlayer, day);
                        /*System.out.println("Противники после добавления:");
                        for (int i = 1; i <= 5; i++) {
                            if (computerPlayer.getUnits()[i].getCount() > 0) {
                                computerPlayer.getUnits()[i].getInfAboutUnit();
                            }
                        }*/
                        break;
                    case 5:
                        Save.saveGame(playerName, player, computerPlayer, mainField, day, playerCastle, computerCastle);

                        break;
                    case 6:
                        System.out.println("Вы действительно хотите покинуть вуз навсегда? (1 - да, 2 - нет).");
                        int sad;
                        sad = scanner.nextInt();
                        if (sad == 1) {
                            System.out.println("Прощай! Надеюсь в том месте, куда ты уходишь, твоя жизнь будет лучше. Успехов!");
                            return;
                        } else if (sad == 2) {
                            System.out.println("Ура, герой все еще с нами!\n");
                        } else {
                            System.out.println("Вы не смогли поставить подпись в нужную клетку," +
                                    "а значит герой все еще с нами!\n");
                        }
                        break;
                }
            }
        }
    }

    private static void runMapEditor(Scanner scanner) {
        System.out.println("\nВы зашли в редактор карт!\n\n");
        MapEditor.runMapEditor(scanner);
    }
}