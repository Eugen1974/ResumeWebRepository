package com.urise.webapp.examples.others;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Objects;

import com.urise.webapp.util.DateUtil;

public class MainFile {
	public static final File START_DIRECTORY = new File("./src");

	public static void main(String[] args) throws IOException {
		try (PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream("d:\\MainFile.txt")))) {
			System.setOut(out);

			System.out.println(getCurrentTime() + "\n\n" + START_DIRECTORY.getCanonicalPath()
					+ "\n-----------------------------------------");

			showFiles(START_DIRECTORY, new String());

			System.out.println("\n" + getCurrentTime());
		}
	}

	public static void showFiles(File startDirectory, String offSet) throws IOException {
		if (!startDirectory.isDirectory()) {
			throw new IllegalArgumentException(startDirectory.getCanonicalPath() + " is not directory !");
		}
		File[] arrayFiles1 = startDirectory.listFiles();
		if (Objects.nonNull(arrayFiles1)) {
			for (File file : arrayFiles1) {
				// First show files and then inner folders
				if (file.isDirectory()) {
					System.out.println(offSet + file.getName());
					File[] arrayFiles2 = file.listFiles();
					if (Objects.nonNull(arrayFiles2)) {
						for (File f : arrayFiles2) {
							if (f.isFile()) {
								System.out.println(offSet + " |-> " + f.getName());
							}
						}
					}
					showFiles(file, offSet + " ");
				}
			}
		}
	}

	public static String getCurrentTime() {
		return LocalDateTime.now().format(DateUtil.PATTERN_5);
	}
}