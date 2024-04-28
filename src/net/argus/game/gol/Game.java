package net.argus.game.gol;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private boolean[][] cells;
	
	private boolean running = false;
	
	public Game() {
		cells = new boolean[GameStatic.WIDTH][GameStatic.HEIGHT];
		
		for(int x = 0; x < GameStatic.WIDTH; x++) {
			for(int y = 0; y < GameStatic.HEIGHT; y++) {
				cells[x][y] = false;
			}
		}
	}
	
	public void update() {
		if(!running)
			return;
		
		List<GameUpdateState> gameUpdateStates = new ArrayList<Game.GameUpdateState>();
		for(int x = 0; x < GameStatic.WIDTH; x++) {
			for(int y = 0; y < GameStatic.HEIGHT; y++) {
				int adj = adj(x, y);
				if(cells[x][y])
					if(adj < 2)
						gameUpdateStates.add(new GameUpdateState(x, y, false));
					else if(adj < 4)
						gameUpdateStates.add(new GameUpdateState(x, y, true));
					else
						gameUpdateStates.add(new GameUpdateState(x, y, false));
				else
					if(adj == 3)
						gameUpdateStates.add(new GameUpdateState(x, y, true));
			}
		}
		
		for(GameUpdateState state : gameUpdateStates)
			cells[state.x][state.y] = state.nValue;
	}
	
	public int adj(int x, int y) {
		int mX = Math.floorMod(x - 1, GameStatic.WIDTH);
		int mY = Math.floorMod(y - 1, GameStatic.HEIGHT);

		int pX = Math.floorMod(x + 1, GameStatic.WIDTH);
		int pY = Math.floorMod(y + 1, GameStatic.HEIGHT);

		int c = 0;
		c += cells[mX][mY]?1:0;
		c += cells[x][mY]?1:0;
		c += cells[pX][mY]?1:0;
		c += cells[mX][y]?1:0;
		c += cells[pX][y]?1:0;
		c += cells[mX][pY]?1:0;
		c += cells[x][pY]?1:0;
		c += cells[pX][pY]?1:0;
		
		return c;
	}
	
	public void live(int x, int y) {
		cells[x][y] = true;
	}

	public void dead(int x, int y) {
		cells[x][y] = false;
	}
	
	public void place(int x, int y) {
		cells[x][y] = !cells[x][y];
	}
	
	public void draw(Graphics2D g) {
		int offX = 0;
		int offY = 0;
		for(int y = 0; y < GameStatic.HEIGHT; y++) {
			for(int x = 0; x < GameStatic.WIDTH; x++) {
				if(cells[x][y])
					g.setColor(Color.BLACK);
				else
					g.setColor(Color.WHITE);					
				
				g.fillRect(0, 0, GameStatic.CELL_WIDTH, GameStatic.CELL_HEIGHT);
				g.translate(GameStatic.CELL_WIDTH, 0);
				offX += GameStatic.CELL_WIDTH;
			}
			g.translate(-GameStatic.CELL_WIDTH*GameStatic.WIDTH, GameStatic.CELL_HEIGHT);
			offX -= GameStatic.CELL_WIDTH*GameStatic.WIDTH;
			offY += GameStatic.CELL_HEIGHT;
		}
		g.translate(-offX, -offY);
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void playPause() {
		running = !running;
	}
	
	class GameUpdateState {
		
		private int x, y;
		private boolean nValue;
		
		public GameUpdateState(int x, int y, boolean nValue) {
			this.x = x;
			this.y = y;
			this.nValue = nValue;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public boolean getNewValue() {
			return nValue;
		}
		
	}

}
