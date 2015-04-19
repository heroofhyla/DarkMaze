package com.aezart.darkmaze;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;

public class DarkMaze extends JFrame{
		
	static final int FULLBRIGHT = 0;
	static final int ALL_TORCHES = 1;
	static final int ENEMY_TORCHES = 2;
	static final int PLAYER_TORCH = 3;
	static final int FULLDARK = 4;
			
	int displayMode = ALL_TORCHES;
	int[] directions = new int[5];
	int testingMerges;
	BufferedImage glowingEyes;
	BufferedImage redEyes;
	BufferedImage light;
	BufferedImage droppedTorch;
	BufferedImage wallshadow;
	BufferedImage knightSprite;
	BufferedImage cloakSprite;
	BufferedImage alertIcon;
	BufferedImage lostIcon;
	BufferedImage gameOver;
	BufferedImage noSprite = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
	boolean debug = false;
	Random rng = new Random();
	HashMap<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
	BufferedImage tileset;
	Screen screen = new Screen(this);
	Scene currentScene;
	public DarkMaze(){
		try {
	
			gameOver = importImage("resources/gameover.png", Transparency.TRANSLUCENT);
			//gameOver = ImageIO.read(this.getClass().getResource("resources/gameover.png"));
			tileset = importImage("resources/tileset2.png", Transparency.BITMASK);
			//tileset = ImageIO.read(this.getClass().getResource("resources/tileset2.png"));
			//light = ImageIO.read(this.getClass().getResource("resources/alphalight.png"));
			light = importImage("resources/alphalight.png",Transparency.TRANSLUCENT);
			//wallshadow = ImageIO.read(this.getClass().getResource("resources/wallshadow.png"));
			wallshadow = importImage("resources/wallshadow.png",Transparency.TRANSLUCENT);
			//glowingEyes = ImageIO.read(this.getClass().getResource("resources/glowingeyes.png"));
			//redEyes = ImageIO.read(this.getClass().getResource("resources/redeyes.png"));
			//droppedTorch = ImageIO.read(this.getClass().getResource("resources/droppedtorch.png"));
			droppedTorch = importImage("resources/droppedtorch.png",Transparency.BITMASK);
			//knightSprite = ImageIO.read(this.getClass().getResource("resources/littleknightsheet.png"));
			knightSprite = importImage("resources/littleknightsheet.png",Transparency.BITMASK);
			//cloakSprite = ImageIO.read(this.getClass().getResource("resources/littlecloaksheet.png"));
			cloakSprite = importImage("resources/littlecloaksheet.png",Transparency.BITMASK);
			//alertIcon = ImageIO.read(this.getClass().getResource("resources/alerticon.png"));
			alertIcon = importImage("resources/alerticon.png",Transparency.BITMASK);
			//lostIcon = ImageIO.read(this.getClass().getResource("resources/losticon.png"));
			lostIcon = importImage("resources/losticon.png", Transparency.BITMASK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final Graphics sg = screen.mapImage.getGraphics();
		
		currentScene = new TitleScene(this);

		screen.setPreferredSize(new Dimension(608,480));
		add(screen);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				keyStates.put(arg0.getKeyCode(), true);
				currentScene.keyPressed(arg0);
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
	
	public BufferedImage importImage(String filePath, int transparency) throws IOException{
		BufferedImage rawImage = ImageIO.read(this.getClass().getResource(filePath));
		BufferedImage optimizedImage = getGraphicsConfiguration().createCompatibleImage(rawImage.getWidth(), rawImage.getHeight(), transparency);
		optimizedImage.getGraphics().drawImage(rawImage, 0, 0, null);
		return optimizedImage;
	}
}
