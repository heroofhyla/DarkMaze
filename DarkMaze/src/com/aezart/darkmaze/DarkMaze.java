package com.aezart.darkmaze;

import java.awt.AlphaComposite;
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
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DarkMaze extends JFrame{
	
	int lives = 3;
	
	static final int FULLBRIGHT = 0;
	static final int ALL_TORCHES = 1;
	static final int ENEMY_TORCHES = 2;
	static final int PLAYER_TORCH = 3;
	static final int FULLDARK = 4;
	
	EntityType knightType = new EntityType("littleknight.png"){

		@Override
		public void draw(Graphics dg, Entity e) {
			dg.drawImage(sprite, e.x, e.y, null);
		}

		@Override
		public void drawLights(Graphics2D lg, Entity e) {
			lg.drawImage(light, e.x-28, e.y-28, null);
			
		}

		@Override
		public void drawEffects(Graphics g, Entity e) {
			//no effects
		}
	};
	
	EntityType cloakType = new EntityType("littlecloak.png"){
		@Override
		public void draw(Graphics dg, Entity e) {
			dg.drawImage(sprite, e.x, e.y, null);
		}

		@Override
		public void drawLights(Graphics2D lg, Entity e) {
			//no light
		}

		@Override
		public void drawEffects(Graphics g, Entity e) {
			if (lineOfSight(knight.xTile(), knight.yTile(), e.xTile(), e.yTile()))
			{
				g.drawImage(redEyes, e.x, e.y, null);
			}else{
				g.drawImage(glowingEyes, e.x, e.y, null);
			}
		}
	};
	
	
	EntityType torchType = new EntityType("droppedtorch.png"){

		@Override
		public void draw(Graphics dg, Entity e) {
			dg.drawImage(sprite, e.x, e.y, null);
		}

		@Override
		public void drawLights(Graphics2D lg, Entity e) {
			lg.drawImage(light, e.x-28, e.y-28, null);
		}

		@Override
		public void drawEffects(Graphics g, Entity e) {
			//no effects
		}
		
	};
	
	
	int displayMode = ALL_TORCHES;
	int[] directions = new int[5];
	Vector<Entity> entities = new Vector<Entity>();
	Vector<Entity> cloaks = new Vector<Entity>(4);
	BufferedImage glowingEyes;
	BufferedImage redEyes;
	BufferedImage light;
	BufferedImage droppedTorch;
	BufferedImage wallshadow;
	boolean showmap = false;
	Random rng = new Random();
	boolean[][] maze = new boolean[15][19];
	boolean[][] coins = new boolean[7][9];
	HashMap<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
	Entity knight = new Entity(knightType);
	//Entity[] torches = new Entity[5];
	int lastTorch = -1;
	BufferedImage tileset;
	Screen screen = new Screen(this);
		
	public DarkMaze(){
		entities.add(knight);
		for (int i = 0; i < 4; ++i){
			cloaks.add(new Entity(cloakType));
		}
		cloaks.get(0).x = 40;
		cloaks.get(0).y = 40;
		cloaks.get(1).x = 40;
		cloaks.get(1).y = 424;
		cloaks.get(2).x = 552;
		cloaks.get(2).y = 40;
		cloaks.get(3).x = 552;
		cloaks.get(3).y = 424;
		
		entities.addAll(cloaks);
		for (int i = 0; i < coins.length; ++i){
			for (int k = 0; k < coins[0].length; ++k){
				coins[i][k] = true;
			}
		}
		try {
			tileset  = ImageIO.read(new File("tileset2.png"));
			light = ImageIO.read(new File("alphalight.png"));
			wallshadow = ImageIO.read(new File("wallshadow.png"));
			glowingEyes = ImageIO.read(new File("glowingeyes.png"));
			redEyes = ImageIO.read(new File("redeyes.png"));
			droppedTorch = ImageIO.read(new File("droppedtorch.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
					//displayMode = (displayMode+1)%5;
					
				}
				if (arg0.getKeyCode() == KeyEvent.VK_Z){
					cloaks.get(0).x = 40;
					cloaks.get(0).y = 40;
					cloaks.get(1).x = 40;
					cloaks.get(1).y = 424;
					cloaks.get(2).x = 552;
					cloaks.get(2).y = 40;
					cloaks.get(3).x = 552;
					cloaks.get(3).y = 424;
					generateMaze(maze);
					paintBackground(sg);

				}
				if (arg0.getKeyCode() == KeyEvent.VK_SPACE){
					entities.add(new Entity(torchType));
					entities.lastElement().x = knight.x;
					entities.lastElement().y = knight.y;
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
					
					if (darkMaze.knight.x%64 > 26 && darkMaze.knight.x%64 < 54 && 
							darkMaze.knight.y%64 >26 && darkMaze.knight.y%64 < 54){
						darkMaze.coins[darkMaze.knight.y/64][darkMaze.knight.x/64] = false;
					}
					if ((darkMaze.keyStates.get(KeyEvent.VK_LEFT) == true)){
						if (!darkMaze.maze[darkMaze.knight.y/32][(darkMaze.knight.x-2)/32] &&
								!darkMaze.maze[(darkMaze.knight.y+16)/32][(darkMaze.knight.x-2)/32]){
							darkMaze.knight.x -= 2;
						}
					}
					if ((darkMaze.keyStates.get(KeyEvent.VK_RIGHT) == true)){
						if (!darkMaze.maze[darkMaze.knight.y/32][(darkMaze.knight.x+18)/32] &&
								!darkMaze.maze[(darkMaze.knight.y+16)/32][(darkMaze.knight.x+18)/32]){
							darkMaze.knight.x += 2;
						}
					}
					if ((darkMaze.keyStates.get(KeyEvent.VK_UP) == true)){
						if (!darkMaze.maze[(darkMaze.knight.y-2)/32][(darkMaze.knight.x)/32] &&
								!darkMaze.maze[(darkMaze.knight.y-2)/32][(darkMaze.knight.x+16)/32]){
							darkMaze.knight.y -= 2;
						}
					}
					if ((darkMaze.keyStates.get(KeyEvent.VK_DOWN) == true)){
						if (!darkMaze.maze[(darkMaze.knight.y+18)/32][(darkMaze.knight.x)/32] &&
								!darkMaze.maze[(darkMaze.knight.y+18)/32][(darkMaze.knight.x+16)/32]){
							darkMaze.knight.y += 2;
						}
					}
					
					for (int i = 0; i < darkMaze.cloaks.size(); ++i){
						if (darkMaze.lineOfSight(darkMaze.cloaks.get(i).xTile(), darkMaze.cloaks.get(i).yTile(), darkMaze.knight.xTile(), darkMaze.knight.yTile())){
							darkMaze.directions[i] = darkMaze.cloaks.get(i).directionTo(darkMaze.knight);
						}
						int nextX = darkMaze.cloaks.get(i).x;
						int nextY = darkMaze.cloaks.get(i).y;
						
						if (darkMaze.directions[i] == 0 || darkMaze.directions[i] == 1 || darkMaze.directions[i] == 7){
							if (!darkMaze.maze[darkMaze.cloaks.get(i).y/32][(darkMaze.cloaks.get(i).x+18)/32] &&
									!darkMaze.maze[(darkMaze.cloaks.get(i).y+16)/32][(darkMaze.cloaks.get(i).x+18)/32]){
								nextX += 2;
							}
						}
						if (darkMaze.directions[i] == 3 || darkMaze.directions[i] == 4 || darkMaze.directions[i] == 5){
							if (!darkMaze.maze[darkMaze.cloaks.get(i).y/32][(darkMaze.cloaks.get(i).x-2)/32] &&
									!darkMaze.maze[(darkMaze.cloaks.get(i).y+16)/32][(darkMaze.cloaks.get(i).x-2)/32]){
								nextX -= 2;
							}
						}
						if (darkMaze.directions[i] == 1 || darkMaze.directions[i] == 2 || darkMaze.directions[i] == 3){
							if (!darkMaze.maze[(darkMaze.cloaks.get(i).y-2)/32][(darkMaze.cloaks.get(i).x)/32] &&
									!darkMaze.maze[(darkMaze.cloaks.get(i).y-2)/32][(darkMaze.cloaks.get(i).x+16)/32]){
								nextY -= 2;
							}
						}
						if (darkMaze.directions[i] == 5 || darkMaze.directions[i] == 6 || darkMaze.directions[i] == 7){
							if (!darkMaze.maze[(darkMaze.cloaks.get(i).y+18)/32][(darkMaze.cloaks.get(i).x)/32] &&
									!darkMaze.maze[(darkMaze.cloaks.get(i).y+18)/32][(darkMaze.cloaks.get(i).x+16)/32]){
								nextY += 2;
							}
						}
						while (nextX == darkMaze.cloaks.get(i).x && nextY == darkMaze.cloaks.get(i).y){
							darkMaze.directions[i] = darkMaze.rng.nextInt(8)/2 * 2;
							nextX = darkMaze.cloaks.get(i).x;
							nextY = darkMaze.cloaks.get(i).y;
							if (darkMaze.directions[i] == 0 || darkMaze.directions[i] == 1 || darkMaze.directions[i] == 7){
								if (!darkMaze.maze[darkMaze.cloaks.get(i).y/32][(darkMaze.cloaks.get(i).x+18)/32] &&
										!darkMaze.maze[(darkMaze.cloaks.get(i).y+16)/32][(darkMaze.cloaks.get(i).x+18)/32]){
									nextX += 2;
								}
							}
							if (darkMaze.directions[i] == 3 || darkMaze.directions[i] == 4 || darkMaze.directions[i] == 5){
								if (!darkMaze.maze[darkMaze.cloaks.get(i).y/32][(darkMaze.cloaks.get(i).x-2)/32] &&
										!darkMaze.maze[(darkMaze.cloaks.get(i).y+16)/32][(darkMaze.cloaks.get(i).x-2)/32]){
									nextX -= 2;
								}
							}
							if (darkMaze.directions[i] == 1 || darkMaze.directions[i] == 2 || darkMaze.directions[i] == 3){
								if (!darkMaze.maze[(darkMaze.cloaks.get(i).y-2)/32][(darkMaze.cloaks.get(i).x)/32] &&
										!darkMaze.maze[(darkMaze.cloaks.get(i).y-2)/32][(darkMaze.cloaks.get(i).x+16)/32]){
									nextY -= 2;
								}
							}
							if (darkMaze.directions[i] == 5 || darkMaze.directions[i] == 6 || darkMaze.directions[i] == 7){
								if (!darkMaze.maze[(darkMaze.cloaks.get(i).y+18)/32][(darkMaze.cloaks.get(i).x)/32] &&
										!darkMaze.maze[(darkMaze.cloaks.get(i).y+18)/32][(darkMaze.cloaks.get(i).x+16)/32]){
									nextY += 2;
								}
							}
						}
						
						darkMaze.cloaks.get(i).x = nextX;
						darkMaze.cloaks.get(i).y = nextY;
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
		Vector<TileXY> deadEnds = new Vector<TileXY>();
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
		
		deadEnds.add(new TileXY(firstX,firstY));
		//entities.add(new Entity(torchType));
		entities.lastElement().x = 64 * firstX + 40;
		entities.lastElement().y = 64 * firstY + 40;

		generateNext(firstX, firstY, maze, visited, deadEnds);
		
		for (TileXY d: deadEnds){
			ArrayList<TileXY> adjacentCells = new ArrayList<TileXY>();
			if (d.x > 0){
				adjacentCells.add(new TileXY(2*d.x,2*d.y+1));
			}
			if (d.x < (maze[0].length-1)/2-1){
				adjacentCells.add(new TileXY(2*d.x+2,2*d.y+1));
			}
			if (d.y > 0){
				adjacentCells.add(new TileXY(2*d.x+1,2*d.y));
			}
			if (d.y < (maze.length-1)/2-1){
				adjacentCells.add(new TileXY(2*d.x+1,2*d.y+2));
			}
			Collections.shuffle(adjacentCells);
			for (TileXY t: adjacentCells){
				if (maze[t.y][t.x]){
					maze[t.y][t.x] = false;
					break;
				}
			}
		}
	}
	
	void generateNext(int x, int y, boolean[][] maze, boolean[][] visited, Vector<TileXY> deadEnds){
		visited[y][x] = true;
		ArrayList<TileXY> adjacentCells = new ArrayList<TileXY>();
		if (x > 0){
			adjacentCells.add(new TileXY(x-1,y));
		}
		if (x < visited[0].length - 1){
			adjacentCells.add(new TileXY(x+1,y));
		}
		if (y>0){
			adjacentCells.add(new TileXY(x,y-1));
		}
		if (y < visited.length - 1){
			adjacentCells.add(new TileXY(x,y+1));
		}
		
		Collections.shuffle(adjacentCells);
		int numVisited = 0;

		for (TileXY d: adjacentCells){

			if (!visited[d.y][d.x]){
				++numVisited;
				int deltaX = x - d.x;
				int deltaY = y - d.y;
				
				maze[y*2 + 1 - deltaY][x*2 + 1 - deltaX] = false;
				generateNext(d.x, d.y, maze, visited, deadEnds);
			}
		}
		if (numVisited == 0){
			deadEnds.add(new TileXY(x,y));
			//entities.add(new Entity(torchType));
			entities.lastElement().x = 64 * x + 40;
			entities.lastElement().y = 64 * y + 40;

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
}
