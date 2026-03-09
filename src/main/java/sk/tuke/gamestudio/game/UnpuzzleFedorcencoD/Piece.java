package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

public class Piece
{
    private final int id;
    private PieceState state;
    private final Direction direction;
    private final int row;
    private final int col;


    public Piece(int id, int row, int col, Direction direction)
    {
        this.id = id;
        this.direction = direction;
        this.row = row;
        this.col = col;
    }

    public int getId() {
        return id;
    }

    public PieceState getState() {
        return state;
    }

    public void setState(PieceState state) {
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Direction getDirection() {
        return direction;
    }
}
