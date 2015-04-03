package com.aezart.darkmaze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DarkMaze extends JFrame{
	
	static final int FULLBRIGHT = 0;
	static final int ALL_TORCHES = 1;
	static final int ENEMY_TORCHES = 2;
	static final int PLAYER_TORCH = 3;
	static final int FULLDARK = 4;
	int displayMode = ALL_TORCHES;
	int[] directions = new int[5];
	Entity[] entities = new Entity[5];
	BufferedImage mapSurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage displaySurface = new BufferedImage(608, 480, BufferedImage.TYPE_3BYTE_BGR);
	BufferedImage glowingEyes;
	BufferedImage light;
	BufferedImage lightsurface = new BufferedImage(608,480,BufferedImage.TYPE_4BYTE_ABGR);
	BufferedImage wallshadow;
	boolean showmap = false;
	Random rng = new Random();
	boolean[][] maze = new boolean[15][19];
	boolean[][] coins = new boolean[7][9];
	HashMap<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
	Entity knight = new Entity("littleknight.png");
	BufferedImage tileset;
	JPanel screen = new JPanel(){
		@Override
		protected void paintComponent(Graphics g) {
			
			Graphics dg = displaySurface.getGraphics();
			super.paintComponent(g);
			dg.setColor(Color.black);
			Graphics2D lg = (Graphics2D)lightsurface.getGraphics();
			lg.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
			lg.fillRect(0, 0, 608, 480);
			lg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			
			dg.drawImage(mapSurface, 0, 0, null);
			
			if (displayMode == FULLBRIGHT ||
					displayMode == ALL_TORCHES ||
					displayMode == PLAYER_TORCH){
				lg.drawImage(light, knight.x-28, knight.y-28, null);
			}
			dg.drawImage(knight.sprite, knight.x, knight.y, null);
			for (int i = 1; i < entities.length; ++i){
				dg.drawImage(entities[i].sprite, entities[i].x, entities[i].y, null);
				if (displayMode == FULLBRIGHT || 
						displayMode == ALL_TORCHES || 
						displayMode == ENEMY_TORCHES){
					lg.drawImage(light, entities[i].x-28, entities[i].y-28, null);
				}
			}

			for (int i = 0; i < coins.length; ++i){
				for (int k = 0; k < coins[0].length; ++k){
					if (coins[i][k]){
						dg.drawImage(tileset, k*64+32, i*64+32, k*64+64, i*64+64, 32, 0, 48, 16, null);
					}
				}
			}
			if (displayMode == FULLBRIGHT){
				lg.fillRect(0, 0, 608, 480);
			}
			lg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
			lg.drawImage(displaySurface, 0, 0, null);
			
			lg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			
			Graphics2D g2D = (Graphics2D) g;
			
			g2D.setColor(Color.black);
			g2D.fillRect(0, 0, 608, 480);
			g2D.drawImage(lightsurface, 0, 0, null);
			for (int i = 1; i < entities.length; ++i){
				g.drawImage(glowingEyes, entities[i].x, entities[i].y, null);
			}
		}
	};
	
	public DarkMaze(){
		entities[0] = knight;
		for (int i = 1; i < entities.length; ++i){
			entities[i] = new Entity("littlecloak.png");
		}
		entities[1].x = 40;
		entities[1].y = 40;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		generateMaze(maze);
		
		final Graphics sg = mapSurface.getGraphics();
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
					
				}
				if (arg0.getKeyCode() == KeyEvent.VK_Z){
					generateMaze(maze);
					paintBackground(sg);
					for (int i = 1; i < entities.length; ++i){
						entities[i].x = 40;
						entities[i].y = 40;
					}
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
		screen.setPreferredSize(new Dimension(608,480));
		add(screen);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
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
					
					for (int i = 1; i < darkMaze.entities.length; ++i){
						int nextX = darkMaze.entities[i].x;
						int nextY = darkMaze.entities[i].y;
						switch (darkMaze.directions[i]){
						case 0:
							nextX += 2;
							break;
						case 1:
							nextY -= 2;
							break;
						case 2:
							nextX -= 2;
							break;
						case 3:
							nextY += 2;
							break;
						}
						while (darkMaze.maze[(nextY + 16)/32][(nextX + 16)/32] || 
								darkMaze.maze[nextY/32][nextX/32]){
							darkMaze.directions[i] = darkMaze.rng.nextInt(4);
							nextX = darkMaze.entities[i].x;
							nextY = darkMaze.entities[i].y;
							switch (darkMaze.directions[i]){
							case 0:
								nextX += 2;
								break;
							case 1:
								nextY -= 2;
								break;
							case 2:
								nextX -= 2;
								break;
							case 3:
								nextY += 2;
								break;
							}
							
						}
						
						darkMaze.entities[i].x = nextX;
						darkMaze.entities[i].y = nextY;
					}
						SwingUtilities.invokeLater(new Runnable(){

							@Override
							public void run() {
								darkMaze.repaint();
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
		
		generateNext(firstX, firstY, maze, visited);
		
		for (int i = 0; i < 18; ++i){
			int passageX = rng.nextInt(visited[0].length - 1);
			int passageY = rng.nextInt(visited.length - 1);
			
			if (rng.nextBoolean()){
				maze[2*passageY+1][2*passageX+2] = false;
			}else{
				maze[2*passageY+2][2*passageX+1] = false;
			}
		}
	}
	
	void generateNext(int x, int y, boolean[][] maze, boolean[][] visited){
		visited[y][x] = true;
		ArrayList<Dimension> adjacentCells = new ArrayList<Dimension>();
		if (x > 0){
			adjacentCells.add(new Dimension(x-1,y));
		}
		if (x < visited[0].length - 1){
			adjacentCells.add(new Dimension(x+1,y));
		}
		if (y>0){
			adjacentCells.add(new Dimension(x,y-1));
		}
		if (y < visited.length - 1){
			adjacentCells.add(new Dimension(x,y+1));
		}
		
		Collections.shuffle(adjacentCells);
		for (Dimension d: adjacentCells){
			if (!visited[d.height][d.width]){
				int deltaX = x - d.width;
				int deltaY = y - d.height;
				
				maze[y*2 + 1 - deltaY][x*2 + 1 - deltaX] = false;
				generateNext(d.width, d.height, maze, visited);
			}
		}
		
	}
	
	boolean lineOfSight(int x1, int y1, int x2, int y2, boolean[][] maze){
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
