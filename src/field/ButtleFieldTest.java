package field;

import humans.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.Scanner;

public class ButtleFieldTest {
    private ButtleField buttleField;
    private Unit[] playerUnits;
    private Unit[] enemyUnits;
    private final int FIELD_SIZE_Y = 6;
    private final int FIELD_SIZE_X = 6;

    @Before
    public void setUp() {
        playerUnits = new Unit[Hero.UNITS_COUNT];
        enemyUnits = new Unit[Hero.UNITS_COUNT];
        for (int i = 1; i < Hero.UNITS_COUNT; i++) {
            playerUnits[i] = new Unit(i,  2);
            enemyUnits[i] = new Unit(i,  2);
        }
        buttleField = new ButtleField(FIELD_SIZE_Y, FIELD_SIZE_X, playerUnits, enemyUnits);
    }

    @Test
    public void testAttackDistance() {
        Unit playerUnit = playerUnits[1];
        Unit enemyUnit = enemyUnits[1];

        playerUnit.setPosX(2);
        playerUnit.setPosY(2);
        enemyUnit.setPosX(2);
        enemyUnit.setPosY(3);
        assertTrue("Атака студента 1-го курса расстояние 1", buttleField.attackDistance(playerUnit, enemyUnit));

        enemyUnit.setPosX(4);
        enemyUnit.setPosY(2);
        assertFalse("Атака студента 1-го курса расстояние > 1",
                buttleField.attackDistance(playerUnit, enemyUnit));
    }

    @Test
    public void testAttackUnit() {
        Unit playerUnit = playerUnits[1];
        Unit enemyUnit = enemyUnits[5];

        playerUnit.setPosY(2);
        playerUnit.setPosX(2);
        enemyUnit.setPosY(3);
        enemyUnit.setPosX(2);

        int initialCount = enemyUnit.getCount();

        Scanner testScanner = new Scanner("1\n3\n2\n");

        buttleField.attackUnit(playerUnits, enemyUnits, 1, testScanner);

        assertEquals("Никого не убили: sumAttack < health противника",
                initialCount, enemyUnit.getCount());

        enemyUnit = enemyUnits[1];
        playerUnit.setPosY(3);
        playerUnit.setPosX(3);
        enemyUnit.setPosY(4);
        enemyUnit.setPosX(3);

        testScanner = new Scanner("1\n4\n3\n");
        buttleField.attackUnit(playerUnits, enemyUnits, 1, testScanner);
        assertEquals("Убили (одного) : sumAttack > health противника",
                1, enemyUnit.getCount());
    }

    @Test
    public void testMoveUnit() {
        Unit unit = playerUnits[1];
        unit.setPosY(1);
        unit.setPosX(1);

        buttleField.moveUnit(playerUnits, enemyUnits, 1, new Scanner("1\n2\n2\n"));
        assertEquals("Y + 1", 2, unit.getPosY());
        assertEquals("X + 1", 2, unit.getPosX());

        buttleField.moveUnit(playerUnits, enemyUnits, 1, new Scanner("1\n4\n4\n"));
        assertEquals("Шаг > возможного", 2, unit.getPosY());
        assertEquals("Шаг > возможного", 2, unit.getPosX());

        buttleField.moveUnit(playerUnits, enemyUnits, 1, new Scanner("1\n0\n0\n"));
        assertEquals("Координата Y за полем", 2, unit.getPosY());
        assertEquals("Координата X за полем", 2, unit.getPosX());
    }

    @Test
    public void testMoveToOccupiedCell() {
        Unit movingUnit = playerUnits[1];
        Unit blockUnit = playerUnits[2];
        Unit enemyUnit = enemyUnits[1];

        movingUnit.setPosY(2);
        movingUnit.setPosX(2);
        blockUnit.setPosY(3);
        blockUnit.setPosX(2);
        int startPosX = movingUnit.getPosX();
        int startPosY = movingUnit.getPosY();

        buttleField.moveUnit(playerUnits, enemyUnits, 1, new Scanner("1\n3\n2\n"));
        assertEquals("Студент пересекся с союзником (не переместился)",
                startPosX, movingUnit.getPosX());
        assertEquals("Студент пересекся с союзником (не переместился)",
                startPosY, movingUnit.getPosY());

        enemyUnit.setPosY(2);
        enemyUnit.setPosX(3);

        buttleField.moveUnit(playerUnits, enemyUnits, 1, new Scanner("1\n2\n3\n"));
        assertEquals("Студент пересекся с противником (не переместился)",
                startPosX, movingUnit.getPosX());
        assertEquals("Студент пересекся с противником (не переместился)",
                startPosY, movingUnit.getPosY());
    }

    @Test
    public void testUnitDeath() {
        Unit unit = playerUnits[1];
        unit.addCount(-1);

        int killed = unit.attack(15);
        assertEquals("Убили 1 студента", 1, killed);
        assertEquals("Теперь студентов больше нет", 0, unit.getCount());
    }

    @Test                       // перехватили ввод - в консоль больше не вывод
    public void testDisplayButtleField() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        playerUnits[1].setPosY(2);
        playerUnits[1].setPosX(2);
        enemyUnits[5].setPosY(5);
        enemyUnits[5].setPosX(5);

        buttleField.displayButtleField(playerUnits, enemyUnits);
        String output = outputStream.toString();

        assertTrue("Координаты X", output.contains("1x\t2x\t"));
        assertTrue("Координаты Y", output.contains("1y\t"));
        assertTrue("Студент", output.contains("1\t"));
        assertTrue("Враг", output.contains("5\t"));

        System.setOut(System.out);
    }

    @Test
    public void testComputerMovement() {
        Unit playerUnit = playerUnits[1];
        playerUnit.setPosY(2);
        playerUnit.setPosX(2);
        Unit enemyUnit = enemyUnits[1];
        enemyUnit.setPosY(1);
        enemyUnit.setPosX(5);

        int initialX = enemyUnit.getPosX();
        int initialY = enemyUnit.getPosY();

        buttleField.moveEnemyUnits(playerUnits, enemyUnits);
        boolean xChanged = enemyUnit.getPosX() != initialX;
        boolean yChanged = enemyUnit.getPosY() != initialY;

        assertTrue("Враг подвинуться должен",
                xChanged || yChanged);

        if (xChanged) {
            assertTrue("Враг подвинулся по X",
                    enemyUnit.getPosX() < initialX);
        }
        if (yChanged) {
            assertTrue("Враг подвинулся по Y",
                    enemyUnit.getPosY() > initialY);
        }
    }

    @Test
    public void testComputerAttack() {
        Unit playerUnit = playerUnits[1];
        playerUnit.setPosY(2);
        playerUnit.setPosX(2);

        Unit enemyUnit = enemyUnits[2];
        enemyUnit.setPosY(2);
        enemyUnit.setPosX(3);

        int initialCount = playerUnit.getCount();
        buttleField.computerAttack(playerUnits, enemyUnits);
        assertTrue("Количество студентов уменьшилось",
                playerUnit.getCount() < initialCount);
    }
}
