package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	protected static final Logger LOGGER = Logger.getLogger("GitMoveFolder");

	public static void main(String[] args) {
		printProperties();
		LOGGER.info("Carregando propriedades...");
		try {
			PropertiesUtils.load();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Falha ao carregar as propriedades", e);
			return;
		}
		checkProperties(args);
		try {
			if (PropertiesUtils.GIT.getValue() == null || PropertiesUtils.GIT.getValue().isEmpty()) {
				setGitValue();
			} else {
				checkGitValue();
			}
			if (!GitUtils.isChecked()) {
				return;
			}
			checkFolders();
		} finally {
			GUIUtils.dispose();
		}
		try {
			PropertiesUtils.store();
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Falha ao gravar as propriedades", e);
		}
	}

	private static void checkProperties(String[] args) {
		String key, value;
		PropertiesUtils property;
		PropertiesUtils.DESTINATION.setValue(null);
		PropertiesUtils.SOURCE.setValue(null);
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
	}

	private static void checkFolders() {
		String folder = PropertiesUtils.SOURCE.getValue();
		File repository, source, destination;
		if (folder == null || folder.isEmpty()) {
			repository = new File(
					((folder = PropertiesUtils.REPOSITORY.getValue()) == null || folder.isEmpty()) ? "." : folder);
			if ((source = GUIUtils.selectDirectory(repository, "Origem")) == null) {
				return;
			}
			PropertiesUtils.SOURCE.setValue(source.getAbsolutePath());
		} else if (!(source = new File(folder)).exists()) {
			GUIUtils.warning("Diretório de origem não encontrado: " + folder);
			return;
		}
		try {
			if ((repository = GitUtils.getRepository(source)) == null) {
				GUIUtils.warning("Diretório de origem não pertence a um repositório: " + source);
				return;
			}
			PropertiesUtils.REPOSITORY.setValue(repository.getAbsolutePath());
			folder = PropertiesUtils.DESTINATION.getValue();
			if (folder == null || folder.isEmpty()) {
				if ((destination = GUIUtils.selectDirectory(repository, "Destino")) == null) {
					return;
				}
				PropertiesUtils.DESTINATION.setValue(destination.getAbsolutePath());
			} else if (!(destination = new File(folder)).exists()) {
				GUIUtils.warning("Diretório de destino não encontrado: " + folder);
				return;
			}
			if (!destination.getAbsolutePath().startsWith(repository.getAbsolutePath())) {
				GUIUtils.warning("O destino não pertence ao repositório.");
				return;
			}
			GUIUtils.info("Iniciando...");
			LocalDateTime start = LocalDateTime.now();
			GUIUtils.show(String.format("Foram %d arquivo(s) movido(s) em %d segundo(s)", //
					MoveFolder.move(repository, source, destination), //
					start.until(LocalDateTime.now(), ChronoUnit.SECONDS)));
		} catch (Throwable t) {
			GUIUtils.show(t);
		}
	}

	private static void checkGitValue() {
		LOGGER.info("Verificando executável do Git...");
		File git = new File(PropertiesUtils.GIT.getValue());
		try {
			if (git.exists()) {
				if (GitUtils.checkCommand(git)) {
					LOGGER.info("Carregando executável do Git: " + git);
					return;
				}
			}
			PropertiesUtils.GIT.setValue(null);
			setGitValue();
		} catch (Throwable t) {
			GUIUtils.show(t);
		}
	}

	private static void setGitValue() {
		LOGGER.info("Carregando executável do Git...");
		File git = GUIUtils.selectFile("Git");
		try {
			if (GitUtils.checkCommand(git)) {
				PropertiesUtils.GIT.setValue(git.getAbsolutePath());
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
