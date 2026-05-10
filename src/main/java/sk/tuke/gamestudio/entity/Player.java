package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Player.findByUsername",
                query = "SELECT p FROM Player p WHERE p.username = :username"
        )
})
public class Player
{
    @Id
    @GeneratedValue
    private int ident;

    private String username;
    private String password;

    public Player() {}

    public Player(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public int getIdent() { return ident; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}