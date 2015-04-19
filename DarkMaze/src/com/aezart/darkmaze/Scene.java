package com.aezart.darkmaze;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

public abstract class Scene {
	public abstract void tick();
	public abstract void draw(Graphics g);
	
	public abstract void keyPressed(KeyEvent keyEvent);
}
