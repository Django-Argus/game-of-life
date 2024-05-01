package net.argus.game.gol;

import java.awt.Point;
import java.util.LinkedHashSet;

public class Cells {
	
	private LinkedHashSet<Point> cells = new LinkedHashSet<Point>();
	
	public Cells() {}
	
	
	public void add(int x, int y) {
		cells.add(new Point(x, y));
	}
	
	public void remove(int x, int y) {
		cells.remove(new Point(x, y));
	}
	
	public boolean isAlive(int x, int y) {
		return cells.contains(new Point(x, y));
	}
	
	public void clear() {
		cells.clear();	
	}
	
	public int adj(int x, int y) {
		int c = 0;
		c += cells.contains(new Point(x-1, y-1))?1:0;
		c += cells.contains(new Point(x, y-1))?1:0;
		c += cells.contains(new Point(x+1, y-1))?1:0;
		c += cells.contains(new Point(x-1, y))?1:0;
		c += cells.contains(new Point(x+1, y))?1:0;
		c += cells.contains(new Point(x-1, y+1))?1:0;
		c += cells.contains(new Point(x, y+1))?1:0;
		c += cells.contains(new Point(x+1, y+1))?1:0;
		
		return c;
	}
	
	public LinkedHashSet<Point> getCells() {
		return cells;
	}

}
