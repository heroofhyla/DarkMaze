package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class DungeonScene extends Scene{
	boolean fancyGraphics = true;
	DarkMaze game;

	//BufferedImage mapImage = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage mapImage;
	BufferedImage drawingSurface;
	//BufferedImage drawingSurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage fancyLightSurface;
	BufferedImage fastLightSurface;
	//BufferedImage lightSurface = new BufferedImage(608, 480, BufferedImage.TYPE_4BYTE_ABGR);
	BufferedImage lightSurface;
	//BufferedImage finalRender = new BufferedImage(608,480,BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage finalRender;
	
	Graphics mapG;
	Graphics drawingG;
	Graphics2D lightG2D;
	Graphics finalG;
	
	int level = 0;
	int coinCount = 0;
	int lives = 3;
	boolean readyForNextLevel;
	boolean[][] maze = new boolean[15][19];
	boolean[][] coins = new boolean[7][9];
	Vector<Entity> entities = new Vector<Entity>();
	Vector<Entity> toRemove = new Vector<Entity>();
	Vector<Cloak> cloaks = new Vector<Cloak>(4);
	Knight knight;
	TextAlert textAlert;
	Stairs stairs;
	int lastTorch = -1;


	public DungeonScene(final DarkMaze game){
		try {
			for (Object o: AudioSystem.getAudioFileTypes()){
				System.out.println(o);
			}
			
			for (Object o: game.coinClip.getControls()){
				System.out.println(o);
			}
			try{
				((FloatControl) game.coinClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-5);
			} catch (IllegalArgumentException e){
				((FloatControl) game.coinClip.getControl(FloatControl.Type.VOLUME)).setValue(40000);
				System.out.println("MASTER_GAIN not supported, using VOLUME");
			}
			game.bgmIS =  (this.getClass().getResourceAsStream("resources/audio/Oppressive Gloom.mp3"));
			game.bgmPlayer = new Player(game.bgmIS);
			
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						game.bgmPlayer.play();
					} catch (JavaLayerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			).start();
			
		} catch (JavaLayerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.game = game;
		mapImage = game.createImage(608, 480, Transparency.OPAQUE);
		drawingSurface = game.createImage(608, 480, Transparency.OPAQUE);
		fancyLightSurface = game.createImage(608,480,Transparency.TRANSLUCENT);
		fastLightSurface = game.createImage(608, 480, Transparency.BITMASK);
		lightSurface = fancyLightSurface;
		finalRender = game.createImage(608, 480, Transparency.OPAQUE);

		mapG = mapImage.getGraphics();
		drawingG = drawingSurface.getGraphics();
		lightG2D = (Graphics2D) lightSurface.getGraphics();
		finalG = finalRender.getGraphics();
		
		stairs = new Stairs(this);
		stairs.setTile(5, 5, 0, 0);
		entities.add(stairs);
		knight = new Knight(this);
		textAlert = new TextAlert(this);
		entities.add(knight);
		entities.add(textAlert);
		cloaks.add(new Cloak(this, -8,-8,23,23));
		cloaks.add(new Cloak(this, -24,-8,7,23));
		cloaks.add(new Cloak(this, -8,-24,23,7));
		cloaks.add(new Cloak(this, -24,-24,7,7));
		entities.addAll(cloaks);		
		nextLevel();

	}
	@Override
	public void tick() {
		for (Entity e: entities){
			e.tick();
		}
		
		if (readyForNextLevel){
			nextLevel();
			stairs.stairsReady = false;
			stairs.coinTarget = coinCount + 40;
			
			readyForNextLevel = false;

		}
		if (lives < 0){
			game.currentScene = new GameOverScene(game,finalRender);
		}
	}

	@Override
	public void draw(Graphics g) {
		long drawStartTime = System.currentTimeMillis();
		drawingG.drawImage(mapImage, 0, 0, null);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		lightG2D.fillRect(0, 0, 608, 480);
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		
		
		for (int i = 0; i < coins.length; ++i){
			for (int k = 0; k < coins[0].length; ++k){
				if (coins[i][k]){
					drawingG.drawImage(game.tileset, k*64+32, i*64+32, k*64+64, i*64+64, 32, 0, 48, 16, null);
				}
			}
		}

		
		for (Entity e: entities){
			e.draw(drawingG);
			e.drawLights(lightG2D);
		}
		
		if (game.debug){
			lightG2D.fillRect(0, 0, 608, 480);
		}
		lightG2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
		lightG2D.drawImage(drawingSurface, 0, 0, null);
		finalG.setColor(Color.black);
		finalG.fillRect(0, 0, 608, 480);
		finalG.drawImage(lightSurface, 0, 0, null);
		
		finalG.setColor(Color.black);
		finalG.fillRect(0,468,608,480);
		finalG.setColor(Color.white);
		finalG.drawString("Lives: " + lives + " Depth: " + (10*level)  + "ft Coins: " + coinCount, 0, 478);

		for (Entity e: entities){
			e.drawEffects(finalG);
		}
		g.drawImage(finalRender, 0, 0, null);
		//System.out.println((System.currentTimeMillis() - drawStartTime));
	}
	
	public void nextLevel(){
		++level;
		textAlert.showTextAlert("Depth: " + 10*level + "ft", 60);
		generateMaze(maze);
		resetEntities();
		paintBackground(mapG);
		for (int i = 0; i < coins.length; ++i){
			for (int k = 0; k < coins[0].length; ++k){
				coins[i][k] = true;
			}
			coins[3][4] = false;
			
		}

	}
	
	void resetEntities(){
		cloaks.get(0).setTile(1, 1, 8, 8);
		cloaks.get(1).setTile(17,1, 24, 8);
		cloaks.get(2).setTile(1,13, 8, 24);
		cloaks.get(3).setTile(17,13,24, 24);
		
		for (Cloak c: cloaks){
			c.playerLastSeen.setPosition(0,0);
			c.playerNextTurn.setPosition(0,0);
			c.playerStillInView = false;
			c.alertState = 0;
		}
		knight.setTile(9, 7, 8, 8);


	}
	
	void generateMaze(boolean[][] maze){
		Iterator<Entity> itr = entities.iterator();
		while (itr.hasNext()){
			Entity e = itr.next();
			if (e instanceof Torch){
				itr.remove();
			}
		}
		Vector<XYCoords> deadEnds = new Vector<XYCoords>();
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (i%2 == 1 && k%2 == 1){
					maze[i][k] = false;
				}else{
					maze[i][k] = true;
				}
			}
		}
		boolean[][] visited = new boolean[(maze.length-1)/2][(maze[0].length-1)/2];
		int firstX = game.rng.nextInt(visited[0].length);
		int firstY = game.rng.nextInt(visited.length);
		
		deadEnds.add(new XYCoords(firstX, firstY, 0, 0));
		entities.add(new Torch(this));
		entities.lastElement().setTile(deadEnds.lastElement().xTile()*2 + 1, deadEnds.lastElement().yTile()*2 + 1, 8, 8);

		generateNext(firstX, firstY, maze, visited, deadEnds);
		
		for (XYCoords d: deadEnds){
			ArrayList<XYCoords> adjacentCells = new ArrayList<XYCoords>();
			if (d.xTile() > 0){
				adjacentCells.add(new XYCoords(2*d.xTile(),2*d.yTile()+1, 0, 0));
			}
			if (d.xTile() < (maze[0].length-1)/2-1){
				adjacentCells.add(new XYCoords(2*d.xTile()+2,2*d.yTile()+1, 0, 0));
			}
			if (d.yTile() > 0){
				adjacentCells.add(new XYCoords(2*d.xTile()+1,2*d.yTile(), 0, 0));
			}
			if (d.yTile() < (maze.length-1)/2-1){
				adjacentCells.add(new XYCoords(2*d.xTile()+1,2*d.yTile()+2, 0, 0));
			}
			Collections.shuffle(adjacentCells);
			for (XYCoords t: adjacentCells){
				if (maze[t.yTile()][t.xTile()]){
					maze[t.yTile()][t.xTile()] = false;
					break;
				}
			}
		}
	}
	
	void generateNext(int x, int y, boolean[][] maze, boolean[][] visited, Vector<XYCoords> deadEnds){
		visited[y][x] = true;
		ArrayList<XYCoords> adjacentCells = new ArrayList<XYCoords>();
		if (x > 0){
			adjacentCells.add(new XYCoords(x-1,y, 0, 0));
		}
		if (x < visited[0].length - 1){
			adjacentCells.add(new XYCoords(x+1,y, 0, 0));
		}
		if (y>0){
			adjacentCells.add(new XYCoords(x,y-1, 0, 0));
		}
		if (y < visited.length - 1){
			adjacentCells.add(new XYCoords(x,y+1, 0, 0));
		}
		
		Collections.shuffle(adjacentCells);
		int numVisited = 0;

		for (XYCoords d: adjacentCells){

			if (!visited[d.yTile()][d.xTile()]){
				++numVisited;
				int deltaX = x - d.xTile();
				int deltaY = y - d.yTile();
				
				maze[y*2 + 1 - deltaY][x*2 + 1 - deltaX] = false;
				generateNext(d.xTile(), d.yTile(), maze, visited, deadEnds);
			}
		}
		if (numVisited == 0){
			deadEnds.add(new XYCoords(x,y, 0, 0));
			entities.add(new Torch(this));
			entities.lastElement().setTile(deadEnds.lastElement().xTile()*2 + 1, deadEnds.lastElement().yTile()*2 + 1, 8, 8);
		}
	}
	
	void paintBackground(Graphics sg){
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				sg.setColor(Color.white);
				sg.drawImage(game.tileset, 32*k, 32*i, 32*k+32, 32*i+32, 0, 0, 16, 16, null);
			}
		}
		sg.setColor(Color.black);
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (maze[i][k]){
					sg.drawImage(game.wallshadow, 32*k-3, 32*i-3, null);
				}
			}
		}
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (maze[i][k]){
					sg.drawImage(game.tileset, 32*k, 32*i, 32*k+32, 32*i+32, 16, 0, 32, 16, null);
				}
			}
		}
	}
	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE){
			entities.add(new Torch(this));
			entities.lastElement().setPosition(knight.position);
		}

		if (keyEvent.getKeyCode() == KeyEvent.VK_F){
			game.fancygraphics = !game.fancygraphics;
			
			if (game.fancygraphics){
				lightSurface = fancyLightSurface;
				game.light = game.fancyLight;
			}else{
				lightSurface = fastLightSurface;
				game.light = game.fastLight;
			}
			lightG2D = (Graphics2D)lightSurface.getGraphics();
		}
		
	}
}
	
