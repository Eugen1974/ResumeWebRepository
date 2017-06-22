package com.urise.webapp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Config {

	private static final Config INSTANCE = new Config();

	private final String dbDriverType;
	private final String dbServerName;
	private final String databaseName;
	private final int dbPortNumber;
	private final String dbUser;
	private final String dbPassword;
	private final String dbUrl;
	private final boolean ssl;
	private final String sslFactory;
	private final String storageDir;
	private final String logDir;
	private final String logFileName;

	private Config() {
		dbDriverType = null;
		dbServerName = "ec2-54-247-92-185.eu-west-1.compute.amazonaws.com";
		databaseName = "deose5g9bqotv8";
		dbPortNumber = 5432;
		dbUser = "anatjptksphrvn";
		dbPassword = "fa5ea8301caf0c10c793d5646fa9b597c9284dcbfec81c615d5249ed680e7270";
		dbUrl = "jdbc:postgresql://ec2-54-247-92-185.eu-west-1.compute.amazonaws.com:5432/deose5g9bqotv8";
		ssl = true;
		sslFactory = "org.postgresql.ssl.NonValidatingFactory";
		storageDir = "storage";
		logDir = "log";
		logFileName = "log_resume";
		try {
			Path pathStorageDir = Paths.get(storageDir);
			if (Files.notExists(pathStorageDir)) {
				Files.createDirectory(pathStorageDir);
			}
			Path pathLogDir = Paths.get(logDir);
			if (Files.notExists(pathLogDir)) {
				Files.createDirectory(pathLogDir);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
/*
	private Config() {
		try (Reader reader = new BufferedReader(new InputStreamReader(
				Class.class.getResourceAsStream("/config/resumes.properties"), Charset.forName("UTF-8")))) {
			Properties properties = new Properties();
			properties.load(reader);
			dbDriverType = properties.getProperty("db.driverType");
			dbServerName = properties.getProperty("db.serverName");
			databaseName = properties.getProperty("db.databaseName");
			dbPortNumber = Integer.valueOf(properties.getProperty("db.portNumber"));
			dbUser = properties.getProperty("db.user");
			dbPassword = properties.getProperty("db.password");
			dbUrl = properties.getProperty("db.url");
			ssl = Boolean.valueOf(properties.getProperty("ssl"));
			sslFactory = properties.getProperty("sslFactory");
			storageDir = properties.getProperty("storage.dir");
			logDir = properties.getProperty("log.dir");
			logFileName = properties.getProperty("log.fileName");

			Path pathStorageDir = Paths.get(storageDir);
			if (Files.notExists(pathStorageDir)) {
				Files.createDirectory(pathStorageDir);
			}
			Path pathLogDir = Paths.get(logDir);
			if (Files.notExists(pathLogDir)) {
				Files.createDirectory(pathLogDir);
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
*/
	public static Config getInstance() {
		return INSTANCE;
	}

	public String getDbDriverType() {
		return dbDriverType;
	}

	public String getDbServerName() {
		return dbServerName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public int getDbPortNumber() {
		return dbPortNumber;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getStorageDir() {
		return storageDir;
	}

	public String getLogDir() {
		return logDir;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public boolean getSsl() {
		return ssl;
	}

	public String getSslFactory() {
		return sslFactory;
	}
}
