package net.argus.game.gol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class GamePanel extends JComponent implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8963639406859989131L;
	
	private Game game;
	
	private Timer timer;
	private int period = 50;
	
	public GamePanel(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(GameStatic.CELL_WIDTH * GameStatic.WIDTH + GameStatic.FRAME_BORDER*2, GameStatic.CELL_HEIGHT * GameStatic.HEIGHT+GameStatic.FRAME_BORDER*2));
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
		startTimer();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);

		g2d.drawString("running: " + game.isRunning() + "     period: " + period, GameStatic.FRAME_BORDER, g2d.getFontMetrics().getHeight());
		
		g2d.translate((getWidth() - (GameStatic.CELL_WIDTH * GameStatic.WIDTH)) / 2, (getHeight() - (GameStatic.CELL_HEIGHT * GameStatic.HEIGHT)) / 2);
		game.draw(g2d);

	}

	public TimerTask getTimerTask() {
		return new TimerTask() {
			
			@Override
			public void run() {
				game.update();
				SwingUtilities.invokeLater(() -> {
					repaint();
				});
			}
		};
	}
	
	private void startTimer() {
		this.timer = new Timer();
		timer.schedule(getTimerTask(), period, period);	
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = ((e.getX() - (getWidth() - (GameStatic.CELL_WIDTH * GameStatic.WIDTH)) / 2)) / GameStatic.CELL_WIDTH;
		int y = ((e.getY() - (getHeight() - (GameStatic.CELL_HEIGHT * GameStatic.HEIGHT)) / 2)) / GameStatic.CELL_HEIGHT;
		
		if(!game.isRunning() && x >= 0 && y >= 0 && x < GameStatic.WIDTH && y < GameStatic.HEIGHT) {
			if(SwingUtilities.isLeftMouseButton(e))
				game.dead(x, y);
			else if(SwingUtilities.isRightMouseButton(e))
				game.live(x, y);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			game.playPause();
			
			timer.cancel();
			startTimer();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int nVal = period + e.getWheelRotation() * -5;
		if(nVal <= 0) {
			period = 1;
		}else if(nVal > 100) {
			period = 100;
		}else	
			period = nVal;


		timer.cancel();
		startTimer();
		
		SwingUtilities.invokeLater(() -> {
			repaint();
		});
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
