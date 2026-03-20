package sk.tuke.gamestudio.game.UnpuzzleFedorcencoD;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI
{
    public static final String GAME_NAME = "unpuzzle";

    private final Field field;
    private final Scanner scanner;

    private final ScoreService scoreService;
    private final CommentService commentService;
    private final RatingService ratingService;


    /*public ConsoleUI(Field field, Scanner scanner)
    {
        this.field = field;
        this.scanner = scanner;
    }*/

    public ConsoleUI(Field field, Scanner scanner,
                     ScoreService scoreService,
                     CommentService commentService,
                     RatingService ratingService)
    {
        this.field = field;
        this.scanner = scanner;
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    public ConsoleUI(Field field, Scanner scanner)
    {
        this(field, scanner, null, null, null);
    }


    public ConsoleUI(Field field)
    {
        this(field, new Scanner(System.in), null, null, null);
    }

    public void run(String playerName)
    {
        int moveCount = 0;

        while (field.getGameState() == GameState.PLAYING)
        {
            printField();
            int id = getUserInput();
            boolean success = field.removePiece(id);

            if (!success)
            {
                System.out.println("  Blok " + id + " je zablokovany! Cesta v smere sipky nie je volna.");
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

        int points = calculatePoints(field.getPieces().size(), moveCount);
        saveScore(playerName, points);
        showTopScores();
        askForComment(playerName);
        askForRating(playerName);
        showAverageRating();
    }

    private int calculatePoints(int pieceCount, int moveCount)
    {
        int base = pieceCount * 100;
        int penalty = moveCount * 5;
        return Math.max(10, base - penalty);
    }

    private void saveScore(String player, int points)
    {
        if (scoreService == null) return;
        try
        {
            Score score = new Score(player, GAME_NAME, points,
                    Timestamp.valueOf(LocalDateTime.now()));
            scoreService.addScore(score);
            System.out.println("  Skore " + points + " bodov bolo ulozene pre hraca " + player + ".");
        } catch (Exception e)
        {
            System.out.println("  [WARN] Skore sa nepodarilo ulozit: " + e.getMessage());
        }
    }

    private void showTopScores()
    {
        if (scoreService == null) return;
        try
        {
            List<Score> top = scoreService.getTopScores(GAME_NAME);
            System.out.println();
            System.out.println("  === TOP 10 SKORE ===");
            if (top.isEmpty())
            {
                System.out.println("  Zatial ziadne skore.");
            }
            else
            {
                int rank = 1;
                for (Score s : top)
                {
                    System.out.printf("  %2d. %s%n", rank++, s);
                }
            }
            System.out.println();
        } catch (Exception e)
        {
            System.out.println("  [WARN] Skore sa nepodarilo nacitat: " + e.getMessage());
        }
    }

    private void askForComment(String player)
    {
        if (commentService == null) return;
        System.out.print("  Chcete pridat komentar? (Enter = preskocit): ");
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) return;

        if (text.length() > 64) text = text.substring(0, 64);

        try
        {
            Comment comment = new Comment(player, GAME_NAME, text, Timestamp.valueOf(LocalDateTime.now()));
            commentService.addComment(comment);
            System.out.println("  Komentar bol ulozeny.");
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Komentar sa nepodarilo ulozit: " + e.getMessage());
        }

        showComments();
    }

    private void showComments()
    {
        if (commentService == null) return;
        try
        {
            List<Comment> comments = commentService.getComments(GAME_NAME);
            System.out.println();
            System.out.println("  === POSLEDNE KOMENTARE ===");
            if (comments.isEmpty())
            {
                System.out.println("  Zatial ziadne komentare.");
            }
            else
            {
                for (Comment c : comments)
                {
                    System.out.println("  " + c);
                }
            }
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Komentare sa nepodarilo nacitat: " + e.getMessage());
        }
    }

    private void askForRating(String player)
    {
        if (ratingService == null) return;

        try
        {
            int existing = ratingService.getRating(GAME_NAME, player);
            if (existing > 0)
            {
                System.out.println("  Vase aktualne hodnotenie hry: " + existing + "/5");
            }
        }
        catch (Exception ignored) {}

        System.out.print("  Ohodnoťte hru (1-5, Enter = preskocit): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;

        try
        {
            int stars = Integer.parseInt(input);
            if (stars < 1 || stars > 5)
            {
                System.out.println("  Neplatne hodnotenie. Zadajte cislo od 1 do 5.");
                return;
            }
            Rating rating = new Rating(player, GAME_NAME, stars, Timestamp.valueOf(LocalDateTime.now()));
            ratingService.setRating(rating);
            System.out.println("  Hodnotenie " + stars + "/5 bolo ulozene.");
        }
        catch (NumberFormatException e)
        {
            System.out.println("  Neplatny vstup. Hodnotenie nebolo ulozene.");
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Hodnotenie sa nepodarilo ulozit: " + e.getMessage());
        }
    }

    private void showAverageRating()
    {
        if (ratingService == null) return;
        try
        {
            int avg = ratingService.getAverageRating(GAME_NAME);
            System.out.println("  Priemerne hodnotenie hry Unpuzzle: " + avg + "/5");
            System.out.println();
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Hodnotenie sa nepodarilo nacitat: " + e.getMessage());
        }
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
                }
                else
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