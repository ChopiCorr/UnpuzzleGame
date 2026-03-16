package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

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
        System.out.println("  +" + "------".repeat(field.getCols()) + "+");
        for (int row = 0; row < field.getRows(); row++)
        {
            System.out.print("  |");
            for (int col = 0; col < field.getCols(); col++)
            {
                Piece piece = field.getPieceAt(row, col);
                if (piece == null)
                {
                    System.out.print("[    ]");
                } else
                {
                    System.out.printf("[%2d%s ]", piece.getId(), piece.getDirection().getSymbol());
                }
            }
            System.out.println("|");
        }
        System.out.println("  +" + "------".repeat(field.getCols()) + "+");
    }

    private void printResult(int moveCount)
    {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       GRATULUJEME! VYHRALI STE!      ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf( "║  %-36s║%n", "Vsetky bloky odstranene.");
        System.out.printf( "║  Pocet tahov: %-22d║%n", moveCount);
        System.out.println("╚══════════════════════════════════════╝");
    }

    private int getUserInput()
    {
        while (true)
        {
            System.out.print("  Bloky na poli: ");
            for (Piece piece : field.getPieces())
            {
                if (piece.getState() == PieceState.ON_BOARD)
                {
                    System.out.print(piece.getId() + piece.getDirection().getSymbol() + " ");
                }
            }
            System.out.println();
            System.out.print("  Zadajte ID bloku: ");

            if (!scanner.hasNextInt())
            {
                System.out.println("  Neplatny vstup. Zadajte cislo.");
                scanner.nextLine();
                continue;
            }

            int id = scanner.nextInt();
            scanner.nextLine();

            boolean valid = false;
            for (Piece piece : field.getPieces())
            {
                if (piece.getId() == id && piece.getState() == PieceState.ON_BOARD)
                {
                    valid = true;
                    break;
                }
            }

            if (!valid)
            {
                System.out.println("  Blok c." + id + " neexistuje alebo je uz odstraneny.");
                continue;
            }

            return id;
        }
    }
}