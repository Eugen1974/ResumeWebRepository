package com.urise.webapp.examples.others;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainFilePath {

	public static final Path START_PATH = new File("./src").toPath();

	public static void main(String[] args) throws IOException {
		try (PrintStream out = new PrintStream(
				new BufferedOutputStream(new FileOutputStream("d:\\MainFilePath.txt")))) {
			System.setOut(out);

			System.out.println(MainFile.getCurrentTime() + "\n\n" + START_PATH.toFile().getCanonicalPath()
					+ "\n-----------------------------------------");

			showFiles(START_PATH, new String());

			System.out.println("\n" + MainFile.getCurrentTime());
		}
	}

	public static void showFiles(Path path, String offSet) {
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException(path + " is not directory !");
		}
		try {
			Files.list(path).forEach(path1 -> {
				// First show files and then inner folders
				if (Files.isDirectory(path1)) {
					System.out.println(offSet + path1.getFileName().toString());
					try {
						Files.list(path1).forEach(path2 -> {
							if (!Files.isDirectory(path2)) {
								System.out.println(offSet + " |-> " + path2.getFileName().toString());
							}
						});
					} catch (AccessDeniedException e) {
						System.out.println(e.getMessage());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					showFiles(path1, offSet + " ");
				}
			});
		} catch (AccessDeniedException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}