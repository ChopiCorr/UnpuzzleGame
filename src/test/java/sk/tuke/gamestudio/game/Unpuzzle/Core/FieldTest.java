package sk.tuke.gamestudio.game.Unpuzzle.Core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FieldTest
{
    private Field createLevel1()
    {
        return new Field(LevelPresets.getLevel(1));
    }

    @Test
    public void testFieldInitializesWithCorrectPieceCount()
    {
        Field field = createLevel1();
        assertEquals(8, field.getPieces().size(),
                "Level 1 musí mať 8 blokov.");
    }

    @Test
    public void testAllPiecesStartOnBoard()
    {
        Field field = createLevel1();
        for (Piece piece : field.getPieces())
        {
            assertEquals(PieceState.ON_BOARD, piece.getState(),
                    "Všetky bloky musia začínať v stave ON_BOARD.");
        }
    }

    @Test
    public void testFieldDimensionsMatchLevel()
    {
        Field field = createLevel1();
        assertEquals(3, field.getRows(), "Level 1 musí mať 3 riadky.");
        assertEquals(3, field.getCols(), "Level 1 musí mať 3 stĺpce.");
    }

    @Test
    public void testPiecesPlacedAtCorrectPositions()
    {
        Field field = createLevel1();
        assertNotNull(field.getPieceAt(0, 0));
        assertNotNull(field.getPieceAt(0, 1));
        assertNotNull(field.getPieceAt(0, 2));
        assertNotNull(field.getPieceAt(1, 0));
        assertNotNull(field.getPieceAt(1, 2));
        assertNotNull(field.getPieceAt(2, 0));
        assertNotNull(field.getPieceAt(2, 1));
        assertNotNull(field.getPieceAt(2, 2));
        assertNull(field.getPieceAt(1, 1), "Stred (1,1) musí byť prázdny.");
    }

    @Test
    public void testInitialGameStateIsPlaying()
    {
        Field field = createLevel1();
        assertEquals(GameState.PLAYING, field.getGameState(),
                "Stav hry na začiatku musí byť PLAYING.");
    }

    @Test
    public void testFreeBlockIsNotBlocked()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        assertNotNull(p2);
        assertFalse(field.isBlocked(p2), "p2(0,1) UP musí byť voľný.");
    }

    @Test
    public void testBlockedByDirectNeighbour()
    {
        Field field = createLevel1();
        Piece p5 = field.getPieceAt(1, 2);
        assertNotNull(p5);
        assertTrue(field.isBlocked(p5), "p5(1,2) LEFT musí byť zablokovaný p4(1,0).");
    }

    @Test
    public void testBlockedByNonAdjacentPiece()
    {
        Field field = createLevel1();
        Piece p1 = field.getPieceAt(0, 0);
        assertNotNull(p1);
        assertTrue(field.isBlocked(p1), "p1(0,0) RIGHT musí byť zablokovaný p2(0,1).");
    }

    @Test
    public void testRemovingBlockerFreesOtherPiece()
    {
        Field field = createLevel1();
        Piece p4 = field.getPieceAt(1, 0);
        Piece p5 = field.getPieceAt(1, 2);

        assertTrue(field.isBlocked(p5), "p5 musí byť zablokovaný pred odstránením p4.");
        field.removePiece(p4.getId());
        assertFalse(field.isBlocked(p5), "p5 musí byť voľný po odstránení p4.");
    }

    @Test
    public void testRemoveFreePieceReturnsTrue()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        assertTrue(field.removePiece(p2.getId()),
                "Odstránenie voľného bloku musí vrátiť true.");
    }

    @Test
    public void testRemoveBlockedPieceReturnsFalse()
    {
        Field field = createLevel1();
        Piece p5 = field.getPieceAt(1, 2);
        assertFalse(field.removePiece(p5.getId()),
                "Odstránenie zablokovaného bloku musí vrátiť false.");
    }

    @Test
    public void testRemovePieceChangesStateToRemoved()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        field.removePiece(p2.getId());
        assertEquals(PieceState.REMOVED, p2.getState(),
                "Blok po odstránení musí mať stav REMOVED.");
    }

    @Test
    public void testRemovePieceClearsGridCell()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        assertNotNull(p2);
        field.removePiece(p2.getId());
        assertNull(field.getPieceAt(0, 1),
                "Po odstránení musí byť bunka v mriežke null.");
    }

    @Test
    public void testRemoveNonExistentPieceReturnsFalse()
    {
        Field field = createLevel1();
        assertFalse(field.removePiece(999),
                "Pokus o odstránenie neexistujúceho ID musí vrátiť false.");
    }

    @Test
    public void testRemoveAlreadyRemovedPieceReturnsFalse()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        field.removePiece(p2.getId());
        assertFalse(field.removePiece(p2.getId()),
                "Druhý pokus o odstránenie toho istého bloku musí vrátiť false.");
    }

    @Test
    public void testGameStateRemainsPlayingAfterPartialRemoval()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        field.removePiece(p2.getId());
        assertEquals(GameState.PLAYING, field.getGameState(),
                "Stav hry musí zostať PLAYING kým nie sú všetky bloky odstránené.");
    }

    @Test
    public void testGameStateSolvedWhenAllRemoved()
    {
        Field field = createLevel1();
        Piece p2 = field.getPieceAt(0, 1);
        Piece p3 = field.getPieceAt(0, 2);
        Piece p1 = field.getPieceAt(0, 0);
        Piece p4 = field.getPieceAt(1, 0);
        Piece p5 = field.getPieceAt(1, 2);
        Piece p6 = field.getPieceAt(2, 0);
        Piece p7 = field.getPieceAt(2, 1);
        Piece p8 = field.getPieceAt(2, 2);

        assertTrue(field.removePiece(p2.getId()));
        assertTrue(field.removePiece(p3.getId()));
        assertTrue(field.removePiece(p1.getId()));
        assertTrue(field.removePiece(p4.getId()));
        assertTrue(field.removePiece(p5.getId()));
        assertTrue(field.removePiece(p6.getId()));
        assertTrue(field.removePiece(p7.getId()));
        assertTrue(field.removePiece(p8.getId()));

        assertEquals(GameState.SOLVED, field.getGameState(),
                "Stav hry musí byť SOLVED keď sú všetky bloky odstránené.");
    }

    @Test
    public void testLevel4Has16Pieces()
    {
        Field field = new Field(LevelPresets.getLevel(4));
        assertEquals(16, field.getPieces().size(),
                "Level 4 musí mať 16 blokov.");
    }

    @Test
    public void testLevel2HasCorrectDimensions()
    {
        Field field = new Field(LevelPresets.getLevel(2));
        assertEquals(4, field.getRows());
        assertEquals(4, field.getCols());
    }

    @Test
    public void testLevel3HasCorrectDimensions()
    {
        Field field = new Field(LevelPresets.getLevel(3));
        assertEquals(4, field.getRows());
        assertEquals(6, field.getCols());
    }
}