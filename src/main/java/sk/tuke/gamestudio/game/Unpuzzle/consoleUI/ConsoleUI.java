package sk.tuke.gamestudio.game.Unpuzzle.consoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.Unpuzzle.Core.Field;
import sk.tuke.gamestudio.game.Unpuzzle.Core.GameState;
import sk.tuke.gamestudio.game.Unpuzzle.Core.Level;
import sk.tuke.gamestudio.game.Unpuzzle.Core.LevelPresets;
import sk.tuke.gamestudio.game.Unpuzzle.Core.Piece;
import sk.tuke.gamestudio.game.Unpuzzle.Core.PieceState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI
{
    public static final String GAME_NAME = "unpuzzle";

    private Field field;
    private final Scanner scanner;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private ServiceHandler session;

    public ConsoleUI(Scanner scanner)
    {
        this.scanner = scanner;
    }

    public void play()
    {
        session = new ServiceHandler(scanner, scoreService, commentService, ratingService);

        printBanner();

        String playerName = "";
        while (playerName.isEmpty())
        {
            System.out.print("Zadajte svoje meno: ");
            System.out.flush();
            playerName = scanner.nextLine().trim();

            if (playerName.equalsIgnoreCase("wipe"))
            {
                wipeAllData();
                playerName = "";
                continue;
            }

            if (playerName.isEmpty()) playerName = "Anonymous";
            if (playerName.length() > 64) playerName = playerName.substring(0, 64);
        }
        System.out.println();

        int choice = selectLevel();
        field = new Field(LevelPresets.getLevel(choice));

        run(playerName);

        while (playAgain())
        {
            choice = selectLevel();
            field = new Field(LevelPresets.getLevel(choice));
            run(playerName);
        }

        System.out.println();
        System.out.println("Dakujeme za hru! Zbohom.");
        scanner.close();
    }

    public void run(String playerName)
    {
        int moveCount = 0;
        int totalPoints = 0;

        while (field.getGameState() == GameState.PLAYING)
        {
            printField();
            printLiveScore(totalPoints, moveCount);
            long moveStart = System.currentTimeMillis();

            String input = getUserInput();

            if (input.equalsIgnoreCase("comment"))
            {
                session.askForComment(playerName);
                showComments();
                continue;
            }
            if (input.equalsIgnoreCase("rating"))
            {
                session.askForRating(playerName);
                showAverageRating();
                continue;
            }

            int id = Integer.parseInt(input);
            boolean success = field.removePiece(id);

            if (!success)
            {
                System.out.println("  Blok " + id + " je zablokovany! Cesta v smere sipky nie je volna.");
            }
            else
            {
                long moveSeconds = (System.currentTimeMillis() - moveStart) / 1000;
                int pointsForMove = session.calculatePointsForMove(moveSeconds);
                totalPoints += pointsForMove;
                moveCount++;
                System.out.println("  Blok " + id + " bol odstraneny. (tah c." + moveCount + ")");
            }
            System.out.println();
        }

        printField();
        printResult(moveCount, totalPoints);

        session.saveScore(playerName, totalPoints);
        showTopScores();
        session.askForComment(playerName);
        showComments();
        session.askForRating(playerName);
        showAverageRating();
    }

    private void wipeAllData()
    {
        try
        {
            if (scoreService   != null) scoreService.reset();
            if (commentService != null) commentService.reset();
            if (ratingService  != null) ratingService.reset();
            System.out.println("  [WIPE] Vsetky data boli vymazane.");
        }
        catch (Exception e)
        {
            System.out.println("  [WIPE] Chyba pri mazani dat: " + e.getMessage());
        }
    }

    private int selectLevel()
    {
        System.out.println("Vyberte uroven:");
        for (int i = 1; i <= LevelPresets.LEVEL_COUNT; i++)
        {
            Level level = LevelPresets.getLevel(i);
            System.out.println("  " + i + ".  " + level.getName());
        }
        System.out.println();

        int choice = 0;
        while (choice < 1 || choice > LevelPresets.LEVEL_COUNT)
        {
            System.out.print("Vasa volba (1-" + LevelPresets.LEVEL_COUNT + "): ");
            if (scanner.hasNextInt())
            {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > LevelPresets.LEVEL_COUNT)
                    System.out.println("  Neplatna volba. Zadajte cislo od 1 do " + LevelPresets.LEVEL_COUNT + ".");
            }
            else
            {
                System.out.println("  Neplatny vstup. Zadajte cislo.");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private boolean playAgain()
    {
        System.out.println();
        System.out.print("Chcete hrat znova? (a = ano, inak ukoncit): ");
        String input = scanner.next().trim().toLowerCase();
        return input.equals("a");
    }

    private void printBanner()
    {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║                                      ║");
        System.out.println("║          U N P U Z Z L E             ║");
        System.out.println("║                                      ║");
        System.out.println("║  Odstranujte bloky v spravnom        ║");
        System.out.println("║  poradi. Kazdy blok sa pohybuje      ║");
        System.out.println("║  v smere svojej sipky.               ║");
        System.out.println("║                                      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
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
                    System.out.print("[    ]");
                else
                    System.out.printf("[%2d%s ]", piece.getId(), piece.getDirection().getSymbol());
            }
            System.out.println("|");
        }
        System.out.println("  +" + "------".repeat(field.getCols()) + "+");
    }

    private void printLiveScore(int totalPoints, int moveCount)
    {
        int remaining = (int) field.getPieces().stream()
                .filter(p -> p.getState() == PieceState.ON_BOARD)
                .count();

        System.out.println("  Skore: " + totalPoints + "b"
                + "  |  Tahy: " + moveCount
                + "  |  Zostatok blokov: " + remaining);
        System.out.println();
    }

    private void printResult(int moveCount, int totalPoints)
    {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       GRATULUJEME! VYHRALI STE!      ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf( "║  %-36s║%n", "Vsetky bloky odstranene.");
        System.out.printf( "║  Pocet tahov:   %-21d║%n", moveCount);
        System.out.printf( "║  Celkove skore: %-21d║%n", totalPoints);
        System.out.println("╚══════════════════════════════════════╝");
    }

    public void showTopScores()
    {
        if (scoreService == null) return;
        try
        {
            List<Score> top = scoreService.getTopScores(GAME_NAME);
            System.out.println();
            System.out.println("  ╔════╦════════════════════╦════════╦═════════════════════╗");
            System.out.println("  ║ #  ║ Hráč               ║  Body  ║ Dátum               ║");
            System.out.println("  ╠════╬════════════════════╬════════╬═════════════════════╣");

            if (top.isEmpty())
                System.out.println("  ║         Zatial ziadne skore.                         ║");
            else
            {
                int rank = 1;
                for (Score s : top)
                {
                    System.out.printf("  ║ %-2d ║ %-18s ║ %6d ║ %-19s ║%n",
                            rank++,
                            truncate(s.getPlayer(), 18),
                            s.getPoints(),
                            formatDate(s.getPlayedOn()));
                }
            }
            System.out.println("  ╚════╩════════════════════╩════════╩═════════════════════╝");
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Skore sa nepodarilo nacitat: " + e.getMessage());
        }
    }

    public void showComments()
    {
        if (commentService == null) return;
        try
        {
            List<Comment> comments = commentService.getComments(GAME_NAME);
            System.out.println();
            System.out.println("  ╔════════════════════╦══════════════════════════════════════════╦═════════════════════╗");
            System.out.println("  ║ Hráč               ║ Komentár                                 ║ Dátum               ║");
            System.out.println("  ╠════════════════════╬══════════════════════════════════════════╬═════════════════════╣");

            if (comments.isEmpty())
                System.out.println("  ║                        Zatial ziadne komentare.                                 ║");
            else
            {
                for (Comment c : comments)
                {
                    System.out.printf("  ║ %-18s ║ %-40s ║ %-19s ║%n",
                            truncate(c.getPlayer(), 18),
                            truncate(c.getComment(), 40),
                            formatDate(c.getCommentedOn()));
                }
            }
            System.out.println("  ╚════════════════════╩══════════════════════════════════════════╩═════════════════════╝");
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Komentare sa nepodarilo nacitat: " + e.getMessage());
        }
    }

    public void showAverageRating()
    {
        if (ratingService == null) return;
        try
        {
            int avg = ratingService.getAverageRating(GAME_NAME);
            System.out.println("  Priemerne hodnotenie hry Unpuzzle: " + avg + "/5");
            System.out.println();
            System.out.flush();
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Hodnotenie sa nepodarilo nacitat: " + e.getMessage());
        }
    }

    private String getUserInput()
    {
        while (true)
        {
            System.out.print("  Bloky na poli: ");
            for (Piece piece : field.getPieces())
            {
                if (piece.getState() == PieceState.ON_BOARD)
                    System.out.print(piece.getId() + piece.getDirection().getSymbol() + " ");
            }
            System.out.println();
            System.out.print("  ID bloku / [comment] / [rating]: ");
            System.out.flush();

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("comment") || input.equalsIgnoreCase("rating"))
                return input;

            if (input.isEmpty())
            {
                System.out.println("  Neplatny vstup. Zadajte cislo alebo prikaz.");
                continue;
            }

            int id;
            try
            {
                id = Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("  Neznamy prikaz. Zadajte cislo, 'comment' alebo 'rating'.");
                continue;
            }

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
                System.out.println("  Blok c." + id + " neexistuje.");
                continue;
            }

            return String.valueOf(id);
        }
    }

    private String truncate(String text, int maxLen)
    {
        if (text == null) return "";
        return text.length() <= maxLen ? text : text.substring(0, maxLen - 2) + "..";
    }

    private String formatDate(java.util.Date date)
    {
        if (date == null) return "-";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }
}