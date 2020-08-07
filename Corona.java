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
        	conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
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
		JFrame frame = new JFrame("Coronavirus Archive: A Look at the Last Few Months");
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
	
	/** For querying by county **/
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
	    /** For our first query, we need 4 drop-down menus and an APPLY button! **/
	    
	    /** 1st **/
	    // date combo-box
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
	    date.setEnabled(true); // this combo-box is to ALWAYS be enabled! :)

	    /** 2nd **/
	    // state combo-box
	    JComboBox<String> states = new JComboBox<String>();
	    states.addItem("SELECT STATE");
	    // don't add anything until a date is selected
	    states.setEnabled(false); // this should be disabled if no date is selected
	    
	    /** 3rd **/
	    // county combo-box
	    JComboBox<String> counties = new JComboBox<String>();
	    counties.addItem("SELECT COUNTY");
	    // don't add anything until a state is selected
	    counties.setEnabled(false); // this should be disabled if no state is selected
	    
	    /** 4th **/
	    // confirmed (cases), deaths, recoveries, or active?
	    JComboBox<String> cdra = new JComboBox<String>();
		// add items to the combo box
		cdra.addItem("SELECT");
		cdra.addItem("cases");
		cdra.addItem("deaths");
		cdra.addItem("recoveries");
		cdra.addItem("active");
		cdra.setEnabled(false); // this should be disabled if no county is selected
		
		/** 5th **/
	    // first apply button (for determining the number of cases
	    JButton apply1 = new JButton("Apply");
	    Dimension a1Size = to_main.getPreferredSize();
	    apply1.setBounds(300, 160, a1Size.width, a1Size.height);
	    apply1.setEnabled(false); // this should be disabled if no selection is made in CDRA
	    
	    /** Finally, a bonus... we need to provide a JTextArea to
	        output the results of ANY query! **/
	    JTextArea result = new JTextArea();
	    // this will change (in terms of number of columns) based on the query performed
	    
	    
	    /** Next, we have to provide ActionListeners for each of these... **/
	    
	    /** 1st **/
	    // ComboBox ActionListener for date... after all the combo-boxes have been created
	    // when a date is selected
	    date.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedDate = (String) date.getSelectedItem();
	    	    String sqlDate = sqlDateFormat(selectedDate);
	    	    
	    	    boolean enableStates = false;
	    	    
	    	    if (sqlDate == null) // if no date is selected, remove all the states!
	    	    {
	    	    	
	    	    	states.setEnabled(false);
	    	    	// delete every item in the states combo-box except for the default
					for (int i = states.getItemCount() - 1; i >= 0; i--)
					{
						if (!states.getItemAt(i).equals("SELECT STATE"))
						{
							states.removeItemAt(i);
						}
					}
	    	    	return;
	    	    }
	    	    else
	    	    {
	    	    	enableStates = true;
	    	    }
	    	    
	    	    // now populate the states combo-box with... the states!
	    	    try
	    	    {
	    	    	states.setEnabled(false); // before you reset its contents
					ResultSet all_states = stmt.executeQuery("SELECT DISTINCT state FROM " + sqlDate + " ORDER BY state;");
					
					// delete every item in the states combo-box except for the default
					for (int i = states.getItemCount() - 1; i >= 0; i--)
					{
						if (!states.getItemAt(i).equals("SELECT STATE"))
						{
							states.removeItemAt(i);
						}
					}
					
					if (enableStates)
					{
						states.setEnabled(true);
						while (all_states.next()) // add all the states (plus DC!)
						{
							states.addItem(all_states.getString("state"));
						}
					}
					conn.commit();
				}
	    	    catch (Exception e1)
	    	    {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	    
            }
	    });
		
	    /** 2nd **/
	    // ComboBox ActionListener for state
	    // when a state is selected
	    states.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedState = (String) states.getSelectedItem();
    			final String selectedDate = (String) date.getSelectedItem(); // trust me, you'll need this
    			String sqlDate = sqlDateFormat(selectedDate);
    			
    			boolean enableCounties = false;
    			
    			if (selectedState.equals("SELECT STATE"))
    			{
    				// just disable all the combo-boxes ahead!
    				counties.setEnabled(false);
    				cdra.setEnabled(false);
    				return; // get out!
    			}
    			else
    			{
    				enableCounties = true;
    			}
    			
    			// now populate the counties combo-box with... the counties!
	    		try
	    		{
	    			counties.setEnabled(false);
	    			ResultSet all_counties = stmt.executeQuery("SELECT DISTINCT county FROM " + sqlDate + " WHERE state = '" + selectedState + "' ORDER BY county;");
					
					// delete every item in the counties combo-box except for the default
					for (int i = counties.getItemCount() - 1; i >= 0; i--)
					{
						if (!counties.getItemAt(i).equals("SELECT COUNTY"))
						{
							counties.removeItemAt(i);
						}
					}
					
					if (enableCounties)
					{
						counties.setEnabled(true);
						while (all_counties.next()) // add all the states (plus DC!)
						{
							counties.addItem(all_counties.getString("county"));
						}
					}
					conn.commit();
	    		}
	    		catch (SQLException e1)
	    	    {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    });
	    
	    /** 3rd **/
	    // ComboBox ActionListener for county
	    // when a county is selected
	    counties.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCounty = (String) counties.getSelectedItem();
    			
				if (selectedCounty.equals("SELECT COUNTY"))
				{
					cdra.setEnabled(false);
				}
				else
				{
					cdra.setEnabled(true);
				}
				// uh... that's it for this ActionListener! :)
	    	}
	    });
	    
	    /** 4th **/
	    // ComboBox ActionListener for our special cdra
	    // when either confirmed (cases), deaths, recoveries, or active is selected
	    cdra.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCDRA = (String) cdra.getSelectedItem();
    			
				if (selectedCDRA.equals("SELECT"))
				{
					apply1.setEnabled(false);
				}
				else
				{
					apply1.setEnabled(true);
				}
	    	}
	    });
	    		
		/** 5th **/
	    apply1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		// this will be for the simplest query:
		    	// How many cases/deaths/recoveries/actives were there in this particular county on this particular day?
		    	try
		    	{
		    		final String selectedDate = (String) date.getSelectedItem();
		    		String sqlDate = sqlDateFormat(selectedDate);
		    	    
			    	final String selectedState = (String) states.getSelectedItem();
			    	final String selectedCounty = (String) counties.getSelectedItem();
			    	final String selectedCDRA = (String) cdra.getSelectedItem();
			    	
			    	result.setText(null); // clears the result pane
			    	result.setColumns(1); // puts 1 column in the result pane
			    	
			    	ResultSet cdraResult;
			    	cdraResult = stmt.executeQuery("SELECT " + selectedCDRA + " FROM " + sqlDate + " WHERE state = '" + selectedState + "' AND county = '" + selectedCounty + "';");
			    	while (cdraResult.next())
			    	{
			    		// result is the name of the JTextArea to which the query answer will be outputted
			    		result.append( Integer.toString(cdraResult.getInt(selectedCDRA)) );
			    	}
			    	
		    	}
		    	catch (SQLException e1)
	    	    {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    });
	    
		/*** WHEW.. THAT WAS A LOT OF WORK! ***/
	    /*** Now let's setup the data frame ***/
	    
	    /**  First and foremost, put up the JTextArea! **/
	    result.setBounds(500, 100, 250, 350); // x-coordinate, y-coordinate, x-length, y-length
	    
	    /** For the first query **/
	    // "On (date), (county), (state) had how many (cases, deaths, recoveries, active)?"
	    JLabel q1_part1 = new JLabel("On");
	    Dimension q1p1size = new Dimension(150, 100);
	    q1_part1.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part1.setBounds(20, 60, q1p1size.width, q1p1size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the date combo-box
	    date.setBounds(50, 100, 150, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // position the counties combo-box
	    counties.setBounds(15, 130, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // next text
	    JLabel q1_part2 = new JLabel("County,");
	    Dimension q1p2size = new Dimension(150, 100);
	    q1_part2.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part2.setBounds(215, 90, q1p2size.width, q1p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the states combo-box
	    states.setBounds(275, 130, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // next text
	    JLabel q1_part3 = new JLabel("had how many");
	    Dimension q1p3size = new Dimension(200, 100);
	    q1_part3.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part3.setBounds(20, 120, q1p2size.width, q1p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the cdra combo-box
	    cdra.setBounds(130, 160, 125, 25);
	    
	    // one final question mark...
	    JLabel q1_part4 = new JLabel("?");
	    Dimension q1p4size = new Dimension(50, 50);
	    q1_part4.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part4.setBounds(255, 120, q1p2size.width, q1p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // we've already positioned the apply button... at the start!
	    
	    /** Before anything can be added... **/
	    countymenu.setLayout(null);
	    
	    /** Now, add all the components! **/
	    // menu label
	    countymenu.add(current_loc);
	    // button for main menu
	    countymenu.add(to_main);
	    // text area
	    countymenu.add(result);
	    
	    // text for the first query
	    countymenu.add(q1_part1);
	    countymenu.add(q1_part2);
	    countymenu.add(q1_part3);
	    countymenu.add(q1_part4);
	    // dropboxes for first query
	    countymenu.add(date);
	    countymenu.add(counties);
	    countymenu.add(states);
	    countymenu.add(cdra);
	    // apply button
	    countymenu.add(apply1);
	    
	    // Finally, display everything!
	    countymenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.add(countymenu);
	    frame.setSize(800, 600);
	    frame.setVisible(true);
	}
	
	
	/** Query by state **/
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
	
	private static String sqlDateFormat(String selectedDate)
	{
		String sqlDate;
		// have to format the date string so it can be processed in the upcoming SQL query
	    switch (selectedDate)
	    {
	    	case "March 24, 2020":
	    		sqlDate = "March_24_2020";
	    		break;
	    	case "March 31, 2020":
	    		sqlDate = "March_31_2020";
	    		break;
	    	case "April 7, 2020":
	    		sqlDate = "April_07_2020";
	    		break;
	    	case "April 14, 2020":
	    		sqlDate = "April_14_2020";
	    		break;
	    	case "April 21, 2020":
	    		sqlDate = "April_21_2020";
	    		break;
	    	case "April 28, 2020":
	    		sqlDate = "April_28_2020";
	    		break;
	    	case "May 5, 2020":
	    		sqlDate = "May_05_2020";
	    		break;
	    	case "May 12, 2020":
	    		sqlDate = "May_12_2020";
	    		break;
	    	case "May 19, 2020":
	    		sqlDate = "May_19_2020";
	    		break;
	    	case "May 26, 2020":
	    		sqlDate = "May_26_2020";
	    		break;
	    	case "June 2, 2020":
	    		sqlDate = "June_02_2020";
	    		break;
	    	case "June 9, 2020":
	    		sqlDate = "June_09_2020";
	    		break;
	    	case "June 16, 2020":
	    		sqlDate = "June_16_2020";
	    		break;
	    	case "June 23, 2020":
	    		sqlDate = "June_23_2020";
	    		break;
	    	case "June 30, 2020":
	    		sqlDate = "June_30_2020";
	    		break;
	    	case "July 7, 2020":
	    		sqlDate = "July_07_2020";
	    		break;
	    	case "July 14, 2020":
	    		sqlDate = "July_14_2020";
	    		break;
	    	case "July 21, 2020":
	    		sqlDate = "July_21_2020";
	    		break;
	    	default:
	    		// nothing happens if no date is selected!
	    		return null;// get out of here!
	    }
	    
	    return sqlDate;
	}
}

/***
SQL Help:

-- output all the states from a particular day
SELECT DISTINCT state
FROM March_24_2020
ORDER BY state;

-- output all the counties in Alabama
SELECT DISTINCT county
FROM March_24_2020
WHERE state = "Delaware"
ORDER BY county;

-- output the number of deaths in Talladega County, Alabama on April 14
SELECT deaths
FROM April_14_2020
WHERE state = "Alabama" AND county = "Talladega";
***/
