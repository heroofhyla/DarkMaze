package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class DarkMaze extends JFrame{
	
	int lives = 3;
	int coinCount = 0;
	
	static final int FULLBRIGHT = 0;
	static final int ALL_TORCHES = 1;
	static final int ENEMY_TORCHES = 2;
	static final int PLAYER_TORCH = 3;
	static final int FULLDARK = 4;
	
	boolean readyForNextLevel = false;
		
	int displayMode = ALL_TORCHES;
	int[] directions = new int[5];
	BufferedImage glowingEyes;
	BufferedImage redEyes;
	BufferedImage light;
	BufferedImage droppedTorch;
	BufferedImage wallshadow;
	BufferedImage knightSprite;
	BufferedImage cloakSprite;
	BufferedImage alertIcon;
	BufferedImage lostIcon;
	BufferedImage noSprite = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
	boolean debug = false;
	Random rng = new Random();
	HashMap<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
	BufferedImage tileset;
	Screen screen = new Screen(this);
	Scene currentScene;
	public DarkMaze(){
		try {
			tileset = ImageIO.read(this.getClass().getResource("resources/tileset2.png"));
			light = ImageIO.read(this.getClass().getResource("resources/alphalight.png"));
			wallshadow = ImageIO.read(this.getClass().getResource("resources/wallshadow.png"));
			glowingEyes = ImageIO.read(this.getClass().getResource("resources/glowingeyes.png"));
			redEyes = ImageIO.read(this.getClass().getResource("resources/redeyes.png"));
			droppedTorch = ImageIO.read(this.getClass().getResource("resources/droppedtorch.png"));
			knightSprite = ImageIO.read(this.getClass().getResource("resources/littleknightsheet.png"));
			cloakSprite = ImageIO.read(this.getClass().getResource("resources/littlecloaksheet.png"));
			alertIcon = ImageIO.read(this.getClass().getResource("resources/alerticon.png"));
			lostIcon = ImageIO.read(this.getClass().getResource("resources/losticon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final Graphics sg = screen.mapImage.getGraphics();
		
		currentScene = new DungeonScene(this);

		screen.setPreferredSize(new Dimension(608,480));
		add(screen);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				keyStates.put(arg0.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				keyStates.put(arg0.getKeyCode(), false);
				if (arg0.getKeyCode() == KeyEvent.VK_SHIFT){
					displayMode = (displayMode+1)%5;
					debug = !debug;
					//textAlert.showTextAlert("Debug mode toggle", 30);

					
				}
				if (arg0.getKeyCode() == KeyEvent.VK_Z){
					//resetMaze();
					//generateMaze(maze);
					//paintBackground(screen.mapImage.getGraphics());

				}
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE){
					//entities.add(new Torch(DarkMaze.this));
					//entities.lastElement().setPosition(new XYCoords(knight.xTile(), knight.yTile(), 8, 8));
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
		});
		keyStates.put(KeyEvent.VK_LEFT, false);
		keyStates.put(KeyEvent.VK_RIGHT, false);
		keyStates.put(KeyEvent.VK_UP, false);
		keyStates.put(KeyEvent.VK_DOWN, false);
	}
	
	public static void main(String[] args){
		
		final DarkMaze darkMaze = new DarkMaze();
		ActionListener gameLoop = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				darkMaze.currentScene.tick();
				darkMaze.screen.repaint();
			}
		};
		
		new Timer(30, gameLoop).start();
	}
}
