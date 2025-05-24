package field;

import java.io.*;
import java.util.Scanner;

public class MapEditor {
    private static final String MAPS_FOLDER = "savemaps";

    public static void runMapEditor(Scanner scanner) {
        int finalDesicion = 4;
        int choice = 0;
        while(choice != finalDesicion){
            System.out.print("1 - Создать свой ВУЗ\n2 - Редактировать существующую модель ВУЗа\n" +
                    "3 - Уничтожить ВУЗ\n4 - Вернуться к основной игре\n");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createNewMap(scanner);
                    break;
                case 2:
                    editExistingMap(scanner);
                    break;
                case 3:
                    deleteMap(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Выберите один из существующих вариантов!");
            }
        }
    }

    private static void createNewMap(Scanner scanner) {
        System.out.print("Введите название ВУЗа: ");
        String mapName = scanner.nextLine();

        System.out.print("Введите ширину игрового поля (мин 10): ");
        int width = scanner.nextInt();
        ++width;
        System.out.print("Введите высоту игрового поля (мин 10): ");
        int height = scanner.nextInt();
        ++height;

        if (width < 10 || height < 10) {
            System.out.println("Минимальный размер игрового поля - 10x10!");
            return;
        }

        Field map = new Field(height, width, true);
        editMap(map, mapName, scanner);
    }

    private static void editExistingMap(Scanner scanner) {
        File mapsFolder = new File(MAPS_FOLDER);
        if (!mapsFolder.exists() || mapsFolder.listFiles() == null || mapsFolder.listFiles().length == 0) {
            System.out.println("Нет доступных Вузов для редактирования!");
            return;
        }

        System.out.println("Доступные ВУЗы:");
        for (File file : mapsFolder.listFiles()) {
            System.out.println(file.getName().replace(".txt", ""));
        }

        System.out.print("Введите название ВУЗа для редактирования: ");
        String mapName = scanner.nextLine();

        Field map = loadMap(mapName);
        if (map != null) {
            editMap(map, mapName, scanner);
        } else {
            System.out.println("ВУЗ не найден!");
        }
    }


