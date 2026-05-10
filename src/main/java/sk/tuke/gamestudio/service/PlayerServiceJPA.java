package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Player;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class PlayerServiceJPA implements PlayerService
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean register(String username, String password)
    {
        if (username == null || username.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;

        List<Player> existing = entityManager
                .createNamedQuery("Player.findByUsername", Player.class)
                .setParameter("username", username.trim())
                .getResultList();

        if (!existing.isEmpty()) return false;

        entityManager.persist(new Player(username.trim(), password));
        return true;
    }

    @Override
    public boolean login(String username, String password)
    {
        if (username == null || password == null) return false;

        List<Player> result = entityManager
                .createNamedQuery("Player.findByUsername", Player.class)
                .setParameter("username", username.trim())
                .getResultList();

        if (result.isEmpty()) return false;
        return password.equals(result.get(0).getPassword());
    }
}