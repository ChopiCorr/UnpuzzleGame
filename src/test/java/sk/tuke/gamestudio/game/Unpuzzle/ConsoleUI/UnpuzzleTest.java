package sk.tuke.gamestudio.game.Unpuzzle.consoleUI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class UnpuzzleTest
{
    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
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
        System.setIn(originalIn);
    }

    private void setInput(String input)
    {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void testMainRunsWithoutException()
    {
        setInput("TestPlayer\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        assertDoesNotThrow(() -> Unpuzzle.main(new String[]{}),
                "main() nesmie vyhodiť výnimku pri platnom vstupe.");
    }

    @Test
    public void testBannerIsDisplayed()
    {
        setInput("TestPlayer\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        Unpuzzle.main(new String[]{});
        String output = outputCapture.toString();
        assertTrue(output.contains("U N P U Z Z L E"),
                "Banner musí obsahovať názov hry U N P U Z Z L E.");
    }

    @Test
    public void testLevelMenuIsDisplayed()
    {
        setInput("TestPlayer\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        Unpuzzle.main(new String[]{});
        String output = outputCapture.toString();
        assertTrue(output.contains("Vyberte uroven"),
                "Menu musí zobrazovať výber úrovne.");
    }

    @Test
    public void testAllLevelNamesAppearedInMenu()
    {
        setInput("TestPlayer\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        Unpuzzle.main(new String[]{});
        String output = outputCapture.toString();
        assertTrue(output.contains("1."), "Menu musí obsahovať Level 1.");
        assertTrue(output.contains("2."), "Menu musí obsahovať Level 2.");
        assertTrue(output.contains("3."), "Menu musí obsahovať Level 3.");
        assertTrue(output.contains("4."), "Menu musí obsahovať Level 4.");
    }

    @Test
    public void testPlayerNamePromptIsDisplayed()
    {
        setInput("TestPlayer\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        Unpuzzle.main(new String[]{});
        String output = outputCapture.toString();
        assertTrue(output.contains("meno"),
                "Musí byť zobrazená výzva na zadanie mena hráča.");
    }

    @Test
    public void testGoodbyeMessageIsDisplayed()
    {
        setInput("TestPlayer\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        Unpuzzle.main(new String[]{});
        String output = outputCapture.toString();
        assertTrue(output.contains("Zbohom") || output.contains("Dakujeme"),
                "Na konci musí byť zobrazená rozlúčková správa.");
    }

    @Test
    public void testInvalidLevelChoiceIsHandled()
    {
        setInput("TestPlayer\n9\n0\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        assertDoesNotThrow(() -> Unpuzzle.main(new String[]{}),
                "Neplatná voľba úrovne nesmie spôsobiť výnimku.");
    }

    @Test
    public void testEmptyPlayerNameDefaultsToAnonymous()
    {
        setInput("\n1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\nn\n");
        Unpuzzle.main(new String[]{});
        String output = outputCapture.toString();
        assertTrue(output.contains("Anonymous"),
                "Prázdne meno hráča musí byť nahradené 'Anonymous'.");
    }
}