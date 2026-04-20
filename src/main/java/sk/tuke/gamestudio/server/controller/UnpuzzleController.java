package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.Unpuzzle.Core.*;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UnpuzzleController
{
    public static final String GAME_NAME = "unpuzzle";

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private Field field;
    private String playerName = "Anonymous";
    private int totalPoints = 0;
    private int moveCount = 0;
    private boolean scoreSaved = false;
    private String message = "";

    @RequestMapping("/unpuzzle")
    public String unpuzzle(
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "pieceId", required = false) String pieceId,
            @RequestParam(value = "player", required = false) String player,
            @RequestParam(value = "level", required = false) String level,
            @RequestParam(value = "comment", required = false) String comment,
            @RequestParam(value = "rating", required = false) String rating,
            Model model)
    {
        if (player != null && !player.isEmpty())
        {
            playerName = player;
        }

        if (action != null)
        {
            switch (action)
            {
                case "new":
                    int lvl = (level != null) ? Integer.parseInt(level) : 1;
                    field = new Field(LevelPresets.getLevel(lvl));
                    totalPoints = 0;
                    moveCount = 0;
                    scoreSaved = false;
                    message = "";
                    break;

                case "remove":
                    if (field != null && pieceId != null)
                    {
                        int id = Integer.parseInt(pieceId);
                        long start = System.currentTimeMillis();
                        boolean success = field.removePiece(id);
                        if (success)
                        {
                            long seconds = (System.currentTimeMillis() - start) / 1000;
                            totalPoints += calcPoints(seconds);
                            moveCount++;
                            message = "Blok " + id + " odstraneny.";

                            if (field.getGameState() == GameState.SOLVED && !scoreSaved)
                            {
                                saveScore();
                                scoreSaved = true;
                                message = "Vyhral si! Skore: " + totalPoints;
                            }
                        }
                        else
                        {
                            message = "Blok " + id + " je zablokovany!";
                        }
                    }
                    break;

                case "comment":
                    if (comment != null && !comment.isEmpty())
                    {
                        try
                        {
                            commentService.addComment(new Comment(
                                    playerName, GAME_NAME,
                                    comment.substring(0, Math.min(comment.length(), 64)),
                                    new java.sql.Timestamp(System.currentTimeMillis())
                            ));
                        }
                        catch (Exception e) { message = "Komentar sa nepodarilo ulozit."; }
                    }
                    break;

                case "rating":
                    if (rating != null)
                    {
                        try
                        {
                            int stars = Integer.parseInt(rating);
                            if (stars >= 1 && stars <= 5)
                            {
                                ratingService.setRating(new Rating(
                                        playerName, GAME_NAME, stars,
                                        new java.sql.Timestamp(System.currentTimeMillis())
                                ));
                            }
                        }
                        catch (Exception e) { message = "Hodnotenie sa nepodarilo ulozit."; }
                    }
                    break;
            }
        }

        model.addAttribute("field", field);
        model.addAttribute("playerName", playerName);
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("moveCount", moveCount);
        model.addAttribute("message", message);
        model.addAttribute("solved", field != null && field.getGameState() == GameState.SOLVED);
        model.addAttribute("levelCount", LevelPresets.LEVEL_COUNT);

        try { model.addAttribute("topScores", scoreService.getTopScores(GAME_NAME)); }
        catch (Exception e) { model.addAttribute("topScores", List.of()); }

        try { model.addAttribute("comments", commentService.getComments(GAME_NAME)); }
        catch (Exception e) { model.addAttribute("comments", List.of()); }

        try { model.addAttribute("avgRating", ratingService.getAverageRating(GAME_NAME)); }
        catch (Exception e) { model.addAttribute("avgRating", 0); }

        return "unpuzzle";
    }

    private void saveScore()
    {
        try
        {
            scoreService.addScore(new Score(GAME_NAME, playerName, totalPoints, new Date()));
        }
        catch (Exception e) { message = "Skore sa nepodarilo ulozit."; }
    }

    private int calcPoints(long seconds)
    {
        if (seconds <= 3)  return 20;
        if (seconds <= 6)  return 15;
        if (seconds <= 10) return 10;
        return 5;
    }

    public String getHtmlField()
    {
        if (field == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='game-field'>");
        for (int row = 0; row < field.getRows(); row++)
        {
            sb.append("<tr>");
            for (int col = 0; col < field.getCols(); col++)
            {
                Piece piece = field.getPieceAt(row, col);
                if (piece == null || piece.getState() == PieceState.REMOVED)
                {
                    sb.append("<td class='cell empty'></td>");
                }
                else
                {
                    boolean blocked = field.isBlocked(piece);
                    String css = blocked ? "cell piece blocked" : "cell piece";
                    String arrow = piece.getDirection().getSymbol();
                    sb.append("<td class='").append(css).append("'>")
                            .append("<a href='/unpuzzle?action=remove&pieceId=").append(piece.getId()).append("'>")
                            .append(piece.getId()).append(arrow)
                            .append("</a></td>");
                }
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
}