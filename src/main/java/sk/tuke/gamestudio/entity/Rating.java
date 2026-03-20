package sk.tuke.gamestudio.entity;

import java.sql.Timestamp;

public class Rating
{

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

    public void setPlayer(String player)
    {
        this.player = player;
    }

    public void setGame(String game)
    {
        this.game = game;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public void setRatedOn(Timestamp ratedOn)
    {
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString()
    {
        return String.format("%-20s : %d/5  (%s)", player, rating, ratedOn);
    }
}