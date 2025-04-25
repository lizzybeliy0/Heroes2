package humans;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class UnitTest {
    private Unit firstYearStudent;
    private Unit thirdYearStudent;
    private Unit rector;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        firstYearStudent = new Unit(1, 6);
        thirdYearStudent = new Unit(3, 2);
        rector = new Unit(5, 5);
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testUnitInitialization() {
        assertEquals(10, firstYearStudent.getHealth());
        assertEquals(16, thirdYearStudent.getAttack());
        assertEquals(5, rector.getDistanceAttack());
    }

    @Test
    public void testAttack() {

        int killed = firstYearStudent.attack(12);
        assertEquals(1, killed);
        assertEquals(5, firstYearStudent.getCount());

        killed = firstYearStudent.attack(5);
        assertEquals(0, killed);
        assertEquals(5, firstYearStudent.getCount());

        killed = firstYearStudent.attack(50);
        assertEquals(5, killed);
        assertEquals(0, firstYearStudent.getCount());
    }

    @Test
    public void testRectorAttack() {
        int rectorAttackPower = rector.getAttack() * rector.getCount();
        int killed = thirdYearStudent.attack(rectorAttackPower);
        assertEquals("Ректор убил 2 студентов 1 курса", 2, killed);
        assertEquals("Все студенты убиты", 0, thirdYearStudent.getCount());
    }

    @Test
    public void testAddCount() {
        firstYearStudent.addCount(3);
        assertEquals(9, firstYearStudent.getCount());
        firstYearStudent.addCount(-2);
        assertEquals(7, firstYearStudent.getCount());
    }

    @Test
    public void testGetInfAboutStudent() {
        firstYearStudent.getInfAboutUnit(2);
        String output = outputStream.toString();
        assertTrue(output.contains("Тип студента (курс обучения): 1"));
        assertTrue(output.contains("Количество студентов с 1-го курса: 6"));
        assertTrue(output.contains("Суммарная атака студентов 1-го курса: 48"));
    }

    @Test
    public void testGetInfAboutRector() {
        rector.getInfAboutUnit(1);
        String output = outputStream.toString();
        assertTrue(output.contains("Обозначение ректора на поле: 5"));
    }

    @Test
    public void testMovementSteps() {
        assertEquals(1, firstYearStudent.getStep());
        assertEquals(2, thirdYearStudent.getStep());
        assertEquals(10, rector.getStep());
    }

    @Test
    public void testCostCalculation() {
        assertEquals(100, firstYearStudent.getCost());
        assertEquals(200, thirdYearStudent.getCost());
        assertEquals(500, rector.getCost());
    }

}