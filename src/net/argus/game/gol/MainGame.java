package net.argus.game.gol;

public class MainGame {
	
	public static void main(String[] args) {
		Game game = new Game();
		
		GamePanel pan = new GamePanel(game);
		GameFrame fen = new GameFrame(pan);
		
		fen.setVisible(true);
	}

}
