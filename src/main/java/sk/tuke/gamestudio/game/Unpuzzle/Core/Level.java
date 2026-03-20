package sk.tuke.gamestudio.game.Unpuzzle.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level
{
    private final int number;
    private final String name;
    private final int rows;
    private final int cols;
    private final List<PieceDefinition> pieces;
    private boolean sealed = false;

    public Level(int number, String name, int rows, int cols)
    {
        this.cols = cols;
        this.rows = rows;
        this.name = name;
        this.number = number;
        this.pieces = new ArrayList<>();
    }

    public static class PieceDefinition
    {
        private final int row;
        private final int col;
        private final Direction direction;

        public PieceDefinition(int row, int col, Direction direction)
        {
            this.row = row;
            this.col = col;
            this.direction = direction;
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

    public void addPiece(int row, int col, Direction direction)
    {
        if (sealed)
        {
            throw new IllegalStateException("Level je zapečatený, nie je možné pridávať bloky.");
        }
        pieces.add(new PieceDefinition(row, col, direction));
    }

    public void seal()
    {
        this.sealed = true;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public List<PieceDefinition> getPieces()
    {
        return Collections.unmodifiableList(pieces);
    }

    public int getPieceCount()
    {
        return pieces.size();
    }
}
