package view;

import java.util.Queue;

import javax.swing.JFrame;

import controller.App;
import model.User;

public class Window extends JFrame {
	private WelcomeScreen welcomeScreen;
	
	public Window(App app, WelcomeScreen welcomeScreen) {
		this.welcomeScreen = welcomeScreen;

		setupFrame();	
	}
	
	// Sets the content pane and makes it visible
	private void setupFrame() {
		this.setTitle("EmpStats");
		this.setContentPane(welcomeScreen);
        this.setSize(500, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
