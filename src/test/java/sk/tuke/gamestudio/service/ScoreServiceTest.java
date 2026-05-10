package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.Score.ScoreServiceJPA;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ScoreServiceJPA.class)
@Transactional
@ContextConfiguration(classes = TestJpaConfig.class)
public class ScoreServiceTest
{
    @Autowired
    private ScoreServiceJPA service;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    public void setUp()
    {
        service.reset();
        em.flush();
    }

    @Test
    public void testAddScoreAndRetrieve()
    {
        service.addScore(new Score("unpuzzle", "TestPlayer", 300,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        List<Score> top = service.getTopScores("unpuzzle");
        assertFalse(top.isEmpty(), "Zoznam skóre by nemal byť prázdny po pridaní záznamu.");
        assertEquals("TestPlayer", top.get(0).getPlayer());
        assertEquals(300, top.get(0).getPoints());
    }

    @Test
    public void testTopScoresOrderedByPointsDesc()
    {
        service.addScore(new Score("unpuzzle", "PlayerA", 100,
                Timestamp.valueOf(LocalDateTime.now())));
        service.addScore(new Score("unpuzzle", "PlayerB", 500,
                Timestamp.valueOf(LocalDateTime.now())));
        service.addScore(new Score("unpuzzle", "PlayerC", 300,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        List<Score> top = service.getTopScores("unpuzzle");
        assertEquals(3, top.size());
        assertTrue(top.get(0).getPoints() >= top.get(1).getPoints(),
                "Skóre nie sú zoradené zostupne.");
        assertTrue(top.get(1).getPoints() >= top.get(2).getPoints(),
                "Skóre nie sú zoradené zostupne.");
    }

    @Test
    public void testTopScoresLimitedToTen()
    {
        for (int i = 1; i <= 15; i++)
        {
            service.addScore(new Score("unpuzzle", "Player" + i, i * 10,
                    Timestamp.valueOf(LocalDateTime.now())));
        }
        em.flush();

        List<Score> top = service.getTopScores("unpuzzle");
        assertTrue(top.size() <= 10, "Výsledok by mal byť obmedzený na 10 záznamov.");
    }

    @Test
    public void testTopScoresFilteredByGame()
    {
        service.addScore(new Score("unpuzzle", "PlayerA", 200,
                Timestamp.valueOf(LocalDateTime.now())));
        service.addScore(new Score("othergame", "PlayerB", 999,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();

        List<Score> top = service.getTopScores("unpuzzle");
        assertEquals(1, top.size(), "Musia byť vrátené iba skóre pre hru 'unpuzzle'.");
        assertEquals("PlayerA", top.get(0).getPlayer());
    }

    @Test
    public void testReset()
    {
        service.addScore(new Score("unpuzzle", "PlayerA", 100,
                Timestamp.valueOf(LocalDateTime.now())));
        em.flush();
        service.reset();
        em.flush();

        List<Score> top = service.getTopScores("unpuzzle");
        assertTrue(top.isEmpty(), "Po reset() musí byť tabuľka prázdna.");
    }

    @Test
    public void testEmptyTableReturnsEmptyList()
    {
        List<Score> top = service.getTopScores("unpuzzle");
        assertNotNull(top, "Výsledok nesmie byť null.");
        assertTrue(top.isEmpty(), "Prázdna tabuľka musí vrátiť prázdny zoznam.");
    }
}