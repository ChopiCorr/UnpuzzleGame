package sk.tuke.gamestudio.game.Unpuzzle.Core;

public enum Direction
{
    UP("↑"),
    DOWN("↓"),
    LEFT("←"),
    RIGHT("→");

    private final String symbol;

    Direction(String symbol)
    {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }
}
