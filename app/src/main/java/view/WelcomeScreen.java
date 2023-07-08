package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.App;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class WelcomeScreen extends JPanel{
	private App app;
	private JLabel loginText;
	private JLabel welcomeIcon;
	private JLabel userLabel, passLabel;
	private JTextField userText;
	private JPasswordField passText;
	private JPanel loginPanel, userPanel, passPanel;
	private JButton loginButton;
	
	public WelcomeScreen(App app) {
		this.app = app;
		
		// Initalising and setting the colour and fonts of components
		loginPanel = new JPanel();
		userPanel = new JPanel();
		passPanel = new JPanel();
		loginText = new JLabel("Login");
		userLabel = new JLabel("Username");
		passLabel = new JLabel("Password");
		userText = new JTextField(20);
		passText = new JPasswordField(20);
		loginButton = new JButton("Login");
		welcomeIcon = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/EEWEmblem.png")).getImage().getScaledInstance(200, 110, Image.SCALE_DEFAULT)));
		
		loginPanel.setBounds(507, 346, 436, 265); // x = 707 for big screen
		welcomeIcon.setBounds(0, 0, 200, 175);
		loginText.setBounds(704, 159, 200, 175); // x = 904 for big screen
		
		this.setBackground(new Color(4,15,43));
		loginText.setPreferredSize(new Dimension(0, 200));
		loginText.setBackground(new Color(4,15,43));
		loginPanel.setBackground(new Color(4,15,43));
		loginPanel.setPreferredSize(new Dimension(324, 155));
		userPanel.setBackground(new Color(4,15,43));
		passPanel.setBackground(new Color(4,15,43));
		loginText.setFont(new Font("Default", Font.PLAIN, 30));
		loginText.setForeground(Color.WHITE);
		userLabel.setFont(new Font("Default", Font.PLAIN, 20));
		userLabel.setForeground(Color.WHITE);
		passLabel.setFont(new Font("Default", Font.PLAIN, 20));
		passLabel.setForeground(Color.WHITE);
		
		setupLayout();
		setupPanel();
		setupListener();
	}
	
	private void setupLayout() {
		setLayout(null);
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
	}
	
	private void setupPanel() {
		userPanel.add(userLabel, BorderLayout.WEST);
		userPanel.add(userText, BorderLayout.SOUTH);
		passPanel.add(passLabel, BorderLayout.WEST);
		passPanel.add(passText, BorderLayout.SOUTH);
		loginPanel.add(userPanel);
		loginPanel.add(passPanel);
		loginPanel.add(loginButton);

		this.add(welcomeIcon);
		this.add(loginPanel);
		this.add(loginText);

		
	}
	
	private void setupListener() {
		// Get the text from the textfields to attempt to login user
        loginButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
            	app.showLoadingScreen();
            	startThread();

        		
        	}
        });

	}
	
	// Invoking worker thread to avoid thread interference
	private void startThread() 
    {
        SwingWorker sw1 = new SwingWorker() 
        {
            @Override
            protected String doInBackground() throws Exception 
            {
                // define what thread will do here
            	try {
            		String username = userText.getText();
            		String password = passText.getText();
                	app.authenticateUser(username, password);
  
            	} catch (Exception error) {
        			JOptionPane.showMessageDialog(null, "Please enter the correct username and password",
     		               "Login error", JOptionPane.ERROR_MESSAGE);
     			userText.setText("");
     			passText.setText("");
     			app.showWelcomeScreen();
     		}
            	String res = "Finished Execution";
            	return res;
            	
            }
  
            @Override
            protected void done() 
            {
                // this method is called when the background 
                // thread finishes execution
            	app.showHomeScreen();
                
            }
        };
          
        // executes the swingworker on worker thread
        sw1.execute(); 
    }
}
