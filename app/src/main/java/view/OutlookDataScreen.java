package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.App;
import model.ProcessData;
import model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OutlookDataScreen extends JPanel{
	JPanel screens;
	JPanel alldata, averageOrg;
	JButton outlookScreen, teamsScreen, overallScreen, exit;
	JTextField generaltext;
	ChartPanel chartPanel;
    User user;
    App app;

    public OutlookDataScreen(String type, String applicationTitle, User user, App app) {
        this.user = user;
        this.app = app;
        
        
        //main.setPreferredSize(new Dimension(1350, 780));		//Change this back
        alldata = new JPanel();
        averageOrg = new JPanel();
        screens = new JPanel(new GridLayout(1, 0, 3, 3));
        
        try {
        	 // Adding the average data 
            generaltext = createAverageDataText();

            // Declaring the outlookchart with its respective data
            // Source: https://www.tutorialspoint.com/jfreechart/jfreechart_bar_chart.htm
            JFreeChart outlookchart = ChartFactory.createBarChart(
                    applicationTitle + " Chart",
                    "Day",
                    type,
                    createDataset(),
                    PlotOrientation.VERTICAL,
                    true, true, false);

            chartPanel = new ChartPanel(outlookchart);
            
            // Adding redirecting buttons to screen JPanel
            outlookScreen = new JButton("Outlook Data");
            outlookScreen.setBackground(Color.WHITE);
            
            teamsScreen = new JButton("Teams Data");
            teamsScreen.setBackground(Color.WHITE);       
            
            overallScreen = new JButton("Overall Data");
            overallScreen.setBackground(Color.WHITE);
            
            exit = new JButton("Exit");
            exit.setBackground(Color.RED);
            
            setupLayout();
            setupPanel();
            setupListener();
        	
        } catch (Exception e) {
        	System.out.println(e.getCause());
        }
       
        
    }
    
    protected void setupLayout() {
    	screens.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }
    
    protected void setupPanel() {
    	// Adding the redirecting buttons
        screens.add(outlookScreen);
        screens.add(teamsScreen);
        screens.add(overallScreen);
        screens.add(exit);
        
        // Adding general data and num of people in organisation in averageOrg JPanel
        averageOrg.add(generaltext, BorderLayout.CENTER);
        averageOrg.revalidate();
        
        // Adding chart and general data into alldata JPanel
        alldata.add(chartPanel, BorderLayout.PAGE_START);
        alldata.add(averageOrg, BorderLayout.PAGE_END);
        alldata.revalidate();
        
        // Adding both data and redirecting buttons to main JPanel
        this.setLayout(new BorderLayout());
        this.add(screens, BorderLayout.PAGE_START);
        this.add(alldata, BorderLayout.CENTER);
        this.revalidate();
    }
    
    protected void setupListener() {
    	// Feature to redirect the user to selected screen
        outlookScreen.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (user.getRead() != null) 
        		{
        			app.showOutlookDataScreen(user);
        		}
        		else 
        		{
        			JOptionPane.showMessageDialog(null, "Not enough information found",
        					"User error", JOptionPane.ERROR_MESSAGE);
        		}
        		
        		
        	}
        });
        
        teamsScreen.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (user.getPickedUp() != null) 
        		{
        			app.showTeamsDataScreen(user);
        		}
        		else 
        		{
        			JOptionPane.showMessageDialog(null, "Not enough information found",
        					"User error", JOptionPane.ERROR_MESSAGE);
        		}
        		
        		
        	}
        });
        
        
        overallScreen.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (user.getRead() != null && user.getPickedUp() != null) 
        		{
        			app.showOverallDataScreen(user);
        		}
        		else 
        		{
        			JOptionPane.showMessageDialog(null, "Not enough information found",
        					"User error", JOptionPane.ERROR_MESSAGE);
        		}
        		
        	}
        });
        
        exit.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		app.showHomeScreen();
        	}
        });
    }
    
    // Using protected and not private otherwise overriding method will not work from inheritance
    // Creating dataset
    protected CategoryDataset createDataset() {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int[] read = user.getRead();
        int[] notread = user.getNotRead();

        // Adding datasets from User class
        dataset.addValue(read[0], "Read", "Monday");
        dataset.addValue(notread[0], "Not Read", "Monday");

        dataset.addValue(read[1], "Read", "Tuesday");
        dataset.addValue(notread[1], "Not Read", "Tuesday");


        dataset.addValue(read[2], "Read", "Wednesday");
        dataset.addValue(notread[2], "Not Read", "Wednesday");;


        dataset.addValue(read[3], "Read", "Thursday");
        dataset.addValue(notread[3], "Not Read", "Thursday");


        dataset.addValue(read[4], "Read", "Friday");
        dataset.addValue(notread[4], "Not Read", "Friday");


        dataset.addValue(read[5], "Read", "Saturday");
        dataset.addValue(notread[5], "Not Read", "Saturday");


        dataset.addValue(read[6], "Read", "Sunday");
        dataset.addValue(notread[6], "Not Read", "Sunday");;

        return dataset;
    }
    
    
    protected JTextField createAverageDataText() {
    	// Getting average
    	int[] read = user.getRead();
		int average = ProcessData.averageRate(read);
		JTextField text = new JTextField("Average Emails Read:\n" + average + " emails per day");
		
		return text;
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
    
    
}
    
