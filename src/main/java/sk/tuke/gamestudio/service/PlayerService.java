package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

public interface PlayerService
{
    boolean register(String username, String password);
    boolean login(String username, String password);
}