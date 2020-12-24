package br.dev.juniorlatalisa.git;

import java.awt.Frame;
import java.io.File;

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
				t.printStackTrace();
			}
			frame = new JFrame();
		}
		return frame;
	}

	private static JFileChooser getFileChooser(int mode) {
		if (fileChooser == null) {
			getFrame();
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(
					(PropertiesUtils.REPOSITORY.getValue() == null || PropertiesUtils.REPOSITORY.getValue().isEmpty())
							? "."
							: PropertiesUtils.REPOSITORY.getValue()));
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		}
		fileChooser.setFileSelectionMode(mode);
		return fileChooser;
	}

	public static File selectFile(String approveButtonText) {
		if (JFileChooser.APPROVE_OPTION == getFileChooser(JFileChooser.FILES_ONLY).showDialog(frame,
				approveButtonText)) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	public static void show(Throwable throwable) {

	}
}
