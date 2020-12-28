package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.IOException;

public class MoveFolder {

	public static void move(File repository, File source, File destination) throws IOException {
		if (!(repository.isDirectory() && source.isDirectory() && destination.isDirectory())) {
			return;
		}
		if (destination.exists() && destination.list().length > 0) {
			destination = new File(destination, source.getName());
		}
		if (!GitUtils.isAtivo()) {
			return;
		}
		moveDirectory(repository, source, destination);
	}

	private static void moveDirectory(final File repository, final File source, final File destination)
			throws IOException {
		if (!(destination.exists() || destination.mkdirs())) {
			throw new IOException("Fail on mkdirs: " + destination);
		}
		for (File file : source.listFiles()) {
			if (file.isDirectory()) {
				moveDirectory(repository, file, new File(destination, file.getName()));
			} else if (file.isFile()) {
				moveFile(repository, file, new File(destination, file.getName()));
			}
		}
	}

	private static void moveFile(final File repository, final File source, final File destination) throws IOException {
		if (GitUtils.move(repository, source, destination)) {
			return;
		}
		//TODO SE NAO MOVER
	}

}
