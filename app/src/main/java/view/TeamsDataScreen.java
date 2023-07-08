package view;

import javax.swing.JTextField;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.App;
import model.ProcessData;
import model.User;

public class TeamsDataScreen extends OutlookDataScreen{

	public TeamsDataScreen(String type, String applicationTitle, User user, App app) {
		super(type, applicationTitle, user, app);
		
	}
	
	protected CategoryDataset createDataset() {
		// Initialising data
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int[] pickedUp = user.getPickedUp();
        int[] missedCalls = user.getMissedCalls();
        
        // Adding datasets from User class
		dataset.addValue(pickedUp[0], "Picked up", "Monday");
		dataset.addValue(missedCalls[0], "Missed calls", "Monday");

        dataset.addValue(pickedUp[1], "Picked up", "Tuesday");
        dataset.addValue(missedCalls[1], "Missed calls", "Tuesday");


        dataset.addValue(pickedUp[2], "Picked up", "Wednesday");
        dataset.addValue(missedCalls[2], "Missed calls", "Wednesday");;


        dataset.addValue(pickedUp[3], "Picked up", "Thursday");
        dataset.addValue(missedCalls[3], "Missed calls", "Thursday");


        dataset.addValue(pickedUp[4], "Picked up", "Friday");
        dataset.addValue(missedCalls[4], "Missed calls", "Friday");


        dataset.addValue(pickedUp[5], "Picked up", "Saturday");
        dataset.addValue(missedCalls[5], "Missed calls", "Saturday");


        dataset.addValue(pickedUp[6], "Picked up", "Sunday");
        dataset.addValue(missedCalls[6], "Missed calls", "Sunday");

        return dataset;
    }
	
	protected JTextField createAverageDataText() {
    	// Getting average
    	int[] pickedUp = user.getPickedUp();
		int average = ProcessData.averageRate(pickedUp);
		JTextField text = new JTextField("Average calls made:\n" + average + " calls per day");
		
		return text;
    }

}
