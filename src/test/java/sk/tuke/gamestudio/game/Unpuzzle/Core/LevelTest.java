package sk.tuke.gamestudio.game.Unpuzzle.Core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest
{
    @Test
    public void testLevelInitializesWithCorrectNumber()
    {
        Level level = new Level(1, "TestLevel", 3, 3);
        assertEquals(1, level.getNumber());
    }

    @Test
    public void testLevelInitializesWithCorrectName()
    {
        Level level = new Level(2, "Podkova", 4, 4);
        assertEquals("Podkova", level.getName());
    }

    @Test
    public void testLevelInitializesWithCorrectDimensions()
    {
        Level level = new Level(1, "Test", 5, 7);
        assertEquals(5, level.getRows());
        assertEquals(7, level.getCols());
    }

    @Test
    public void testNewLevelStartsEmpty()
    {
        Level level = new Level(1, "Test", 3, 3);
        assertEquals(0, level.getPieceCount(), "Nový level musí začínať bez blokov.");
        assertTrue(level.getPieces().isEmpty());
    }

    @Test
    public void testAddPieceIncreasesCount()
    {
        Level level = new Level(1, "Test", 3, 3);
        level.addPiece(0, 0, Direction.UP);
        assertEquals(1, level.getPieceCount());
    }

    @Test
    public void testAddMultiplePieces()
    {
        Level level = new Level(1, "Test", 3, 3);
        level.addPiece(0, 1, Direction.UP);
        level.addPiece(1, 0, Direction.LEFT);
        level.addPiece(1, 2, Direction.RIGHT);
        level.addPiece(2, 1, Direction.DOWN);
        assertEquals(4, level.getPieceCount());
    }

    @Test
    public void testPieceDefinitionStoresCorrectValues()
    {
        Level level = new Level(1, "Test", 3, 3);
        level.addPiece(1, 2, Direction.RIGHT);

        Level.PieceDefinition def = level.getPieces().get(0);
        assertEquals(1, def.getRow());
        assertEquals(2, def.getCol());
        assertEquals(Direction.RIGHT, def.getDirection());
    }

    @Test
    public void testSealPreventsAddingPieces()
    {
        Level level = new Level(1, "Test", 3, 3);
        level.addPiece(0, 0, Direction.UP);
        level.seal();

        assertThrows(IllegalStateException.class, () ->
                        level.addPiece(1, 1, Direction.DOWN),
                "Po seal() nesmie byť možné pridávať bloky.");
    }

    @Test
    public void testPieceCountDoesNotChangeAfterSeal()
    {
        Level level = new Level(1, "Test", 3, 3);
        level.addPiece(0, 0, Direction.UP);
        level.addPiece(1, 1, Direction.LEFT);
        level.seal();

        assertEquals(2, level.getPieceCount(),
                "Počet blokov sa nesmie zmeniť po zapečatení.");
    }

    @Test
    public void testGetPiecesReturnsUnmodifiableList()
    {
        Level level = new Level(1, "Test", 3, 3);
        level.addPiece(0, 0, Direction.UP);
        level.seal();

        assertThrows(UnsupportedOperationException.class, () ->
                        level.getPieces().add(new Level.PieceDefinition(1, 1, Direction.DOWN)),
                "getPieces() musí vrátiť nemenný zoznam.");
    }
}