package view;

import app.SinglePlayerMapType;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MenuPanel extends JPanel {

    private final Image defaultBackgroundImage;
    private final Image desertBackgroundImage;
    private Image backgroundImage;

    private final JButton playButton;
    private final JButton modeButton;
    private final JButton onePlayerButton;
    private final JButton twoPlayerButton;
    private final JButton mapButton;
    private final JButton defaultMapButton;
    private final JButton wrapMapButton;
    private final JButton desertMapButton;

    private int selectedPlayers = 1;
    private SinglePlayerMapType selectedMapType = SinglePlayerMapType.DEFAULT;

    public MenuPanel() {
        defaultBackgroundImage = loadImage(
                new String[]{"/assets/Snake_OG-logo.jpg", "/Snake_OG-logo.jpg"},
                new String[]{"src/assets/Snake_OG-logo.jpg", "src/Snake_OG-logo.jpg"}
        );
        desertBackgroundImage = loadImage(
                new String[]{"/assets/desert-level-sand-real.png", "/desert-level-sand-real.png"},
                new String[]{"src/assets/desert-level-sand-real.png", "src/desert-level-sand-real.png"}
        );
        backgroundImage = defaultBackgroundImage;

        setLayout(null);

        JLabel title = new JLabel("SNAKE GAME");
        title.setBounds(150, 90, 300, 50);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title);

        playButton = createButton("Play Game", 200, 190, 200, 50, 18, Font.BOLD);
        modeButton = createButton("Select Mode", 200, 260, 200, 50, 18, Font.BOLD);
        onePlayerButton = createButton("1 Player", 200, 320, 200, 40, 16, Font.PLAIN);
        twoPlayerButton = createButton("2 Players", 200, 370, 200, 40, 16, Font.PLAIN);
        mapButton = createButton("Select Map", 200, 430, 200, 45, 18, Font.BOLD);
        defaultMapButton = createButton("Default Map", 200, 480, 200, 35, 16, Font.PLAIN);
        wrapMapButton = createButton("Wrap Wall Map", 200, 520, 200, 35, 16, Font.PLAIN);
        desertMapButton = createButton("Desert Map", 200, 560, 200, 35, 16, Font.PLAIN);

        onePlayerButton.setVisible(false);
        twoPlayerButton.setVisible(false);
        defaultMapButton.setVisible(false);
        wrapMapButton.setVisible(false);
        desertMapButton.setVisible(false);

        updatePlayerButtonColors();
        updateMapButtonColors();
        updateBackgroundImage();
        registerActions();
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
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton createButton(String text, int x, int y, int width, int height, int fontSize, int fontStyle) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", fontStyle, fontSize));
        add(button);
        return button;
    }

    private void registerActions() {
        modeButton.addActionListener(e -> togglePlayerButtons());
        mapButton.addActionListener(e -> toggleMapButtons());

        onePlayerButton.addActionListener(e -> {
            selectedPlayers = 1;
            updatePlayerButtonColors();
            hidePlayerButtons();
        });

        twoPlayerButton.addActionListener(e -> {
            selectedPlayers = 2;
            updatePlayerButtonColors();
            hidePlayerButtons();
        });

        defaultMapButton.addActionListener(e -> {
            selectedMapType = SinglePlayerMapType.DEFAULT;
            updateMapButtonColors();
            updateBackgroundImage();
            hideMapButtons();
        });

        wrapMapButton.addActionListener(e -> {
            selectedMapType = SinglePlayerMapType.HORIZONTAL_WRAP;
            updateMapButtonColors();
            updateBackgroundImage();
            hideMapButtons();
        });

        desertMapButton.addActionListener(e -> {
            selectedMapType = SinglePlayerMapType.DESERT;
            updateMapButtonColors();
            updateBackgroundImage();
            hideMapButtons();
        });
    }

    private void togglePlayerButtons() {
        boolean visible = !onePlayerButton.isVisible();
        onePlayerButton.setVisible(visible);
        twoPlayerButton.setVisible(visible);
    }

    private void toggleMapButtons() {
        boolean visible = !defaultMapButton.isVisible();
        defaultMapButton.setVisible(visible);
        wrapMapButton.setVisible(visible);
        desertMapButton.setVisible(visible);
    }

    private void hidePlayerButtons() {
        onePlayerButton.setVisible(false);
        twoPlayerButton.setVisible(false);
    }

    private void hideMapButtons() {
        defaultMapButton.setVisible(false);
        wrapMapButton.setVisible(false);
        desertMapButton.setVisible(false);
    }

    private void updatePlayerButtonColors() {
        if (selectedPlayers == 1) {
            onePlayerButton.setBackground(Color.GREEN);
            twoPlayerButton.setBackground(Color.LIGHT_GRAY);
        } else {
            onePlayerButton.setBackground(Color.LIGHT_GRAY);
            twoPlayerButton.setBackground(Color.RED);
        }
    }

    private void updateMapButtonColors() {
        defaultMapButton.setBackground(Color.LIGHT_GRAY);
        wrapMapButton.setBackground(Color.LIGHT_GRAY);
        desertMapButton.setBackground(Color.LIGHT_GRAY);

        if (selectedMapType == SinglePlayerMapType.DEFAULT) {
            defaultMapButton.setBackground(Color.GREEN);
        } else if (selectedMapType == SinglePlayerMapType.HORIZONTAL_WRAP) {
            wrapMapButton.setBackground(Color.GREEN);
        } else {
            desertMapButton.setBackground(new Color(205, 133, 63));
        }
    }

    private void updateBackgroundImage() {
        if (selectedMapType == SinglePlayerMapType.DESERT && desertBackgroundImage != null) {
            backgroundImage = desertBackgroundImage;
        } else {
            backgroundImage = defaultBackgroundImage;
        }
        repaint();
    }

    private Image loadImage(String[] resourcePaths, String[] filePaths) {
        for (String resourcePath : resourcePaths) {
            URL resource = getClass().getResource(resourcePath);
            if (resource != null) {
                return new ImageIcon(resource).getImage();
            }
        }

        for (String filePath : filePaths) {
            Image image = loadImageFromFile(filePath);
            if (image != null) {
                return image;
            }
        }

        return null;
    }

    private Image loadImageFromFile(String filePath) {
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            return null;
        }

        try {
            return ImageIO.read(imageFile);
        } catch (IOException ignored) {
            return null;
        }
    }
}
