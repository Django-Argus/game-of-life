package net.argus.game.gol;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game {
	
	private Cells cells;
	
	private boolean running = false;
	
	private ReentrantLock lock = new ReentrantLock();
	
	public Game() {
		cells = new Cells();
		
		clear();
	}
	
	public void update() {
		if(!running)
			return;
		
		List<GameUpdateState> gameUpdateStates = new ArrayList<Game.GameUpdateState>();
		
		lock.lock();
		for(Point point : cells.getCells()) {
			GameUpdateState up = test(point, false);
			gameUpdateStates.add(up);
			
			up = test(new Point(point.x-1, point.y-1), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x, point.y-1), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x+1, point.y-1), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x-1, point.y), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x+1, point.y), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x-1, point.y+1), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x, point.y+1), true);
			gameUpdateStates.add(up);
			up = test(new Point(point.x+1, point.y+1), true);
			gameUpdateStates.add(up);
			
		}
		
		for(GameUpdateState state : gameUpdateStates) {
			if(state == null)
				continue;
			if(state.nValue) 
				cells.add(state.x, state.y);
			else
				cells.remove(state.x, state.y);	
		}
		lock.unlock();
	}
	
	public GameUpdateState test(Point point, boolean strict) {
		if(cells.isAlive(point.x, point.y) && !strict) {
			int adj = cells.adj(point.x, point.y);
			if(adj < 2)
				return new GameUpdateState(point.x, point.y, false);
			else if(adj < 4)
				return null;
			else
				return new GameUpdateState(point.x, point.y, false);
		}else if(strict && !cells.isAlive(point.x, point.y)) {
			int adj = cells.adj(point.x, point.y);
			if(adj == 3)
				return new GameUpdateState(point.x, point.y, true);
		}
		
		return null;
	}
	
	public void clear() {
		cells.clear();
	}
	
	public void clearLocal(Point origin, Dimension dim, boolean reversWidth, boolean reversHeight) {
		for(int x = 0; x < dim.width; x++)
			for(int y = 0; y < dim.height; y++)
				cells.remove((x) * (reversWidth?-1:1) + origin.x, y * (reversHeight?-1:1) + origin.y);
	}
	
	public void fill(Point origin, Dimension dim, boolean reversWidth, boolean reversHeight) {
		for(int x = 0; x < dim.width; x++)
			for(int y = 0; y < dim.height; y++)
				cells.add((x) * (reversWidth?-1:1) + origin.x, y * (reversHeight?-1:1) + origin.y);
	}
	
	public void add(int x, int y) {
		cells.add(x, y);
	}

	public void remove(int x, int y) {
		cells.remove(x, y);
	}
	
	public void random(long seed, Point origin, Dimension dim, boolean reversWidth, boolean reversHeight) {
		if(running)
			return;

		clearLocal(origin, dim, reversWidth, reversHeight);
		Random rand;
		if(seed == 0)
			rand = new Random();
		else
			rand = new Random(seed);
		
		for(int x = 0; x < dim.width; x++)
			for(int y = 0; y < dim.height; y++)
				if(rand.nextBoolean())
					cells.add((x) * (reversWidth?-1:1) + origin.x, y * (reversHeight?-1:1) + origin.y);
	}	
	
	public void draw(Graphics2D g) {
		lock.lock();
		for(Point point : cells.getCells())
			g.fillRect(point.x * GameStatic.CELL_WIDTH, point.y * GameStatic.CELL_HEIGHT, GameStatic.CELL_WIDTH, GameStatic.CELL_HEIGHT);
		
		lock.unlock();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void playPause() {
		running = !running;
	}
	
	public Cells getCells() {
		return cells;
	}
	
	public ReentrantLock getLock() {
		return lock;
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
