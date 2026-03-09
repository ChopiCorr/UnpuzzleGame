package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

/*
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.Field;
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.Piece;
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.GameState;
import sk.tuke.gamestudio.game.UnpuzzleFedorcencoD.PieceState;
*/

import java.util.Scanner;

public class ConsoleUI
{
    private final Field field;
    private final Scanner scanner;

    public ConsoleUI(Field field, Scanner scanner)
    {
        this.field = field;
        this.scanner = scanner;
    }

    public ConsoleUI(Field field)
    {
        this(field, new Scanner(System.in));
    }

    public void run()
    {
        int moveCount = 0;

        while (field.getGameState() == GameState.PLAYING)
        {
            printField();
            int id = getUserInput();

            boolean success = field.removePiece(id);

            if (!success)
            {
                System.out.println("  Blok " + id + " je zablokovaný! Cesta v smere sipky nie je volna.");
            }
            else
            {
                moveCount++;
                System.out.println("  Blok " + id + " bol odstraneny. (tah c." + moveCount + ")");
            }
            System.out.println();
        }
        printField();
        printResult(moveCount);
    }


    public void printField()
    {

    }

    private void printResult(int moveCount) {
    }

    private int getUserInput()
    {
        while (true)
        {
            System.out.print("  Bloky na poli: ");

        }
        }
}