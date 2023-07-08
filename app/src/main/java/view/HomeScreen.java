package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;
import javax.swing.text.AttributeSet.ColorAttribute;

import controller.App;
import model.*;

import javax.swing.plaf.basic.BasicScrollBarUI;

public class HomeScreen extends JPanel{
	JPanel employees;
	JPanel searchbar;
    JTextField searchfield;
    JButton search;
    JButton employee;
    Queue<User> users;
    JScrollPane scrollPane; 
    App app;
    GridLayout emplayout;

    public HomeScreen(App app, Queue<User> users) {
        this.users = users;
        this.app = app;
        
     // Initialising all Swing components
        emplayout = new GridLayout(users.size()/3, 2);
        employees = new JPanel(emplayout);
        scrollPane = new JScrollPane(employees);
        scrollPane.setPreferredSize(new Dimension(1600, 950));							//1600, 950			//1300, 780 small
        scrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getHorizontalScrollBar().setBackground(Color.WHITE);
        searchbar = new JPanel();
        searchfield = new JTextField(120);

        
     // Setting up the JPanel with its layout, components, and listeners   
        setupLayout();
        setupPanel();
        setupListener();

    }
    
    
    
    
    
    private void setupLayout() {
    	emplayout.setHgap(25); //gap between users
        emplayout.setVgap(25);
        setLayout(new BorderLayout());
    }
    
    private void setupPanel() {
        // Display Employees on HomeScreen
        listEmployees(users);
        
        // Adding all Jpanels
        searchbar.add(searchfield);
        this.add(searchbar, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupListener() {
    	// Execute search algorithm
        Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Queue<User> tempusers = new LinkedList(users);
				Queue<User> newusers = new LinkedList();
				for (int i = 0; i < users.size(); i++) {
					boolean pattern = ProcessData.naiveSearch(tempusers.peek().getDisplayName(), searchfield.getText());
					
					if (pattern == true) 
					{
						newusers.add(tempusers.peek());
					}
					tempusers.remove();
				}
				listEmployees(newusers);
			}
        };
        searchfield.addActionListener(action);
    }
    
    
    
    private void listEmployees (Queue<User> altusers) {
    	// Clearing all contents on employee panel
    	/*
    	 * Reference for removing contents in JPanel: https://stackoverflow.com/questions/38349445/how-to-delete-all-components-in-a-jpanel-dynamically/38350395
    	 */
    	employees.removeAll();
    	employees.revalidate();
    	employees.repaint();
    	
    	Queue<User> tempusers = new LinkedList<>(altusers);
    	// Creating the employee JPanel with picture and username
        Iterator iterator = tempusers.iterator();
        while (iterator.hasNext()) {
            employee = new JButton();
            employee.setPreferredSize(new Dimension(300, 300));
            employee.setBackground(Color.WHITE);
            User current = tempusers.peek();
            ImageIcon image = current.getIcon();
            JLabel picture = new JLabel(image);  
            JTextArea info = new JTextArea(current.getDisplayName() + "\n" + current.getEmail() + "\n" + current.getJobTitle());

            employee.add(picture, BorderLayout.PAGE_END);     
            employee.add(info, BorderLayout.PAGE_START);

            
            employee.addActionListener(new java.awt.event.ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		app.reInitialiseGUI(current);
            		
            		if (current.getRead() != null) 
            		{
            			app.showOutlookDataScreen(current);
            		}
            		else 
            		{
            			JOptionPane.showMessageDialog(null, "Not enough information found",
            					"User error", JOptionPane.ERROR_MESSAGE);
            		} 
            	}
            });

            employee.setFocusable(true);		
            employees.add(employee);
            tempusers.remove();
        }
    }

    
    
}
