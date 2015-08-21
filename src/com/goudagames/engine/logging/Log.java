package com.goudagames.engine.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

	private static Logger logger = null;
	private static boolean initiated = false;
	
	private static String path;
	private static File tempFile;
	
	private static boolean save = false;
	
	public static void destroy() {
		
		if (save) {
			
			saveLog();
		}
	}
	
	@SuppressWarnings("resource")
	public static void saveLog() {
		
		try {
			File dir = new File("logs/");
			File file = new File(path);
			
			if (!file.exists()) {
				
				dir.mkdirs();
				file.createNewFile();
			}
			
			FileChannel src = new FileInputStream(tempFile).getChannel();
			FileChannel dest = new FileOutputStream(file).getChannel();
			dest.transferFrom(src, 0, src.size());
			
			src.close();
			dest.close();
		}
		catch (Exception ex) {
			
			ex.printStackTrace();
		}
	}
	
	public static void init() {
	
		logger = Logger.getLogger("main");
		logger.setUseParentHandlers(false);
		
		logger.setLevel(Level.ALL);
		try {
			
			path = String.format("logs/log-%02d-%02d-%02d_%02d.%02d.%02d.txt", Calendar.getInstance().get(Calendar.DATE), Calendar.getInstance().get(Calendar.MONTH) + 1,
					Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND));
			
			tempFile = File.createTempFile("gtopn-log", null);
			tempFile.deleteOnExit();
			
			LogFormatter formatter = new LogFormatter();
			
			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.ALL);
			
			FileHandler fileHandler = new FileHandler(tempFile.getPath());
			fileHandler.setLevel(Level.ALL);
			
			logger.addHandler(fileHandler);
			logger.addHandler(consoleHandler);
			
			for (Handler handler : logger.getHandlers()) {
				
				handler.setFormatter(formatter);
			}
			
		}catch (Exception ex) {
			
			ex.printStackTrace();
			System.err.println("Error creating logger!");
		}
		
		logger.info("Initiating logger...");
		initiated = true;
	}
	
	public static Logger getLogger() {
		
		return logger;
	}
	
	public static void log(Level level, String msg) {
		
		if (!initiated) return;
		
		if (level == Level.SEVERE) {
			save = true;
		}
		
		logger.log(level, msg);
	}
	
	public static void log(String msg) {
		
		log(Level.INFO, msg);
	}
}
