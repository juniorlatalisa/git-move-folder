package br.dev.juniorlatalisa.git;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public final class GUIUtils {

	private static Label label = null;
	private static Frame frame = null;
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
			frame = new Frame();
			frame.setTitle(Main.LOGGER.getName());
			frame.setUndecorated(true);
			frame.setAlwaysOnTop(true);
			frame.setLayout(new GridLayout(1, 1));
		}
		return frame;
	}

	public static Label getLabel() {
		if (label == null) {
			label = new Label(null);
			label.setBackground(frame.getBackground());
			label.setAlignment(Label.LEFT);
			label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));

			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) (screen.width * 0.8);
			int height = 80;
			frame.setBounds(screen.width - width, screen.height - height, width, height);

			frame.add(label);
			frame.setVisible(true);
		}
		return label;
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
		return selectFile(new File(System.getProperty("user.home")), JFileChooser.FILES_ONLY, approveButtonText);
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
		String message = (throwable.getMessage() == null || throwable.getMessage().isEmpty())
				? throwable.getClass().getSimpleName()
				: throwable.getMessage();
		if (frame == null) {
			Main.LOGGER.log(Level.SEVERE, message, throwable);
		} else {
			frame.setVisible(false);
			JOptionPane.showMessageDialog(frame, message, Main.LOGGER.getName(), JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void warning(String message) {
		show(message, true);
	}

	public static void show(String message) {
		show(message, false);
	}

	private static void show(String message, boolean warning) {
		if (frame != null) {
			frame.setVisible(false);
			JOptionPane.showMessageDialog(frame, message, Main.LOGGER.getName(),
					(warning) ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
		} else if (warning) {
			Main.LOGGER.warning(message);
		} else {
			Main.LOGGER.info(message);
		}
	}

	public static void info(String message) {
		if (frame == null) {
			Main.LOGGER.info(message);
		} else {
			getLabel().setText(message);
		}
	}
}
