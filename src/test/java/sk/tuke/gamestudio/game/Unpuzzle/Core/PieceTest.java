package sk.tuke.gamestudio.game.Unpuzzle.Core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PieceTest
{
    @Test
    public void testPieceInitializesWithCorrectId()
    {
        Piece piece = new Piece(3, 1, 2, Direction.UP);
        assertEquals(3, piece.getId());
    }

    @Test
    public void testPieceInitializesWithCorrectPosition()
    {
        Piece piece = new Piece(1, 2, 4, Direction.LEFT);
        assertEquals(2, piece.getRow());
        assertEquals(4, piece.getCol());
    }

    @Test
    public void testPieceInitializesWithCorrectDirection()
    {
        Piece piece = new Piece(1, 0, 0, Direction.RIGHT);
        assertEquals(Direction.RIGHT, piece.getDirection());
    }

    @Test
    public void testPieceInitialStateIsOnBoard()
    {
        Piece piece = new Piece(1, 0, 0, Direction.DOWN);
        assertEquals(PieceState.ON_BOARD, piece.getState(),
                "Každý nový blok musí začínať v stave ON_BOARD.");
    }

    @Test
    public void testSetStateToRemoved()
    {
        Piece piece = new Piece(1, 0, 0, Direction.UP);
        piece.setState(PieceState.REMOVED);
        assertEquals(PieceState.REMOVED, piece.getState());
    }

    @Test
    public void testSetStateBackToOnBoard()
    {
        Piece piece = new Piece(1, 0, 0, Direction.UP);
        piece.setState(PieceState.REMOVED);
        piece.setState(PieceState.ON_BOARD);
        assertEquals(PieceState.ON_BOARD, piece.getState());
    }

    @Test
    public void testAllDirectionsHaveSymbol()
    {
        for (Direction dir : Direction.values())
        {
            assertNotNull(dir.getSymbol(), "Smer " + dir + " musí mať symbol.");
            assertFalse(dir.getSymbol().isEmpty(), "Symbol smeru " + dir + " nesmie byť prázdny.");
        }
    }

    @Test
    public void testDirectionSymbols()
    {
        assertEquals("↑", Direction.UP.getSymbol());
        assertEquals("↓", Direction.DOWN.getSymbol());
        assertEquals("←", Direction.LEFT.getSymbol());
        assertEquals("→", Direction.RIGHT.getSymbol());
    }
}