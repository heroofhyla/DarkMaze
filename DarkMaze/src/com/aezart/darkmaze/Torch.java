package com.aezart.darkmaze;

public class Torch extends Entity{
	public Torch(DungeonScene scene){
		super(scene.game.droppedTorch, scene);
		drawXOffset = -sprite.getWidth()/2;
		drawYOffset = -sprite.getHeight()/2;
	}
	
	
}
