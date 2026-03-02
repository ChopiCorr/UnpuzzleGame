package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;


import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.Field;
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.Piece;
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.GameState;
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.PieceState;

import java.util.Scanner;

public class ConsoleUI
{
    private final Field field;
    private final Scanner scanner;

    public ConsoleUI(Field field)
    {
        this.field = field;
        this.scanner = new Scanner(System.in);
    }
}
