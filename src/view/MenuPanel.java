package view;

import app.SinglePlayerMapType;

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
    private final JButton mapButton;
    private final JButton defaultMapButton;
    private final JButton wrapMapButton;

    private int selectedPlayers = 1;
    private SinglePlayerMapType selectedMapType = SinglePlayerMapType.DEFAULT;

    public MenuPanel() {
        backgroundImage = new ImageIcon("src/assets/Snake_OG-logo.jpg").getImage();

        setLayout(null);

        JLabel title = new JLabel("SNAKE GAME");
        title.setBounds(150, 90, 300, 50);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        playButton = new JButton("Play Game");
        playButton.setBounds(200, 190, 200, 50);
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(playButton);

        modeButton = new JButton("Select Mode");
        modeButton.setBounds(200, 260, 200, 50);
        modeButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(modeButton);

        onePlayerButton = new JButton("1 Player");
        onePlayerButton.setBounds(200, 320, 200, 40);
        onePlayerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        onePlayerButton.setBackground(Color.GREEN);
        onePlayerButton.setVisible(false);
        add(onePlayerButton);

        twoPlayerButton = new JButton("2 Players");
        twoPlayerButton.setBounds(200, 370, 200, 40);
        twoPlayerButton.setFont(new Font("Arial", Font.PLAIN, 16));
        twoPlayerButton.setBackground(Color.LIGHT_GRAY);
        twoPlayerButton.setVisible(false);
        add(twoPlayerButton);

        mapButton = new JButton("Select Map");
        mapButton.setBounds(200, 440, 200, 50);
        mapButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(mapButton);

        defaultMapButton = new JButton("Default Map");
        defaultMapButton.setBounds(200, 500, 200, 40);
        defaultMapButton.setFont(new Font("Arial", Font.PLAIN, 16));
        defaultMapButton.setBackground(Color.GREEN);
        defaultMapButton.setVisible(false);
        add(defaultMapButton);

        wrapMapButton = new JButton("Wrap Wall Map");
        wrapMapButton.setBounds(200, 550, 200, 40);
        wrapMapButton.setFont(new Font("Arial", Font.PLAIN, 16));
        wrapMapButton.setBackground(Color.LIGHT_GRAY);
        wrapMapButton.setVisible(false);
        add(wrapMapButton);

        modeButton.addActionListener(e -> {
            boolean visible = !onePlayerButton.isVisible();
            onePlayerButton.setVisible(visible);
            twoPlayerButton.setVisible(visible);
        });

        onePlayerButton.addActionListener(e -> {
            selectedPlayers = 1;
            onePlayerButton.setBackground(Color.GREEN);
            twoPlayerButton.setBackground(Color.LIGHT_GRAY);
            onePlayerButton.setVisible(false);
            twoPlayerButton.setVisible(false);
        });

        twoPlayerButton.addActionListener(e -> {
            selectedPlayers = 2;
            onePlayerButton.setBackground(Color.LIGHT_GRAY);
            twoPlayerButton.setBackground(Color.RED);
            onePlayerButton.setVisible(false);
            twoPlayerButton.setVisible(false);
        });

        mapButton.addActionListener(e -> {
            boolean visible = !defaultMapButton.isVisible();
            defaultMapButton.setVisible(visible);
            wrapMapButton.setVisible(visible);
        });

        defaultMapButton.addActionListener(e -> {
            selectedMapType = SinglePlayerMapType.DEFAULT;
            defaultMapButton.setBackground(Color.GREEN);
            wrapMapButton.setBackground(Color.LIGHT_GRAY);
            defaultMapButton.setVisible(false);
            wrapMapButton.setVisible(false);
        });

        wrapMapButton.addActionListener(e -> {
            selectedMapType = SinglePlayerMapType.HORIZONTAL_WRAP;
            defaultMapButton.setBackground(Color.LIGHT_GRAY);
            wrapMapButton.setBackground(Color.GREEN);
            defaultMapButton.setVisible(false);
            wrapMapButton.setVisible(false);
        });
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public int getSelectedPlayers() {
        return selectedPlayers;
    }

    public SinglePlayerMapType getSelectedMapType() {
        return selectedMapType;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
