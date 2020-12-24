package br.dev.juniorlatalisa.git;

import java.awt.Frame;
import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MoveFolder {

	public static void main(String[] args) throws Throwable {
		File source, destination, repository;
		try {
			int index = 0;
			String folder;
			// repository
			if (((folder = (args.length > index) ? args[index] : getFolder("repositório")) == null)
					|| (folder.isEmpty())) {
				return;
			}
			if (!(repository = new File(folder)).isDirectory()) {
				throw new IllegalArgumentException(folder + " not a dir");
			}
			index++;
			if (fileChooser != null) {
				fileChooser.setCurrentDirectory(repository);
			}
			// source
			if (((folder = (args.length > index) ? args[index] : getFolder("origem")) == null) || (folder.isEmpty())) {
				return;
			}
			if (!(source = new File(folder)).isDirectory()) {
				throw new IllegalArgumentException(folder + " not a dir");
			}
			index++;
			if (fileChooser != null) {
				fileChooser.setCurrentDirectory(repository);
			}
			// destination
			if (((folder = (args.length > index) ? args[index] : getFolder("destino")) == null) || (folder.isEmpty())) {
				return;
			}
			if (!(destination = new File(folder)).isDirectory()) {
				throw new IllegalArgumentException(folder + " not a dir");
			}
		} finally {
			if (frame != null) {
				frame.dispose();
			}
		}
		try {
			move(repository, source, destination);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void move(File repository, File source, File destination) throws Throwable {
		if (source.isFile()) {
			String command = String.format("\"C:/Program Files/Git/cmd/git.exe\" mv \"%s\" \"%s\"",
					source.getAbsolutePath(), new File(destination, source.getName()).getAbsolutePath());
			Process retorno = new ProcessBuilder(command)//
					.directory(repository)//
					.redirectOutput(Redirect.INHERIT)//
					.redirectError(Redirect.INHERIT)//
					.start();
			System.out.print(source.getName() + ": ");
			while (retorno.isAlive()) {
				System.out.print(". ");
				Thread.sleep(100);
			}
			System.out.println(retorno.exitValue());
			return;
		}
		if (!source.isDirectory()) {
			return;
		}
		destination = new File(destination, source.getName());
		if (!destination.exists()) {
			destination.mkdirs();
		}
		for (File file : source.listFiles()) {
			move(repository, file, destination);
		}
		source.delete();
	}

	private static JFrame frame = null;
	private static JFileChooser fileChooser = null;

	private static Frame getFrame() {
		if (frame == null) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Throwable t) {
				t.printStackTrace();
			}
			frame = new JFrame();
		}
		return frame;
	}

	private static String getFolder(String alvo) {
		if (fileChooser == null) {
			getFrame();
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		}
		if (JFileChooser.APPROVE_OPTION == fileChooser.showDialog(frame,
				String.format("Selecione o diretório de %s", alvo))) {
			return (fileChooser.getSelectedFile() == null) ? null : fileChooser.getSelectedFile().getPath();
		}
		return null;
	}
}
