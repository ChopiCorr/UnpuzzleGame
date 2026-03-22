package sk.tuke.gamestudio.game.Unpuzzle.consoleUI;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.Unpuzzle.Core.Field;
import sk.tuke.gamestudio.game.Unpuzzle.Core.GameState;
import sk.tuke.gamestudio.game.Unpuzzle.Core.Piece;
import sk.tuke.gamestudio.game.Unpuzzle.Core.PieceState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
                askForComment(playerName);
                continue;
            }
            if (input.equalsIgnoreCase("rating"))
            {
                askForRating(playerName);
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
                int pointsForMove = calculatePointsForMove(moveSeconds);
                totalPoints += pointsForMove;

                moveCount++;
                System.out.println("  Blok " + id + " bol odstraneny. (tah c." + moveCount + ")");
            }
            System.out.println();
        }

        printField();
        printResult(moveCount, totalPoints);

        saveScore(playerName, totalPoints);
        showTopScores();
        askForComment(playerName);
        askForRating(playerName);
        showAverageRating();
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

    private int calculatePointsForMove(long seconds)
    {
        if (seconds <= 3)  return 20;
        if (seconds <= 6)  return 15;
        if (seconds <= 10) return 10;
        return 5;
    }

    private void saveScore(String player, int points)
    {
        if (scoreService == null) return;
        try
        {
            Score score = new Score(GAME_NAME, player, points,
                    Timestamp.valueOf(LocalDateTime.now()));
            scoreService.addScore(score);
            System.out.println("  Skore " + points + " bodov bolo ulozene pre hraca " + player + ".");
        }
        catch (Exception e)
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
            System.out.println("  ╔════╦════════════════════╦════════╦═════════════════════╗");
            System.out.println("  ║ #  ║ Hráč               ║  Body  ║ Dátum               ║");
            System.out.println("  ╠════╬════════════════════╬════════╬═════════════════════╣");

            if (top.isEmpty())
            {
                System.out.println("  ║         Zatial ziadne skore.                         ║");
            }
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


    private void askForComment(String player)
    {
        if (commentService == null) return;
        System.out.print("  Chcete pridat komentar? (Enter = preskocit): ");
        System.out.flush();
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
            System.out.println("  ╔════════════════════╦══════════════════════════════════════════╦═════════════════════╗");
            System.out.println("  ║ Hráč               ║ Komentár                                 ║ Dátum               ║");
            System.out.println("  ╠════════════════════╬══════════════════════════════════════════╬═════════════════════╣");

            if (comments.isEmpty())
            {
                System.out.println("  ║                        Zatial ziadne komentare.                                 ║");
            }
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

        System.out.print("  Ohodnotte hru (1-5, Enter = preskocit): ");
        System.out.flush();
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;

        try
        {
            int stars = Integer.parseInt(input);
            if (stars < 1 || stars > 5)
            {
                System.out.println("  Neplatne hodnotenie. Zadajte cislo od 1 do 5.");
                System.out.flush();
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
            System.out.flush();
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

    private String getUserInput()
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
            System.out.print("  ID bloku / [comment] / [rating]: ");
            System.out.flush();

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("comment") || input.equalsIgnoreCase("rating"))
            {
                return input;
            }

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
}