package sk.tuke.gamestudio.entity;

import java.sql.Timestamp;

public class Comment
{
    private String player;
    private String game;
    private String comment;
    private Timestamp commentedOn;

    public Comment(String player, String game, String comment, Timestamp commentedOn)
    {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public String getPlayer() {
        return player;
    }

    public String getGame() {
        return game;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getCommentedOn() {
        return commentedOn;
    }

    public void setGame(String game) {
        this.game = game;
    }
}