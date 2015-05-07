package com.aezart.darkmaze;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

public class AudioPlayer {
	public final Clip COIN;
	public final Clip FOOTSTEPS;
	public final Clip FIRECRACKER;
	Player bgmPlayer;
	InputStream bgmIS;
	
	public AudioPlayer (DarkMaze game){
			COIN = createSoundClip("resources/audio/coin.wav");
			FOOTSTEPS = createSoundClip("resources/audio/footsteps.wav");
			FIRECRACKER = createSoundClip("resources/audio/firecracker.wav");
			try{
				((FloatControl) FOOTSTEPS.getControl(FloatControl.Type.MASTER_GAIN)).setValue(2);
				
				((FloatControl) COIN.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-5);
			} catch (IllegalArgumentException e){
				((FloatControl) COIN.getControl(FloatControl.Type.VOLUME)).setValue(50000);
				System.out.println("MASTER_GAIN not supported, using VOLUME");
			}

	}
	
	public void play(Clip c){
		c.stop();
		c.setFramePosition(0);
		c.start();
	}
	
	//If an exception is thrown, clip will be null
	public Clip createSoundClip(String filePath){
		Clip clip;
		try{
			BufferedInputStream bis = new BufferedInputStream(this.getClass().getResourceAsStream(filePath));
			AudioInputStream ais = AudioSystem.getAudioInputStream(bis);
			AudioFormat af = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, af);
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(ais);
		} catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
			//e.printStackTrace();
			System.out.println("Couldn't load " + filePath + ":");
			e.printStackTrace();
			clip = null;
			
		}
		return clip;
	}
}
