package net.argus.game.gol;

public class GameLoadedState {
	
	private int period = 0;
	private boolean drawGrid = true;
	
	public GameLoadedState() {}
	
	public GameLoadedState(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}

	public GameLoadedState(int period) {
		this.period = period;
	}
	
	public GameLoadedState(int period, boolean drawGrid) {
		this.period = period;
		this.drawGrid = drawGrid;
	}
	
	public void setDrawGrid(boolean drawGrid) {
		this.drawGrid = drawGrid;
	}
	
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public boolean isDrawGrid() {
		return drawGrid;
	}

}
