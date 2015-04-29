package com.aezart.darkmaze;

import java.awt.Graphics;

public class Firecracker extends Entity{
	
	int lifetime = 0;
	boolean readyToRemove = false;
	public Firecracker(DungeonScene scene) {
		super(scene.game.firecracker, scene);
		drawXOffset = -sprite.getWidth()/8;
		drawYOffset = -sprite.getHeight()/2;

	}
	@Override
	public void draw(Graphics g){
		if (lifetime < 16){
			g.drawImage(sprite, x() + drawXOffset, y() + drawYOffset, x() + drawXOffset + 16, y() + drawYOffset + 16, 16 * (lifetime/4), 0, 16 * (lifetime/4) + 16, 16, null);
		}
	}
		
	@Override
	public void drawEffects(Graphics g){
		if (lifetime >= 16){
			g.drawImage(game.explosion, x() - 24, y() - 24, null);
		}
	}
	@Override
	public void tick(){
		++lifetime;
		if (lifetime == 16){
			game.firecrackerClip.stop();
			game.firecrackerClip.setFramePosition(0);
			game.firecrackerClip.start();

			for (Cloak c: scene.cloaks){
				if (lineOfSight(c) && c.alertState != 2){
					c.direction = c.directionToTile(this.position);
					c.alertState = 2;
					c.confusionTimer = 34;
					c.playerLastSeen.setPosition(this.position);
					//c.playerNextTurn.setPosition(this.position);
				}
			}
		}else if (lifetime >= 20){
			dead = true;
		}
	}

}
