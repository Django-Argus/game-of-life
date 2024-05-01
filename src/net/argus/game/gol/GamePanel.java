package net.argus.game.gol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
	private double scale = 1;
	
	private int tx, ty;
	
	private Point originSelection = new Point(0, 0);
	private Dimension selectedDim = new Dimension(0, 0);
	
	private boolean reversSelectedWidth = false;
	private boolean reversSelectedHeight = false;
	
	private boolean drawGrid = true;
	
	public GamePanel(Game game) {
		this.game = game;
		setPreferredSize(new Dimension(GameStatic.CELL_WIDTH * GameStatic.WIDTH + GameStatic.FRAME_BORDER*2, GameStatic.CELL_HEIGHT * GameStatic.HEIGHT+GameStatic.FRAME_BORDER*2));
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		
		startTimer();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);

		g2d.drawString("running: " + game.isRunning() + "     period: " + period + "     x / y: " + -tx + " / "+ -ty + "     scale: " + scale , GameStatic.FRAME_BORDER, g2d.getFontMetrics().getHeight());
		
		g2d.translate(tx, ty);
		g2d.scale(scale, scale);
		
		game.draw(g2d);
		
		if(!game.isRunning() && (selectedDim.width != 0 || selectedDim.height != 0)) {
			g2d.setColor(Color.GRAY);
			g2d.drawRect((originSelection.x - (reversSelectedWidth?selectedDim.width:0)) * GameStatic.CELL_WIDTH, (originSelection.y - (reversSelectedHeight?selectedDim.height:0)) * GameStatic.CELL_HEIGHT, selectedDim.width * GameStatic.CELL_WIDTH, selectedDim.height * GameStatic.CELL_HEIGHT);
		}
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
	
	public void setPeriod(int period) {
		if(period == 0)
			this.period = 50;
		else if(period < 0)
			this.period = 1;
		else if(period > 200)
			this.period = 200;
		else
			this.period = period;
	}
	
	public void setDrawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}
	
	public boolean isDrawGrid() {
		return drawGrid;
	}
	
	private int oldX = -1, oldY = -1;

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = (int) ((double) ((e.getX() - tx) / (double) GameStatic.CELL_WIDTH / scale));
		int y = (int) ((double) ((e.getY() - ty) / (double) GameStatic.CELL_HEIGHT / scale));
		
		if(e.isControlDown() && SwingUtilities.isLeftMouseButton(e) && !game.isRunning()) {
			int nx = x - originSelection.x;
			int ny = y - originSelection.y;
			
			if(nx < 0) {
				reversSelectedWidth = true;
				nx = -nx;
			}else {
				reversSelectedWidth  = false;

			}
			
			if(ny < 0) {
				reversSelectedHeight = true;
				ny = -ny;
			}else
				reversSelectedHeight = false;

			
			selectedDim = new Dimension(nx, ny);
			return;
		}

		if(SwingUtilities.isMiddleMouseButton(e)) {
			if(oldX == -1 && oldY == -1) {
				oldX = e.getX();
				oldY = e.getY();
				return;
			}
			
			tx += e.getX() - oldX;
			ty += e.getY() - oldY;
			oldX = e.getX();
			oldY = e.getY();
		}

		
		
		if(!game.isRunning()) {
			if(SwingUtilities.isLeftMouseButton(e))
				game.remove(x, y);
			else if(SwingUtilities.isRightMouseButton(e))
				game.add(x, y);
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
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_G) {
			this.drawGrid = !this.drawGrid;
		}
		
		if(game.isRunning())
			return;
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
			game.random(0, originSelection, selectedDim, reversSelectedWidth, reversSelectedHeight);
		}
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_D) {
			selectedDim = new Dimension(0, 0);
		}
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
			game.fill(originSelection, selectedDim, reversSelectedWidth, reversSelectedHeight);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			game.clearLocal(originSelection, selectedDim, reversSelectedWidth, reversSelectedHeight);
		}
		
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
			GameSave.save(game, period, scale, tx, ty);
		}
		
		if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
			GameLoadedState state = GameSave.load(game);
			
			setPeriod(state.getPeriod());
			this.tx = state.getTx();
			this.ty = state.getTy();
			this.scale = state.getScale();
		}
		
		if(e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_DELETE) {
			game.clear();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.isControlDown()) {
			double nScaleVal = scale + (double) (e.getWheelRotation()) * (scale / 4d);
			if(nScaleVal < 0.05d)
				scale = 0.05d;
			else if(nScaleVal > 100)
				scale = 100;
			else
				scale = nScaleVal;
			return;
		}
		
		int nVal = period + e.getWheelRotation() * -5;
		if(nVal <= 0) {
			period = 1;
		}else if(nVal > 200) {
			period = 200;
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
		if(e.isControlDown() && SwingUtilities.isLeftMouseButton(e) && !game.isRunning()) {
			int x = (int) ((double) ((e.getX() - tx) / (double) GameStatic.CELL_WIDTH / scale));
			int y = (int) ((double) ((e.getY() - ty) / (double) GameStatic.CELL_HEIGHT / scale));
						
			originSelection = new Point(x, y);
			return;
		}
		
		mouseDragged(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		oldX = -1;
		oldY = -1;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
