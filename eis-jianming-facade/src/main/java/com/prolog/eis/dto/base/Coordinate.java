package com.prolog.eis.dto.base;

public class Coordinate {

	private int layer;
	
	private int x;
	
	private int y;

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Coordinate [layer=" + layer + ", x=" + x + ", y=" + y + "]";
	}
	
	
}
