package sk.tuke.gamestudio.game.Unpuzzle.ConsoleUI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.Unpuzzle.Core.*;
import sk.tuke.gamestudio.game.Unpuzzle.consoleUI.ConsoleUI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleUITest
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

    private ConsoleUI createUI(Field field, String simulatedInput)
    {
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        return new ConsoleUI(field, scanner, null, null, null);
    }

    @Test
    public void testGameNameConstant()
    {
        assertEquals("unpuzzle", ConsoleUI.GAME_NAME);
    }

    @Test
    public void testPrintFieldContainsBorderChars()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        ConsoleUI ui = createUI(field, "");
        ui.printField();
        String output = outputCapture.toString();
        assertTrue(output.contains("+"));
        assertTrue(output.contains("|"));
    }

    @Test
    public void testPrintFieldShowsEmptyCell()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        ConsoleUI ui = createUI(field, "");
        ui.printField();
        assertTrue(outputCapture.toString().contains("[    ]"));
    }

    @Test
    public void testPrintFieldShowsArrowSymbol()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        ConsoleUI ui = createUI(field, "");
        ui.printField();
        String output = outputCapture.toString();
        boolean hasArrow = output.contains("↑") || output.contains("↓")
                || output.contains("←") || output.contains("→");
        assertTrue(hasArrow);
    }

    @Test
    public void testPrintFieldRowCount()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        ConsoleUI ui = createUI(field, "");
        ui.printField();
        long rowLines = outputCapture.toString().lines()
                .filter(line -> line.contains("|") && !line.contains("+"))
                .count();
        assertEquals(field.getRows(), rowLines);
    }

    @Test
    public void testPrintFieldLevel4Has4Rows()
    {
        Field field = new Field(LevelPresets.getLevel(4));
        ConsoleUI ui = createUI(field, "");
        ui.printField();
        long rowLines = outputCapture.toString().lines()
                .filter(line -> line.contains("|") && !line.contains("+"))
                .count();
        assertEquals(4, rowLines);
    }

    @Test
    public void testRunCompletesLevel1WithValidInput()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        String input = "2\n3\n1\n4\n5\n6\n7\n8\n\n\n";
        ConsoleUI ui = createUI(field, input);
        ui.run("TestPlayer");
        assertEquals(GameState.SOLVED, field.getGameState());
    }

    @Test
    public void testRunHandlesInvalidInputGracefully()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        String input = "abc\n999\n2\n3\n1\n4\n5\n6\n7\n8\n\n\n";
        ConsoleUI ui = createUI(field, input);
        assertDoesNotThrow(() -> ui.run("TestPlayer"));
    }

    @Test
    public void testRunHandlesBlockedPieceAttempt()
    {
        Field field = new Field(LevelPresets.getLevel(1));
        String input = "1\n2\n3\n1\n4\n5\n6\n7\n8\n\n\n";
        ConsoleUI ui = createUI(field, input);
        assertDoesNotThrow(() -> ui.run("TestPlayer"));
    }
}