package sk.tuke.gamestudio.game.Unpuzzle.Core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelPresetsTest
{

    @Test
    public void testLevel1HasFourPieces()
    {
        Level level = LevelPresets.getLevel(1);
        assertEquals(4, level.getPieceCount(), "Level 1 musí mať 4 bloky.");
    }

    @Test
    public void testLevel2HasTenPieces()
    {
        Level level = LevelPresets.getLevel(2);
        assertEquals(10, level.getPieceCount(), "Level 2 musí mať 10 blokov.");
    }

    @Test
    public void testLevel3HasTenPieces()
    {
        Level level = LevelPresets.getLevel(3);
        assertEquals(10, level.getPieceCount(), "Level 3 musí mať 10 blokov.");
    }

    @Test
    public void testLevel4HasSixteenPieces()
    {
        Level level = LevelPresets.getLevel(4);
        assertEquals(16, level.getPieceCount(), "Level 4 musí mať 16 blokov.");
    }

    @Test
    public void testLevel1Dimensions()
    {
        Level level = LevelPresets.getLevel(1);
        assertEquals(3, level.getRows());
        assertEquals(3, level.getCols());
    }

    @Test
    public void testLevel2Dimensions()
    {
        Level level = LevelPresets.getLevel(2);
        assertEquals(4, level.getRows());
        assertEquals(6, level.getCols());
    }

    @Test
    public void testLevel3And4AreSquare()
    {
        Level level3 = LevelPresets.getLevel(3);
        Level level4 = LevelPresets.getLevel(4);
        assertEquals(level3.getRows(), level3.getCols(), "Level 3 musí byť štvorcový.");
        assertEquals(level4.getRows(), level4.getCols(), "Level 4 musí byť štvorcový.");
    }

    @Test
    public void testInvalidLevelThrowsException()
    {
        assertThrows(IllegalArgumentException.class, () -> LevelPresets.getLevel(0));
        assertThrows(IllegalArgumentException.class, () -> LevelPresets.getLevel(5));
        assertThrows(IllegalArgumentException.class, () -> LevelPresets.getLevel(-1));
    }

    @Test
    public void testAllLevelsHaveName()
    {
        for (int i = 1; i <= LevelPresets.LEVEL_COUNT; i++)
        {
            Level level = LevelPresets.getLevel(i);
            assertNotNull(level.getName(), "Level " + i + " musí mať názov.");
            assertFalse(level.getName().isEmpty(), "Názov levelu " + i + " nesmie byť prázdny.");
        }
    }

    @Test
    public void testSealedLevelCannotAddPiece()
    {
        Level level = LevelPresets.getLevel(1);
            assertThrows(IllegalStateException.class, () ->
                        level.addPiece(0, 0, Direction.UP),
                "Zapečatený level nesmie dovoliť pridávanie blokov.");
    }

    @Test
    public void testNoDuplicatePositionsInAnyLevel()
    {
        for (int i = 1; i <= LevelPresets.LEVEL_COUNT; i++)
        {
            Level level = LevelPresets.getLevel(i);
            long uniquePositions = level.getPieces().stream()
                    .map(p -> p.getRow() * 100 + p.getCol())
                    .distinct()
                    .count();
            assertEquals(level.getPieceCount(), uniquePositions,
                    "Level " + i + " nesmie mať dva bloky na rovnakej pozícii.");
        }
    }

    @Test
    public void testAllPiecePositionsWithinBounds()
    {
        for (int i = 1; i <= LevelPresets.LEVEL_COUNT; i++)
        {
            Level level = LevelPresets.getLevel(i);
            for (Level.PieceDefinition def : level.getPieces())
            {
                assertTrue(def.getRow() >= 0 && def.getRow() < level.getRows(),
                        "Level " + i + ": riadok bloku je mimo poľa.");
                assertTrue(def.getCol() >= 0 && def.getCol() < level.getCols(),
                        "Level " + i + ": stĺpec bloku je mimo poľa.");
            }
        }
    }
}

