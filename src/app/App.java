package app;

import controller.GameController;
import controller.TwoPlayerGameController;
import model.DefaultSinglePlayerMap;
import model.DesertSinglePlayerMap;
import model.HorizontalWallWrapMap;
import model.SinglePlayerMap;
import model.HorizontalWallWrapMap;
import model.SnakeGameModel;
import model.TwoPlayerSnakeGameModel;
import view.GamePanel;
import view.MenuPanel;
import view.TwoPlayerGamePanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.Dimension;

public class App {

    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 600;
    private static final int TILE_SIZE = 25;

    private static final String MENU_CARD = "menu";
    private static final String SINGLE_PLAYER_CARD = "game";
    private static final String TWO_PLAYER_CARD = "twoPlayerGame";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake");
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        mainPanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        SnakeGameModel singlePlayerModel = new SnakeGameModel(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE);
        GamePanel gamePanel = new GamePanel(singlePlayerModel);
        GameController gameController = new GameController(singlePlayerModel, gamePanel);

        TwoPlayerSnakeGameModel twoPlayerModel = new TwoPlayerSnakeGameModel(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE);
        TwoPlayerGamePanel twoPlayerGamePanel = new TwoPlayerGamePanel(twoPlayerModel);
        TwoPlayerGameController twoPlayerGameController = new TwoPlayerGameController(twoPlayerModel, twoPlayerGamePanel);

        MenuPanel menuPanel = new MenuPanel();

        connectMenuButtons(
                mainPanel,
                cardLayout,
                menuPanel,
                singlePlayerModel,
                gamePanel,
                gameController,
                twoPlayerGamePanel,
                twoPlayerGameController
        );

        connectBackButtons(mainPanel, cardLayout, menuPanel, gamePanel, gameController, twoPlayerGamePanel, twoPlayerGameController);

        mainPanel.add(menuPanel, MENU_CARD);
        mainPanel.add(gamePanel, SINGLE_PLAYER_CARD);
        mainPanel.add(twoPlayerGamePanel, TWO_PLAYER_CARD);
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
                if (menuPanel.getSelectedMapType() == SinglePlayerMapType.HORIZONTAL_WRAP) {
                    gameModel.setMap(new HorizontalWallWrapMap(boardWidth, boardHeight, 25));
                } else {
                    gameModel.setMap(new DefaultSinglePlayerMap(boardWidth, boardHeight, 25));
                }
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

        cardLayout.show(mainPanel, MENU_CARD);
    }

    private static void connectMenuButtons(
            JPanel mainPanel,
            CardLayout cardLayout,
            MenuPanel menuPanel,
            SnakeGameModel singlePlayerModel,
            GamePanel gamePanel,
            GameController gameController,
            TwoPlayerGamePanel twoPlayerGamePanel,
            TwoPlayerGameController twoPlayerGameController
    ) {
        menuPanel.getPlayButton().addActionListener(e -> {
            if (menuPanel.getSelectedPlayers() == 2) {
                twoPlayerGameController.startGame();
                cardLayout.show(mainPanel, TWO_PLAYER_CARD);
                twoPlayerGamePanel.requestFocusInWindow();
                return;
            }

            singlePlayerModel.setMap(createMap(menuPanel.getSelectedMapType()));
            gameController.startGame();
            cardLayout.show(mainPanel, SINGLE_PLAYER_CARD);
            gamePanel.requestFocusInWindow();
        });
    }

    private static void connectBackButtons(
            JPanel mainPanel,
            CardLayout cardLayout,
            MenuPanel menuPanel,
            GamePanel gamePanel,
            GameController gameController,
            TwoPlayerGamePanel twoPlayerGamePanel,
            TwoPlayerGameController twoPlayerGameController
    ) {
        gamePanel.setMenuAction(() -> {
            gameController.resetGame();
            cardLayout.show(mainPanel, MENU_CARD);
            menuPanel.requestFocusInWindow();
        });

        twoPlayerGamePanel.setMenuAction(() -> {
            twoPlayerGameController.resetGame();
            cardLayout.show(mainPanel, MENU_CARD);
            menuPanel.requestFocusInWindow();
        });
    }

    private static SinglePlayerMap createMap(SinglePlayerMapType mapType) {
        if (mapType == SinglePlayerMapType.HORIZONTAL_WRAP) {
            return new HorizontalWallWrapMap(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE);
        }
        if (mapType == SinglePlayerMapType.DESERT) {
            return new DesertSinglePlayerMap(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE);
        }
        return new DefaultSinglePlayerMap(BOARD_WIDTH, BOARD_HEIGHT, TILE_SIZE);
    }
}
