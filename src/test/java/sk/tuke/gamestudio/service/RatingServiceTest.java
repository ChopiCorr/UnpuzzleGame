package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import sk.tuke.gamestudio.entity.Rating;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(RatingServiceJPA.class)
@Transactional
@ContextConfiguration(classes = TestJpaConfig.class)
public class RatingServiceTest
{
    @Autowired
    private RatingServiceJPA service;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    public void setUp()
    {
        service.reset();
        em.flush();
    }

    @Test
    public void testSetAndGetRating()
    {
        service.setRating(new Rating("PlayerA", "unpuzzle", 4,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        int result = service.getRating("unpuzzle", "PlayerA");
        assertEquals(4, result, "Hodnotenie musí byť rovnaké ako bolo uložené.");
    }

    @Test
    public void testSetRatingUpdatesExistingRating()
    {
        service.setRating(new Rating("PlayerA", "unpuzzle", 3,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        service.setRating(new Rating("PlayerA", "unpuzzle", 5,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        int result = service.getRating("unpuzzle", "PlayerA");
        assertEquals(5, result, "Po aktualizácii musí byť nová hodnota hodnotenia.");
    }

    @Test
    public void testAverageRating()
    {
        service.setRating(new Rating("PlayerA", "unpuzzle", 4,
                Timestamp.valueOf(LocalDateTime.now())));
        service.setRating(new Rating("PlayerB", "unpuzzle", 2,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        int avg = service.getAverageRating("unpuzzle");
        assertEquals(3, avg, "Priemer hodnotení 4 a 2 musí byť 3.");
    }

    @Test
    public void testAverageRatingNoEntries()
    {
        int avg = service.getAverageRating("unpuzzle");
        assertEquals(0, avg, "Priemer pre prázdnu tabuľku musí byť 0.");
    }

    @Test
    public void testGetRatingNoEntry()
    {
        int result = service.getRating("unpuzzle", "NonExistentPlayer");
        assertEquals(0, result, "Hodnotenie pre neexistujúceho hráča musí byť 0.");
    }

    @Test
    public void testReset()
    {
        service.setRating(new Rating("PlayerA", "unpuzzle", 5,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();
        service.reset();
        em.flush();

        assertEquals(0, service.getAverageRating("unpuzzle"),
                "Po reset() musí byť tabuľka prázdna.");
    }

    @Test
    public void testRatingBoundaries()
    {
        assertThrows(IllegalArgumentException.class, () ->
                        new Rating("PlayerA", "unpuzzle", 0,
                                Timestamp.valueOf(LocalDateTime.now())),
                "Hodnotenie 0 musí vyhodiť výnimku.");
        assertThrows(IllegalArgumentException.class, () ->
                        new Rating("PlayerA", "unpuzzle", 6,
                                Timestamp.valueOf(LocalDateTime.now())),
                "Hodnotenie 6 musí vyhodiť výnimku.");
    }
}