package humans;

import field.Field;
import field.PartField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Scanner;


public class HeroTest {

    private Hero hero;
    private Field field;
    private Scanner scanner;

    @Before
    public void setUp() {
        hero = new Hero(2, 3);
        field = new Field(11, 11, false);
        scanner = new Scanner(System.in);
    }

    @Test
    public void testHeroInitialization() {
        assertEquals(2, hero.getPosY());
        assertEquals(3, hero.getPosX());
        assertEquals(8, hero.getStep());
        assertEquals(5000, hero.getGold());

        Unit[] units = hero.getUnits();
        assertNotNull(units);
        assertEquals(6, units.length);
        assertEquals(6, units[1].getCount());
    }

    @Test
    public void testGoldManagement() {
        hero.addGold(1000);
        assertEquals(6000, hero.getGold());

        hero.addGold(-2000);
        assertEquals(4000, hero.getGold());
    }

    @Test
    public void testMakeEnemyUnits() {
        Unit[] enemyUnits = hero.makeEnemyUnits(3);
        assertEquals(10, enemyUnits[1].getCount());
        assertEquals(8, enemyUnits[2].getCount());
        assertEquals(15, enemyUnits[3].getCount());

        Unit[] rectorUnits = hero.makeEnemyUnits(10);
        assertEquals(5, rectorUnits[5].getCount());
    }

    @Test
    public void testMovePossible() {
        assertTrue(Human.isMovePossibleSimple(hero.getStep(), 4, 5, hero.getPosY(), hero.getPosX(), field));
        assertFalse(Human.isMovePossibleSimple(hero.getStep(), 0, 0, hero.getPosY(), hero.getPosX(), field));
        assertFalse(Human.isMovePossibleSimple(5, 10, 5, hero.getPosY(), hero.getPosX(), field));
        assertFalse(Human.isMovePossibleSimple(hero.getStep(), hero.getPosY(), hero.getPosX(), hero.getPosY(), hero.getPosX(), field));
    }

    @Test
    public void testMovement() {
        Field testField = new Field(11, 11, false) {
            @Override
            public PartField getPartField(int y, int x) {
                if (y == 3 && x == 4) {
                    PartField hardCell = new PartField(3); // лес
                    return hardCell;
                }
                return super.getPartField(y, x);
            }
        };
        int remainingSteps = hero.move(null, null, hero.getStep(), 3, 4,  //todo м б ошиб в hero.getStep()
                hero.getPosY(), hero.getPosX(), testField, scanner);
        assertEquals(5, remainingSteps);
    }

    @Test
    public void testBlockedPathMovement() {
        Field testField = new Field(10, 10, false) {
            @Override
            public PartField getPartField(int y, int x) {
                if (y == 3 && x == 4) {
                    PartField blockedCell = new PartField(8); // горы
                    return blockedCell;
                }
                return super.getPartField(y, x);
            }
        };

        int remainingSteps = hero.move(null, null, hero.getStep(), 3, 4,
                hero.getPosY(), hero.getPosX(), testField, scanner);
        assertEquals(hero.getStep(), remainingSteps); // Должен вернуть исходное значение шагов
    }

    @Test
    public void testMoveEndUpdatesPosition() {
        hero.moveEnd(5, 6);
        assertEquals(5, hero.getPosY());
        assertEquals(6, hero.getPosX());
    }

}
