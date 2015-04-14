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
		this.x = xy.xTile() * 32 + xOffset;
		this.y = xy.yTile() * 32 + yOffset;
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
	public final int xTile(){
		return x/32;
	}
	
	public final int yTile(){
		return y/32;
	}
	
	public final boolean equalsTile(XYCoords xy){
		if (xy == null){
			return false;
		}
		if (! (xy instanceof XYCoords)){
			return false;
		}
		return this.xTile() == xy.xTile() && this.yTile() == xy.yTile();
	}
	
	@Override
	public final boolean equals(Object xy){
		if (xy == null){
			return false;
		}
		if (! (xy instanceof XYCoords)){
			return false;
		}
		return this.x() == ((XYCoords)xy).x() && this.y() == ((XYCoords)xy).y();
	}
	
	@Override
	public final int hashCode(){
		return ((x * y) + 29)*37;
	}
}
