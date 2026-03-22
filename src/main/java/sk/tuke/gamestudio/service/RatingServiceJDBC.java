package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService
{
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "1234";
    public static final String SELECT_AVERAGE = "SELECT AVG(rating) FROM rating WHERE game = ?";
    public static final String SELECT_PLAYER  = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";
    // ON CONFLICT (player, game) – upsert: ak záznam existuje, aktualizuje ho
    public static final String UPSERT = "INSERT INTO rating (player, game, rating, ratedon) VALUES (?, ?, ?, ?) ON CONFLICT (player, game) DO UPDATE SET rating = EXCLUDED.rating, ratedon = EXCLUDED.ratedon";

    @Override
    public void setRating(Rating rating)
    {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(UPSERT)
        )
        {
            statement.setString(1, rating.getPlayer());
            statement.setString(2, rating.getGame());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e)
        {
            throw new RatingException("Problem inserting/updating rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_AVERAGE)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    return rs.wasNull() ? 0 : (int) Math.round(avg);
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem selecting average rating", e);
        }
        return 0;
    }

    @Override
    public int getRating(String game, String player)
    {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_PLAYER)
        )
        {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RatingException("Problem selecting player rating", e);
        }
        return 0;
    }

    @Override
    public void reset()
    {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        )
        {
            statement.executeUpdate(DELETE);
        }
        catch (SQLException e)
        {
            throw new RatingException("Problem deleting ratings", e);
        }
    }
}