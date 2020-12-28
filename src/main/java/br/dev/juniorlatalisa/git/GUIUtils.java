package br.dev.juniorlatalisa.git;

import java.awt.Frame;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;

public final class GUIUtils {

	private static JFrame frame = null;
	private static JFileChooser fileChooser = null;

	public static void dispose() {
		if (frame != null) {
			frame.dispose();
		}
	}

	private static Frame getFrame() {
		if (frame == null) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Throwable t) {
				Main.LOGGER.log(Level.WARNING, "Falha ao atribuir LookAndFeel", t);
			}
			frame = new JFrame();
		}
		return frame;
	}

	private static JFileChooser getFileChooser(File currentDirectory, int mode) {
		if (fileChooser == null) {
			getFrame();
			fileChooser = new JFileChooser();
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		}
		fileChooser.setCurrentDirectory(currentDirectory);
		fileChooser.setFileSelectionMode(mode);
		return fileChooser;
	}

	public static File selectFile(String approveButtonText) {
		return selectFile(new File(".").toPath().getRoot().toFile(), JFileChooser.FILES_ONLY, approveButtonText);
	}

	public static File selectDirectory(File currentDirectory, String approveButtonText) {
		return selectFile(currentDirectory, JFileChooser.DIRECTORIES_ONLY, approveButtonText);
	}

	private static File selectFile(File currentDirectory, int mode, String approveButtonText) {
		if (JFileChooser.APPROVE_OPTION == getFileChooser(currentDirectory, mode).showDialog(frame,
				approveButtonText)) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	public static void show(Throwable throwable) {
		// TODO Auto-generated method stub
		System.err.println(throwable);
	}

	public static void show(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}
	
	public static void info(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}
}
