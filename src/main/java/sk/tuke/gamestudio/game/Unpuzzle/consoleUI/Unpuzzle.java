package sk.tuke.gamestudio.game.Unpuzzle.consoleUI;

import sk.tuke.gamestudio.game.Unpuzzle.Core.Field;
import sk.tuke.gamestudio.game.Unpuzzle.Core.Level;
import sk.tuke.gamestudio.game.Unpuzzle.Core.LevelPresets;
import sk.tuke.gamestudio.service.*;

import java.util.Scanner;

public class Unpuzzle
{
    /* public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        ScoreService scoreService     = null;
        CommentService commentService = null;
        RatingService ratingService   = null;

        try
        {
            scoreService   = new ScoreServiceJDBC();
            commentService = new CommentServiceJDBC();
            ratingService  = new RatingServiceJDBC();
            System.out.println("  [DB] Pripojenie k databaze uspesne.");
        }
        catch (Exception e)
        {
            System.out.println("  [WARN] Databaza nie je dostupna. Hra bezi bez sluzieb.");
            System.out.println("  [WARN] " + e.getMessage());
        }
        printBanner();

        String playerName = "";
        while (playerName.isEmpty())
        {
            System.out.print("Zadajte svoje meno: ");
            System.out.flush();
            playerName = scanner.nextLine().trim();

            if (playerName.equalsIgnoreCase("wipe"))
            {
                wipeAllData(scoreService, commentService, ratingService);
                playerName = "";
                continue;
            }


            if (playerName.isEmpty()) playerName = "Anonymous";
            if (playerName.length() > 64) playerName = playerName.substring(0, 64);
        }
        System.out.println();

        int choice = selectLevel(scanner);

        Level level = LevelPresets.getLevel(choice);
        Field field = new Field(level);

        //
        ConsoleUI ui = new ConsoleUI(field, scanner, scoreService, commentService, ratingService);
        ui.run(playerName);

        while (playAgain(scanner))
        {
            choice = selectLevel(scanner);
            field = new Field(LevelPresets.getLevel(choice));
            ui = new ConsoleUI(field, scanner, scoreService, commentService, ratingService);
            ui.run(playerName);
        }

        System.out.println();
        System.out.println("Dakujeme za hru! Zbohom.");
        scanner.close();
    }*/

    private static void wipeAllData(ScoreService scoreService,
                                    CommentService commentService,
                                    RatingService ratingService)
    {
        try
        {
            if (scoreService   != null) scoreService.reset();
            if (commentService != null) commentService.reset();
            if (ratingService  != null) ratingService.reset();
            System.out.println("  [WIPE] Vsetky data boli vymazane.");
        }
        catch (Exception e)
        {
            System.out.println("  [WIPE] Chyba pri mazani dat: " + e.getMessage());
        }
    }

    private static int selectLevel(Scanner scanner)
    {
        System.out.println("Vyberte uroven:");
        for (int i = 1; i <= LevelPresets.LEVEL_COUNT; i++)
        {
            Level level = LevelPresets.getLevel(i);
            System.out.println("  " + i + ".  " + level.getName());
        }
        System.out.println();

        int choice = 0;
        while (choice < 1 || choice > LevelPresets.LEVEL_COUNT)
        {
            System.out.print("Vasa volba (1-" + LevelPresets.LEVEL_COUNT + "): ");
            if (scanner.hasNextInt())
            {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice < 1 || choice > LevelPresets.LEVEL_COUNT)
                {
                    System.out.println("  Neplatna volba. Zadajte cislo od 1 do " + LevelPresets.LEVEL_COUNT + ".");
                }
            }
            else
            {
                System.out.println("  Neplatny vstup. Zadajte cislo.");
                scanner.nextLine();
            }
        }
        return choice;
    }

    private static boolean playAgain(Scanner scanner)
    {
        System.out.println();
        System.out.print("Chcete hrat znova? (a = ano, inak ukoncit): ");
        String input = scanner.next().trim().toLowerCase();
        return input.equals("a");
    }


    private static void printBanner()
    {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║                                      ║");
        System.out.println("║          U N P U Z Z L E             ║");
        System.out.println("║                                      ║");
        System.out.println("║  Odstranujte bloky v spravnom        ║");
        System.out.println("║  poradi. Kazdy blok sa pohybuje      ║");
        System.out.println("║  v smere svojej sipky.               ║");
        System.out.println("║                                      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
    }
}
