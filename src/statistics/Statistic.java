package statistics;

import java.io.*;
import java.util.*;

public class Statistic {
    private static final String ST_FOLDER = "statistic";
    private static final String TIME_STATS_FILE = "statistic/time_stats.txt";
    private static final String SWITCH_STATS_FILE = "statistic/switch_stats.txt";

    public static void initStatsFolder() {
        new File(ST_FOLDER).mkdirs();
    }

    public static void saveTimeStat(String playerName, int days, String mapName) {
        initStatsFolder();
        try (FileWriter fw = new FileWriter(TIME_STATS_FILE, true)) {
            fw.write(playerName + " " + days + " " + mapName + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения статистики времени: " + e.getMessage());
        }
    }
    public static void saveSwitchStat(String playerName, int switches, String mapName) {
        initStatsFolder();
        try (FileWriter fw = new FileWriter(SWITCH_STATS_FILE, true)) {
            fw.write(playerName + " " + switches + " " + mapName + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка сохранения статистики стоек: " + e.getMessage());
        }
    }

    private static class StatRecord {
        private String playerName;
        private int value;
        private String mapName;

        public StatRecord(String playerName, int value, String mapName) {
            this.playerName = playerName;
            this.value = value;
            this.mapName = mapName;
        }

        public String getPlayerName() { return playerName; }
        public int getValue() { return value; }
        public String getMapName() { return mapName; }
    }

    private static List<String> formatStats(List<StatRecord> records, int limit) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, records.size()); i++) {
            StatRecord r = records.get(i);
            result.add((i+1) + ". " + r.getPlayerName() + " - " + r.getValue() +
                    " (карта: " + r.getMapName() + ")");
        }
        return result;
    }

    public static List<String> getTopTimeStats() {
        List<StatRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TIME_STATS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    records.add(new StatRecord(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (IOException e) {
            return Collections.emptyList();
        }
        records.sort(Comparator.comparingInt(StatRecord::getValue));
        return formatStats(records, 5);
    }

    public static List<String> getTopSwitchStats() {
        List<StatRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SWITCH_STATS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    records.add(new StatRecord(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (IOException e) {
            return Collections.emptyList();
        }
        records.sort(Comparator.comparingInt(StatRecord::getValue).reversed());
        return formatStats(records, 5);
    }
}
