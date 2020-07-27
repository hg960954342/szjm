package com.prolog.eis.dto.eis;

public class SxkPointDto {

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

	public SxkPointDto(int layer, int x, int y) {
		super();
		this.layer = layer;
		this.x = x;
		this.y = y;
	}

	public SxkPointDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