    private static void editMap(Field map, String mapName, Scanner scanner) {
        while (true) {
            System.out.println("\nТекущий ВУЗ:");
            map.displayField(null);

            System.out.println("\nВыберите действие:");
            System.out.println("1 - Добавить/изменить элемент\n2 - Сохранить игровое поле\n3 - Отменить изменения");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                editMapElement(map, scanner);
            } else if (choice == 2) {
                if (!haveCastles(map)) { //todo (выполнено доп 3)
                    System.out.println("Ошибка! В вузе должно быть ваше место (И) и место обитания приемной комиссии (К)!");
                } else {
                    saveMap(map, mapName);
                    return;
                }
            } else if (choice == 3) {
                System.out.println("Изменения отменены");
                return;
            } else { System.out.println("Введите одно число из предложенных");}
        }
    }

    private static boolean haveCastles(Field map) {
        int playerCastles = 0;
        int enemyCastles = 0;

        for (int y = 1; y < map.getLenY(); y++) {
            for (int x = 1; x < map.getLenX(); x++) {
                PartField cell = map.getPartField(y, x);
                if (cell.isCastle()) {
                    if (cell.getOwnage() == 1) {
                        playerCastles++;
                    } else if (cell.getOwnage() == 2) {
                        enemyCastles++;
                    }
                }
            }
        }

        return playerCastles == 1 && enemyCastles == 1;
    }


    private static void editMapElement(Field map, Scanner scanner) {
        System.out.print("Введите координату Y: ");
        int y = scanner.nextInt();
        System.out.print("Введите координату X: ");
        int x = scanner.nextInt();

        if (x <= 0 || y <= 0 || x >= map.getLenX() || y >= map.getLenY()) {
            System.out.println("Неверные координаты (их невозможно изменить)!");
            return;
        }

        System.out.println("Выберите тип элемента:\n1 - Дорога (+)\n2 - Лес (*)\n3 - Горы (^)\n" +
                "4 - Сокровище (T)\n5 - Территория игрока (#)\n6 - Территория врага (&)\n" +
                "7 - Ваше место (И)\n8 - Место обитания приемной комиссии (К)\n" +
                "9 - Стойка с коммутаторами (с)");

        int type = scanner.nextInt();
        PartField cell = map.getPartField(y, x);

        switch (type) {
            case 1:
                cell.setValue(1);
                cell.setOwnage(0);
                cell.setTreasure(false);
                cell.setCastle(false);
                break;
            case 2:
                cell.setValue(3);
                cell.setOwnage(0);
                cell.setTreasure(false);
                cell.setCastle(false);
                break;
            case 3:
                cell.setValue(8);
                cell.setOwnage(0);
                cell.setTreasure(false);
                cell.setCastle(false);
                break;
            case 4:
                cell.setValue(1);
                cell.setOwnage(0);
                cell.setTreasure(true);
                cell.setCastle(false);
                break;
            case 5:
                cell.setValue(2);
                cell.setOwnage(1);
                cell.setTreasure(false);
                cell.setCastle(false);
                break;
            case 6:
                cell.setValue(2);
                cell.setOwnage(2);
                cell.setTreasure(false);
                cell.setCastle(false);
                break;
            case 7:
                cell.setValue(0);
                cell.setOwnage(1);
                cell.setTreasure(false);
                cell.setCastle(true);
                break;
            case 8:
                cell.setValue(0);
                cell.setOwnage(2);
                cell.setTreasure(false);
                cell.setCastle(true);
                break;
            case 9:
                cell.setValue(1);
                cell.setOwnage(0);
                cell.setTreasure(false);
                cell.setCastle(false);
                cell.setSwitchStand(true);
                cell.setActive(false);

                System.out.print("Введите количество коммутаторов в стойке (1-9): ");
                int count = scanner.nextInt();
                while(count<1 || count>9){
                    System.out.print("\nВ стойке не может быть меньше 1 коммутатора, введите число >=1 или <=9: ");
                    scanner.nextLine();
                    count = scanner.nextInt();
                }
                cell.setSwitchesCount(count);

                System.out.print("Введите номер главного коммутатора (1-" + count + "): ");
                int mainIndex = scanner.nextInt();
                while(mainIndex>count || mainIndex<1){
                    System.out.print("\nДанного коммутатора нет в стойке(\n Введите номер коммутатора, который" +
                            " может быть в стойке: ");
                    scanner.nextLine();
                    mainIndex = scanner.nextInt();
                }
                cell.setMainSwitchIndex(mainIndex);

                System.out.print("\nМесто расположения стойки секретно! О нем знают лишь просвещенные!\n" +
                        "Запомните его, до начала игры вы его больше не увидите!\n");
                break;
            default:
                System.out.println("Неверный тип элемента!");
        }
    }

    public static void saveMap(Field map, String mapName) {
        new File(MAPS_FOLDER).mkdirs();

        try (FileWriter fw = new FileWriter(MAPS_FOLDER + "/" + mapName + ".txt")) {
            fw.write(map.getLenY() + " " + map.getLenX() + "\n");

            for (int y = 1; y < map.getLenY(); y++) {
                for (int x = 1; x < map.getLenX(); x++) {
                    PartField cell = map.getPartField(y, x);
                    fw.write(cell.getValue() + " " +
                            cell.getOwnage() + " " +
                            (cell.isTreasure() ? 1 : 0) + " " +
                            (cell.isCastle() ? 1 : 0) + " ");
                    // вот добавила запись коммутаторов стоек
                    fw.write((cell.isSwitchStand() ? 1 : 0) + " ");
                    if (cell.isSwitchStand()) {
                        fw.write(cell.getSwitchesCount() + " " +
                                cell.getMainSwitchIndex() + " " +
                                (cell.isActive() ? 1 : 0) + " ");
                    } else {
                        fw.write("0 0 0 ");
                    }
                }
                fw.write("\n");
            }

            System.out.println("ВУЗ успешно сохранен!");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении ВУЗа: " + e.getMessage());
        }
    }

    public static Field loadMap(String mapName) {
        try (BufferedReader br = new BufferedReader(new FileReader(MAPS_FOLDER + "/" + mapName + ".txt"))) {
            String[] size = br.readLine().split(" ");
            int lenY = Integer.parseInt(size[0]);
            int lenX = Integer.parseInt(size[1]);

            Field map = new Field(lenY, lenX, true);

            for (int y = 1; y < lenY; y++) {
                String[] row = br.readLine().split(" "); //построчно раздел пробел
                for (int x = 1; x < lenX; x++) {
                    int index = (x-1)*8;
                    PartField cell = map.getPartField(y, x);

                    cell.setValue(Integer.parseInt(row[index]));
                    cell.setOwnage(Integer.parseInt(row[index+1]));
                    cell.setTreasure(row[index+2].equals("1"));
                    cell.setCastle(row[index+3].equals("1"));

                    boolean isSwitchStand = row[index+4].equals("1");
                    cell.setSwitchStand(isSwitchStand);

                    if (isSwitchStand) {
                        cell.setSwitchesCount(Integer.parseInt(row[index+5]));
                        cell.setMainSwitchIndex(Integer.parseInt(row[index+6]));
                        cell.setActive(row[index+7].equals("1"));
                    }
                }
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    private static void deleteMap(Scanner scanner) {
        File mapsFolder = new File(MAPS_FOLDER);
        if (!mapsFolder.exists() || mapsFolder.listFiles() == null || mapsFolder.listFiles().length == 0) {
            System.out.println("Нет доступных ВУЗов для уничтожения!");
            return;
        }

        System.out.println("Доступные ВУЗы:");
        for (File file : mapsFolder.listFiles()) {
            System.out.println(file.getName().replace(".txt", ""));
        }

        System.out.print("Введите название ВУЗа для уничтожения: ");
        String mapName = scanner.nextLine();

        File map = new File(MAPS_FOLDER + "/" + mapName + ".txt");
        if (map.exists() && map.delete()) { //проверь и удали, если возможно
            System.out.println("ВУЗ успешно уничтожен!");
        } else {
            System.out.println("Не удалось уничтожить ВУЗ(");
        }
    }

}
