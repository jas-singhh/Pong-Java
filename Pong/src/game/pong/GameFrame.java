package game.pong;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	public static final int WIDTH = 700;
	public static final int HEIGHT = 600;
	
	GameFrame() {
		setupWindow();
		this.add(new GamePanel());
		this.pack();
		this.setVisible(true);// Important to add this at the end to make the window visible
	}
	
	private void setupWindow() {
		this.setTitle("Pong");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		centerWindow();
	}
	
	private void centerWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width / 2) - (this.getWidth() / 2),
				(screenSize.height / 2) - (this.getHeight() / 2));
	}
	
}