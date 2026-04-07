import model.TwoPlayerSnakeGameModel;
var m = new TwoPlayerSnakeGameModel(600, 600, 25);
m.changePlayerOneDirection(-1, 0);
for (int i = 0; i < 5; i++) {
    System.out.println("tick=" + i + " gameOver=" + m.isGameOver() + " p1=" + m.isPlayerOneAlive() + " p2=" + m.isPlayerTwoAlive());
    m.move();
}
System.out.println("after crash gameOver=" + m.isGameOver() + " winner=" + m.getWinnerText() + " p1=" + m.isPlayerOneAlive() + " p2=" + m.isPlayerTwoAlive());
/exit
