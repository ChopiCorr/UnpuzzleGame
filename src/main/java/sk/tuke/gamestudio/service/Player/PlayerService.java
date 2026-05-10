package sk.tuke.gamestudio.service.Player;

public interface PlayerService
{
    boolean register(String username, String password);
    boolean login(String username, String password);
}