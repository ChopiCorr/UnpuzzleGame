package sk.tuke.gamestudio.game.Unpuzzle.Core;

public class LevelPresets
{
    public static final int LEVEL_COUNT = 4;

    private LevelPresets() {}

    public static Level getLevel(int number)
    {
        switch (number)
        {
            case 1: return buildLevel1();
            case 2: return buildLevel2();
            case 3: return buildLevel3();
            case 4: return buildLevel4();
            default:
                throw new IllegalArgumentException("Level " + number + " neexistuje. Dostupné levely: 1–" + LEVEL_COUNT);
        }
    }

    private static Level buildLevel1()
    {
        Level level = new Level(1, "Kríž  (3×3,  4 bloky)", 3, 3);

        level.addPiece(0, 0, Direction.RIGHT);
        level.addPiece(0, 1, Direction.UP);
        level.addPiece(0, 2, Direction.RIGHT);
        level.addPiece(1, 0, Direction.LEFT);
        level.addPiece(1, 2, Direction.LEFT);
        level.addPiece(2, 0, Direction.LEFT);
        level.addPiece(2, 1, Direction.UP);
        level.addPiece(2, 2, Direction.UP);

        level.seal();
        return level;
    }

    private static Level buildLevel2()
    {
        Level level = new Level(2, "Skupiny  (4×6, 10 blokov)", 4, 6);

        level.addPiece(0, 0, Direction.RIGHT);
        level.addPiece(1, 0, Direction.LEFT);

        level.addPiece(0, 2, Direction.DOWN);
        level.addPiece(0, 3, Direction.RIGHT);
        level.addPiece(1, 2, Direction.DOWN);
        level.addPiece(1, 3, Direction.LEFT);
        level.addPiece(2, 2, Direction.LEFT);
        level.addPiece(2, 3, Direction.DOWN);

        level.addPiece(0, 5, Direction.DOWN);
        level.addPiece(1, 5, Direction.LEFT);

        level.seal();
        return level;
    }

    private static Level buildLevel3()
    {
        Level level = new Level(3, "Podkova  (4×4, 10 blokov)", 4, 4);

        level.addPiece(0, 0, Direction.DOWN);
        level.addPiece(0, 3, Direction.LEFT);
        level.addPiece(1, 0, Direction.DOWN);
        level.addPiece(1, 3, Direction.DOWN);

        level.addPiece(2, 0, Direction.RIGHT);
        level.addPiece(2, 1, Direction.DOWN);
        level.addPiece(2, 2, Direction.UP);
        level.addPiece(2, 3, Direction.DOWN);

        level.addPiece(3, 1, Direction.RIGHT);
        level.addPiece(3, 2, Direction.UP);

        level.seal();
        return level;
    }

    private static Level buildLevel4()
    {
        Level level = new Level(4, "Mriežka  (4×4, 16 blokov)", 4, 4);

        level.addPiece(0, 0, Direction.RIGHT);
        level.addPiece(0, 1, Direction.RIGHT);
        level.addPiece(0, 2, Direction.DOWN);
        level.addPiece(0, 3, Direction.UP);

        level.addPiece(1, 0, Direction.UP);
        level.addPiece(1, 1, Direction.RIGHT);
        level.addPiece(1, 2, Direction.DOWN);
        level.addPiece(1, 3, Direction.UP);

        level.addPiece(2, 0, Direction.UP);
        level.addPiece(2, 1, Direction.LEFT);
        level.addPiece(2, 2, Direction.RIGHT);
        level.addPiece(2, 3, Direction.DOWN);

        level.addPiece(3, 0, Direction.LEFT);
        level.addPiece(3, 1, Direction.LEFT);
        level.addPiece(3, 2, Direction.DOWN);
        level.addPiece(3, 3, Direction.LEFT);

        level.seal();
        return level;
    }
}
