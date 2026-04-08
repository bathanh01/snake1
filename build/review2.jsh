import model.TwoPlayerSnakeGameModel;
var m = new TwoPlayerSnakeGameModel(600, 600, 25);
m.changePlayerOneDirection(1, 0);
m.changePlayerTwoDirection(-1, 0);
for (int i = 0; i < 8; i++) {
    m.move();
    System.out.println("tick=" + i + " p1=(" + m.getPlayerOneHead().getX() + "," + m.getPlayerOneHead().getY() + ") p2=(" + m.getPlayerTwoHead().getX() + "," + m.getPlayerTwoHead().getY() + ") gameOver=" + m.isGameOver() + " winner=" + m.getWinnerText());
    if (m.isGameOver()) {
        break;
    }
}
/exit
