package view;

import java.awt.Font; 
import javax.swing.JTextField;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.App;
import model.ProcessData;
import model.User;
import model.Utils;

public class OverallDataScreen extends OutlookDataScreen{

	public OverallDataScreen(String type, String applicationTitle, User user, App app) {
		super(type, applicationTitle, user, app);
		
	}
	
	protected CategoryDataset createDataset() {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int[] read = user.getRead();
        int[] notread = user.getNotRead();
        int[] pickedUp = user.getPickedUp();
        int[] missedCalls = user.getMissedCalls();
        
        int[] readpicked = Utils.mergeArrays(read, pickedUp);
        int[] missedread = Utils.mergeArrays(notread, missedCalls);

        // Adding datasets from User class
        dataset.addValue(readpicked[0], "Read/Calls", "Monday");
        dataset.addValue(missedread[0], "Not Read/Missed Calls", "Monday");

        dataset.addValue(readpicked[1], "Read/Calls", "Tuesday");
        dataset.addValue(missedread[1], "Not Read/Missed Calls", "Tuesday");


        dataset.addValue(readpicked[2], "Read/Calls", "Wednesday");
        dataset.addValue(missedread[2], "Not Read/Missed Calls", "Wednesday");


        dataset.addValue(readpicked[3], "Read/Calls", "Thursday");
        dataset.addValue(missedread[3], "Not Read/Missed Calls", "Thursday");


        dataset.addValue(readpicked[4], "Read/Calls", "Friday");
        dataset.addValue(missedread[4], "Not Read/Missed Calls", "Friday");


        dataset.addValue(readpicked[5], "Read/Calls", "Saturday");
        dataset.addValue(missedread[5], "Not Read/Missed Calls", "Saturday");


        dataset.addValue(readpicked[6], "Read/Calls", "Sunday");
        dataset.addValue(missedread[6], "Not Read/Missed Calls", "Sunday");;

        return dataset;
    }
	
	protected JTextField createAverageDataText() {
    	// Calculating avread and avcall
    	int[] read = user.getRead();
    	int[] pickedUp = user.getPickedUp();
    	int avread = ProcessData.averageRate(read);
    	int avcall = ProcessData.averageRate(pickedUp);
    	
    	// Determining productivity
		String productivity = ProcessData.productivityCalc(avread, avcall);
		JTextField text = new JTextField("Productivity: " + productivity);
		text.setFont(new Font("Calibri", Font.BOLD, 24));
		
		return text;
    }

}
