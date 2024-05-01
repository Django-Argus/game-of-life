package net.argus.game.gol;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2340582052636536008L;
	
	public GameFrame(GamePanel mainComp) {
		super("Game Of Life");
		setContentPane(mainComp);
		addKeyListener(mainComp);
		
		pack();
		
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(3);
	}
	
}
