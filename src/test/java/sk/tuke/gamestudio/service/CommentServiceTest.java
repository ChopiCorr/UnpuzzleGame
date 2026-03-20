package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest
{

    private CommentService service;

    @BeforeEach
    public void setUp()
    {
        service = new CommentServiceJDBC();
        service.reset();
    }

    @Test
    public void testAddCommentAndRetrieve()
    {
        Comment comment = new Comment("PlayerA", "unpuzzle", "Skvelá hra!",
                Timestamp.valueOf(LocalDateTime.now()));
        service.addComment(comment);

        List<Comment> comments = service.getComments("unpuzzle");
        assertFalse(comments.isEmpty(), "Zoznam komentárov by nemal byť prázdny.");
        assertEquals("PlayerA", comments.get(0).getPlayer());
        assertEquals("Skvelá hra!", comments.get(0).getComment());
    }

    @Test
    public void testCommentsOrderedByDateDesc() throws InterruptedException
    {
        service.addComment(new Comment("PlayerA", "unpuzzle", "Prvý",
                Timestamp.valueOf(LocalDateTime.now())));
        Thread.sleep(50);
        service.addComment(new Comment("PlayerB", "unpuzzle", "Druhý",
                Timestamp.valueOf(LocalDateTime.now())));

        List<Comment> comments = service.getComments("unpuzzle");
        assertEquals(2, comments.size());
        assertEquals("Druhý", comments.get(0).getComment(),
                "Komentáre musia byť zoradené od najnovšieho.");
    }

    @Test
    public void testCommentsLimitedToTen()
    {
        for (int i = 1; i <= 15; i++)
        {
            service.addComment(new Comment("Player" + i, "unpuzzle", "Komentár " + i,
                    Timestamp.valueOf(LocalDateTime.now())));
        }
        List<Comment> comments = service.getComments("unpuzzle");
        assertTrue(comments.size() <= 10, "Výsledok by mal byť obmedzený na 10 komentárov.");
    }

    @Test
    public void testCommentsFilteredByGame()
    {
        service.addComment(new Comment("PlayerA", "unpuzzle", "Pre unpuzzle",
                Timestamp.valueOf(LocalDateTime.now())));
        service.addComment(new Comment("PlayerB", "othergame", "Pre inú hru",
                Timestamp.valueOf(LocalDateTime.now())));

        List<Comment> comments = service.getComments("unpuzzle");
        assertEquals(1, comments.size(), "Musia byť vrátené iba komentáre pre hru 'unpuzzle'.");
    }

    @Test
    public void testReset()
    {
        service.addComment(new Comment("PlayerA", "unpuzzle", "Test",
                Timestamp.valueOf(LocalDateTime.now())));
        service.reset();
        List<Comment> comments = service.getComments("unpuzzle");
        assertTrue(comments.isEmpty(), "Po reset() musí byť tabuľka prázdna.");
    }

    @Test
    public void testEmptyTableReturnsEmptyList()
    {
        List<Comment> comments = service.getComments("unpuzzle");
        assertNotNull(comments, "Výsledok nesmie byť null.");
        assertTrue(comments.isEmpty());
    }
}

