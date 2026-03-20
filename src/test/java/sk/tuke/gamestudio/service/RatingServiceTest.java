package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RatingServiceTest
{

    private RatingService service;

    @BeforeEach
    public void setUp()
    {
        service = new RatingServiceJDBC();
        service.reset();
    }

    @Test
    public void testSetAndGetRating()
    {
        Rating rating = new Rating("PlayerA", "unpuzzle", 4,
                Timestamp.valueOf(LocalDateTime.now()));
        service.setRating(rating);

        int result = service.getRating("unpuzzle", "PlayerA");
        assertEquals(4, result, "Hodnotenie musí byť rovnaké ako bolo uložené.");
    }

    @Test
    public void testSetRatingUpdatesExistingRating()
    {
        service.setRating(new Rating("PlayerA", "unpuzzle", 3,
                Timestamp.valueOf(LocalDateTime.now())));

        service.setRating(new Rating("PlayerA", "unpuzzle", 5,
                Timestamp.valueOf(LocalDateTime.now())));

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
        service.reset();
        assertEquals(0, service.getAverageRating("unpuzzle"),
                "Po reset() musí byť tabuľka prázdna.");
    }

    @Test
    public void testRatingBoundaries()
    {
        assertThrows(IllegalArgumentException.class, () ->
                        new Rating("PlayerA", "unpuzzle", 0, Timestamp.valueOf(LocalDateTime.now())),
                "Hodnotenie 0 musí vyhodiť výnimku.");
        assertThrows(IllegalArgumentException.class, () ->
                        new Rating("PlayerA", "unpuzzle", 6, Timestamp.valueOf(LocalDateTime.now())),
                "Hodnotenie 6 musí vyhodiť výnimku.");
    }
}

