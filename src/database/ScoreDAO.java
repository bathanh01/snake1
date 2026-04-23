package database;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ScoreDAO {

    public static void saveScore(String playerName, int score, int playTime){
        String sql =
                "INSERT INTO player_scores(player_name, score, play_time_seconds) VALUES(?,?,?)";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, playerName);
            ps.setInt(2, score);
            ps.setInt(3, playTime);

            ps.executeUpdate();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public List<Object[]> getTopScores() {

        List<Object[]> scores = new ArrayList<>();

        String sql =
                "SELECT player_name, score " +
                        "FROM player_scores " +
                        "ORDER BY score DESC, play_time_seconds ASC " +
                        "LIMIT 5";

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

        String sql =
                "SELECT player_name, score, play_time_seconds " +
                        "FROM player_scores " +
                        "ORDER BY score DESC, play_time_seconds ASC " +
                        "LIMIT 10";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
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

        }catch(Exception e){
            e.printStackTrace();
        }

        return scores;
    }
}
