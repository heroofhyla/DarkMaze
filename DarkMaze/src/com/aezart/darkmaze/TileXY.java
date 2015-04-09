package com.aezart.darkmaze;

public class TileXY {
	int xTile;
	int yTile;
	
	public TileXY(int x, int y){
		this.xTile = x;
		this.yTile = y;
	}
	
	public int x(){
		return 32*xTile + 8;
	}
	
	public int y(){
		return 32*yTile + 8;
	}
}
