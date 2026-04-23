package view;

import database.ScoreDAO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardFrame extends JFrame {

    public LeaderboardFrame() {

        setTitle("Top 10 Bảng Xếp Hạng");
        setSize(500,500);
        setLocationRelativeTo(null);

        DefaultListModel<String> model =
                new DefaultListModel<>();

        List<String> top10 =
                ScoreDAO.getTop10Scores();

        for(String s : top10){
            model.addElement(s);
        }

        JList<String> list =
                new JList<>(model);

        list.setFont(
                new Font("Arial", Font.BOLD,18)
        );

        add(new JScrollPane(list));

        setVisible(true);
    }
}