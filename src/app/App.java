package app;
import view.LeaderboardFrame;
import controller.GameController;
import controller.TwoPlayerGameController;
import model.DefaultSinglePlayerMap;
import model.DesertSinglePlayerMap;
import model.HorizontalWallWrapMap;
import model.SnakeGameModel;
import model.TwoPlayerSnakeGameModel;
import view.GamePanel;
import view.MenuPanel;
import view.TwoPlayerGamePanel;
//import view.LeaderboardFrame;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.CardLayout;

public class App {

    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;
//        new LeaderboardFrame();
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
        menuPanel.getPlayButton().addActionListener(e -> {

                String playerName = JOptionPane.showInputDialog(
                        null,
                        "Nhập tên người chơi:"
                );

                if (playerName == null || playerName.trim().isEmpty()) {
                    return;
                }

                gameModel.setPlayerName(playerName);


                // phần code bắt đầu game giữ nguyên bên dưới...
            GameMode selectedMode =
                    menuPanel.getSelectedPlayers() == 2
                            ? GameMode.TWO_PLAYER
                            : GameMode.SINGLE_PLAYER;
            if (selectedMode == GameMode.TWO_PLAYER) {

                twoPlayerGameController.startGame();
                cardLayout.show(mainPanel, "twoPlayerGame");
                twoPlayerGamePanel.requestFocusInWindow();

            } else {

                SinglePlayerMapType mapType =
                        menuPanel.getSelectedMapType();

                gameModel.setDesertVisual(
                        mapType == SinglePlayerMapType.DESERT
                );

                if (mapType == SinglePlayerMapType.HORIZONTAL_WRAP) {

                    gameModel.setMap(
                            new HorizontalWallWrapMap(
                                    boardWidth, boardHeight, 25
                            )
                    );

                } else if (mapType == SinglePlayerMapType.DESERT) {

                    gameModel.setMap(
                            new DesertSinglePlayerMap(
                                    boardWidth, boardHeight, 25
                            )
                    );

                } else {

                    gameModel.setMap(
                            new DefaultSinglePlayerMap(
                                    boardWidth, boardHeight, 25
                            )
                    );
                }

                gameController.startGame();
                cardLayout.show(mainPanel, "game");
                gamePanel.requestFocusInWindow();
            }
        });
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