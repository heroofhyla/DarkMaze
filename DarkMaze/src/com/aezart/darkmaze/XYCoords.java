package com.aezart.darkmaze;

public class XYCoords {
	private int x;
	private int y;
	
	private XYCoords(){
	}
	
	static XYCoords fromAbsolute(int x, int y){
		XYCoords xy = new XYCoords();
		xy.x = x;
		xy.y = y;
		return xy;
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
	
	public void xPlus(int x){
		this.x += x;
	}
	
	public void yPlus(int y){
		this.y += y;
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
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(XYCoords xy){
		this.x = xy.x();
		this.y = xy.y();
	}

	public void setTile(XYCoords xy, int xOffset, int yOffset){
		setXTile(xy.xTile(0), xOffset);
		setYTile(xy.yTile(0), yOffset);
	}
	
	public void setTile(int xTile, int yTile, int xOffset, int yOffset){
		setXTile(xTile, xOffset);
		setYTile(yTile, yOffset);
	}
	
	private void setXTile(int xTile, int xOffset){
		this.x = xTile*32 + xOffset;
	}
	
	private void setYTile(int yTile, int yOffset){
		javax.swing.JPanel c;
		
		this.y = yTile*32 + yOffset;
	}
	
}
