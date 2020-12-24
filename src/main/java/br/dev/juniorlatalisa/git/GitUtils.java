package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

public class GitUtils {

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
		return 0 == execute(git.getParentFile(), git.getName(), "--version");
	}

//	public static void main(String[] args) throws IOException, InterruptedException {
//		System.out.println(checkGitCommand(new File("C:\\Program Files\\Git\\cmd\\git.exe")));
//	}
}
