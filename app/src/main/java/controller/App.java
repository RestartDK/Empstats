package controller;

import java.awt.Component; 
import java.awt.image.BufferedImage; 
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Graph;
import model.User;
import view.HomeScreen;
import view.LoadingScreen;
import view.OutlookDataScreen;
import view.OverallDataScreen;
import view.TeamsDataScreen;
import view.WelcomeScreen;
import view.Window;

public class App {
	private Queue<User> users;
	private Window window;
	private WelcomeScreen welcomeScreen;
	private LoadingScreen loadingScreen;
	private HomeScreen homeScreen;
	private OutlookDataScreen outlookScreen;
	private TeamsDataScreen teamsScreen;
	private OverallDataScreen overallScreen;
	
	public void setUp() {
		// Initialising all the GUI frames
		loadingScreen = new LoadingScreen(this);
		welcomeScreen = new WelcomeScreen(this);
		outlookScreen = new OutlookDataScreen("emails read", "Outlook Data", null, this);
		teamsScreen = new TeamsDataScreen("calls picked up", "Teams Data", null, this);
		overallScreen = new OverallDataScreen("emails read / calls picked up", "Overall Data", null, this);
		window = new Window(this, welcomeScreen);  	
	}
	
	// Initialise the main data structure
	private Queue<User> initialiseUsers(String[][] tempusers) {
		users = new LinkedList();

        for (int i = 0; i < tempusers.length; i++) {
        	// Initialising data for a specific user
            int[] readmails = Graph.getEmailRead(tempusers[i][0]);
            int[] notreadmails = Graph.getEmailNotRead(tempusers[i][0]);
            int[] pickedup = Graph.getPickedUpCalls(tempusers[i][0]);
            int[] notpickedup = Graph.getMissedCalls(tempusers[i][0]);
            BufferedImage photo = Graph.getPhoto(tempusers[i][0]);
            
            // Initialising user object
        	User user = new User(tempusers[i][0], tempusers[i][1], tempusers[i][2], tempusers[i][3], tempusers[i][4], photo, 
        			readmails, notreadmails, pickedup, notpickedup);
        	
        	// Adding to users
        	users.add(user);
        }
       
        // Test Data
        int[] fakeread = {2, 3, 1, 4, 5, 12, 23};
        int[] fakecall = {10, 0, 0, 2, 3, 1, 6};
        User dummy = new User("daniel", "dk@gmail.com", "123341", "Tester", "DK", null, fakeread, fakeread, fakecall, fakecall); 
        users.add(dummy);
        
        
        return users;
	}
	
	// Showing screen in application
	public void showScreen(JPanel screen) {
		window.setContentPane(screen);
		window.revalidate();
		window.repaint();
	}
	
	public void showWelcomeScreen() {
		showScreen(welcomeScreen);
	}
	
	public void showLoadingScreen() {
		showScreen(loadingScreen);
	}
	
	public void showHomeScreen() {
		showScreen(homeScreen);
	}
	
	public void showOutlookDataScreen(User user) {
		outlookScreen.setUser(user);
		showScreen(outlookScreen);
	}
	
	public void showTeamsDataScreen(User user) {
		teamsScreen.setUser(user);
		showScreen(teamsScreen);
	}
	
	public void showOverallDataScreen(User user) {
		overallScreen.setUser(user);
		showScreen(overallScreen);
	}
	
	
	// Using oAuth authentication to login user natively from the application for the microsoft graph api
	public void authenticateUser(String username, String password) {
		// Load OAuth settings
        final Properties oAuthProperties = new Properties();
        try {
            oAuthProperties.load(App.class.getResourceAsStream("/oAuth.properties"));
        } catch (IOException e) {
            System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
            return;
        }

        final String appId = oAuthProperties.getProperty("app.id");
        System.out.println(appId);
        final List<String> appScopes = Arrays
            .asList(oAuthProperties.getProperty("app.scopes").split(","));

        Graph.initializeGraphAuth(appId, appScopes, username, password);
        
        // Initialise all the users from the online database and inserting them in queue data structure
        String[][] tempusers2 = Graph.getUsersInfo();
        this.users = initialiseUsers(tempusers2);
        homeScreen = new HomeScreen(this, users);
        
        
	}

	// Reinitalise GUI to populate User information for Data screens
	public void reInitialiseGUI(User user) {
		outlookScreen = new OutlookDataScreen("emails read", "Outlook Data", user, this);
		teamsScreen = new TeamsDataScreen("calls picked up", "Teams Data", user, this);
		overallScreen = new OverallDataScreen("emails read / calls picked up", "Overall Data", user, this);
		
	}

	
	
	

}
