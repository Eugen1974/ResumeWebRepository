package com.urise.webapp.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class ResumeLogger {

	private static final ResumeLogger INSTANCE = new ResumeLogger();

	private final Logger logger;

	private ResumeLogger() {
		try {
			logger = Logger.getLogger(ResumeLogger.class.getName());
			logger.setLevel(Level.ALL);
			FileHandler handler = new FileHandler(
					Config.getInstance().getLogDir() + "/" + Config.getInstance().getLogFileName(), 1000 * 1024, 5,
					true);
			handler.setEncoding("UTF-8");
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (SecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ResumeLogger getInstance() {
		return INSTANCE;
	}

	public Logger getLogger() {
		return logger;
	}
}
