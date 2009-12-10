// Programmer:  Douglas Jordan
// Section:     1
// Program:     Project03-Toll Road System (Applet.java)
// Date:        12/9/09
// Description: This class is the main interface for the database. It has
//              buttons to start and end trips as well as deposit money. It also
//              displays all trips from the trips database table in a JTable.

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Applet extends JApplet implements ActionListener
{
    private final String[] COLUMN_NAMES = {"Start exit", // Names of the columns in the table.
                                           "End exit",   
                                           "Date",
                                           "Payment",
                                           "Class",
                                           "Status",
                                           "Customer"};


    private final String DBURL = 
            "jdbc:derby://localhost:1527/Toll-Road-DB";  // URL of the database
    private final String DBUSER = "root";                // User name for the db
    private final String DBPASS = "root";                // Password for the db

    private final int APPLET_WIDTH = 600;                // Width of the applet
    private final int APPLET_HEIGHT = 400;               // Height of the applet

    private int rowCounter;                              // Current row

    private Connection dbConnection;                     // Connection to the db

    private Statement tripsStatement;                    // Trips statement object

    private ResultSet tripsResults;                      // Results set for trip

    private String tripsQuerry;                          // Querry for trips

    private JButton startButton;                         // Start button
    private JButton endButton;                           // End button
    private JButton depositButton;                       // Deposit money button
    private JButton statementButton;                     // Account statement button
    private JButton tollReportButton;                    // Toll report(special feature) button

    private BorderLayout appletLayout;                   // Layout for the applet
    private BorderLayout tablePanelLayout;               // Layout for the table panel
    private FlowLayout buttonPanelLayout;                // Layout for the button panel

    private JPanel buttonPanel;                          // Panel for the buttons
    private JPanel tablePanel;                           // Panel for the table

    private JTable tripsTable;                           // JTable to display the trips

    private Object[][] data = new Object[20][7];         // Null array of objects to initialize the tbl.

    @Override
    public void init()
    // POST: The applet and gui are set up, and the database connection method is called. update is then
    //       called.
    {
        setSize(APPLET_WIDTH, APPLET_HEIGHT);

        // INITIALIZE BUTTONS
        startButton = new JButton("Start trip");
        endButton = new JButton("End trip");
        depositButton = new JButton("Deposit money");
        statementButton = new JButton("Account Statement");
        tollReportButton = new JButton("Toll Report");

        // INITIALIZE LAYOUTS
        appletLayout = new BorderLayout();
        tablePanelLayout = new BorderLayout();
        buttonPanelLayout = new FlowLayout();

        // INITIALIZE PANELS
        buttonPanel = new JPanel();
        tablePanel = new JPanel();

        tripsTable = new JTable(data, COLUMN_NAMES);

        // SET LAYOUTS
        setLayout(appletLayout);
        buttonPanel.setLayout(buttonPanelLayout);
        tablePanel.setLayout(tablePanelLayout);

        // ADD ACTION LISTENERS
        startButton.addActionListener(this);
        endButton.addActionListener(this);
        depositButton.addActionListener(this);
        statementButton.addActionListener(this);
        tollReportButton.addActionListener(this);

        // ADD BUTTONS TO BUTTON_PANEL
        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(statementButton);
        buttonPanel.add(tollReportButton);

        // ADD TABLE OBJECTS TO TABLE PANEL
        tablePanel.add(tripsTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(tripsTable, BorderLayout.CENTER);

        // ADD PANELS TO APPLET
        add(buttonPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        startDBConnection();  //  Start DB connection
        update();             // Update the table.
    }

    private void startDBConnection()
    // POST: The database connection is setup, and tripsStatement is initialized.
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER, DBPASS);
            tripsStatement = dbConnection.createStatement();
        }
        catch (InstantiationException ie)
        {
            System.out.println("An instantiation exception has occurered");
        }
        catch (IllegalAccessException iae)
        {
            System.out.println("An illegal access exception has occurered");
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println("A class not found exception has occurred");
        }
        catch(SQLException sqle)
        {
            System.out.println("A SQL exception has occurered");
        }
        catch(Exception e)
        {
            System.out.println("An unknown error has occurred.");
        }
    }

    public void update()
    // POST: The database is querried, and the table is updated to display those results.
    {
        rowCounter = 0;    // Reset row counter
        tripsQuerry = "";  // Reset querry

        try
        {
            tripsQuerry = "select (select EXIT_NUMBER from EXITS where EXIT_ID = t.START_EXIT_ID) " +
                          "START_EXIT, (select EXIT_NUMBER from EXITS where EXIT_ID = t.END_EXIT_ID) " +
                          "END_EXIT, DATE, PAYMENT_TYPE, CLASS, STATUS, (select NAME from CUSTOMERS c " +
                          "where CUSTOMER_ID = (select CUSTOMER_ID from TRANSMITTERS where " +
                          "TRANSMITTER_ID = t.TRANSMITTER_ID)) CUSTOMER  from TRIPS t";

            tripsResults = tripsStatement.executeQuery(tripsQuerry);
            
            while(tripsResults.next())  // While there is another row
            {
                // SET VALUES
                tripsTable.setValueAt(tripsResults.getString("START_EXIT"), rowCounter, 0);
                tripsTable.setValueAt(tripsResults.getString("END_EXIT"), rowCounter, 1);
                tripsTable.setValueAt(tripsResults.getString("DATE"), rowCounter, 2);
                tripsTable.setValueAt(tripsResults.getString("PAYMENT_TYPE"), rowCounter, 3);
                tripsTable.setValueAt(tripsResults.getString("CLASS"), rowCounter, 4);
                tripsTable.setValueAt(tripsResults.getString("STATUS"), rowCounter, 5);
                tripsTable.setValueAt(tripsResults.getString("CUSTOMER"), rowCounter, 6);

                rowCounter++;
            }
        }
        catch (SQLException sqle)
        {
            System.out.println("A SQLException has occurred.");
        }
        catch (Exception e)
        {
            System.out.println("An unknown error has occurred.");
        }
    }

    public void actionPerformed(ActionEvent e)
    // PRE: e is not null.
    // POST: The respective applet is launched based on which button is pressed.
    {
        if(e.getSource() == startButton)
        {
            StartTrip start = new StartTrip(this); // Called with a reference to "this" so that it can
            start.init();                          //   call update.
        }
        else if(e.getSource() == endButton)
        {
            EndTrip end = new EndTrip(this);  // Called with a reference to "this" so that it can call
                                              //    update.
            end.init();
        }
        else if(e.getSource() == depositButton)
        {
            CustomerManagement deposit = new CustomerManagement();
            deposit.init();
        }
        else if(e.getSource() == statementButton)
        {
            AccountStatement statement = new AccountStatement();
            statement.init();
        }
        else if(e.getSource() == tollReportButton)
        {
            TollReport report = new TollReport();
            report.init();
        }
    }

    @Override
    public void destroy() // Called when the exit button is pressed.
    // POST: All database-related objects are closed, before the applet is closed.
    {
        try
        {
            dbConnection.close();
            tripsStatement.close();
            tripsResults.close();
        }
        catch(SQLException e)
        {

        }
    }
}