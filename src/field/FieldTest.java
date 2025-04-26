package field;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import humans.Hero;

public class FieldTest {
    private Field field;
    private Hero hero;
    private final int TEST_SIZE_Y = 11;
    private final int TEST_SIZE_X = 11;

    @Before
    public void setUp() {
        field = new Field(TEST_SIZE_Y, TEST_SIZE_X, false);
        hero = new Hero(3,3);
    }

    @Test
    public void testFieldInitialization() {
        assertEquals(-1, field.getPartField(0, 1).getValue()); // граница
        assertEquals(-1, field.getPartField(1, 0).getValue());
        assertTrue(field.getPartField(1, 1).isCastle());                // замки на месте
        assertTrue(field.getPartField(TEST_SIZE_Y-1, TEST_SIZE_X-1).isCastle());
        assertTrue(field.getPartField(2, 4).isTreasure());              // сокровища установлены
        assertFalse(field.getPartField(1, 1).isTreasure());
    }

    @Test
    public void testDisplayField() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        field.displayField(hero);

        String output = outputStream.toString();

        assertTrue("Координаты X",output.contains("1x\t2x\t"));
        assertTrue("Координаты Y",output.contains("1y\t"));
        assertTrue("Герой отобразился",output.contains("Г\t"));
        System.setOut(System.out);
    }


    @Test
    public void testFieldGetSize() {
        assertEquals(TEST_SIZE_Y, field.getLenY());
        assertEquals(TEST_SIZE_X, field.getLenX());
    }

    @Test
    public void testCellTypes() {
        assertEquals(1, field.getPartField(2, 2).getValue());
        assertEquals(3, field.getPartField(1, 6).getValue());
        assertEquals(8, field.getPartField(1, TEST_SIZE_X-1).getValue());
    }
}