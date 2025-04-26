package save;

import field.*;
import humans.*;
import building.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class SaveTest {
    private static final String TEST_PLAYER = "test_player";
    private static final String SAVES_FOLDER = "saves";

    @Before
    public void setUp() throws IOException {
        new File(SAVES_FOLDER).mkdirs();
    }

    @After
    public void deleteFile() {
        File saveFile = new File(SAVES_FOLDER + "/" + TEST_PLAYER + ".txt");
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @Test
    public void testSaveAndLoadGame() {
        Hero player = new Hero(5, 5);
        Hero computer = new Hero(10, 10);
        Field field = new Field(11, 11, false);
        Castle playerCastle = new Castle();
        Castle computerCastle = new Castle();
        int day = 5;

        Save.saveGame(TEST_PLAYER, player, computer, field, day, playerCastle, computerCastle);

        Object[] loaded = Save.loadGame(TEST_PLAYER);
        assertNotNull("Сохранение существует", loaded);

        Hero loadedPlayer = (Hero) loaded[0];
        assertEquals("Позиция студента", 5, loadedPlayer.getPosY());
        assertEquals("Позиция студента", 5, loadedPlayer.getPosX());

        Field loadedField = (Field) loaded[2];
        assertNotNull("Поле существует", loadedField);

        assertEquals("День сохранился", day-1, loaded[3]);
    }

    @Test
    public void testLoadNonExistentSave() {
        Object[] loaded = Save.loadGame("non_existent_player");
        assertNull("Не существует", loaded);
    }

    @Test
    public void testSaveLoadUnits() {
        Hero player = new Hero(1, 1);
        player.getUnits()[1].setCount(10);

        Save.saveGame(TEST_PLAYER, player, new Hero(10, 10), new Field(11, 11, false),
                1, new Castle(), new Castle());

        Object[] loaded = Save.loadGame(TEST_PLAYER);
        Hero loadedPlayer = (Hero) loaded[0];

        assertEquals("Количество юнитов соответствует сохраненному", 10, loadedPlayer.getUnits()[1].getCount());
    }

    @Test
    public void testSaveLoadCastleLevel() {
        Castle castle = new Castle();
        castle.setLevelOfProf(3);

        Save.saveGame(TEST_PLAYER, new Hero(1, 1), new Hero(10, 10),
                new Field(11, 11, false), 1, castle, new Castle());

        Object[] loaded = Save.loadGame(TEST_PLAYER);
        Castle loadedCastle = (Castle) loaded[4];

        assertEquals("Уровень профсоюза соответствует сохраненному", 3, loadedCastle.getLevelOfProf());
    }
}
