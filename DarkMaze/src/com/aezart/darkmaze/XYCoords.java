package com.aezart.darkmaze;

public class XYCoords {
	int x;
	int y;
	
	private XYCoords(){
	}
	
	static XYCoords fromAbsolute(int x, int y){
		XYCoords xy = new XYCoords();
		xy.x = x;
		xy.y = y;
		return xy;
	}
	
	static XYCoords fromTile(int xTile, int yTile){
		return fromTile(xTile, yTile, 0, 0);
	}
	
	static XYCoords fromTile(int xTile, int yTile, int xOffset, int yOffset){
		XYCoords xy = new XYCoords();
		xy.x = xTile*32 + xOffset;
		xy.y = yTile*32 + yOffset;
		return xy;
	}
	public int x(){
		return x;
	}
	
	public int y(){
		return y;
	}
	
	public int xTile(){
		return x/32;
	}
	
	public int yTile(){
		return y/32;
	}
	
	public int xTile(int xOffset){
		return (x+xOffset)/32;
	}
	
	public int yTile(int yOffset){
		return (y+yOffset)/32;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setXTile(int xTile){
		this.x = xTile*32;
	}
	public void setYTile(int yTile){
		this.y = yTile*32;
	}
}
