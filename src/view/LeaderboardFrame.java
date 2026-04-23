package view;

import database.ScoreDAO;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

public class LeaderboardFrame extends JFrame {

    private final JTable table;
    private final DefaultTableModel model;
    private final ImageIcon goldMedalIcon;
    private final ImageIcon silverMedalIcon;
    private final ImageIcon bronzeMedalIcon;

    public LeaderboardFrame() {
        setTitle("Snake Leaderboard");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        goldMedalIcon = createMedalIcon(new Color(245, 196, 67), "1");
        silverMedalIcon = createMedalIcon(new Color(192, 198, 212), "2");
        bronzeMedalIcon = createMedalIcon(new Color(193, 120, 62), "3");

        Color bgDark = new Color(18, 18, 18);
        Color rowDark = new Color(30, 30, 30);
        Color neonGreen = new Color(0, 255, 204);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgDark);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("TOP PLAYERS", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(neonGreen);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));

        String[] columns = {"Rank", "Player", "Score"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(48);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setBackground(rowDark);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(50, 50, 50));
        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(45, 45, 45));
        table.getTableHeader().setForeground(neonGreen);

        table.getColumnModel().getColumn(0).setCellRenderer(new RankRenderer());
        table.getColumnModel().getColumn(1).setCellRenderer(createTextRenderer(Color.WHITE, false));
        table.getColumnModel().getColumn(2).setCellRenderer(createTextRenderer(neonGreen, true));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(bgDark);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bgDark);

        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        refreshBtn.setBackground(new Color(60, 60, 60));
        refreshBtn.setForeground(Color.WHITE);
        backBtn.setBackground(new Color(60, 60, 60));
        backBtn.setForeground(Color.WHITE);

        refreshBtn.addActionListener(e -> loadScores());
        backBtn.addActionListener(e -> dispose());

        bottomPanel.add(backBtn);
        bottomPanel.add(refreshBtn);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadScores();
        setVisible(true);
    }

    private DefaultTableCellRenderer createTextRenderer(Color textColor, boolean bold) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                );
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setForeground(textColor);
                label.setBackground(table.getBackground());
                label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 16));
                return label;
            }
        };
    }

    private ImageIcon createMedalIcon(Color medalColor, String rankText) {
        int width = 34;
        int height = 40;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(52, 152, 219));
        g2.fillRoundRect(8, 2, 7, 16, 4, 4);
        g2.setColor(new Color(231, 76, 60));
        g2.fillRoundRect(19, 2, 7, 16, 4, 4);

        g2.setColor(medalColor.darker());
        g2.fillOval(4, 12, 26, 26);
        g2.setColor(medalColor);
        g2.fillOval(6, 14, 22, 22);

        g2.setColor(new Color(255, 255, 255, 90));
        g2.fillOval(10, 17, 7, 7);

        g2.setColor(new Color(32, 32, 32));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.drawString(rankText, 14, 30);

        g2.dispose();
        return new ImageIcon(image);
    }

    private void loadScores() {
        model.setRowCount(0);

        try {
            ScoreDAO dao = new ScoreDAO();
            List<Object[]> scores = dao.getTopScores();
            int rank = 1;

            for (Object[] row : scores) {
                model.addRow(new Object[]{rank, row[0], row[1]});
                rank++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RankRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, "", isSelected, hasFocus, row, column
            );
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBackground(table.getBackground());
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.BOLD, 15));

            int rank = value instanceof Integer ? (Integer) value : -1;
            if (rank == 1) {
                label.setIcon(goldMedalIcon);
                label.setText("");
            } else if (rank == 2) {
                label.setIcon(silverMedalIcon);
                label.setText("");
            } else if (rank == 3) {
                label.setIcon(bronzeMedalIcon);
                label.setText("");
            } else {
                label.setIcon(null);
                label.setText("#" + rank);
            }

            return label;
        }
    }
}
