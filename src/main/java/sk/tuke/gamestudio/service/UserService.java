package sk.tuke.gamestudio.service;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserService
{
    private final Map<String, String> users = new HashMap<>();

    public boolean register(String username, String password)
    {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;
        if (users.containsKey(username)) return false;
        users.put(username.trim(), password);
        return true;
    }

    public boolean login(String username, String password)
    {
        if (username == null || password == null) return false;
        return password.equals(users.get(username.trim()));
    }
}