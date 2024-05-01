package net.argus.game.gol;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2340582052636536008L;
	
	private boolean fullScreen = false;
	
	public GameFrame(GamePanel mainComp) {
		super("Game Of Life");
		setContentPane(mainComp);
		addKeyListener(mainComp);
		
		pack();
		
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(3);
		JFrame thiss = this;
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_F11) {
					if(!fullScreen) {
						getGraphicsConfiguration().getDevice().setFullScreenWindow(thiss);
						fullScreen = true;
					}else {
						getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
						fullScreen = false;
					}
				}
				
			}
		});
	}
	
}
