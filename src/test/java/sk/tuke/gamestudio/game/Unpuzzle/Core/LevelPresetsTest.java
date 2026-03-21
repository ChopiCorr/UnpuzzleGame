package sk.tuke.gamestudio.game.Unpuzzle.Core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelPresetsTest
{
    @Test
    public void testLevel1HasEightPieces()
    {
        Level level = LevelPresets.getLevel(1);
        assertEquals(8, level.getPieceCount(), "Level 1 musí mať 8 blokov.");
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
    public void testLevel1Is3x3()
    {
        Level level = LevelPresets.getLevel(1);
        assertEquals(3, level.getRows());
        assertEquals(3, level.getCols());
    }

    @Test
    public void testLevel2Is4x4()
    {
        Level level = LevelPresets.getLevel(2);
        assertEquals(4, level.getRows());
        assertEquals(4, level.getCols());
    }

    @Test
    public void testLevel3Is4x6()
    {
        Level level = LevelPresets.getLevel(3);
        assertEquals(4, level.getRows());
        assertEquals(6, level.getCols());
    }

    @Test
    public void testLevel4Is4x4()
    {
        Level level = LevelPresets.getLevel(4);
        assertEquals(4, level.getRows());
        assertEquals(4, level.getCols());
    }

    @Test
    public void testSquareLevels()
    {
        // Level 1, 2, 4 sú štvorcové
        Level l1 = LevelPresets.getLevel(1);
        Level l2 = LevelPresets.getLevel(2);
        Level l4 = LevelPresets.getLevel(4);
        assertEquals(l1.getRows(), l1.getCols(), "Level 1 musí byť štvorcový.");
        assertEquals(l2.getRows(), l2.getCols(), "Level 2 musí byť štvorcový.");
        assertEquals(l4.getRows(), l4.getCols(), "Level 4 musí byť štvorcový.");
    }

    @Test
    public void testLevel3IsNotSquare()
    {
        Level l3 = LevelPresets.getLevel(3);
        assertNotEquals(l3.getRows(), l3.getCols(),
                "Level 3 (Skupiny 4x6) nesmie byť štvorcový.");
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