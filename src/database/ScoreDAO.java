package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ScoreDAO {

    public static void saveScore(String playerName, int score, int playTime){
        try{
            Connection conn = DatabaseConnection.getConnection();

            String sql =
                    "INSERT INTO player_scores(player_name, score, play_time_seconds) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, playerName);
            ps.setInt(2, score);
            ps.setInt(3, playTime);

            ps.executeUpdate();

            conn.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public List<Object[]> getTopScores() {

        List<Object[]> scores = new ArrayList<>();

        String sql =
                "SELECT player_name, score " +
                        "FROM player_scores " +
                        "ORDER BY score DESC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                scores.add(new Object[]{
                        rs.getString("player_name"),
                        rs.getInt("score")
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return scores;
    }


    public static List<String> getTop10Scores(){

        List<String> scores = new ArrayList<>();

        try{
            Connection conn = DatabaseConnection.getConnection();

            String sql =
                    "SELECT player_name, score, play_time_seconds " +
                            "FROM player_scores " +
                            "ORDER BY score DESC, play_time_seconds ASC " +
                            "LIMIT 10";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            int rank = 1;

            while(rs.next()){

                scores.add(
                        rank + ". " +
                                rs.getString("player_name") +
                                " - " +
                                rs.getInt("score") +
                                " điểm - " +
                                rs.getInt("play_time_seconds") +
                                " giây"
                );

                rank++;
            }

            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return scores;
    }
}