package com.goudagames.engine.logging;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.goudagames.engine.start.Engine;

public class LogFormatter extends Formatter {
	
	public LogFormatter() {
		
		super();
	}

	@Override
	public String format(LogRecord arg0) {
		
		String result = String.format("%02d-%02d-%02d %02d.%02d.%02d [%s - %s] %s \n", Calendar.getInstance().get(Calendar.DATE), Calendar.getInstance().get(Calendar.MONTH) + 1,
				Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND),
				(Engine.getCurrentStateName() != "" ? Engine.getCurrentStateName():"Engine"), arg0.getLevel().getName(), arg0.getMessage());
		return result;
	}
}