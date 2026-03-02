package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

import java.util.List;
import java.util.ArrayList;

public class Field
{
    private final int rows;
    private final int cols;
    private final Piece[][] grid;
    private final List<Piece> pieces;
    private GameState gameState;


    public Field(int rows, int cols, Piece[][] grid, List<Piece> pieces)
    {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.pieces = pieces;
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
}
