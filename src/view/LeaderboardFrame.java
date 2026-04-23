package view;

import database.ScoreDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderboardFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private List<Object[]> scores;

    public LeaderboardFrame() {

        setTitle("🏆 Snake Leaderboard");
        setSize(600,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // nền game
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.white);

        // tiêu đề
        JLabel title = new JLabel("TOP PLAYERS", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(Color.black);

        // cột bảng
        String[] columns = {
                "Rank",
                "Player",
                "Score"
        };

        model = new DefaultTableModel(columns,0);
        table = new JTable(model);

        // style bảng
        table.setRowHeight(35);
        table.setFont(new Font("Monospaced", Font.BOLD,18));
        table.setBackground(Color.decode("#DDDDDD"));
        table.setForeground(Color.black);
        table.setGridColor(Color.BLACK);

        table.getTableHeader().setFont(
                new Font("Monospaced", Font.BOLD,20)
        );
        table.getTableHeader().setBackground(Color.decode("#BBBBBB"));
        table.getTableHeader().setForeground(Color.black);

        // căn giữa
        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        for(int i=0;i<3;i++){
            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(center);
        }

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.getViewport().setBackground(
                Color.white
        );

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(
                new Font("Arial",Font.BOLD,16)
        );

        refreshBtn.addActionListener(
                e -> loadScores()
        );

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.white);
        bottomPanel.add(refreshBtn);

        mainPanel.add(title,BorderLayout.NORTH);
        mainPanel.add(scrollPane,BorderLayout.CENTER);
        mainPanel.add(bottomPanel,BorderLayout.SOUTH);


        add(mainPanel);

        loadScores();

        setVisible(true);
    }

    private void loadScores() {

        model.setRowCount(0);

        try {
            ScoreDAO dao = new ScoreDAO();

            // sửa theo hàm DAO của bạn nếu tên khác
            List<Object[]> scores =
                    dao.getTopScores();

            int rank = 1;

            for(Object[] row : scores){

                String medal = switch(rank){
                    case 1 -> "♛";
                    case 2 -> "✪";
                    case 3 -> "✦";
                    default -> "#" + rank;
                };

                model.addRow(new Object[]{
                        medal,
                        row[0], // player_name
                        row[1]  // score
                });

                rank++;
            }

        } catch(Exception e){
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi tải leaderboard!"
            );
        }
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back");

        refreshButton.addActionListener(e -> loadScores());

        backButton.addActionListener(e -> {
            dispose(); // đóng leaderboard
            new MenuPanel(); // nếu menu của bạn là JFrame riêng
        });
    }

}