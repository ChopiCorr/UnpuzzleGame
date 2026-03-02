package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

import java.security.Permission;
import java.util.List;
import java.util.ArrayList;

public class Field
{
    private final int rows;
    private final int cols;
    private final Piece[][] grid;
    private final List<Piece> pieces;
    private GameState gameState;


    public Field(int rows, int cols)
        {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Piece[rows][cols];
        this.pieces = new ArrayList<>();
        this.gameState = GameState.PLAYING;
        //generateField(piececount);
    }

    public GameState getGameState() {
        return gameState;
    }

    public List<Piece> getPieces() {
        return pieces;
    }
}
