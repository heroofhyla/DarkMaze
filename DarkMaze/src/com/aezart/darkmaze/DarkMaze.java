package com.aezart.darkmaze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

public class DarkMaze extends JFrame{
	
	int lives = 3;
	int coinCount = 0;
	
	static final int FULLBRIGHT = 0;
	static final int ALL_TORCHES = 1;
	static final int ENEMY_TORCHES = 2;
	static final int PLAYER_TORCH = 3;
	static final int FULLDARK = 4;
		
	int displayMode = ALL_TORCHES;
	int[] directions = new int[5];
	Vector<Entity> entities = new Vector<Entity>();
	Vector<Cloak> cloaks = new Vector<Cloak>(4);
	BufferedImage glowingEyes;
	BufferedImage redEyes;
	BufferedImage light;
	BufferedImage droppedTorch;
	BufferedImage wallshadow;
	BufferedImage knightSprite;
	BufferedImage cloakSprite;
	boolean debug = false;
	boolean showmap = false;
	Random rng = new Random();
	boolean[][] maze = new boolean[15][19];
	boolean[][] coins = new boolean[7][9];
	HashMap<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
	Entity knight;
		
	int lastTorch = -1;
	BufferedImage tileset;
	Screen screen = new Screen(this);
		
	public DarkMaze(){
		
		for (int i = 0; i < coins.length; ++i){
			for (int k = 0; k < coins[0].length; ++k){
				coins[i][k] = true;
			}
			coins[3][4] = false;
			
		}
		try {
			tileset  = ImageIO.read(new File("tileset2.png"));
			light = ImageIO.read(new File("alphalight.png"));
			wallshadow = ImageIO.read(new File("wallshadow.png"));
			glowingEyes = ImageIO.read(new File("glowingeyes.png"));
			redEyes = ImageIO.read(new File("redeyes.png"));
			droppedTorch = ImageIO.read(new File("droppedtorch.png"));
			knightSprite = ImageIO.read(new File("littleknight.png"));
			cloakSprite = ImageIO.read(new File("littlecloak.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		knight = new Knight(this);
		entities.add(knight);
		for (int i = 0; i < 4; ++i){
			cloaks.add(new Cloak(this));
		}
		cloaks.get(0).setPosition(XYCoords.fromTile(1, 1, 8, 8));
		cloaks.get(0).drawXOffset = -8;
		cloaks.get(0).drawYOffset = -8;
		cloaks.get(1).setPosition(XYCoords.fromTile(1,13, 8, 8));
		cloaks.get(1).drawXOffset = -8;
		cloaks.get(1).drawYOffset = 8; 
		cloaks.get(2).setPosition(XYCoords.fromTile(17,1, 8, 8));
		cloaks.get(2).drawXOffset = 8;
		cloaks.get(2).drawYOffset = -8;
		cloaks.get(3).setPosition(XYCoords.fromTile(17,13,8, 8));
		cloaks.get(3).drawXOffset = 8;
		cloaks.get(3).drawYOffset = 8;
		entities.addAll(cloaks);
		
		knight.setPosition(XYCoords.fromTile(9, 7, 8, 8));
		final Graphics sg = screen.mapSurface.getGraphics();
		
		screen.setPreferredSize(new Dimension(608,480));
		add(screen);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		generateMaze(maze);

		paintBackground(sg);

		this.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				keyStates.put(arg0.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				keyStates.put(arg0.getKeyCode(), false);
				if (arg0.getKeyCode() == KeyEvent.VK_SHIFT){
					//showmap = !showmap;
					displayMode = (displayMode+1)%5;
					debug = !debug;
					
				}
				if (arg0.getKeyCode() == KeyEvent.VK_Z){
					resetMaze();
				}
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE){
					//entities.add(new Entity(torchType));
					//entities.lastElement().x = knight.x;
					//entities.lastElement().y = knight.y;
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
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				while(true){
					for (Entity e: darkMaze.entities){
						e.tick();
					}
						SwingUtilities.invokeLater(new Runnable(){

							@Override
							public void run() {
								darkMaze.screen.repaint();
							}
							
						});
					try {
						Thread.sleep(33);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
	
	void generateMaze(boolean[][] maze){
		/*Iterator<Entity> itr = entities.iterator();
		while (itr.hasNext()){
			Entity e = itr.next();
			if (e.entityType == torchType){
				itr.remove();
			}
		}*/
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
		int firstX = rng.nextInt(visited[0].length);
		int firstY = rng.nextInt(visited.length);
		
		deadEnds.add(XYCoords.fromTile(firstX,firstY));
		//entities.add(new Entity(torchType));
		//entities.lastElement().x = 64 * firstX + 40;
		//entities.lastElement().y = 64 * firstY + 40;

		generateNext(firstX, firstY, maze, visited, deadEnds);
		
		for (XYCoords d: deadEnds){
			ArrayList<XYCoords> adjacentCells = new ArrayList<XYCoords>();
			if (d.xTile() > 0){
				adjacentCells.add(XYCoords.fromTile(2*d.xTile(),2*d.yTile()+1));
			}
			if (d.xTile() < (maze[0].length-1)/2-1){
				adjacentCells.add(XYCoords.fromTile(2*d.xTile()+2,2*d.yTile()+1));
			}
			if (d.yTile() > 0){
				adjacentCells.add(XYCoords.fromTile(2*d.xTile()+1,2*d.yTile()));
			}
			if (d.yTile() < (maze.length-1)/2-1){
				adjacentCells.add(XYCoords.fromTile(2*d.xTile()+1,2*d.yTile()+2));
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
			adjacentCells.add(XYCoords.fromTile(x-1,y));
		}
		if (x < visited[0].length - 1){
			adjacentCells.add(XYCoords.fromTile(x+1,y));
		}
		if (y>0){
			adjacentCells.add(XYCoords.fromTile(x,y-1));
		}
		if (y < visited.length - 1){
			adjacentCells.add(XYCoords.fromTile(x,y+1));
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
			deadEnds.add(XYCoords.fromTile(x,y));
			//entities.add(new Entity(torchType));
			//entities.lastElement().x = 64 * x + 40;
			//entities.lastElement().y = 64 * y + 40;
		}
	}
	
	boolean lineOfSight(int x1, int y1, int x2, int y2){
		boolean lineOfSight = false;
		if (y1 == y2){
			lineOfSight = true;
			for (int j = Math.min(x1, x2); j < Math.max(x1, x2); ++j){
				if (maze[y1][j]){
					lineOfSight = false;
				}
			}
		}
		if (x1 == x2){
			lineOfSight = true;
			for (int j = Math.min(y1, y2); j < Math.max(y1, y2); ++j){
				if (maze[j][x1]){
					lineOfSight = false;
				}
			}
		}
		
		return lineOfSight;
	}
	
	void paintBackground(Graphics sg){
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				sg.setColor(Color.white);
				sg.drawImage(tileset, 32*k, 32*i, 32*k+32, 32*i+32, 0, 0, 16, 16, null);
				//sg.fillRect(32*k, 32*i, 32, 32);
			}
		}
		sg.setColor(Color.black);
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (maze[i][k]){
					//sg.fillRect(32*k-2, 32*i-2, 36, 36);
					sg.drawImage(wallshadow, 32*k-3, 32*i-3, null);
				}
			}
		}
		for (int i = 0; i < maze.length; ++i){
			for (int k = 0; k < maze[0].length; ++k){
				if (maze[i][k]){
					sg.drawImage(tileset, 32*k, 32*i, 32*k+32, 32*i+32, 16, 0, 32, 16, null);
				}
			}
		}
	}
	
	void resetMaze(){
		generateMaze(maze);

		cloaks.get(0).setPosition(XYCoords.fromTile(1, 1, 8, 8));
		cloaks.get(1).setPosition(XYCoords.fromTile(1,13, 8, 8));
		cloaks.get(2).setPosition(XYCoords.fromTile(17,1, 8, 8));
		cloaks.get(3).setPosition(XYCoords.fromTile(17,13, 8, 8));
		
		for (Cloak c: cloaks){
			c.playerLastSeen.setX(0);
			c.playerLastSeen.setY(0);
			c.playerNextTurn.setX(0);
			c.playerNextTurn.setY(0);
			c.playerStillInView = false;
		}
		paintBackground(screen.mapSurface.getGraphics());

	}
}
