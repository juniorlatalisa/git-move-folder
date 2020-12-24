package br.dev.juniorlatalisa.git;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public enum PropertiesUtils {

	GIT, REPOSITORY, SOURCE, DESTINATION;

	public static final Map<String, PropertiesUtils> VALUES = Collections.unmodifiableMap(
			Arrays.asList(PropertiesUtils.values()).stream().collect(Collectors.toMap(PropertiesUtils::name, p -> p)));

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void store() throws IOException {
		Properties propriedades = new Properties();
		VALUES.values().stream().filter(p -> !(p.value == null || p.value.isEmpty()))
				.forEach(p -> propriedades.setProperty(p.name(), p.value));
		File dir = getDirectory();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(getProperties(dir));
		try {
			propriedades.storeToXML(out, LocalDateTime.now().toString());
		} finally {
			out.close();
		}
	}

	public static void load() throws IOException {
		File dir = getDirectory();
		if (!dir.exists()) {
			return;
		}
		File config = getProperties(dir);
		if (!config.exists()) {
			return;
		}
		Properties propriedades = new Properties();
		FileInputStream in = new FileInputStream(config);
		try {
			propriedades.loadFromXML(in);
		} finally {
			in.close();
		}
		propriedades.entrySet().forEach(entry -> VALUES.get(entry.getKey()).value = (String) entry.getValue());
	}

	private static File getProperties(File dir) {
		return new File(dir, "Properties.xml");
	}

	private static File getDirectory() {
		return new File(System.getProperty("user.home"), ".git-move.folder");
	}
}
