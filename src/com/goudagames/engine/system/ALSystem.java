package com.goudagames.engine.system;

import java.io.BufferedInputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import com.goudagames.engine.logging.Log;

public class ALSystem {
	
	public static boolean MUTED = false;
	public static float SOUND_VOLUME = 1f;
	public static float MUSIC_VOLUME = 1f;
	
	public static int SOUNDS;
	
	private static HashMap<String, Integer> ids = new HashMap<String, Integer>();
	private static ArrayList<String> paths = new ArrayList<String>();
	
	private static boolean initiated;
	
	private static IntBuffer buffers, sources;
	
	private static FloatBuffer srcPos, srcVel;
	private static FloatBuffer lisPos = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
	private static FloatBuffer lisVel = BufferUtils.createFloatBuffer(3).put(new float[] {0f, 0f, 0f});
	private static FloatBuffer lisOri = BufferUtils.createFloatBuffer(6).put(new float[] {0f, 0f, -1f, 0f, 1f, 0f});
	
	public static void load(String path, String name) {
		
		if (initiated) {
			
			Log.log(Level.WARNING, "Cannot load audio files after Sound System initialization.");
			return;
		}
		
		String file = path.substring(path.lastIndexOf("/") == -1 ? 0: path.lastIndexOf("/") + 1);
		
		if (!file.endsWith(".wav")) {
			
			return;
		}
		
		ids.put(name, SOUNDS);
		paths.add(SOUNDS++, path);
		
		Log.log("Loaded audio with name " + name);
	}
	
	public static void initAL() {
		
		Log.log("Initiating OpenAL Sound System...");
		
		try {
			
			AL.create();
		}
		catch (Exception ex) {
			
			ex.printStackTrace();
			return;
		}
		
		lisPos.flip();
		lisVel.flip();
		lisOri.flip();
		
		buffers = BufferUtils.createIntBuffer(SOUNDS);
		sources = BufferUtils.createIntBuffer(SOUNDS);
		srcPos = BufferUtils.createFloatBuffer(SOUNDS * 3);
		srcVel = BufferUtils.createFloatBuffer(SOUNDS * 3);
		
		AL10.alGenBuffers(buffers);
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {
			
			Log.log(Level.SEVERE, "Failed to initialize Sound System on point 0!");
			return;
		}
		
		for (int i = 0; i < SOUNDS; i++) {
			
			String path = paths.get(i);
			
			try {
				
				WaveData wave = WaveData.create(new BufferedInputStream(ClassLoader.getSystemResourceAsStream(path)));
				AL10.alBufferData(buffers.get(i), wave.format, wave.data, wave.samplerate);
				wave.dispose();
			}
			catch (Exception ex) {
				
				ex.printStackTrace();
			}
			
			if (AL10.alGetError() != AL10.AL_NO_ERROR) {
				
				Log.log(Level.SEVERE, "Failed to initialize Sound System on point 1!");
				return;
			}
		}
		
		AL10.alGenSources(sources);
		
		for (int i = 0; i < SOUNDS; i++) {
			
			AL10.alSourcei(sources.get(i), AL10.AL_BUFFER, buffers.get(i));
			AL10.alSourcef(sources.get(i), AL10.AL_PITCH, 1.0f);
			AL10.alSource(sources.get(i), AL10.AL_POSITION, (FloatBuffer) srcPos.position(i * 3));
			AL10.alSource(sources.get(i), AL10.AL_VELOCITY, (FloatBuffer) srcVel.position(i * 3));
			
			if (AL10.alGetError() != AL10.AL_NO_ERROR) {
				
				Log.log(Level.SEVERE, "Failed to initialize Sound System on point 2!");
				return;
			}
		}
		
		AL10.alListener(AL10.AL_POSITION, lisPos);
		AL10.alListener(AL10.AL_VELOCITY, lisVel);
		AL10.alListener(AL10.AL_ORIENTATION, lisOri);
		
		initiated = true;
	}
	
	public static void play(String sound) {
		
		play(sound, 1f);
	}
	
	public static boolean isPlaying(String sound) {
		
		int id = ids.get(sound);
		
		int state = AL10.alGetSourcei(sources.get(id), AL10.AL_SOURCE_STATE);
		
		return state == AL10.AL_PLAYING;
	}
	
	public static void play(String sound, float gain) {
		
		if (!initiated || MUTED) return;
		
		if (isPlaying(sound)) return;
		
		int id = ids.get(sound);
		
		AL10.alSourcei(sources.get(id), AL10.AL_LOOPING, AL10.AL_FALSE);
		AL10.alSourcef(sources.get(id), AL10.AL_GAIN, gain * SOUND_VOLUME);
		AL10.alSourcePlay(sources.get(id));
	}
	
	public static void stop(String sound) {
		
		if (!initiated) return;
		
		AL10.alSourceStop(sources.get(ids.get(sound)));
	}
	
	public static void stopAll() {
		
		if (!initiated) return;
		
		for (int i = 0; i < sources.capacity(); i++) {
			
			AL10.alSourceStop(sources.get(i));
		}
	}
	
	public static void loop(String sound) {
		
		loop(sound, .15f);
	}
	
	public static void loop(String sound, float gain) {
		
		if (!initiated || MUTED) return;
		
		int id = ids.get(sound);
		
		AL10.alSourcei(sources.get(id), AL10.AL_LOOPING, AL10.AL_TRUE);
		AL10.alSourcef(sources.get(id), AL10.AL_GAIN, gain * MUSIC_VOLUME);
		AL10.alSourcePlay(sources.get(id));
	}
	
	public static void destroy() {
		
		AL10.alDeleteBuffers(buffers);
		AL10.alDeleteSources(sources);
		
		AL.destroy();
	}
}
