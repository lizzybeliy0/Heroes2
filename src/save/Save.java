package save;

import humans.Hero;
import field.Field;
import building.Castle;
import java.io.*;

public class Save {
    public static void saveGame(String playerName, Hero player, Hero computer, Field field, int day,
                                Castle playerCastle, Castle computerCastle){

        new File("saves").mkdirs(); //создаст если не было


        try (FileWriter fw = new FileWriter("saves/" + playerName + ".txt")) {

            fw.write(field.getLenY() + " " + field.getLenX() + "\n");

            fw.write(day-1 + "\n");
            fw.write(player.getPosY() + " " + player.getPosX() + "\n");
            fw.write(computer.getPosY() + " " + computer.getPosX() + "\n");
            fw.write(player.getGold() + "\n");
            fw.write(computer.getGold() + "\n");

            for (int i = 1; i <= Hero.TYPES_COUNT; i++) {
                fw.write(player.getUnits()[i].getCount() + " ");
            }
            fw.write("\n");

            for (int i = 1; i <= Hero.TYPES_COUNT; i++) {
                fw.write(computer.getUnits()[i].getCount() + " ");
            }
            fw.write("\n");

            fw.write(playerCastle.getLevelOfProf() + "\n");

            for (int y = 1; y < field.getLenY(); y++) {
                for (int x = 1; x < field.getLenX(); x++) {
                    fw.write(field.getPartField(y, x).getValue() + " ");
                }
                fw.write("\n");
            }

            System.out.println("\nИгра успешно сохранена!\n");
        }
        catch (IOException e) {
            System.out.println("Ошибка при сохранении игры: " + e.getMessage());
        }
    }

    public static Object[] loadGame(String playerName) {
        try (BufferedReader br = new BufferedReader(new FileReader("saves/" + playerName + ".txt"))) {
            Object[] result = new Object[6]; // [0]player, [1]computer, [2]field, [3]day, [4]playerCastle, [5]computerCastle

            // Читаем размеры поля
            String[] fieldSize = br.readLine().split(" ");
            int lenY = Integer.parseInt(fieldSize[0]);
            int lenX = Integer.parseInt(fieldSize[1]);


            int day = Integer.parseInt(br.readLine());

            // Позиции игрока
            String[] playerPos = br.readLine().split(" ");
            int playerY = Integer.parseInt(playerPos[0]);
            int playerX = Integer.parseInt(playerPos[1]);

            String[] computerPos = br.readLine().split(" ");
            int computerY = Integer.parseInt(computerPos[0]);
            int computerX = Integer.parseInt(computerPos[1]);

            // Золото игрока и компьютера
            int playerGold = Integer.parseInt(br.readLine());
            int computerGold = Integer.parseInt(br.readLine());

            // Создаем героев
            Hero player = new Hero(playerY, playerX);
            Hero computer = new Hero(computerY, computerX);

            // Устанавливаем золото
            player.addGold( - 5000); // 5000 - начальное значение
            player.addGold(playerGold);
            computer.addGold(- 5000);
            computer.addGold(computerGold);

            String[] playerUnits = br.readLine().split(" ");
            for (int i = 1; i <= Hero.TYPES_COUNT; i++) {
                player.getUnits()[i].setCount(Integer.parseInt(playerUnits[i-1]));
            }

            String[] computerUnits = br.readLine().split(" ");
            for (int i = 1; i <= Hero.TYPES_COUNT; i++) {
                computer.getUnits()[i].setCount(Integer.parseInt(computerUnits[i-1]));
            }

            Castle playerCastle = new Castle();
            int playerLevel = Integer.parseInt(br.readLine());
            playerCastle.setLevelOfProf(playerLevel);

            Field field = new Field(lenY, lenX, false);
            for (int y = 1; y < field.getLenY(); y++) {
                String[] row = br.readLine().split(" ");
                for (int x = 1; x < field.getLenX(); x++) {
                    field.getPartField(y, x).setValue(Integer.parseInt(row[x-1]));
                }
            }
            Castle computerCastle = new Castle();

            result[0] = player;
            result[1] = computer;
            result[2] = field;
            result[3] = day;
            result[4] = playerCastle;
            result[5] = computerCastle;

            System.out.println("\nИгра успешно загружена!\n");
            return result;
        } catch (FileNotFoundException e) {
            System.out.println("Сохранение для игрока '" + playerName + "' не найдено.");
            return null;
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке игры: " + e.getMessage());
            return null;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Файл сохранения поврежден: " + e.getMessage());
            return null;
        }
    }

}
