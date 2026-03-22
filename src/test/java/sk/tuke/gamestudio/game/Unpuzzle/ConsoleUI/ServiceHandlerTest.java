package sk.tuke.gamestudio.game.Unpuzzle.ConsoleUI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.Unpuzzle.consoleUI.ServiceHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceHandlerTest
{
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputCapture;

    @BeforeEach
    public void setUp()
    {
        outputCapture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputCapture));
    }

    @AfterEach
    public void tearDown()
    {
        System.setOut(originalOut);
    }

    private ServiceHandler createSession(String simulatedInput)
    {
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        return new ServiceHandler(scanner, null, null, null);
    }

    @Test
    public void testFastMoveGives20Points()
    {
        ServiceHandler session = createSession("");
        assertEquals(20, session.calculatePointsForMove(0));
        assertEquals(20, session.calculatePointsForMove(2));
        assertEquals(20, session.calculatePointsForMove(3));
    }

    @Test
    public void testMediumMoveGives15Points()
    {
        ServiceHandler session = createSession("");
        assertEquals(15, session.calculatePointsForMove(4));
        assertEquals(15, session.calculatePointsForMove(6));
    }

    @Test
    public void testSlowMoveGives10Points()
    {
        ServiceHandler session = createSession("");
        assertEquals(10, session.calculatePointsForMove(7));
        assertEquals(10, session.calculatePointsForMove(10));
    }

    @Test
    public void testVerySlowMoveGives5Points()
    {
        ServiceHandler session = createSession("");
        assertEquals(5, session.calculatePointsForMove(11));
        assertEquals(5, session.calculatePointsForMove(60));
    }

    @Test
    public void testBoundary3To4Seconds()
    {
        ServiceHandler session = createSession("");
        assertEquals(20, session.calculatePointsForMove(3));
        assertEquals(15, session.calculatePointsForMove(4));
    }

    @Test
    public void testBoundary6To7Seconds()
    {
        ServiceHandler session = createSession("");
        assertEquals(15, session.calculatePointsForMove(6));
        assertEquals(10, session.calculatePointsForMove(7));
    }

    @Test
    public void testBoundary10To11Seconds()
    {
        ServiceHandler session = createSession("");
        assertEquals(10, session.calculatePointsForMove(10));
        assertEquals(5, session.calculatePointsForMove(11));
    }

    @Test
    public void testSaveScoreWithNullServiceDoesNotThrow()
    {
        ServiceHandler session = createSession("");
        assertDoesNotThrow(() -> session.saveScore("TestPlayer", 100));
    }

    @Test
    public void testSaveScoreWithNullServiceProducesNoOutput()
    {
        ServiceHandler session = createSession("");
        session.saveScore("TestPlayer", 100);
        assertTrue(outputCapture.toString().isEmpty());
    }

    @Test
    public void testAskForCommentWithNullServiceDoesNotThrow()
    {
        ServiceHandler session = createSession("nejaky komentar\n");
        assertDoesNotThrow(() -> session.askForComment("TestPlayer"));
    }

    @Test
    public void testAskForCommentWithNullServiceProducesNoOutput()
    {
        ServiceHandler session = createSession("nejaky komentar\n");
        session.askForComment("TestPlayer");
        assertTrue(outputCapture.toString().isEmpty());
    }

    @Test
    public void testAskForRatingWithNullServiceDoesNotThrow()
    {
        ServiceHandler session = createSession("5\n");
        assertDoesNotThrow(() -> session.askForRating("TestPlayer"));
    }

    @Test
    public void testAskForRatingWithNullServiceProducesNoOutput()
    {
        ServiceHandler session = createSession("5\n");
        session.askForRating("TestPlayer");
        assertTrue(outputCapture.toString().isEmpty());
    }

    @Test
    public void testGettersReturnNullWhenServicesAreNull()
    {
        ServiceHandler session = createSession("");
        assertNull(session.getScoreService());
        assertNull(session.getCommentService());
        assertNull(session.getRatingService());
    }
}