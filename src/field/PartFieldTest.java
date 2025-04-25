package field;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PartFieldTest {

    private PartField partField;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  // Перенаправляем стандартный поток вывода в ByteArrayOutputStream (метода)
    private final PrintStream originalOut = System.out;  // сохранили поток до тестов

    @Before
    public void setUp() {
        partField = new PartField(0);
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }


    @Test
    public void testDisplayPartFieldTreasure() {
        partField.setTreasure(true);
        partField.displayPartField();
        assertEquals("T\t", outputStream.toString());
        assertEquals(1, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldValue1() {
        partField.setValue(1);
        partField.displayPartField();
        assertEquals("+\t", outputStream.toString());
        assertEquals(1, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldValue3() {
        partField.setValue(3);
        partField.displayPartField();
        assertEquals("*\t", outputStream.toString());
        assertEquals(3, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldValue8() {
        partField.setValue(8);
        partField.displayPartField();
        assertEquals("^\t", outputStream.toString());
        assertEquals(8, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldOwn1CastleTrue() {
        partField.setOwnage(1);
        partField.setCastle(true);
        partField.displayPartField();
        assertEquals("И\t", outputStream.toString());
        assertEquals(0, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldOwn1CastleFalse() {
        partField.setOwnage(1);
        partField.setCastle(false);
        partField.displayPartField();
        assertEquals("#\t", outputStream.toString());
        assertEquals(2, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldOwn2CastleTrue() {
        partField.setOwnage(2);
        partField.setCastle(true);
        partField.displayPartField();
        assertEquals("К\t", outputStream.toString());
        assertEquals(0, partField.getValue());
    }

    @Test
    public void testDisplayPartFieldOwn2CastleFalse() {
        partField.setOwnage(2);
        partField.setCastle(false);
        partField.displayPartField();
        assertEquals("&\t", outputStream.toString());
        assertEquals(2, partField.getValue());
    }
}