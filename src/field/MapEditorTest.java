package field;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class MapEditorTest {
    private static final String TEST_MAP = "test_map";
    private static final String MAPS_FOLDER = "savemaps";

    @Before
    public void setUp() throws IOException {
        new File(MAPS_FOLDER).mkdirs();
    }

    @After
    public void deleteFile() {
        File mapFile = new File(MAPS_FOLDER + "/" + TEST_MAP + ".txt");
        if (mapFile.exists()) {
            mapFile.delete();
        }
    }

    @Test
    public void testCoordinates() {
        Field map = new Field(11, 11, true);
        assertNull("Недоступное поле", map.getPartField(-1, -1));
        assertNull("Недоступное поле", map.getPartField(20, 20));
    }

    @Test
    public void testCreateAndSaveMap() {
        Field map = new Field(11, 11, true);
        MapEditor.saveMap(map, TEST_MAP);
        File mapFile = new File(MAPS_FOLDER + "/" + TEST_MAP + ".txt");
        assertTrue("Файл существует", mapFile.exists());
    }

    @Test
    public void testLoadMap() {
        Field map = new Field(11, 11, true);
        map.getPartField(5, 5).setValue(8);
        MapEditor.saveMap(map, TEST_MAP);

        Field loadedMap = MapEditor.loadMap(TEST_MAP);
        assertNotNull("Карта должна загрузиться", loadedMap);
        assertEquals("Гора должна быть на месте", 8, loadedMap.getPartField(5, 5).getValue());
    }

}
