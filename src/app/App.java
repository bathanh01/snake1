package app;

import controller.GameController;
import controller.TwoPlayerGameController;
import model.SnakeGameModel;
import model.TwoPlayerSnakeGameModel;
import view.GamePanel;
import view.MenuPanel;
import view.TwoPlayerGamePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.CardLayout;

public class App {

    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        mainPanel.setPreferredSize(new Dimension(boardWidth, boardHeight));

        SnakeGameModel gameModel = new SnakeGameModel(boardWidth, boardHeight, 25);
        GamePanel gamePanel = new GamePanel(gameModel);
        TwoPlayerSnakeGameModel twoPlayerModel = new TwoPlayerSnakeGameModel(boardWidth, boardHeight, 25);
        TwoPlayerGamePanel twoPlayerGamePanel = new TwoPlayerGamePanel(twoPlayerModel);
        MenuPanel menuPanel = new MenuPanel();
        GameController gameController = new GameController(gameModel, gamePanel);
        TwoPlayerGameController twoPlayerGameController = new TwoPlayerGameController(twoPlayerModel, twoPlayerGamePanel);

        gamePanel.setMenuAction(() -> {
            gameController.resetGame();
            cardLayout.show(mainPanel, "menu");
            menuPanel.requestFocusInWindow();
        });

        twoPlayerGamePanel.setMenuAction(() -> {
            twoPlayerGameController.resetGame();
            cardLayout.show(mainPanel, "menu");
            menuPanel.requestFocusInWindow();
        });

        menuPanel.getPlayButton().addActionListener(e -> {
            GameMode selectedMode = menuPanel.getSelectedPlayers() == 2 ? GameMode.TWO_PLAYER : GameMode.SINGLE_PLAYER;

            if (selectedMode == GameMode.TWO_PLAYER) {
                twoPlayerGameController.startGame();
                cardLayout.show(mainPanel, "twoPlayerGame");
                twoPlayerGamePanel.requestFocusInWindow();
            } else {
                gameController.startGame();
                cardLayout.show(mainPanel, "game");
                gamePanel.requestFocusInWindow();
            }
        });

        mainPanel.add(menuPanel, "menu");
        mainPanel.add(gamePanel, "game");
        mainPanel.add(twoPlayerGamePanel, "twoPlayerGame");

        frame.add(mainPanel);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "menu");
    }
}
