package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MoveFolder {

	public static int move(File repository, File source, File destination) throws IOException {
		if (!(repository.isDirectory() && source.isDirectory() && destination.isDirectory())) {
			return -1;
		}
		if (destination.exists() && destination.list().length > 0) {
			destination = new File(destination, source.getName());
		}
		if (!GitUtils.isChecked()) {
			return -1;
		}
		return moveDirectory(repository, source, destination);
	}

	private static int moveDirectory(final File repository, final File source, final File destination)
			throws IOException {
		if (!(destination.exists() || destination.mkdirs())) {
			throw new IOException("Fail on mkdirs: " + destination);
		}
		int count = 0;
		for (File file : source.listFiles()) {
			if (file.isDirectory()) {
				count += moveDirectory(repository, file, new File(destination, file.getName()));
			} else if (file.isFile()) {
				moveFile(repository, file, new File(destination, file.getName()));
				count++;
			}
		}
		if (!source.delete()) {
			throw new IOException("Fail on delete: " + source);
		}
		return count;
	}

	private static void moveFile(final File repository, final File source, final File destination) throws IOException {
		if (GitUtils.move(repository, source, destination)) {
			GUIUtils.info("GitMove ...: " + destination);
			return;
		}
		Files.move(source.toPath(), destination.toPath(), StandardCopyOption.ATOMIC_MOVE);
		GUIUtils.info("unGitMove .: " + destination);
	}
}
