import java.sql.*;
import java.io.*;
import java.util.*;
import java.lang.Object;
import javax.swing.*;
import java.awt.*; // includes Dimension, Button, KeyListener
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Corona extends JFrame
{
	private static Connection conn;
	private static Statement stmt;
	private static String host = "localhost";
	private static int port = 3306;
	private static String database = "Coronavirus";
	private static String username = "root";
	private static String password = "128Days!";
	
	public static void main(String[] args) throws SQLException
	{
		// First, connect to MySQL
		try
		{
        	Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&verifyServerCertificate=false&useSSL=true",username,password);
        	conn.setAutoCommit(false);
            stmt = conn.createStatement();
        }
		catch(Exception e)
		{
            System.out.println("\n***Database connection was not established***\n");
            e.printStackTrace();
            System.exit(1);
        }
		
		System.out.println("Connected!");
		
		// Create the Graphical User Interface (GUI) frame
		JFrame frame = new JFrame("Demo Frame");
		frame.getContentPane();
	    mainMenu(frame); // start at the main menu
	}
	
	public static void mainMenu(JFrame frame) throws SQLException
	{
		// create the main menu
		// action listeners for the buttons
	    JPanel mainmenu = new JPanel();
	    
	    JLabel welcome = new JLabel("Welcome to the Coronavirus Archive!");
	    Dimension wSize = new Dimension(600, 100);
	    welcome.setFont(new Font("", Font.BOLD, 25)); // change the font size
	    welcome.setBounds(145, 100, wSize.width, wSize.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    JLabel instruction1 = new JLabel("We have a vast library of coronavirus data from the United");
	    instruction1.setFont(new Font("", Font.PLAIN, 15));
	    instruction1.setBounds(160, 200, 600, 100); // x-coordinate, y-coordinate, x-length, y-length
	    JLabel instruction2 = new JLabel("States, recorded from March to July 2020 (courtesy of Johns");
	    instruction2.setFont(new Font("", Font.PLAIN, 15));
	    instruction2.setBounds(160, 225, 600, 100);
	    JLabel instruction3 = new JLabel("Hopkins University). How would you like to query this data?");
	    instruction3.setFont(new Font("", Font.PLAIN, 15));
	    instruction3.setBounds(160, 250, 600, 100);
	    
	    JButton button1 = new JButton("Query By County");
	    Dimension size1 = button1.getPreferredSize();
	    button1.setBounds(150, 350, size1.width, size1.height); // x-coordinate, y-coordinate, x-length, y-length
	    // Add the ActionListener
	    button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                	mainmenu.removeAll(); // clear the panel before you create the next!
					countyQuery(frame);
				} 
                catch (SQLException e1)
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	    
	    
	    JButton button2 = new JButton("Query By State");
	    Dimension size2 = button2.getPreferredSize();
	    button2.setBounds(340, 350, size2.width, size2.height);
	    // Add the ActionListener
	    button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                	mainmenu.removeAll(); // clear the panel before you create the next!
					stateQuery(frame);
				} 
                catch (SQLException e1)
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	    
	    JButton button3 = new JButton("Quit");
	    Dimension size3 = button3.getPreferredSize();
	    button3.setBounds(510, 350, size3.width, size3.height);
	    // Add the ActionListener
	    button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(1);
            }
        });
	    
	    // Before anything can be added...
	    mainmenu.setLayout(null);
	    
	    // Now, add all the components!
	    mainmenu.add(welcome);
	    mainmenu.add(instruction1);
	    mainmenu.add(instruction2);
	    mainmenu.add(instruction3);
	    mainmenu.add(button1);
	    mainmenu.add(button2);
	    mainmenu.add(button3);
	    
	    // Finally, display everything!
	    mainmenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.add(mainmenu);
	    frame.setSize(800, 600);
	    frame.setVisible(true);
	}
	
	public static void countyQuery(JFrame frame) throws SQLException
	{
		// new page!
		// include a back button that takes you to the main menu
		JPanel countymenu = new JPanel();
		
		JLabel current_loc = new JLabel("Query by County");
	    Dimension locSize = new Dimension(600, 100);
	    current_loc.setFont(new Font("", Font.BOLD, 20)); // change the font size
	    current_loc.setBounds(300, 0, locSize.width, locSize.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    JButton to_main = new JButton("Main Menu");
	    Dimension mSize = to_main.getPreferredSize();
	    to_main.setBounds(670, 500, mSize.width, mSize.height);
	    // Add an ActionListener
	    to_main.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                	countymenu.removeAll(); // clear the panel before you create the next!
					mainMenu(frame);
				}
                catch (SQLException e1)
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	    
	    // Now, we need to provide 4 types of queries...
	    // But first, prepare the drop-down menus!
	    
	    // date cobo-box
	    JComboBox<String> date = new JComboBox<String>();
	    date.addItem("SELECT DAY");
	    date.addItem("March 24, 2020");
	    date.addItem("March 31, 2020");
	    date.addItem("April 7, 2020");
	    date.addItem("April 14, 2020");
	    date.addItem("April 21, 2020");
	    date.addItem("April 28, 2020");
	    date.addItem("May 5, 2020");
	    date.addItem("May 12, 2020");
	    date.addItem("May 19, 2020");
	    date.addItem("May 26, 2020");
	    date.addItem("June 2, 2020");
	    date.addItem("June 9, 2020");
	    date.addItem("June 16, 2020");
	    date.addItem("June 23, 2020");
	    date.addItem("June 30, 2020");
	    date.addItem("July 7, 2020");
	    date.addItem("July 14, 2020");
	    date.addItem("July 21, 2020");

		// state combo-box
	    String selectedDate = String.valueOf(date.getSelectedItem());
	    String sqlDate;
	    
	    // have to format the date string so it can be processed in the upcoming SQL query
	    switch (selectedDate)
	    {
	    	case "March 24, 2020":
	    		sqlDate = "March_24_2020";
	    	case "March 31, 2020":
	    		sqlDate = "March_31_2020";
	    	case "April 7, 2020":
	    		sqlDate = "April_07_2020";
	    	case "April 14, 2020":
	    		sqlDate = "April_14_2020";
	    	case "April 21, 2020":
	    		sqlDate = "April_21_2020";
	    	case "April 28, 2020":
	    		sqlDate = "April_28_2020";
	    	case "May 5, 2020":
	    		sqlDate = "May_05_2020";
	    	case "May 12, 2020":
	    		sqlDate = "May_12_2020";
	    	case "May 19, 2020":
	    		sqlDate = "May_19_2020";
	    	case "May 26, 2020":
	    		sqlDate = "May_26_2020";
	    	case "June 2, 2020":
	    		sqlDate = "June_02_2020";
	    	case "June 9, 2020":
	    		sqlDate = "June_09_2020";
	    	case "June 16, 2020":
	    		sqlDate = "June_16_2020";
	    	case "June 23, 2020":
	    		sqlDate = "June_23_2020";
	    	case "June 30, 2020":
	    		sqlDate = "June_30_2020";
	    	case "July 7, 2020":
	    		sqlDate = "July_07_2020";
	    	case "July 14, 2020":
	    		sqlDate = "July_14_2020";
	    	case "July 21, 2020":
	    		sqlDate = "July_21_2020";
	    		
	    }
	    // sqlDate will be the current data selected in the date combo-box
	    // if set to "SELECT", nothing should happen
	    ResultSet states = stmt.executeQuery("SELECT DISTINCT state FROM March_24_2020\n" + 
	    		"ORDER BY state;")
		
		// respective county combo-box
	    
	    		

	    	    
	    // confirmed (cases), deaths, recoveries, or active?
	    JComboBox<String> cdra = new JComboBox<String>();
		// add items to the combo box
		cdra.addItem("SELECT");
		cdra.addItem("Confirmed cases");
		cdra.addItem("Deaths");
		cdra.addItem("Recoveries");
		cdra.addItem("Active cases");
		
		
	    // Before anything can be added...
	    countymenu.setLayout(null);
	    
	    // Now, add all the components!
	    countymenu.add(current_loc);
	    countymenu.add(to_main);
	    
	    // Finally, display everything!
	    countymenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.add(countymenu);
	    frame.setSize(800, 600);
	    frame.setVisible(true);
	}
	
	public static void stateQuery(JFrame frame) throws SQLException
	{
		// new page!
		// include a back button that takes you to the main menu
		JPanel statemenu = new JPanel();
		
		JLabel current_loc = new JLabel("Query by State");
	    Dimension locSize = new Dimension(600, 100);
	    current_loc.setFont(new Font("", Font.BOLD, 20)); // change the font size
	    current_loc.setBounds(300, 0, locSize.width, locSize.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    JButton to_main = new JButton("Main Menu");
	    Dimension mSize = to_main.getPreferredSize();
	    to_main.setBounds(670, 500, mSize.width, mSize.height);
	    // Add an ActionListener
	    to_main.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                	statemenu.removeAll(); // clear the panel before you create the next!
					mainMenu(frame);
				}
                catch (SQLException e1)
                {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	    
		
	    // Before anything can be added...
	    statemenu.setLayout(null);
	    
	    // Now, add all the components!
	    statemenu.add(current_loc);
	    statemenu.add(to_main);
	    
	    // Finally, display everything!
	    statemenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.add(statemenu);
	    frame.setSize(800, 600);
	    frame.setVisible(true);
	}
	
	private static JComboBox<String> countySelection(String state)
	{
		return null;
	}
}

/* STILL NEED TO FIGURE OUT HOW TO...
 * - implement button actions
 * - create drop-down menus
 * - resize buttons
 * - add graphs
*/
