package sk.tuke.gamestudio.entity;

import java.util.Date;

public class Score
{
    private String game;

    private String player;

    private int points;

    private Date playedOn;

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
}
