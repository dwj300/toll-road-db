// Programmer:  Douglas Jordan
// Section:     1
// Program:     Project03-Toll Road System (AccountStatement.java)
// Date:        12/9/09
// Description: This class displayes the Customers address, and shows the history in a table.

import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

public class AccountStatement extends JApplet implements ActionListener
{
    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";   // URL of the db
    private final String DBUSER = "root";                                      // Username for the db
    private final String DBPASS = "root";
    private final String[] COLUMN_NAMES = {"Start exit", "End exit", "Date", "Payment", "Class", 
                                           "Status"};                          // Column names for table

    private final int JFRAME_WIDTH = 475;                                      // Width of JFrame
    private final int JFRAME_HEIGHT = 600;                                     // Height of JFrame

    private Object[][] data = new Object[20][6];                               // Null array of objects
                                                                               //   to populate table.

    private JTable historyTable;                                               // JTable to display hist

    private Connection dbConnection;                                           // Connection to db

    private Statement statementNames;                                          // Statement for names
    private Statement statementTransmitters;                                   // Statement for transmit
    private Statement statementAccountBalance;                                 // Statement for balance
    private Statement statementHistory;                                        // Statement for history
    private Statement statementAddress;                                        // Statement for address

    private ResultSet resultsNames;                                            // Results for names
    private ResultSet resultsTransmitters;                                     // Results for transmit.
    private ResultSet resultsAccountBalance;                                   // Results for balance
    private ResultSet resultsAccountAddress;                                   // Results for address
    private ResultSet resultsAccountHistory;                                   // Results for history

    private JFrame window;                                                     // Window JFrame

    private JPanel customersPanel;                                             // Panel for customer CB
    private JPanel mainPanel;                                                  // Panel for window
    private JPanel tablePanel;                                                 // Panel for table
    private JPanel infoPanel;                                                  // Panel for information

    private FlowLayout mainFlowLayout;                                         // General flow layout
    private BorderLayout tableLayout;                                          // BorderLayout for table

    private JComboBox custNamesDropDown;                                       // JCB for cust. names
    private JComboBox custTransmittersDropDown;                                // JCB for transmitters

    private JLabel custNameLabel;                                              // Label for cust. name
    private JLabel custTransmitterLabel;                                       // Label for cust. trans.
    private JLabel custAccountBalanceLabel;                                    // Label for balance
    private JLabel custAccountBalance;                                         // Acocunt balance
    private JLabel custAddressLabel;                                           // Label for address
    private JLabel custAddress;                                                // Customer Address
 
    private DecimalFormat currencyFormat = new DecimalFormat("$###,###.00");   // Format for balance

    private String address;                                                    // current address

    private int transmitter_id;                                                // current trans. id

