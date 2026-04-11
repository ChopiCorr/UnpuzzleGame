package sk.tuke.gamestudio.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Comment.getComments",
                query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.commentedOn DESC"
        ),
        @NamedQuery(
                name = "Comment.resetComments",
                query = "DELETE FROM Comment"
        )
})

public class Comment
{
    @Id
    @GeneratedValue
    private int ident;

    private String player;
    private String game;
    private String comment;
    private Timestamp commentedOn;

    public Comment() {}

    public Comment(String player, String game, String comment, Timestamp commentedOn)
    {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
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