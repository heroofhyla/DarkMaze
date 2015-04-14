package com.aezart.darkmaze;

public final class XYCoords {
	private final int x;
	private final int y;
	
	public XYCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public XYCoords(int xTile, int yTile, int xOffset, int yOffset){
		this.x = xTile*32 + xOffset;
		this.y = yTile*32 + yOffset;
	}
	
	//in the same tile as xy, but with a different offset
	//to have the exact same x and y as an existing XYCoords,
	// just use = (don't worry, they're immutable!)
	public XYCoords(XYCoords xy, int xOffset, int yOffset){
		this.x = xy.xTile(0) * 32 + xOffset;
		this.y = xy.yTile(0) * 32 + yOffset;
	}
	
	public final int x(){
		return x;
	}
	
	public final int y(){
		return y;
	}
	
	public final XYCoords plus(int x, int y){
		return new XYCoords(this.x+x, this.y+y);
	}
	public final int xTile(int xOffset){
		return (x+xOffset)/32;
	}
	
	public final int yTile(int yOffset){
		return (y+yOffset)/32;
	}	
}
