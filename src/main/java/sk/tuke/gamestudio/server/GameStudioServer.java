package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.service.Comment.CommentService;
import sk.tuke.gamestudio.service.Comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.Player.PlayerService;
import sk.tuke.gamestudio.service.Player.PlayerServiceJPA;
import sk.tuke.gamestudio.service.Rating.RatingService;
import sk.tuke.gamestudio.service.Rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.Score.ScoreService;
import sk.tuke.gamestudio.service.Score.ScoreServiceJPA;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
public class GameStudioServer
{
    public static void main(String[] args)
    {
        SpringApplication.run(GameStudioServer.class, args);
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

    @Bean
    public PlayerService playerService()
    {
        return new PlayerServiceJPA();
    }
}