package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

//import java.security.Permission;
import java.util.List;
import java.util.ArrayList;

public class Field
{
    private final int rows;
    private final int cols;
    private final Piece[][] grid;
    private final List<Piece> pieces;
    private GameState gameState;


    public Field(int rows, int cols/*, int pieceCount*/)
    {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Piece[rows][cols];
        this.pieces = new ArrayList<>();
        this.gameState = GameState.PLAYING;
        //generateField(piececount);
    }

    public boolean removePiece(int id)
    {
        Piece target = findPiece(id);

        if (target == null || target.getState() == PieceState.REMOVED)
        {
            return false;
        }

        if (isBlocked(target))
        {
            return false;
        }

        grid[target.getRow()][target.getCol()] = null;
        target.setState(PieceState.REMOVED);

        gameState = checkGameState();
        return true;
    }

    public boolean isBlocked(Piece piece)
    {
        int row = piece.getRow();
        int col = piece.getCol();

        switch (piece.getDirection())
        {
            case UP:
                for (int r = row - 1; r >= 0; r--)
                {
                    if (grid[r][col] != null) return true;
                }
                break;
            case DOWN:
                for (int r = row + 1; r < rows; r++)
                {
                    if (grid[r][col] != null) return true;
                }
                break;
            case LEFT:
                for (int c = col - 1; c >= 0; c--)
                {
                    if (grid[row][c] != null) return true;
                }
                break;
            case RIGHT:
                for (int c = col + 1; c < cols; c++)
                {
                    if (grid[row][c] != null) return true;
                }
                break;
        }
        return false;
    }

    public GameState checkGameState()
    {
        for (Piece piece : pieces)
        {
            if (piece.getState() == PieceState.ON_BOARD)
            {
                return GameState.PLAYING;
            }
        }
        return GameState.SOLVED;
    }

    private Piece findPiece(int id)
    {
        for (Piece piece : pieces)
        {
            if (piece.getId() == id)
            {
                return piece;
            }
        }
        return null;
    }



    public GameState getGameState() {
        return gameState;
    }


    public List<Piece> getPieces() {
        return pieces;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Piece getPieceAt(int row, int col) {
        return grid[row][col];
    }
}
