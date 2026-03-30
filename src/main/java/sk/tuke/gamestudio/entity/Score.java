package sk.tuke.gamestudio.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Score
{
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private int points;
    private Date playedOn;

    public Score() {}

    public Score(String game, String player, int points, Date playedOn)
    {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }
}
