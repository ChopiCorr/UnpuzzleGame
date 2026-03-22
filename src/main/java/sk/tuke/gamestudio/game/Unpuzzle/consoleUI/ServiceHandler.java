package sk.tuke.gamestudio.game.Unpuzzle.consoleUI;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ServiceHandler
{
    private static final String GAME_NAME = "unpuzzle";

    private final Scanner scanner;
    private final ScoreService scoreService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public ServiceHandler(Scanner scanner,
                          ScoreService scoreService,
                          CommentService commentService,
                          RatingService ratingService)
    {
        this.scanner = scanner;
        this.scoreService = scoreService;
        this.commentService = commentService;
        this.ratingService = ratingService;
    }

    public ScoreService getScoreService()       { return scoreService; }
    public CommentService getCommentService()   { return commentService; }
    public RatingService getRatingService()     { return ratingService; }

    public int calculatePointsForMove(long seconds)
    {
        if (seconds <= 3)  return 20;
        if (seconds <= 6)  return 15;
        if (seconds <= 10) return 10;
        return 5;
    }

    public void saveScore(String player, int points)
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

    public void askForComment(String player)
    {
        if (commentService == null) return;
        System.out.print("  Chcete pridat komentar? (Enter = preskocit): ");
        System.out.flush();
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) return;

        if (text.length() > 64) text = text.substring(0, 64);

        try
        {
            Comment comment = new Comment(player, GAME_NAME, text,
                    Timestamp.valueOf(LocalDateTime.now()));
            commentService.addComment(comment);
            System.out.println("  Komentar bol ulozeny.");
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Komentar sa nepodarilo ulozit: " + e.getMessage());
        }
    }

    public void askForRating(String player)
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
            Rating rating = new Rating(player, GAME_NAME, stars,
                    Timestamp.valueOf(LocalDateTime.now()));
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
}