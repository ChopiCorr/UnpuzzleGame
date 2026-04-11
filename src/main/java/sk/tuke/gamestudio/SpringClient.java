package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.game.Unpuzzle.consoleUI.ConsoleUI;
import sk.tuke.gamestudio.service.*;

import java.util.Scanner;

@SpringBootApplication
@Configuration
public class SpringClient
{
    public static void main(String[] args)
    {
        SpringApplication.run(SpringClient.class, args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui)
    {
        return args -> ui.play();
    }

    @Bean
    public ConsoleUI consoleUI()
    {
        return new ConsoleUI(new Scanner(System.in));
    }

    @Bean
    public ScoreService scoreService()
    {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService()
    {
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService()
    {
        return new RatingServiceJPA();
    }
}