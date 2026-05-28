package sk.tuke.gamestudio.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Score.getTopScores",
                query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC"
        ),
        @NamedQuery(
                name = "Score.resetScores",
                query = "DELETE FROM Score"
        )
})

public class Score
{
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private int points;
    private Date playedOn;
//cas do bd1-    private long duration;

    public Score() {}
//pridat long duration a this.duration = duration;
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

//    public long getDuration() { return duration; }
}
