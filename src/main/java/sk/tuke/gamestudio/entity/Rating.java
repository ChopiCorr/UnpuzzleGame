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
                name = "Rating.getAverageRating",
                query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game=:game"
        ),
        @NamedQuery(
                name = "Rating.getRating",
                query = "SELECT r FROM Rating r WHERE r.game=:game AND r.player=:player"
        ),
        @NamedQuery(
                name = "Rating.resetRatings",
                query = "DELETE FROM Rating"
        )
})

public class Rating
{
    @Id
    @GeneratedValue
    private int ident;

    private String player;
    private String game;
    private int rating;
    private Timestamp ratedOn;

    public Rating() {}

    public Rating(String player, String game, int rating, Timestamp ratedOn)
    {
        if (rating < 1 || rating > 5)
        {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getPlayer()
    {
        return player;
    }

    public String getGame()
    {
        return game;
    }

    public int getRating()
    {
        return rating;
    }

    public Timestamp getRatedOn()
    {
        return ratedOn;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setGame(String game)
    {
        this.game = game;
    }
}