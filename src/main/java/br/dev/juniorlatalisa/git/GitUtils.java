package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class GitUtils {

	private static String git = null;

	public static boolean isChecked() {
		return !(git == null || git.isEmpty());
	}

	private static int execute(File directory, String... command) throws IOException {
		Process retorno = new ProcessBuilder(command)//
				.directory(directory)//
				.redirectOutput(Redirect.INHERIT)//
				.redirectError(Redirect.INHERIT)//
				.start();
		while (retorno.isAlive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return retorno.exitValue();
	}

	public static boolean checkCommand(File git) throws IOException {
		if (0 == execute(git.getParentFile(), git.getName(), "--version")) {
			GitUtils.git = git.getAbsolutePath();
			return true;
		}
		return false;
	}

	private static final FilenameFilter filter = (dir, name) -> ".git".equals(name);

	public static File getRepository(File dir) throws IOException {
		if (dir == null || !dir.isDirectory()) {
			return null;
		}
		File[] files = dir.listFiles(filter);
		if (!(files == null || files.length == 0)) {
			return (0 == execute(dir, git, "status")) ? dir : null;
		}
		return getRepository(dir.getParentFile());
	}

	public static boolean move(File repository, File source, File destination) throws IOException {
		return 0 == execute(repository, git, "mv", source.getAbsolutePath(), destination.getAbsolutePath());
	}
}
