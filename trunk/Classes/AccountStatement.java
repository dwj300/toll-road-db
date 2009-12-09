import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.table.TableModel;

public class AccountStatement extends JApplet implements ActionListener
{
    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";
    private final String[] COLUMN_NAMES = {"Start exit", "End exit", "Date", "Payment", "Class", "Status"};

    private final int JFRAME_WIDTH = 475;
    private final int JFRAME_HEIGHT = 600;

    private Object[][] data = new Object[20][6];

    private JTable historyTable;

    private Connection dbConnection;

    private Statement statementNames;
    private Statement statementTransmitters;
    private Statement statementAccountBalance;
    private Statement statementHistory;
    private Statement statementAddress;

    private ResultSet resultsNames;
    private ResultSet resultsTransmitters;
    private ResultSet resultsAccountBalance;
    private ResultSet resultsAccountAddress;
    private ResultSet resultsAccountHistory;

    private JFrame window;

    private JPanel customersPanel;
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel infoPanel;

    private FlowLayout mainFlowLayout;
    private BorderLayout tableLayout;

    private JComboBox custNamesDropDown;
    private JComboBox custTransmittersDropDown;

    private JLabel custNameLabel;
    private JLabel custTransmitterLabel;
    private JLabel custAccountBalanceLabel;
    private JLabel custAccountBalance;
    private JLabel custAddressLabel;
    private JLabel custAddress;
 
    private DecimalFormat currencyFormat = new DecimalFormat("$###,###.00");

    private String currentBalance;
    private String address;

    private int transmitter_id;

    public AccountStatement()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);
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
    {
        window = new JFrame("Account Statement");

        customersPanel = new JPanel();
        mainPanel = new JPanel();
        tablePanel = new JPanel();
        infoPanel = new JPanel();

        mainFlowLayout = new FlowLayout();
        tableLayout = new BorderLayout();

        tablePanel.setLayout(tableLayout);
        customersPanel.setLayout(mainFlowLayout);
        mainPanel.setLayout(mainFlowLayout);
        infoPanel.setLayout(mainFlowLayout);

        custNamesDropDown = new JComboBox();
        custNamesDropDown.setMaximumRowCount(5);
        custNamesDropDown.addActionListener(this);

        custTransmittersDropDown = new JComboBox();
        custTransmittersDropDown.setMaximumRowCount(5);
        custTransmittersDropDown.addActionListener(this);

        custNameLabel = new JLabel("Select Customer: ");
        custTransmitterLabel = new JLabel("Select Transmitter: ");
        custAccountBalanceLabel = new JLabel("Account Balance: ");
        custAccountBalance = new JLabel("");
        custAddressLabel = new JLabel("Address: ");
        custAddress = new JLabel("");

        historyTable = new JTable(data, COLUMN_NAMES);

        tablePanel.add(historyTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(historyTable, BorderLayout.CENTER);

        customersPanel.add(custNameLabel);
        customersPanel.add(custNamesDropDown);
        customersPanel.add(custTransmitterLabel);
        customersPanel.add(custTransmittersDropDown);

        infoPanel.add(custAccountBalanceLabel);
        infoPanel.add(custAccountBalance);
        infoPanel.add(custAddressLabel);
        infoPanel.add(custAddress);

        tablePanel.add(historyTable);

        mainPanel.add(customersPanel);
        mainPanel.add(infoPanel);
        mainPanel.add(tablePanel);
 
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        window.setVisible(true);
        window.setResizable(false);
        window.add(mainPanel);
        setSize(0,0);

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

        update();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == custNamesDropDown)
        {
            try
            {
                custTransmittersDropDown.removeAllItems();
                resultsTransmitters = statementTransmitters.executeQuery("select transmitter_id from transmitters where customer_id = (select customer_id from customers where name = '" + custNamesDropDown.getSelectedItem().toString() + "')");
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
                    transmitter_id = Integer.parseInt(custTransmittersDropDown.getSelectedItem().toString());
                    resultsAccountBalance = statementAccountBalance.executeQuery("select account_balance from transmitters where transmitter_id = " + transmitter_id);
                    resultsAccountAddress = statementAddress.executeQuery("select ADDRESS from CUSTOMERS where CUSTOMER_ID = (select CUSTOMER_ID from TRANSMITTERS where TRANSMITTER_ID = " + transmitter_id + ")");

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
                    System.out.println(address);
                }
            }
            catch(SQLException sqle)
            {
                System.out.println("A SQL exception has occurred");
            }
        }
    }

    public void update()
    {
        String tempQuerry = "";
        int row = 0;
        historyTable.removeRowSelectionInterval(0, historyTable.getRowCount() - 1);
        try
        {
            tempQuerry = "select (select EXIT_NUMBER from EXITS where EXIT_ID = t.START_EXIT_ID) START_EXIT, (select EXIT_NUMBER from EXITS where EXIT_ID = t.END_EXIT_ID) END_EXIT, DATE, PAYMENT_TYPE, CLASS, STATUS from TRIPS t where TRANSMITTER_ID = " + transmitter_id;
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
}
