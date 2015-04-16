package com.goudagames.engine.logging;

import java.io.File;
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
	
	public static void destroy() {
		
		File logFile = new File(path);
		
		if (logFile.exists()) {
			logFile.delete();
		}
	}
	
	public static void init() {
	
		logger = Logger.getLogger("main");
		logger.setUseParentHandlers(false);
		
		logger.setLevel(Level.ALL);
		try {
			
			path = String.format("logs/log-%02d-%02d-%02d_%02d.%02d.%02d.txt", Calendar.getInstance().get(Calendar.DATE), Calendar.getInstance().get(Calendar.MONTH) + 1,
					Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND));
			
			File dir = new File("logs/");
			File file = new File(path);
			
			if (!file.exists()) {
				
				dir.mkdirs();
				file.createNewFile();
			}
			
			LogFormatter formatter = new LogFormatter();
			
			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.ALL);
			
			FileHandler fileHandler = new FileHandler(path);
			fileHandler.setLevel(Level.INFO);
			
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
		
		logger.log(level, msg);
	}
	
	public static void log(String msg) {
		
		log(Level.INFO, msg);
	}
}
