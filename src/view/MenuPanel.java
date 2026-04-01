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
        playButton.setBounds(200, 300, 200, 50);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(playButton);
    }

    public JButton getPlayButton() {
        return playButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
