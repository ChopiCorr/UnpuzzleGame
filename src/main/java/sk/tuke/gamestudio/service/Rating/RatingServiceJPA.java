package sk.tuke.gamestudio.service.Rating;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService
{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException
    {
        List<Rating> existing = entityManager.createNamedQuery("Rating.getRating", Rating.class)
                .setParameter("game", rating.getGame())
                .setParameter("player", rating.getPlayer())
                .getResultList();

        if (existing.isEmpty())
        {
            entityManager.persist(rating);
        }
        else
        {
            existing.get(0).setRating(rating.getRating());
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException
    {
        Double result = (Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();

        return result != null ? result.intValue() : 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException
    {
        List<Rating> result = entityManager.createNamedQuery("Rating.getRating", Rating.class)
                .setParameter("game", game)
                .setParameter("player", player)
                .getResultList();

        return result.isEmpty() ? 0 : result.get(0).getRating();
    }

    @Override
    public void reset() throws RatingException
    {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}