    public AccountStatement()
    // POST: The DB connection is set up, and the statement objects are initialized
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();    // Set up driver
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);  // Start connection
            
            // INITIALIZE STATEMENTS
            statementNames = dbConnection.createStatement();
            statementTransmitters = dbConnection.createStatement();
            statementAccountBalance = dbConnection.createStatement();
            statementHistory = dbConnection.createStatement();
            statementAddress = dbConnection.createStatement();
        }
        catch(InstantiationException ie)
        {
            System.out.println("An Instantiation exception has occurred");
        }
        catch(IllegalAccessException iae)
        {
            System.out.println("Am IllegalAccess exception has occurred");
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println("A class not found exception has occurred");
        }
        catch(SQLException sqle)
        {
            System.out.println("A SQL exception has occurred");
        }
    }

    @Override
    public void init()
    // POST: The window and the GUI are set up, and the table and combo boxes are populated.
    {
        window = new JFrame("Account Statement");

        // INITIALIZE PANELS
        customersPanel = new JPanel();
        mainPanel = new JPanel();
        tablePanel = new JPanel();
        infoPanel = new JPanel();

        // INITIALIZE LAYOUTS
        mainFlowLayout = new FlowLayout();
        tableLayout = new BorderLayout();

        // SET LAYOUTS
        tablePanel.setLayout(tableLayout);
        customersPanel.setLayout(mainFlowLayout);
        mainPanel.setLayout(mainFlowLayout);
        infoPanel.setLayout(mainFlowLayout);

        // INITIALIZE COMBO BOXES
        custNamesDropDown = new JComboBox();
        custNamesDropDown.setMaximumRowCount(5);
        custNamesDropDown.addActionListener(this);
        custTransmittersDropDown = new JComboBox();
        custTransmittersDropDown.setMaximumRowCount(5);
        custTransmittersDropDown.addActionListener(this);

        // INITIALIZE LABELS
        custNameLabel = new JLabel("Select Customer: ");
        custTransmitterLabel = new JLabel("Select Transmitter: ");
        custAccountBalanceLabel = new JLabel("Account Balance: ");
        custAccountBalance = new JLabel("");
        custAddressLabel = new JLabel("Address: ");
        custAddress = new JLabel("");

        // SET UP TABLE AND ADD IT TO TABLE PANEL
        historyTable = new JTable(data, COLUMN_NAMES);
        tablePanel.add(historyTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(historyTable, BorderLayout.CENTER);

        // ADD CUI COMPONENTS TO CUST. PANEL
        customersPanel.add(custNameLabel);
        customersPanel.add(custNamesDropDown);
        customersPanel.add(custTransmitterLabel);
        customersPanel.add(custTransmittersDropDown);

        // ADD GUI COMPONENTS TO INFO PANEL
        infoPanel.add(custAccountBalanceLabel);
        infoPanel.add(custAccountBalance);
        infoPanel.add(custAddressLabel);
        infoPanel.add(custAddress);

        // ADD PANELS TO MAIN PANEL
        mainPanel.add(customersPanel);
        mainPanel.add(infoPanel);
        mainPanel.add(tablePanel);

        // SET UP WINDOW
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        window.setVisible(true);
        window.setResizable(false);
        window.add(mainPanel);
        setSize(0,0);

        // POPULATE CUSTOMERS DROPDOWN
        try
        {
            custNamesDropDown.removeAllItems();
            resultsNames = statementNames.executeQuery("select name from customers");
            while(resultsNames.next())
            {
                custNamesDropDown.addItem(resultsNames.getString("name"));
            }
        }
        catch(SQLException sqle)
        {
            System.out.println("A SQL exception has occurred");
        }

        update();     // Populate Table
    }

    public void actionPerformed(ActionEvent e)
    // PRE: e is not null
    // POST: The combo boxes are set up and populated.
    {
        if(e.getSource() == custNamesDropDown)
        {
            try
            {
                custTransmittersDropDown.removeAllItems();
                resultsTransmitters = statementTransmitters.executeQuery("select transmitter_id from " +
                        "transmitters where customer_id = (select customer_id from customers where " +
                        "name = '" + custNamesDropDown.getSelectedItem().toString() + "')");
                while(resultsTransmitters.next())
                {
                    custTransmittersDropDown.addItem(resultsTransmitters.getString("transmitter_id"));
                }
            }
            catch(SQLException sqle)
            {
                System.out.println("A SQL exception has occurred");
            }
        }

        if(e.getSource() == custTransmittersDropDown)
        {
            try
            {
                try
                {
                    transmitter_id = Integer.parseInt(custTransmittersDropDown.
                            getSelectedItem().toString());
                    resultsAccountBalance = statementAccountBalance.executeQuery("select " +
                            "account_balance from transmitters where transmitter_id = " +
                            transmitter_id);
                    resultsAccountAddress = statementAddress.executeQuery("select ADDRESS from " +
                            "CUSTOMERS where CUSTOMER_ID = (select CUSTOMER_ID from TRANSMITTERS " +
                            "where TRANSMITTER_ID = " + transmitter_id + ")");

                    update();
                }
                catch(NullPointerException npe)
                {
                    System.out.println("A null pointer exception has occurred");
                }
                while(resultsAccountBalance.next())
                {
                    custAccountBalance.setText(currencyFormat.format(resultsAccountBalance.getDouble("account_balance")));
                }
                while(resultsAccountAddress.next())
                {
                    address = resultsAccountAddress.getString("ADDRESS");
                    custAddress.setText(address);
                }
            }
            catch(SQLException sqle)
            {
                System.out.println("A SQL exception has occurred");
            }
        }
    }

    public void update()
    // POST: The history table is populated based on the transmitter
    {
        String tempQuerry = "";
        int row = 0;

        clearTable();

        try
        {
            tempQuerry = "select (select EXIT_NUMBER from EXITS where EXIT_ID = t.START_EXIT_ID) " +
                    "START_EXIT, (select EXIT_NUMBER from EXITS where EXIT_ID = t.END_EXIT_ID) " +
                    "END_EXIT, DATE, PAYMENT_TYPE, CLASS, STATUS from TRIPS t where TRANSMITTER_ID = " +
                    transmitter_id;
            resultsAccountHistory = statementHistory.executeQuery(tempQuerry);

            while(resultsAccountHistory.next())
            {
                historyTable.setValueAt(resultsAccountHistory.getString("START_EXIT"), row, 0);
                historyTable.setValueAt(resultsAccountHistory.getString("END_EXIT"), row, 1);
                historyTable.setValueAt(resultsAccountHistory.getString("DATE"), row, 2);
                historyTable.setValueAt(resultsAccountHistory.getString("PAYMENT_TYPE"), row, 3);
                historyTable.setValueAt(resultsAccountHistory.getString("CLASS"), row, 4);
                historyTable.setValueAt(resultsAccountHistory.getString("STATUS"), row, 5);

                row++;
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

    private void clearTable()
    // POST: All cells of the table are set to null.
    {
        for(int r = 0; r < 20; r++)
        {
            for(int c = 0; c < 6; c++)
            {
                historyTable.setValueAt(null, r, c);
            }
        }     
    }

    @Override
    public void destroy()  // Called when the window is closed.
    // POST: All database objects are closed.
    {
        try
        {
            dbConnection.close();
            statementNames.close();
            statementTransmitters.close();
            statementAccountBalance.close();
            statementHistory.close();
            statementAddress.close();

            resultsNames.close();
            resultsTransmitters.close();
            resultsAccountBalance.close();
            resultsAccountAddress.close();
            resultsAccountHistory.close();
        }
        catch(SQLException e)
        {

        }
    }
}
