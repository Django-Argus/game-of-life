package net.argus.game.gol;

public class GameLoadedState {
	
	private int period = 0, tx, ty;
	
	private double scale;
	
	public GameLoadedState() {}
	
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public double getScale() {
		return scale;
	}
	
	public int getTx() {
		return tx;
	}
	
	public int getTy() {
		return ty;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public void setTx(int tx) {
		this.tx = tx;
	}
	
	public void setTy(int ty) {
		this.ty = ty;
	}
}
