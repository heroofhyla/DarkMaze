package com.aezart.darkmaze;

import java.awt.Dimension;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

public class DarkMaze extends JFrame{
		
	static final int FULLBRIGHT = 0;
	static final int ALL_TORCHES = 1;
	static final int ENEMY_TORCHES = 2;
	static final int PLAYER_TORCH = 3;
	static final int FULLDARK = 4;
	
	
	boolean fancygraphics = true;
	
	int displayMode = ALL_TORCHES;
	int[] directions = new int[5];
	
	BufferedImage light;
	BufferedImage fastLight;
	BufferedImage fancyLight;
	BufferedImage droppedTorch;
	BufferedImage wallshadow;
	BufferedImage knightSprite;
	BufferedImage cloakSprite;
	BufferedImage alertIcon;
	BufferedImage lostIcon;
	BufferedImage gameOver;
	BufferedImage tileset;
	BufferedImage firecracker;
	BufferedImage noSprite = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage explosion;
	
	Clip coinClip;
	Clip footstepsClip;
	Clip firecrackerClip;
	Player bgmPlayer;
	InputStream bgmIS;
	
	boolean debug = false;
	Random rng = new Random();
	HashMap<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
	Screen screen = new Screen(this);
	Scene currentScene;
	public DarkMaze(){
		try {
			gameOver = importImage("resources/gameover.png", Transparency.TRANSLUCENT);
			tileset = importImage("resources/tileset2.png", Transparency.BITMASK);
			fancyLight = importImage("resources/alphalight.png",Transparency.TRANSLUCENT);
			fastLight = importImage("resources/alphalight.png",Transparency.BITMASK);
			wallshadow = importImage("resources/wallshadow.png",Transparency.TRANSLUCENT);
			droppedTorch = importImage("resources/droppedtorch.png",Transparency.BITMASK);
			knightSprite = importImage("resources/littleknightsheet.png",Transparency.BITMASK);
			cloakSprite = importImage("resources/littlecloaksheet.png",Transparency.BITMASK);
			alertIcon = importImage("resources/alerticon.png",Transparency.BITMASK);
			lostIcon = importImage("resources/losticon.png", Transparency.BITMASK);
			firecracker = importImage("resources/firecracker.png", Transparency.BITMASK);
			explosion = importImage("resources/explosion1.png", Transparency.BITMASK);
			
			coinClip = createSoundClip("resources/audio/coin.wav");
			footstepsClip = createSoundClip("resources/audio/footsteps.wav");
			firecrackerClip = createSoundClip("resources/audio/firecracker.wav");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		light = fancyLight;
		
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
	
	public BufferedImage createImage(int x, int y, int transparency){
		return getGraphicsConfiguration().createCompatibleImage(x, y, transparency);
	}
	
	public Clip createSoundClip(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		BufferedInputStream bis = new BufferedInputStream(this.getClass().getResourceAsStream(filePath));
		AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
		AudioFormat af = ais.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, af);
		Clip clip = (Clip)AudioSystem.getLine(info);
		clip.open(ais);
		return clip;

	}
	
}
