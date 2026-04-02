package view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class MenuPanel extends JPanel {

    private final Image backgroundImage;
    private final JButton playButton;
    private final JButton modeButton;
    private final JButton onePlayerButton;
    private final JButton twoPlayerButton;

    private int selectedPlayers = 1; // 1 hoặc 2

    public MenuPanel() {
        backgroundImage = new ImageIcon("src/Snake_OG-logo.jpg").getImage();

        setLayout(null);

        JLabel title = new JLabel("SNAKE GAME");
        title.setBounds(150, 120, 300, 50);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        playButton = new JButton("Play Game");
        playButton.setBounds(200, 230, 200, 50);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(playButton);

        // Nút tổng Mode
        modeButton = new JButton("Select Mode");
        modeButton.setBounds(200, 300, 200, 50);
        modeButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(modeButton);

        // Nút 1 Player (ẩn ban đầu)
        onePlayerButton = new JButton("1 Player");
        onePlayerButton.setBounds(200, 360, 200, 40);
        onePlayerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        onePlayerButton.setBackground(Color.GREEN);
        onePlayerButton.setVisible(false);
        add(onePlayerButton);

        // Nút 2 Players (ẩn ban đầu)
        twoPlayerButton = new JButton("2 Players");
        twoPlayerButton.setBounds(200, 410, 200, 40);
        twoPlayerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        twoPlayerButton.setBackground(Color.LIGHT_GRAY);
        twoPlayerButton.setVisible(false);
        add(twoPlayerButton);



        // Hiển thị/ẩn lựa chọn khi nhấn nút Mode
        modeButton.addActionListener(e -> {
            boolean visible = !onePlayerButton.isVisible();
            onePlayerButton.setVisible(visible);
            twoPlayerButton.setVisible(visible);
        });

        // Chọn 1 Player
        onePlayerButton.addActionListener(e -> {
            selectedPlayers = 1;
            onePlayerButton.setBackground(Color.GREEN);
            twoPlayerButton.setBackground(Color.LIGHT_GRAY);
            // ẩn nút sau khi chọn
            onePlayerButton.setVisible(false);
            twoPlayerButton.setVisible(false);
        });

        // Chọn 2 Players
        twoPlayerButton.addActionListener(e -> {
            selectedPlayers = 2;
            onePlayerButton.setBackground(Color.LIGHT_GRAY);
            twoPlayerButton.setBackground(Color.RED);
            // ẩn nút sau khi chọn
            onePlayerButton.setVisible(false);
            twoPlayerButton.setVisible(false);
        });

    }



    public JButton getPlayButton() {
        return playButton;
    }

    public int getSelectedPlayers() {
        return selectedPlayers;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
