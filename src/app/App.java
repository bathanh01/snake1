package app;

import controller.GameController;
import model.SnakeGameModel;
import view.GamePanel;
import view.MenuPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class App {

    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        SnakeGameModel gameModel = new SnakeGameModel(boardWidth, boardHeight, 25);
        GamePanel gamePanel = new GamePanel(gameModel);
        MenuPanel menuPanel = new MenuPanel();
        GameController gameController = new GameController(gameModel, gamePanel);

        menuPanel.getPlayButton().addActionListener(e -> {
            gameController.startGame();
            cardLayout.show(mainPanel, "game");
            gamePanel.requestFocusInWindow();
        });

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");

        frame.add(mainPanel);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "menu");
    }
}
