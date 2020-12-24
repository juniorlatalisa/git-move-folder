package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class GitUtils {

	private static File git = null;

	public static boolean isAtivo() {
		return git != null && git.exists();
	}

	private static int execute(File directory, String... command) throws IOException, InterruptedException {
		Process retorno = new ProcessBuilder(command)//
				.directory(directory)//
				.redirectOutput(Redirect.INHERIT)//
				.redirectError(Redirect.INHERIT)//
				.start();
		while (retorno.isAlive()) {
			Thread.sleep(100);
		}
		return retorno.exitValue();
	}

	public static boolean checkGitCommand(File git) throws IOException, InterruptedException {
		if (0 == execute(git.getParentFile(), git.getName(), "--version")) {
			GitUtils.git = git;
			return true;
		}
		return false;
	}

	private static final FilenameFilter filter = (dir, name) -> ".git".equals(name);

	public static File getRepository(File dir) throws IOException, InterruptedException {
		if (dir == null || !dir.isDirectory()) {
			return null;
		}
		File[] files = dir.listFiles(filter);
		if (!(files == null || files.length == 0)) {
			return (0 == execute(dir, git.getAbsolutePath(), "status")) ? dir : null;
		}
		return getRepository(dir.getParentFile());
	}
}
