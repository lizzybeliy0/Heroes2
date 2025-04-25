package main;

import field.Field;
import humans.Hero;
import humans.Unit;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Random;
import java.util.Scanner;

public class MainTest {
    private Scanner scanner;
    private Hero player;
    private Hero computerPlayer;

    @Before
    public void setUp() {
        Field field = new Field(11,11);
        player = new Hero(1, 1);
        computerPlayer = new Hero(field.getLenY()-1, field.getLenX()-1);
    }


    @Test
    public void testEndOfBattlePlayerWins() {
        Unit[] playerUnits = player.getUnits();
        Unit[] computerUnits = computerPlayer.getUnits();
        computerUnits[1].addCount(-6);
        int result = Main.endOfBattle(playerUnits, computerUnits);
        assertEquals(100, result);
    }

    @Test
    public void testEndOfBattleComputerWins() {
        Unit[] playerUnits = player.getUnits();
        Unit[] computerUnits = computerPlayer.getUnits();
        playerUnits[1].addCount(-6);
        int result = Main.endOfBattle(playerUnits, computerUnits);
        assertEquals(200, result);
    }


    @Test
    public void testButtlePlayerWins() {

        String input = "1\n1\n2\n1\n1\n3\n1\n1\n4\n";
        Scanner testScanner = new Scanner(input);

        Unit[] playerUnits = player.getUnits();
        playerUnits[1].addCount(20);

        Unit[] enemyUnits = player.makeEnemyUnits(1);
        enemyUnits[1].addCount(-10);

        int result = Main.buttle(player, testScanner, 1000, 5);

        assertNotEquals(200, result);
        assertEquals(5, result);
        assertEquals(6000,player.getGold());
    }

    @Test
    public void testButtlePlayerLose() {

        String input = "1\n1\n2\n1\n1\n3\n1\n1\n4\n";
        Scanner testScanner = new Scanner(input);

        Unit[] playerUnits = player.getUnits();
        playerUnits[1].addCount(-5);

        Unit[] enemyUnits = player.makeEnemyUnits(1);
        enemyUnits[1].addCount(10);

        int result = Main.buttle(player, testScanner, 1000, 5);

        assertEquals(200, result);
    }

    @Test
    public void testFinalBattlePlayerWins() {
        Unit[] playerUnits = player.getUnits();
        playerUnits[5].addCount(10);

        Unit[] computerUnits = computerPlayer.getUnits();
        computerUnits[1].addCount(-5);

        String input = "1\n1\n2\n1\n1\n4\n1\n1\n5\n2\n";
        Scanner testScanner = new Scanner(input);

        int result = Main.finalBattle(player, computerPlayer, testScanner);

        assertEquals(100, result);
    }

    @Test
    public void testFinalBattleComputerWins() {

        Unit[] computerUnits = computerPlayer.getUnits();
        computerUnits[5].addCount(10);
        String input = "1\n2\n2\n1\n2\n3\n";
        Scanner testScanner = new Scanner(input);

        int result = Main.finalBattle(player, computerPlayer, testScanner);

        assertEquals(200, result);
    }

    @Test
    public void testFinalFinalBattlePoliceScenarioWin() {
        Unit[] playerUnits = player.getUnits();
        playerUnits[1].addCount(-6);
        playerUnits[4].addCount(30);
        playerUnits[2].addCount(32);
        playerUnits[3].addCount(40);

        // контролируемый Random
        Main.random = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        String input = "1\n1\n3\n1\n2\n3\n1\n3\n3\n1\n1\n4\n1\n2\n4\n1\n3\n4\n";
        Scanner testScanner = new Scanner(input);

        int result = Main.finalfinalBattle(player, testScanner);

        assertEquals(100, result);
    }

    @Test
    public void testFinalFinalBattlePoliceScenarioLose() {

        Main.random = new Random() {
            @Override
            public int nextInt(int bound) {
                return 0;
            }
        };

        String input = "1\n1\n2\n1\n2\n3\n";
        Scanner testScanner = new Scanner(input);

        int result = Main.finalfinalBattle(player, testScanner);

        assertEquals(200, result);
    }

    @Test
    public void testFinalFinalBattleRectorScenarioWin() {
        Unit[] playerUnits = player.getUnits();
        playerUnits[1].addCount(-6);
        playerUnits[5].addCount(20);

        Main.random = new Random() {
            @Override
            public int nextInt(int bound) {
                return 1;
            }
        };

        String input = "1\n5\n3\n1\n5\n4\n";
        Scanner testScanner = new Scanner(input);

        int result = Main.finalfinalBattle(player, testScanner);

        assertNotEquals(200, result);
    }

    @Test
    public void testFinalFinalBattleRectorScenarioLose() {

        Main.random = new Random() {
            @Override
            public int nextInt(int bound) {
                return 1;
            }
        };

        String input = "1\n2\n2\n1\n2\n3\n";
        Scanner testScanner = new Scanner(input);

        int result = Main.finalfinalBattle(player, testScanner);

        assertEquals(200, result);
    }
}