package view;

import java.awt.BorderLayout; 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.App;

public class LoadingScreen extends JPanel {
	private App app;
	private JTextField redirectText;
	private JLabel loadingCircle;
	private JPanel logoPanel;
	private JLabel logo;
	
	public LoadingScreen(App app) {
		this.app = app;
		
		// Initalising and setting the colour and fonts of components
		logo = new JLabel(new ImageIcon(getClass().getResource("/EEWEmblem.png")));
		loadingCircle = new JLabel(new ImageIcon(getClass().getResource("/LoadingCircle.gif")));
		logoPanel = new JPanel();
		
		
		setupLayout();
		setupPanel();
		setupListener();
	}
	
	private void setupLayout() {
		this.setLayout(new BorderLayout(0, 0));
	}
	
	private void setupPanel() {
		logoPanel.add(logo, BorderLayout.WEST);									
		this.add(loadingCircle, BorderLayout.CENTER);
		this.add(logoPanel, BorderLayout.NORTH);
	}
	
	private void setupListener() {

	}

}
