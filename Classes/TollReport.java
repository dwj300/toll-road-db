// Programmer:  Douglas Jordan
// Section:     1
// Program:     Project03-Toll Road System (TollReport.java)
// Date:        12/9/09
// Description: This class is the our special feature. It provides some statistics about the exits.

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class TollReport extends JApplet
{
    private final String[] COLUMN_NAMES = {"Exit Name", "Profit", "Prequency of Car", 
                                           "Frequency of Truck", "Frequency of Transmitter",
                                           "Frequency of Ticket"};             // Names of columns

    private final int JFRAME_WIDTH = 700;                                      // Width of JFrame
    private final int JFRAME_HEIGHT = 500;                                     // Height of JFrame

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";   // URL of the database
    private final String DBUSER = "root";                                      // Username of database
    private final String DBPASS = "root";                                      // Password of database

    private Connection dbConnection;                                           // Connection to the db

    private Statement transactionsStatement;                                   // Statement for trans.

    private ResultSet transactionsResults;                                     // Results for transact.

    private String transactionsQuerry;                                         // Querry for transact.

    private JFrame window;                                                     //Popup window Jframe

    private JPanel tablePanel;                                                 // Panel for the table
    private JPanel mainPanel;

    private BorderLayout tableLayout;                                          // Layout for table panel
    private BorderLayout appletLayout;                                         // Layout for the window

    private JTable reportTable;                                                // JTable to display exit
                                                                               //   stats.
    private Object[][] data = new Object[20][6];                               // Null array to populate
                                                                               //   the table
    public TollReport()
    // POST: The database connection is initialized.
    {
        startDBConnection();
    }

    @Override
    public void init()
    // POST: The window and gui are setup, and the update method is called.
    {
        window = new JFrame("Toll Report");

        // INITIALIZE PANELS
        tablePanel = new JPanel();
        mainPanel = new JPanel();

        // INITIALIZE LAYOUTS
        tableLayout = new BorderLayout();
        appletLayout = new BorderLayout();

        // SET LAYOUTS
        tablePanel.setLayout(tableLayout);
        mainPanel.setLayout(appletLayout);

        // SET UP TABLE / TABLE PANEL
        reportTable = new JTable(data, COLUMN_NAMES);
        tablePanel.add(reportTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(reportTable, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // SETUP WINDOW
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        window.setVisible(true);
        window.setResizable(false);
        window.add(mainPanel);
        setSize(0,0);

        // POPULATE TABLE
        update();
    }

    private void startDBConnection()
    // POST: The database connection is setup and initialized, and the statement object is initialized
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER, DBPASS);
            transactionsStatement = dbConnection.createStatement();
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

    private void update()
    // POST: The table is populated with the statistics from the transactions table.
    {
        // TEMPORARY VARIABLES
        int maxExit = 0;
        int row = 0;
        int car = 0;
        int truck = 0;
        int classTotal = 0;
        int transmitters = 0;
        int tickets = 0;
        int paymentTotal = 0;
        try
        {
            transactionsQuerry = "select max(EXIT_ID) from transactions";
            transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
            if(transactionsResults.next())
            {
                maxExit = transactionsResults.getInt("1");
            }

            for(int currentExit = 0; currentExit <= maxExit; currentExit++)
            {
                transactionsQuerry = "select TRANSACTION_ID from transactions where exit_id = " +
                        currentExit;
                transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                            

                if(transactionsResults.next() == true) // If the exit actually was in the transactions
                                                       //    table
                {
                    transactionsQuerry = "select EXIT_NUMBER A from EXITS where exit_id = " +
                            currentExit;
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    reportTable.setValueAt(transactionsResults.getString("A"), row, 0);

                    transactionsQuerry = "select sum(AMOUNT_PAID) A from transactions where exit_id = "
                            + currentExit;
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    reportTable.setValueAt(transactionsResults.getDouble("A"), row, 1);

                    transactionsQuerry = "select count(*) A from transactions where class = 'Car' and" +
                            " exit_id = " + currentExit;
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    car = transactionsResults.getInt("A");

                    transactionsQuerry = "select count(*) A from transactions where class = 'Truck' " +
                            "and exit_id = " + currentExit;
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    truck = transactionsResults.getInt("A");

                    classTotal = car + truck;

                    reportTable.setValueAt("" + Integer.toString(car) + " / " +
                            Integer.toString(classTotal), row, 2);
                    reportTable.setValueAt("" + Integer.toString(truck) + " / " +
                            Integer.toString(classTotal), row, 3);
                   
                    transactionsQuerry = "select count(*) A from transactions where payment_type = " +
                            "'Transmitter' and exit_id = " + currentExit;
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    transmitters = transactionsResults.getInt("A");

                    transactionsQuerry = "select count(*) A from transactions where payment_type = " +
                            "'Ticket' and exit_id = " + currentExit;
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    tickets = transactionsResults.getInt("A");

                    paymentTotal = transmitters + tickets;

                    reportTable.setValueAt("" + Integer.toString(transmitters) + " / " +
                            Integer.toString(paymentTotal), row, 4);
                    reportTable.setValueAt("" + Integer.toString(tickets) + " / " +
                            Integer.toString(paymentTotal), row, 5);

                    row++;
                }
            }
        }
        catch (SQLException sqle)
        {
            System.out.println("A SQL exception has occurered");
        }
    }

        @Override
    public void destroy()
    //POST: The database related objects are closed.
    {
        try
        {
            dbConnection.close();
            transactionsStatement.close();
            transactionsResults.close();
        }
        catch(SQLException e)
        {

        }
    }
}
