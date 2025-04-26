package field;

import java.io.*;
import java.util.Scanner;

public class MapEditor {
    private static final String MAPS_FOLDER = "savemaps";

    public static void runMapEditor(Scanner scanner) {
        int finalDesicion = 4;
        int choice = 0;
        while(choice!=finalDesicion){
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
                    System.out.println("Неверный выбор!");
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
            System.out.println("Минимальный размер карты - 10x10!");
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
            System.out.println("ВУЗ не найдена!");
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
                saveMap(map, mapName);
                return;
            } else if (choice == 3) {
                System.out.println("Изменения отменены");
                return; //куда ведет? todo
            } else { System.out.println("Введите одно число из предложенных");}
        }
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

        System.out.println("Выберите тип элемента:");
        System.out.println("1 - Дорога (+)");
        System.out.println("2 - Лес (*)");
        System.out.println("3 - Горы (^)");
        System.out.println("4 - Сокровище (T)");
        System.out.println("5 - Территория игрока (#)");
        System.out.println("6 - Территория врага (&)");
        System.out.println("7 - Замок игрока (И)");
        System.out.println("8 - Замок врага (К)");

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
            default:
                System.out.println("Неверный тип элемента!");
        }
    }

    private static void saveMap(Field map, String mapName) {
        new File(MAPS_FOLDER).mkdirs();

        try (FileWriter fw = new FileWriter(MAPS_FOLDER + "/" + mapName + ".txt")) {
            fw.write(map.getLenY() + " " + map.getLenX() + "\n");

            for (int y = 1; y < map.getLenY(); y++) {
                for (int x = 1; x < map.getLenX(); x++) {
                    PartField cell = map.getPartField(y, x);
                    fw.write(cell.getValue() + " " + cell.getOwnage() + " " +
                            (cell.isTreasure() ? 1 : 0) + " " +
                            (cell.isCastle() ? 1 : 0) + " ");
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
                String[] row = br.readLine().split(" ");
                for (int x = 1; x < lenX; x++) {
                    int index = (x-1)*4;
                    PartField cell = map.getPartField(y, x);
                    cell.setValue(Integer.parseInt(row[index]));
                    cell.setOwnage(Integer.parseInt(row[index+1]));
                    cell.setTreasure(row[index+2].equals("1"));
                    cell.setCastle(row[index+3].equals("1"));
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
            System.out.println("Нет доступных карт для удаления!");
            return;
        }

        System.out.println("Доступные карты:");
        for (File file : mapsFolder.listFiles()) {
            System.out.println(file.getName().replace(".txt", ""));
        }

        System.out.print("Введите название карты для удаления: ");
        String mapName = scanner.nextLine();

        File map = new File(MAPS_FOLDER + "/" + mapName + ".txt");
        if (map.exists() && map.delete()) { //проверь и удали, если возможно
            System.out.println("Карта успешно удалена!");
        } else {
            System.out.println("Не удалось удалить карту!");
        }
    }

}
