package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final Logger LOGGER = Logger.getLogger("GitMoveFolder");

	public static void main(String[] args) {
		printProperties();
		LOGGER.info("Carregando propriedades...");
		try {
			PropertiesUtils.load();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Falha ao carregar as propriedades", e);
			return;
		}
		String key, value;
		PropertiesUtils property;
		for (int i = 0; i < args.length; i += 2) {
			if (((key = args[i]) == null) || (key.length() < 3)) {
				continue;
			}
			if (!key.startsWith("--")) {
				continue;
			}
			if ((property = PropertiesUtils.VALUES.get(key.substring(2))) == null) {
				continue;
			}
			if (((value = args[i + 1]) == null) || (value.isEmpty())) {
				continue;
			}
			property.setValue(value);
		}
		try {
			if (PropertiesUtils.GIT.getValue() == null || PropertiesUtils.GIT.getValue().isEmpty()) {
				setGitValue();
			} else {
				checkGitValue();
			}
		} finally {
			GUIUtils.dispose();
		}
	}

	private static void checkGitValue() {
		LOGGER.info("Verificando executável do Git...");
		File git = new File(PropertiesUtils.GIT.getValue());
		try {
			if (git.exists()) {
				if (GitUtils.checkGitCommand(git)) {
					LOGGER.info("Carregando executável do Git: " + git);
					return;
				}
			}
			PropertiesUtils.GIT.setValue(null);
			PropertiesUtils.store();
			setGitValue();
		} catch (Throwable t) {
			GUIUtils.show(t);
		}

	}

	private static void setGitValue() {
		LOGGER.info("Carregando executável do Git...");
		File git = GUIUtils.selectFile("Git");
		try {
			if (GitUtils.checkGitCommand(git)) {
				PropertiesUtils.GIT.setValue(git.getAbsolutePath());
				PropertiesUtils.store();
				LOGGER.info("Carregando executável do Git: " + git);
			}
		} catch (Throwable t) {
			GUIUtils.show(t);
		}
	}

	private static void printProperties() {
		StringBuilder info = new StringBuilder("Propriedades");
		PropertiesUtils.VALUES.keySet().forEach(key -> info.append("\n\t--").append(key));
		LOGGER.info(info.toString());
	}
}
