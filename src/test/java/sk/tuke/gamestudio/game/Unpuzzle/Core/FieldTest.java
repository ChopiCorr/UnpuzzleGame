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
        assertEquals(4, field.getPieces().size(),
                "Level 1 musí mať 4 bloky.");
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
        assertNotNull(field.getPieceAt(0, 1), "Na pozícii (0,1) musí byť blok.");
        assertNotNull(field.getPieceAt(1, 0), "Na pozícii (1,0) musí byť blok.");
        assertNotNull(field.getPieceAt(1, 2), "Na pozícii (1,2) musí byť blok.");
        assertNotNull(field.getPieceAt(2, 1), "Na pozícii (2,1) musí byť blok.");
        assertNull(field.getPieceAt(0, 0), "Na pozícii (0,0) nesmie byť blok.");
        assertNull(field.getPieceAt(1, 1), "Na pozícii (1,1) nesmie byť blok.");
    }

    @Test
    public void testInitialGameStateIsPlaying()
    {
        Field field = createLevel1();
        assertEquals(GameState.PLAYING, field.getGameState(),
                "Stav hry na začiatku musí byť PLAYING.");
    }

    @Test
    public void testAllLevel1PiecesAreNotBlocked()
    {
        Field field = createLevel1();
        for (Piece piece : field.getPieces())
        {
            assertFalse(field.isBlocked(piece), "Blok " + piece.getId() + " nesmie byť zablokovaný v Level 1.");
        }
    }

    @Test
    public void testBlockedPieceInLevel3()
    {
        Field field = new Field(LevelPresets.getLevel(3));
        Piece p6 = field.getPieceAt(2, 1);
        assertNotNull(p6, "Blok na (2,1) musí existovať.");
        assertTrue(field.isBlocked(p6),
                "Blok na (2,1) smer DOWN musí byť zablokovaný blokom na (3,1).");
    }

    @Test
    public void testRemovePieceSuccess()
    {
        Field field = createLevel1();
        boolean result = field.removePiece(1);
        assertTrue(result, "removePiece(1) musí vrátiť true pre voľný blok.");
    }

    @Test
    public void testRemovePieceChangesState()
    {
        Field field = createLevel1();
        field.removePiece(1);
        Piece piece = field.getPieces().get(0);
        assertEquals(PieceState.REMOVED, piece.getState(),
                "Blok po odstránení musí mať stav REMOVED.");
    }

    @Test
    public void testRemovePieceClearsGrid()
    {
        Field field = createLevel1();
        assertNotNull(field.getPieceAt(0, 1));
        field.removePiece(1);
        assertNull(field.getPieceAt(0, 1),
                "Po odstránení bloku musí byť pozícia v mriežke null.");
    }

    @Test
    public void testRemoveBlockedPieceFails()
    {
        Field field = new Field(LevelPresets.getLevel(3));
        Piece p6 = field.getPieceAt(2, 1);
        assertNotNull(p6);
        boolean result = field.removePiece(p6.getId());
        assertFalse(result, "Zablokovaný blok nesmie byť odstránený.");
        assertEquals(PieceState.ON_BOARD, p6.getState(),
                "Zablokovaný blok musí zostať ON_BOARD.");
    }

    @Test
    public void testRemoveNonExistentPieceFails()
    {
        Field field = createLevel1();
        boolean result = field.removePiece(999);
        assertFalse(result, "Pokus o odstránenie neexistujúceho bloku musí vrátiť false.");
    }

    @Test
    public void testRemoveAlreadyRemovedPieceFails()
    {
        Field field = createLevel1();
        field.removePiece(1);
        boolean result = field.removePiece(1);
        assertFalse(result, "Pokus o druhé odstránenie toho istého bloku musí vrátiť false.");
    }

    @Test
    public void testGameStateSolvedWhenAllRemoved()
    {
        Field field = createLevel1();
        field.removePiece(1);
        field.removePiece(2);
        field.removePiece(3);
        field.removePiece(4);
        assertEquals(GameState.SOLVED, field.getGameState(),
                "Stav hry musí byť SOLVED keď sú všetky bloky odstránené.");
    }

    @Test
    public void testGameStateRemainsPlayingAfterPartialRemoval()
    {
        Field field = createLevel1();
        field.removePiece(1);
        field.removePiece(2);
        assertEquals(GameState.PLAYING, field.getGameState(),
                "Stav hry musí zostať PLAYING kým nie sú všetky bloky odstránené.");
    }

    @Test
    public void testLevel4Has16Pieces()
    {
        Field field = new Field(LevelPresets.getLevel(4));
        assertEquals(16, field.getPieces().size(),
                "Level 4 musí mať 16 blokov.");
    }
}