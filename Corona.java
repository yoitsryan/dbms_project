import java.sql.*;
import java.io.*;
import java.util.*;
import java.lang.Object;
import javax.swing.*;
import javax.sql.rowset.*;
import java.awt.*; // includes Dimension, Button, KeyListener
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;

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
	    
	    // Now, we need to provide 3 types of queries...
	    /** For our first query, we need 4 drop-down menus and an APPLY button! **/
	    
	    /** 1st **/
	    // date combo-box
	    JComboBox<String> date1 = new JComboBox<String>();
	    date1.addItem("SELECT DAY");
	    date1.addItem("March 24, 2020");
	    date1.addItem("March 31, 2020");
	    date1.addItem("April 7, 2020");
	    date1.addItem("April 14, 2020");
	    date1.addItem("April 21, 2020");
	    date1.addItem("April 28, 2020");
	    date1.addItem("May 5, 2020");
	    date1.addItem("May 12, 2020");
	    date1.addItem("May 19, 2020");
	    date1.addItem("May 26, 2020");
	    date1.addItem("June 2, 2020");
	    date1.addItem("June 9, 2020");
	    date1.addItem("June 16, 2020");
	    date1.addItem("June 23, 2020");
	    date1.addItem("June 30, 2020");
	    date1.addItem("July 7, 2020");
	    date1.addItem("July 14, 2020");
	    date1.addItem("July 21, 2020");
	    date1.setEnabled(true); // this combo-box is to ALWAYS be enabled! :)

	    /** 2nd **/
	    // state combo-box
	    JComboBox<String> states1 = new JComboBox<String>();
	    states1.addItem("SELECT STATE");
	    // don't add anything until a date is selected
	    states1.setEnabled(false); // this should be disabled if no date is selected
	    
	    /** 3rd **/
	    // county combo-box
	    JComboBox<String> counties1 = new JComboBox<String>();
	    counties1.addItem("SELECT COUNTY");
	    // don't add anything until a state is selected
	    counties1.setEnabled(false); // this should be disabled if no state is selected
	    
	    /** 4th **/
	    // confirmed (cases), deaths, recoveries, or active?
	    JComboBox<String> cdra1 = new JComboBox<String>();
		// add items to the combo box
		cdra1.addItem("SELECT");
		cdra1.addItem("confirmed");
		cdra1.addItem("deaths");
		cdra1.addItem("recovered");
		cdra1.addItem("active");
		cdra1.setEnabled(false); // this should be disabled if no county is selected
		
		/** 5th **/
	    // first apply button (for determining the number of cases)
	    JButton apply1 = new JButton("Apply");
	    Dimension a1Size = apply1.getPreferredSize();
	    apply1.setBounds(300, 160, a1Size.width, a1Size.height);
	    apply1.setEnabled(false); // this should be disabled if no selection is made in CDRA
	    
	    
	    /*************************/
	    /** Finally, a bonus... we need to provide a JTextArea to
	        output the results of ANY query! **/
	    JTextArea result = new JTextArea();
	    // result.setBounds(500, 100, 250, 350); // x-coordinate, y-coordinate, x-length, y-length
	    result.setLineWrap(true); // to wrap text...
	    result.setWrapStyleWord(true); // and ensure no words are split
	    
	    JScrollPane scrollable = new JScrollPane(result);
	    scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    // scrollable.setSize(100,100);
	    scrollable.setBounds(500, 100, 250, 350);
	    // we need to be able to scroll to see a possible motherlode of query results!
	    /*************************/
	    
	    
	    
	    /** Next, we have to provide ActionListeners for each of these... **/
	    
	    /** 1st **/
	    // ComboBox ActionListener for date... after all the combo-boxes have been created
	    // when a date is selected
	    date1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedDate = (String) date1.getSelectedItem();
	    	    String sqlDate = sqlDateFormat(selectedDate);
	    	    
	    	    boolean enableStates = false;
	    	    
	    	    if (sqlDate == null) // if no date is selected, remove all the states!
	    	    {
	    	    	states1.setEnabled(false);
	    	    	counties1.setEnabled(false);
	    	    	cdra1.setEnabled(false);
	    	    	apply1.setEnabled(false);
	    	    	return;
	    	    }
	    	    else
	    	    {
	    	    	enableStates = true;
	    	    }
	    	    
	    	    // now populate the states combo-box with... the states!
	    	    try
	    	    {
	    	    	states1.setEnabled(false); // before you reset its contents
	    	    	ResultSet all_states;
	    	    	Statement getStates;
	    	    	getStates = conn.createStatement();
					all_states = getStates.executeQuery("SELECT DISTINCT state FROM " + sqlDate + " ORDER BY state;");
					
					// delete every item in the states combo-box except for the default
					for (int i = states1.getItemCount() - 1; i >= 0; i--)
					{
						if (!states1.getItemAt(i).equals("SELECT STATE"))
						{
							states1.removeItemAt(i);
						}
					}
					
					if (enableStates)
					{
						states1.setEnabled(true);
						while (all_states.next()) // add all the states (plus DC!)
						{
							states1.addItem(all_states.getString("state"));
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
	    states1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedState = (String) states1.getSelectedItem();
    			final String selectedDate = (String) date1.getSelectedItem(); // trust me, you'll need this
    			String sqlDate = sqlDateFormat(selectedDate);
    			
    			boolean enableCounties = false;
    			
    			if (selectedState.equals("SELECT STATE"))
    			{
    				// just disable all the combo-boxes ahead!
    				counties1.setEnabled(false);
    				cdra1.setEnabled(false);
    				apply1.setEnabled(false);
    				return; // get out!
    			}
    			else
    			{
    				enableCounties = true;
    			}
    			
    			// now populate the counties combo-box with... the counties!
	    		try
	    		{
	    			counties1.setEnabled(false);
	    			ResultSet all_counties = stmt.executeQuery("SELECT DISTINCT county FROM " + sqlDate + " WHERE state = '" + selectedState + "' ORDER BY county;");
					
					// delete every item in the counties combo-box except for the default
					for (int i = counties1.getItemCount() - 1; i >= 0; i--)
					{
						if (!counties1.getItemAt(i).equals("SELECT COUNTY"))
						{
							counties1.removeItemAt(i);
						}
					}
					
					if (enableCounties)
					{
						counties1.setEnabled(true);
						while (all_counties.next()) // add all the states (plus DC!)
						{
							counties1.addItem(all_counties.getString("county"));
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
	    counties1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCounty = (String) counties1.getSelectedItem();
    			
				if (selectedCounty.equals("SELECT COUNTY"))
				{
					cdra1.setEnabled(false);
					apply1.setEnabled(false);
				}
				else
				{
					cdra1.setEnabled(true);
				}
				// uh... that's it for this ActionListener! :)
	    	}
	    });
	    
	    /** 4th **/
	    // ComboBox ActionListener for our special cdra
	    // when either confirmed (cases), deaths, recoveries, or active is selected
	    cdra1.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCDRA = (String) cdra1.getSelectedItem();
    			
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
		    		final String selectedDate = (String) date1.getSelectedItem();
		    		String sqlDate = sqlDateFormat(selectedDate);
		    	    
			    	final String selectedState = (String) states1.getSelectedItem();
			    	final String selectedCounty = (String) counties1.getSelectedItem();
			    	final String selectedCDRA = (String) cdra1.getSelectedItem();
			    	
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
	    /*** But there's still more... :( ***/
	    
	    /** SECOND QUERY **/
	    /** SECOND QUERY **/
	    /** SECOND QUERY **/
	    
	    
	    /*** For our second query, we need the drop-downs for CDRA, a state, a county, and two day selections ***/
	    /*** How many new cases/deaths/recoveries active cases were there between two days? ***/
		
	    JComboBox<String> date2a = new JComboBox<String>();
	    date2a.addItem("SELECT DAY");
	    date2a.addItem("March 24, 2020");
	    date2a.addItem("March 31, 2020");
	    date2a.addItem("April 7, 2020");
	    date2a.addItem("April 14, 2020");
	    date2a.addItem("April 21, 2020");
	    date2a.addItem("April 28, 2020");
	    date2a.addItem("May 5, 2020");
	    date2a.addItem("May 12, 2020");
	    date2a.addItem("May 19, 2020");
	    date2a.addItem("May 26, 2020");
	    date2a.addItem("June 2, 2020");
	    date2a.addItem("June 9, 2020");
	    date2a.addItem("June 16, 2020");
	    date2a.addItem("June 23, 2020");
	    date2a.addItem("June 30, 2020");
	    date2a.addItem("July 7, 2020");
	    date2a.addItem("July 14, 2020");
	    date2a.addItem("July 21, 2020");
	    date2a.setEnabled(true); // this combo-box is to ALWAYS be enabled! :)
	    
	    JComboBox<String> date2b = new JComboBox<String>();
	    date2b.addItem("SELECT DAY");
	    date2b.addItem("March 24, 2020");
	    date2b.addItem("March 31, 2020");
	    date2b.addItem("April 7, 2020");
	    date2b.addItem("April 14, 2020");
	    date2b.addItem("April 21, 2020");
	    date2b.addItem("April 28, 2020");
	    date2b.addItem("May 5, 2020");
	    date2b.addItem("May 12, 2020");
	    date2b.addItem("May 19, 2020");
	    date2b.addItem("May 26, 2020");
	    date2b.addItem("June 2, 2020");
	    date2b.addItem("June 9, 2020");
	    date2b.addItem("June 16, 2020");
	    date2b.addItem("June 23, 2020");
	    date2b.addItem("June 30, 2020");
	    date2b.addItem("July 7, 2020");
	    date2b.addItem("July 14, 2020");
	    date2b.addItem("July 21, 2020");
	    date2b.setEnabled(false); // this should be disabled if no first date is selected
	    
	    
		// state combo-box
	    JComboBox<String> states2 = new JComboBox<String>();
	    states2.addItem("SELECT STATE");
	    // don't add anything until a date is selected
	    states2.setEnabled(false); // this should be disabled if no second date is selected
	    
	    // county combo-box
	    JComboBox<String> counties2 = new JComboBox<String>();
	    counties2.addItem("SELECT COUNTY");
	    // don't add anything until a state is selected
	    counties2.setEnabled(false); // this should be disabled if no state is selected
	    
	    // confirmed (cases), deaths, recoveries, or active?
	    JComboBox<String> cdra2 = new JComboBox<String>();
		// add items to the combo box
		cdra2.addItem("SELECT");
		cdra2.addItem("confirmed");
		cdra2.addItem("deaths");
		cdra2.addItem("recovered");
		cdra2.addItem("active");
		cdra2.setEnabled(false); // this should not be enabled if no county is selected
		
		// second apply button
	    JButton apply2 = new JButton("Apply");
	    Dimension a2Size = apply2.getPreferredSize();
	    apply2.setBounds(330, 280, a2Size.width, a2Size.height);
	    apply2.setEnabled(false); // this should be disabled if no selection is made in CDRA
		
	    
	    
	    /** Now for the action listeners **/
		/** 1st **/
		// ActionListener for the first date
		date2a.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedDate = (String) date2a.getSelectedItem();
	    	    String sqlDate = sqlDateFormat(selectedDate);
	    	    
	    	    if (sqlDate == null) // if no date is selected, remove all the states!
	    	    {
	    	    	// disable all the buttons and combo-boxes above it!
	    	    	date2b.setEnabled(false);
	    	    	counties2.setEnabled(false);
	    	    	states2.setEnabled(false);
	    	    	cdra2.setEnabled(false);
	    	    	apply2.setEnabled(false);
	    	    	return;
	    	    }
	    	    else
	    	    {
	    	    	date2b.setEnabled(true);
	    	    }
            }
	    });
		
		/** 2nd **/
		// ActionListener for the second date
		date2b.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedDateA = (String) date2a.getSelectedItem();
	    		final String selectedDateB = (String) date2b.getSelectedItem();
	    	    String sqlDateA = sqlDateFormat(selectedDateA);
	    	    String sqlDateB = sqlDateFormat(selectedDateB);
	    	    
	    	    boolean enableStates = false;
	    	    
	    	    if (sqlDateB == null) // if no date is selected, remove all the states!
	    	    {
	    	    	states2.setEnabled(false);
	    	    	counties2.setEnabled(false);
	    	    	cdra2.setEnabled(false);
	    	    	apply2.setEnabled(false);
	    	    	// delete every item in the states combo-box except for the default
					for (int i = states2.getItemCount() - 1; i >= 0; i--)
					{
						if (!states2.getItemAt(i).equals("SELECT STATE"))
						{
							states2.removeItemAt(i);
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
	    	    	states2.setEnabled(false); // before you reset its contents
	    	    	ResultSet all_states;
					all_states = stmt.executeQuery("SELECT DISTINCT " + sqlDateA + ".state FROM " + sqlDateA + ", " + sqlDateB + " WHERE " + sqlDateA + ".state = " + sqlDateB + ".state ORDER BY " + sqlDateA + ".state;");
					
					// delete every item in the states combo-box except for the default
					for (int i = states2.getItemCount() - 1; i >= 0; i--)
					{
						if (!states2.getItemAt(i).equals("SELECT STATE"))
						{
							states2.removeItemAt(i);
						}
					}
					
					if (enableStates)
					{
						states2.setEnabled(true);
						while (all_states.next()) // add all the states (plus DC!)
						{
							states2.addItem(all_states.getString("state"));
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
		
		
		/** 3rd **/
	    // ComboBox ActionListener for state
	    // when a state is selected
	    states2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedState = (String) states2.getSelectedItem();
    			final String selectedDateA = (String) date2a.getSelectedItem(); // trust me, you'll need this
    			final String selectedDateB = (String) date2b.getSelectedItem(); // trust me, you'll need this too
    			String sqlDateA = sqlDateFormat(selectedDateA);
    			String sqlDateB = sqlDateFormat(selectedDateB);
    			
    			boolean enableCounties = false;
    			
    			if (selectedState.equals("SELECT STATE"))
    			{
    				// just disable all the combo-boxes ahead!
    				counties2.setEnabled(false);
    				cdra2.setEnabled(false);
    				apply2.setEnabled(false);
    				return; // get out!
    			}
    			else
    			{
    				enableCounties = true;
    			}
    			
    			// now populate the counties combo-box with... the counties!
	    		try
	    		{
	    			counties2.setEnabled(false);
	    			ResultSet all_counties = stmt.executeQuery("SELECT DISTINCT " + sqlDateA + ".county AS county FROM " + sqlDateA +  ", " + sqlDateB + " WHERE " + sqlDateA + ".state = '" + selectedState + "' AND " + sqlDateB + ".state = " + sqlDateA + ".state AND " + sqlDateA + ".county = " + sqlDateB + ".county ORDER BY county;");
					
					// delete every item in the counties combo-box except for the default
					for (int i = counties2.getItemCount() - 1; i >= 0; i--)
					{
						if (!counties2.getItemAt(i).equals("SELECT COUNTY"))
						{
							counties2.removeItemAt(i);
						}
					}
					
					if (enableCounties)
					{
						counties2.setEnabled(true);
						while (all_counties.next()) // add all the states (plus DC!)
						{
							counties2.addItem(all_counties.getString("county"));
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
	    
	    /** 4th **/
	    // ComboBox ActionListener for county
	    // when a county is selected
	    counties2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCounty = (String) counties2.getSelectedItem();
    			
				if (selectedCounty.equals("SELECT COUNTY"))
				{
					cdra2.setEnabled(false);
					apply2.setEnabled(false);
				}
				else
				{
					cdra2.setEnabled(true);
				}
				// uh... that's it for this ActionListener! :)
	    	}
	    });
	    
	    /** 5th **/
	    // ComboBox ActionListener for our special cdra
	    // when either confirmed (cases), deaths, recoveries, or active is selected
	    cdra2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCDRA = (String) cdra2.getSelectedItem();
    			
				if (selectedCDRA.equals("SELECT"))
				{
					apply2.setEnabled(false);
				}
				else
				{
					apply2.setEnabled(true);
				}
	    	}
	    });
	    
	    /** 6th **/
	    apply2.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
		    	// How many new cases/deaths/recoveries/actives came up in this county between the two selected days?
		    	try
		    	{
		    		final String selectedDateA = (String) date2a.getSelectedItem();
		    		final String selectedDateB = (String) date2b.getSelectedItem();
		    		String sqlDateA = sqlDateFormat(selectedDateA);
		    		String sqlDateB = sqlDateFormat(selectedDateB);
		    	    
			    	final String selectedState = (String) states2.getSelectedItem();
			    	final String selectedCounty = (String) counties2.getSelectedItem();
			    	final String selectedCDRA = (String) cdra2.getSelectedItem();
			    	
			    	result.setText(null); // clears the result pane
			    	result.setColumns(1); // puts 1 column in the result pane
			    	
			    	ResultSet cdraResult;
			    	// I warn you, this is going to get dizzying...
			    	cdraResult = stmt.executeQuery("SELECT DISTINCT (" + sqlDateB + "." + selectedCDRA + " - " + sqlDateA + "." + selectedCDRA + ") AS " + selectedCDRA
			    			+ " FROM " + sqlDateB + ", " + sqlDateA
			    			+ " WHERE " + sqlDateB + ".County = " + sqlDateA + ".County AND " + sqlDateB + ".State = " + sqlDateA + ".State AND "
			    			+ sqlDateA + ".State = '" + selectedState + "' AND " + sqlDateA + ".County = '" + selectedCounty + "';");
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
	    
	    
	    /** THIRD QUERY **/
	    /** THIRD QUERY **/
	    /** THIRD QUERY **/
	    
		// need a county dropbox, a state dropbox, two date dropboxes, and our trademark CDRA
	    // first we need a new combo-box (I've been calling them dropboxes too)... for "more", "less" or "the same"!
	    JComboBox<String> moreLess = new JComboBox<String>();
	    moreLess.addItem("SELECT");
	    moreLess.addItem("more");
	    moreLess.addItem("less");
	    moreLess.addItem("the same");
	    moreLess.setEnabled(true); // this combo-box is to ALWAYS be enabled! :)
	    
	    // confirmed (cases), deaths, recoveries, or active?
	    JComboBox<String> cdra3 = new JComboBox<String>();
		// add items to the combo box
		cdra3.addItem("SELECT");
		cdra3.addItem("confirmed");
		cdra3.addItem("deaths");
		cdra3.addItem("recovered");
		cdra3.addItem("active");
		cdra3.setEnabled(false); // this should not be enabled if no inequality operator (just above) is selected
		
	    // first date... for all other counties
	    JComboBox<String> date3a = new JComboBox<String>();
	    date3a.addItem("SELECT DAY");
	    date3a.addItem("March 24, 2020");
	    date3a.addItem("March 31, 2020");
	    date3a.addItem("April 7, 2020");
	    date3a.addItem("April 14, 2020");
	    date3a.addItem("April 21, 2020");
	    date3a.addItem("April 28, 2020");
	    date3a.addItem("May 5, 2020");
	    date3a.addItem("May 12, 2020");
	    date3a.addItem("May 19, 2020");
	    date3a.addItem("May 26, 2020");
	    date3a.addItem("June 2, 2020");
	    date3a.addItem("June 9, 2020");
	    date3a.addItem("June 16, 2020");
	    date3a.addItem("June 23, 2020");
	    date3a.addItem("June 30, 2020");
	    date3a.addItem("July 7, 2020");
	    date3a.addItem("July 14, 2020");
	    date3a.addItem("July 21, 2020");
	    date3a.setEnabled(false); // this should be disabled if no selection is made in CDRA
	    
	    // second date... for selected state/county
	    JComboBox<String> date3b = new JComboBox<String>();
	    date3b.addItem("SELECT DAY");
	    date3b.addItem("March 24, 2020");
	    date3b.addItem("March 31, 2020");
	    date3b.addItem("April 7, 2020");
	    date3b.addItem("April 14, 2020");
	    date3b.addItem("April 21, 2020");
	    date3b.addItem("April 28, 2020");
	    date3b.addItem("May 5, 2020");
	    date3b.addItem("May 12, 2020");
	    date3b.addItem("May 19, 2020");
	    date3b.addItem("May 26, 2020");
	    date3b.addItem("June 2, 2020");
	    date3b.addItem("June 9, 2020");
	    date3b.addItem("June 16, 2020");
	    date3b.addItem("June 23, 2020");
	    date3b.addItem("June 30, 2020");
	    date3b.addItem("July 7, 2020");
	    date3b.addItem("July 14, 2020");
	    date3b.addItem("July 21, 2020");
	    date3b.setEnabled(false); // this should be disabled if no first date is selected
	    
	    
	    // state combo-box
	    JComboBox<String> states3 = new JComboBox<String>();
	    states3.addItem("SELECT STATE");
	    // don't add anything until a date is selected
	    states3.setEnabled(false); // this should be disabled if no second date is selected
	    
	    // county combo-box
	    JComboBox<String> counties3 = new JComboBox<String>();
	    counties3.addItem("SELECT COUNTY");
	    // don't add anything until a state is selected
	    counties3.setEnabled(false); // this should be disabled if no state is selected
	    
	    
	    // third apply button
	    JButton apply3 = new JButton("Apply");
	    Dimension a3Size = apply3.getPreferredSize();
	    apply3.setBounds(420, 400, a2Size.width, a2Size.height);
	    apply3.setEnabled(false); // this should be disabled if no county is selected
	    
	    
	    /** Now for the action listeners **/
	    /** 1st **/
	    // ComboBox ActionListener for the inequality value
	    moreLess.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedSign = (String) moreLess.getSelectedItem();
    			
				if (selectedSign.equals("SELECT"))
				{
					cdra3.setEnabled(false);
					date3a.setEnabled(false);
					states3.setEnabled(false);
					counties3.setEnabled(false);
					date3b.setEnabled(false);
					apply3.setEnabled(false);
				}
				else
				{
					cdra3.setEnabled(true);
				}
	    	}
	    });
	    
	    /** 2nd **/
	    // ComboBox ActionListener for our special cdra
	    // when either confirmed (cases), deaths, recoveries, or active is selected
	    cdra3.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCDRA = (String) cdra3.getSelectedItem();
    			
				if (selectedCDRA.equals("SELECT"))
				{
					date3a.setEnabled(false);
					date3b.setEnabled(false);
					states3.setEnabled(false);
					counties3.setEnabled(false);
					apply3.setEnabled(false);
				}
				else
				{
					date3a.setEnabled(true);
				}
	    	}
	    });
	    
	    /** 3rd **/
		// ActionListener for the first date
		date3a.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedDateA = (String) date3a.getSelectedItem();
	    	    String sqlDateA = sqlDateFormat(selectedDateA);
	    	    
	    	    if (sqlDateA == null) // if no date is selected, remove all the states!
	    	    {
	    	    	// disable all the buttons and combo-boxes above it!
	    	    	date3b.setEnabled(false);
	    	    	states3.setEnabled(false);
	    	    	counties3.setEnabled(false);
	    	    	apply3.setEnabled(false);
	    	    	return;
	    	    }
	    	    else
	    	    {
	    	    	date3b.setEnabled(true);
	    	    }
            }
	    });
		
		/** 4th **/
		// ActionListener for the second date
		date3b.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedDateA = (String) date3a.getSelectedItem();
	    		final String selectedDateB = (String) date3b.getSelectedItem();
	    	    String sqlDateA = sqlDateFormat(selectedDateA);
	    	    String sqlDateB = sqlDateFormat(selectedDateB);
	    	    
	    	    boolean enableStates = false;
	    	    
	    	    if (sqlDateB == null) // if no date is selected, remove all the states!
	    	    {
	    	    	states3.setEnabled(false);
	    	    	counties3.setEnabled(false);
	    	    	apply3.setEnabled(false);
	    	    	// delete every item in the states combo-box except for the default
					for (int i = states3.getItemCount() - 1; i >= 0; i--)
					{
						if (!states3.getItemAt(i).equals("SELECT STATE"))
						{
							states3.removeItemAt(i);
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
	    	    	states3.setEnabled(false); // before you reset its contents
	    	    	ResultSet all_states;
	    	    	
	    	    	if (!sqlDateA.equals(sqlDateB)) // two different dates selected
	    	    	{
	    	    		all_states = stmt.executeQuery("SELECT DISTINCT " + sqlDateA + ".state FROM " + sqlDateA + ", " + sqlDateB + " WHERE " + sqlDateA + ".state = " + sqlDateB + ".state ORDER BY " + sqlDateA + ".state;");
	    	    	}
	    	    	else
	    	    	{
	    	    		all_states = stmt.executeQuery("SELECT DISTINCT state FROM " + sqlDateA + " ORDER BY state;");
	    	    		/*
	    	    		 * -- output all the states from a particular day
								SELECT DISTINCT state
								FROM March_24_2020
								ORDER BY state;
	    	    		 */
	    	    	}
					
					// delete every item in the states combo-box except for the default
					for (int i = states3.getItemCount() - 1; i >= 0; i--)
					{
						if (!states3.getItemAt(i).equals("SELECT STATE"))
						{
							states3.removeItemAt(i);
						}
					}
					
					if (enableStates)
					{
						states3.setEnabled(true);
						while (all_states.next()) // add all the states (plus DC!)
						{
							states3.addItem(all_states.getString("state"));
						}
					}
					conn.commit();
				}
	    	    catch (Exception e1)
	    	    {
					// TODO Auto-generated catch block
	    	    	System.out.println("SQL Failure\n" + e1);
					e1.printStackTrace();
				}
	    	    
            }
	    });
		
		
		/** 5th **/
	    // ComboBox ActionListener for state
	    // when a state is selected
	    states3.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedState = (String) states3.getSelectedItem();
    			final String selectedDateA = (String) date3a.getSelectedItem(); // trust me, you'll need this
    			final String selectedDateB = (String) date3b.getSelectedItem(); // trust me, you'll need this too
    			String sqlDateA = sqlDateFormat(selectedDateA);
    			String sqlDateB = sqlDateFormat(selectedDateB);
    			
    			boolean enableCounties = false;
    			
    			if (selectedState.equals("SELECT STATE"))
    			{
    				// just disable all the combo-boxes ahead!
    				counties3.setEnabled(false);
    				apply3.setEnabled(false);
    				return; // get out!
    			}
    			else
    			{
    				enableCounties = true;
    			}
    			
    			// now populate the counties combo-box with... the counties!
	    		try
	    		{
	    			counties3.setEnabled(false);
	    			ResultSet all_counties;
	    			
	    			if (!sqlDateA.equals(sqlDateB)) // two different dates selected
	    			{
		    			all_counties = stmt.executeQuery("SELECT DISTINCT " + sqlDateA + ".county AS county FROM " + sqlDateA +  ", " + sqlDateB + " WHERE " + sqlDateA + ".state = '" + selectedState + "' AND " + sqlDateB + ".state = " + sqlDateA + ".state AND " + sqlDateA + ".county = " + sqlDateB + ".county ORDER BY county;");
	    			}
	    			else // or not
	    			{
	    				all_counties = stmt.executeQuery("SELECT DISTINCT county FROM " + sqlDateA + " WHERE state = '" + selectedState + "' ORDER BY county;");
	    				/*
	    				 * SELECT DISTINCT county
							FROM March_24_2020
							WHERE state = "Alabama"
							ORDER BY county;
	    				 */
	    			}
					
					// delete every item in the counties combo-box except for the default
					for (int i = counties3.getItemCount() - 1; i >= 0; i--)
					{
						if (!counties3.getItemAt(i).equals("SELECT COUNTY"))
						{
							counties3.removeItemAt(i);
						}
					}
					
					if (enableCounties)
					{
						counties3.setEnabled(true);
						while (all_counties.next()) // add all the states (plus DC!)
						{
							counties3.addItem(all_counties.getString("county"));
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
	    
	    /** 6th **/
	    // ComboBox ActionListener for county
	    // when a county is selected
	    counties3.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		final String selectedCounty = (String) counties3.getSelectedItem();
    			
				if (selectedCounty.equals("SELECT COUNTY"))
				{
					apply3.setEnabled(false);
				}
				else
				{
					apply3.setEnabled(true);
				}
				// uh... that's it for this ActionListener! :)
	    	}
	    });
	    
	    /** 7th **/
	    apply3.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e)
	    	{
		    	// How many cases/deaths/recoveries/actives were there in this particular county on this particular day?
		    	try
		    	{
		    		final String selectedDateA = (String) date3a.getSelectedItem();
		    		final String selectedDateB = (String) date3b.getSelectedItem();
		    		String sqlDateA = sqlDateFormat(selectedDateA);
		    		String sqlDateB = sqlDateFormat(selectedDateB);
		    	    
			    	final String selectedState = (String) states3.getSelectedItem();
			    	final String selectedCounty = (String) counties3.getSelectedItem();
			    	final String selectedCDRA = (String) cdra3.getSelectedItem();
			    	
			    	final String selectedSign = (String) moreLess.getSelectedItem();
			    	String ineqSign = null;
			    	
			    	if (selectedSign.equals("more"))
			    	{
			    		ineqSign = ">";
			    	}
			    	else if (selectedSign.equals("less"))
			    	{
			    		ineqSign = "<";
			    	}
			    	else if (selectedSign.equals("the same"))
			    	{
			    		ineqSign = "=";
			    	}
			    	
			    	
			    	result.setText(null); // clears the result pane
			    	result.setColumns(1); // puts 1 column in the result pane
			    	
			    	ResultSet comparedCounties;
			    	// I warn you, this is going to get VERY dizzying...
			    	if (sqlDateA.equals(sqlDateB)) // same dates were selected
			    	{
			    		String query_line1 = "SELECT DISTINCT county, state, " + selectedCDRA + " ";
			    		String query_line2 = "FROM " + sqlDateA + " ";
			    		String query_line3 = "WHERE " + selectedCDRA + " " + ineqSign + " ";
			    		String query_line4 = "(SELECT "  + selectedCDRA + " FROM " + sqlDateA + " WHERE state = '" + selectedState + "' AND county = '" + selectedCounty + "') ";
	    				String query_line5 = "ORDER BY " + selectedCDRA + " desc;";
	    				String full_query = query_line1 + query_line2 + query_line3 + query_line4 + query_line5;
				    	comparedCounties = stmt.executeQuery(full_query);
			    		
			    		/* EXAMPLE:
			    		 * -- with only 1 date selected
							SELECT DISTINCT County, State, Confirmed
							FROM March_24_2020
							WHERE Confirmed > (SELECT Confirmed
											   FROM March_24_2020
							                   WHERE State = "Texas" AND County = "Bexar")
							ORDER BY Confirmed desc;
						*/
			    	}
			    	else // if they're not the same dates
			    	{
			    		String query_line1 = "SELECT DISTINCT " + sqlDateA + ".county, " + sqlDateA + ".state, " + sqlDateA + "." + selectedCDRA + " ";
				    	String query_line2 = "FROM " + sqlDateA + ", " + sqlDateB + " ";
				    	String query_line3 = "WHERE " + sqlDateA + "." + selectedCDRA + " " + ineqSign + " ";
				    	String query_line4 = "(SELECT " + selectedCDRA + " FROM " + sqlDateB + " WHERE state = '" + selectedState + "' AND county = '" + selectedCounty + "') ";
				    	String query_line5 = "ORDER BY " + sqlDateA + "." + selectedCDRA + " desc;";
				    	String full_query = query_line1 + query_line2 + query_line3 + query_line4 + query_line5;
				    	comparedCounties = stmt.executeQuery(full_query);
			    	}
			    		
			    	
			    	while (comparedCounties.next())
			    	{
			    		// result is the name of the JTextArea to which the query answer will be outputted
			    		result.append("There were " + comparedCounties.getInt(selectedCDRA) + " " + selectedCDRA + " in " + comparedCounties.getString("county") + " County, " + comparedCounties.getString("state") + " on " + selectedDateA + "\n");
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
	    
	    
	    /** DONE SETTING UP ALL THE QUERIES **/
	    
	    /** DONE SETTING UP ALL THE QUERIES **/
	    
	    /** DONE SETTING UP ALL THE QUERIES **/
	    
	    /** DONE SETTING UP ALL THE QUERIES **/
	    
	    /** DONE SETTING UP ALL THE QUERIES **/
	    
	    
	    
	    /** NOW TO SET UP THE DATA FRAME!!! **/
	    
	    /** NOW TO SET UP THE DATA FRAME!!! **/
	    
	    /** NOW TO SET UP THE DATA FRAME!!! **/
	    
	    /** NOW TO SET UP THE DATA FRAME!!! **/
	    
	    /** NOW TO SET UP THE DATA FRAME!!! **/
	    
	    
	    /*** Now let's setup the data frame ***/
	    
	    
	    /**  First and foremost, put up the JTextArea (inside the JScrollPane)! **/
	    result.setBounds(500, 100, 250, 350); // x-coordinate, y-coordinate, x-length, y-length
	    
	    
	    
	    /** For the first query **/
	    // "On (date), (county), (state) had how many (cases, deaths, recoveries, active)?"
	    JLabel q1_part1 = new JLabel("On");
	    Dimension q1p1size = new Dimension(150, 100);
	    q1_part1.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part1.setBounds(20, 60, q1p1size.width, q1p1size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the date combo-box
	    date1.setBounds(50, 100, 150, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // position the counties combo-box
	    counties1.setBounds(15, 130, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // next text
	    JLabel q1_part2 = new JLabel("County,");
	    Dimension q1p2size = new Dimension(150, 100);
	    q1_part2.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part2.setBounds(215, 90, q1p2size.width, q1p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the states combo-box
	    states1.setBounds(275, 130, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // next text
	    JLabel q1_part3 = new JLabel("had how many");
	    Dimension q1p3size = new Dimension(200, 100);
	    q1_part3.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part3.setBounds(20, 120, q1p2size.width, q1p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the cdra combo-box
	    cdra1.setBounds(130, 160, 125, 25);
	    
	    // one final question mark...
	    JLabel q1_part4 = new JLabel("?");
	    Dimension q1p4size = new Dimension(50, 50);
	    q1_part4.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q1_part4.setBounds(255, 120, q1p2size.width, q1p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // we've already positioned the apply button... at the start!
	    
	    
	    
	    /** For the second query... */
	    
	    // "Between (dateA) and (dateB), (county),(state) had how many more (cases/deaths/recoveries/active)?
	    JLabel q2_part1 = new JLabel("Between");
	    Dimension q2p1size = new Dimension(150, 100);
	    q2_part1.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q2_part1.setBounds(20, 180, q2p1size.width, q2p1size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // first menu
	    date2a.setBounds(80, 220, 150, 25);
	    
	    // next text
	    JLabel q2_part2 = new JLabel("and");
	    Dimension q2p2size = new Dimension(80, 50);
	    q2_part2.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q2_part2.setBounds(235, 205, q2p2size.width, q2p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // second menu
	    date2b.setBounds(270, 220, 150, 25);
	    
	    // NEXT LINE!
	    
	    // position the counties combo-box
	    counties2.setBounds(15, 250, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // next text
	    JLabel q2_part3 = new JLabel("County,");
	    Dimension q2p3size = new Dimension(150, 100);
	    q2_part3.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q2_part3.setBounds(215, 210, q2p3size.width, q2p3size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the states combo-box
	    states2.setBounds(275, 250, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // next text
	    JLabel q2_part4 = new JLabel("had how many more");
	    Dimension q2p4size = new Dimension(400, 100);
	    q2_part4.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q2_part4.setBounds(20, 240, q2p4size.width, q2p4size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // final combo-box
	    cdra2.setBounds(170, 280, 125, 25);
	    
	    // one final question mark...
	    JLabel q2_part5 = new JLabel("?");
	    Dimension q2p5size = new Dimension(50, 50);
	    q2_part5.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q2_part5.setBounds(295, 265, q2p5size.width, q2p5size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    
	    
	    /** For the third query **/
	    // "Which counties had (more/less/the same) (cases/deaths/recoveries/actives) on date1 as county, state did on (date2)?" 
	    JLabel q3_part1 = new JLabel("Which counties had");
	    Dimension q3p1size = new Dimension(400, 100);
	    q3_part1.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q3_part1.setBounds(20, 300, q3p1size.width, q3p1size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // first two menus
	    moreLess.setBounds(170, 340, 125, 25); // x-coordinate, y-coordinate, x-length, y-length
	    cdra3.setBounds(285, 340, 125, 25);
	    
	    JLabel q3_part2 = new JLabel("on");
	    Dimension q3p2size = new Dimension(100, 100);
	    q3_part2.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q3_part2.setBounds(415, 300, q3p2size.width, q3p2size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // next menu
	    date3a.setBounds(15, 370, 150, 25);
	    
	    // next text
	    JLabel q3_part3 = new JLabel("than/as");
	    Dimension q3p3size = new Dimension(100, 100);
	    q3_part3.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q3_part3.setBounds(165, 330, q3p3size.width, q3p3size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // position the counties combo-box
	    counties3.setBounds(225, 370, 200, 25); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // next text
	    JLabel q3_part4 = new JLabel("County,");
	    Dimension q3p4size = new Dimension(150, 100);
	    q3_part4.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q3_part4.setBounds(425, 330, q3p4size.width, q3p4size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    // NEXT LINE!
	    
	    // position the states combo-box
	    states3.setBounds(15, 400, 200, 25);
	    
	    // next text
	    JLabel q3_part5 = new JLabel("did on");
	    Dimension q3p5size = new Dimension(100, 100);
	    q3_part5.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q3_part5.setBounds(215, 360, q3p4size.width, q3p4size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    date3b.setBounds(260, 400, 150, 25);
	    
	    // one final question mark...
	    JLabel q3_part6 = new JLabel("?");
	    Dimension q3p6size = new Dimension(50, 50);
	    q3_part6.setFont(new Font("", Font.PLAIN, 15)); // change the font size
	    q3_part6.setBounds(410, 385, q3p6size.width, q3p6size.height); // x-coordinate, y-coordinate, x-length, y-length
	    
	    
	    
	    /** Before anything can be added... **/
	    countymenu.setLayout(null);
	    
	    
	    /** Now, add all the components! **/
	    // menu label
	    countymenu.add(current_loc);
	    // button for main menu
	    countymenu.add(to_main);
	    // text area
	    // countymenu.add(result); // JTextArea, in a JPanel. Can I get any scroll-bars?
	    countymenu.add(scrollable);
	    
	    // text for the first query
	    countymenu.add(q1_part1);
	    countymenu.add(q1_part2);
	    countymenu.add(q1_part3);
	    countymenu.add(q1_part4);
	    // combo-boxes for first query
	    countymenu.add(date1);
	    countymenu.add(counties1);
	    countymenu.add(states1);
	    countymenu.add(cdra1);
	    // apply button
	    countymenu.add(apply1);
	    
	    // text for the second query
	    countymenu.add(q2_part1);
	    countymenu.add(q2_part2);
	    countymenu.add(q2_part3);
	    countymenu.add(q2_part4);
	    countymenu.add(q2_part5);
	    // combo-boxes for second query
	    countymenu.add(date2a);
	    countymenu.add(date2b);
	    countymenu.add(counties2);
	    countymenu.add(states2);
	    countymenu.add(cdra2);
	    // apply button
	    countymenu.add(apply2);
	    
	    // text for the third query
	    countymenu.add(q3_part1);
	    countymenu.add(q3_part2);
	    countymenu.add(q3_part3);
	    countymenu.add(q3_part4);
	    countymenu.add(q3_part5);
	    countymenu.add(q3_part6);
	    // combo-boxes for the third query
	    countymenu.add(moreLess);
	    countymenu.add(cdra3);
	    countymenu.add(date3a);
	    countymenu.add(counties3);
	    countymenu.add(states3);
	    countymenu.add(date3b);
	    // apply button
	    countymenu.add(apply3);
	    
	    
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

-- How many new cases in St. Tammany Parish, Louisiana arose between
-- March 31 and April 21?
SELECT DISTINCT (April_21_2020.Confirmed - March_31_2020.Confirmed) AS newcases
FROM April_21_2020, March_31_2020
WHERE April_21_2020.County = March_31_2020.County AND April_21_2020.State = March_31_2020.State AND
March_31_2020.State = "Louisiana" AND March_31_2020.County = "St. Tammany";

-- Which counties had more confirmed cases on April 7 than
-- Bexar County, Texas did on March 24?
SELECT DISTINCT April_07_2020.County, April_07_2020.State, April_07_2020.Confirmed
FROM April_07_2020, March_24_2020
WHERE April_07_2020.Confirmed > (SELECT Confirmed
								 FROM March_24_2020
								 WHERE County = "Bexar")
								 
-- with only 1 date selected
SELECT DISTINCT County, State, Confirmed
FROM March_24_2020
WHERE Confirmed > (SELECT Confirmed
				   FROM March_24_2020
                   WHERE State = "Texas" AND County = "Bexar")
ORDER BY Confirmed desc;
***/
