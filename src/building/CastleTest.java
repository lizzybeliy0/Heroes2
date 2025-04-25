package building;

import humans.Hero;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Scanner;
import java.io.ByteArrayInputStream;

public class CastleTest {
    private Castle castle;
    private Hero player;

    @Before
    public void setUp() {
        castle = new Castle();
        player = new Hero(1, 1);
    }

    @Test
    public void testInitialState() {
        assertEquals(1, castle.getLevelOfProf());
    }

    @Test
    public void testUpgradeProfSuccess() {
        castle.upgradeProf(player);
        assertEquals(2, castle.getLevelOfProf());
        assertEquals(5000 - 1000, player.getGold());
    }

    @Test
    public void testUpgradeProfNotEnoughGold() {
        player.addGold(-5000);
        castle.upgradeProf(player);
        assertEquals(1, castle.getLevelOfProf());
        assertEquals(0, player.getGold());
    }

    @Test
    public void testUpgradeProfMaxLevel() {
        player.addGold(15000);
        for (int i = 1; i <= 5; i++) {
            castle.upgradeProf(player);
        }
        assertEquals(5, castle.getLevelOfProf());
        int goldBefore = player.getGold();
        castle.upgradeProf(player);
        assertEquals(5, castle.getLevelOfProf());
        assertEquals(goldBefore, player.getGold());
    }

    @Test
    public void testBuyUnitsSuccess() {
        String input = "1\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner testScanner = new Scanner(System.in);

        int initialCount = player.getUnits()[1].getCount();

        castle.buyUnits(player, testScanner, 1);
        assertTrue(player.getUnits()[1].getCount() > initialCount);

        assertTrue(player.getGold() < 5000);
        //assertEquals(4500, player.getGold());

        System.setIn(System.in);
    }

    @Test
    public void testBuyUnitsNotEnoughGold() {
        String input = "1\n10\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner testScanner = new Scanner(System.in);

        player.addGold(-4990);
        int initialCount = player.getUnits()[1].getCount();
        int initialGold = player.getGold();

        castle.buyUnits(player, testScanner, 1);

        assertEquals(initialGold, player.getGold());
        assertEquals(initialCount, player.getUnits()[1].getCount());

        System.setIn(System.in);
    }

    @Test
    public void testBuyUnitsForComputer() {
        Hero computerPlayer = new Hero(2, 2);
        assertEquals(6, computerPlayer.getUnits()[1].getCount());

        castle.buyUnitsForComputer(computerPlayer, 3); //+10
        assertEquals(16, computerPlayer.getUnits()[1].getCount());

        castle.buyUnitsForComputer(computerPlayer, 4);
        assertEquals(16, computerPlayer.getUnits()[1].getCount());
        assertEquals(8, computerPlayer.getUnits()[2].getCount());

        castle.buyUnitsForComputer(computerPlayer, 5);
        assertEquals(18, computerPlayer.getUnits()[2].getCount());
    }
}