import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

public class AccountStatement extends JApplet implements ActionListener
{
    private final String[] COLUMN_NAMES = {"Start exit", "End exit", "Date", 
                                           "Payment", "Class", "Status"};
    private Object[][] data = new Object[20][6];

    private final int JFRAME_WIDTH = 475;
    private final int JFRAME_HEIGHT = 600;

    private JTable historyTable;

    private Connection dbConnection;

    private Statement sqlStatementNames;
    private Statement sqlStatementTransmitters;
    private Statement sqlStatementAccountBalance;
    private Statement sqlStatementAddress;
    private Statement sqlStatementHistory;

    private ResultSet sqlResultsNames;
    private ResultSet sqlResultsTransmitters;
    private ResultSet sqlResultsAccountBalance;
    private ResultSet sqlResultsAccountAddress;
    private ResultSet sqlResultsAccountHistory;


    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private JFrame jfCustWindow;

    private JPanel jpCustRecords;
    private JPanel jpMain;
    private JPanel jpTable;
    private JPanel jpInfo;

    private FlowLayout flMain;
    private BorderLayout tableLayout;

    private JComboBox jcbCustNames;
    private JComboBox jcbCustTransmitters;

    private JLabel jlCustName;
    private JLabel jlCustTransmitter;
    private JLabel jlCustAccountBalanceLabel;
    private JLabel jlCustAccountBalance;
    private JLabel jlCustAddressLabel;
    private JLabel jlCustAddress;
 
   
    private DecimalFormat currencyFormat = new DecimalFormat("$###,###.00");

    private String strCurrentBalance;
    private String strAddress;

    private int transmitter_id;

    public AccountStatement()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);
            sqlStatementNames = dbConnection.createStatement();
            sqlStatementTransmitters = dbConnection.createStatement();
            sqlStatementAccountBalance = dbConnection.createStatement();
            sqlStatementAddress = dbConnection.createStatement();
            sqlStatementHistory = dbConnection.createStatement();
        }
        catch(InstantiationException ie)
        {
            System.out.println("ie");
        }
        catch(IllegalAccessException iae)
        {
            System.out.println("iae");
        }
        catch(ClassNotFoundException cnfe)
        {
            System.out.println("cnfe");
        }
        catch(SQLException sqle)
        {
            System.out.println("sqle");
        }
    }

    @Override
    public void init()
    {
        jfCustWindow = new JFrame("Account Statement");

        jpCustRecords = new JPanel();
        jpMain = new JPanel();
        jpTable = new JPanel();
        jpInfo = new JPanel();

        flMain = new FlowLayout();
        tableLayout = new BorderLayout();

        jpTable.setLayout(tableLayout);
        jpCustRecords.setLayout(flMain);
        jpMain.setLayout(flMain);
        jpInfo.setLayout(flMain);

        jcbCustNames = new JComboBox();
        jcbCustNames.setMaximumRowCount(5);
        jcbCustNames.addActionListener(this);

        jcbCustTransmitters = new JComboBox();
        jcbCustTransmitters.setMaximumRowCount(5);
        jcbCustTransmitters.addActionListener(this);

        jlCustName = new JLabel("Select Customer: ");
        jlCustTransmitter = new JLabel("Select Transmitter: ");
        jlCustAccountBalanceLabel = new JLabel("Account Balance: ");
        jlCustAccountBalance = new JLabel("");
        jlCustAddressLabel = new JLabel("Address: ");
        jlCustAddress = new JLabel("");





        historyTable = new JTable(data, COLUMN_NAMES);

        jpTable.add(historyTable.getTableHeader(), BorderLayout.NORTH);
        jpTable.add(historyTable, BorderLayout.CENTER);


        jpCustRecords.add(jlCustName);
        jpCustRecords.add(jcbCustNames);
        jpCustRecords.add(jlCustTransmitter);
        jpCustRecords.add(jcbCustTransmitters);

        jpInfo.add(jlCustAccountBalanceLabel);
        jpInfo.add(jlCustAccountBalance);
        jpInfo.add(jlCustAddressLabel);
        jpInfo.add(jlCustAddress);

        jpTable.add(historyTable);

        
        jpMain.add(jpCustRecords);
        jpMain.add(jpInfo);
        jpMain.add(jpTable);
 
        jfCustWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfCustWindow.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        jfCustWindow.setVisible(true);
        jfCustWindow.setResizable(false);
        jfCustWindow.add(jpMain);
        setSize(0,0);

        try
        {
            jcbCustNames.removeAllItems();
            sqlResultsNames = sqlStatementNames.executeQuery("select name from customers");
            while(sqlResultsNames.next())
            {
                jcbCustNames.addItem(sqlResultsNames.getString("name"));
            }
        }
        catch(SQLException sqle)
        {

        }
        update();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == jcbCustNames)
        {
            try
            {
                jcbCustTransmitters.removeAllItems();
                sqlResultsTransmitters = sqlStatementTransmitters.executeQuery("select transmitter_id from transmitters where customer_id = (select customer_id from customers where name = '" + jcbCustNames.getSelectedItem().toString() + "')");
                while(sqlResultsTransmitters.next())
                {
                    jcbCustTransmitters.addItem(sqlResultsTransmitters.getString("transmitter_id"));
                }
            }
            catch(SQLException sqle)
            {

            }
        }

        if(e.getSource() == jcbCustTransmitters)
        {
            try
            {
                try
                {
                    transmitter_id = Integer.parseInt(jcbCustTransmitters.getSelectedItem().toString());
                    sqlResultsAccountBalance = sqlStatementAccountBalance.executeQuery("select account_balance from transmitters where transmitter_id = " + transmitter_id);
                }
                catch(NullPointerException npe)
                {

                }
                while(sqlResultsAccountBalance.next())
                {
                    jlCustAccountBalance.setText(currencyFormat.format(sqlResultsAccountBalance.getDouble("account_balance")));
                }
            }
            catch(SQLException sqle)
            {

            }
        }
    }

    public void update()
    {
        String tempQuerry = "";
        int row = 0;
        try
        {
            tempQuerry = "select (select EXIT_NUMBER from EXITS where EXIT_ID = t.START_EXIT_ID) START_EXIT, (select EXIT_NUMBER from EXITS where EXIT_ID = t.END_EXIT_ID) END_EXIT, DATE, PAYMENT_TYPE, CLASS, STATUS from TRIPS t where TRANSMITTER_ID = " + transmitter_id;
            sqlResultsAccountHistory = sqlStatementHistory.executeQuery(tempQuerry);

            while(sqlResultsAccountHistory.next())
            {
                historyTable.setValueAt(sqlResultsAccountHistory.getString("START_EXIT"), row, 0);
                historyTable.setValueAt(sqlResultsAccountHistory.getString("END_EXIT"), row, 1);
                historyTable.setValueAt(sqlResultsAccountHistory.getString("DATE"), row, 2);
                historyTable.setValueAt(sqlResultsAccountHistory.getString("PAYMENT_TYPE"), row, 3);
                historyTable.setValueAt(sqlResultsAccountHistory.getString("CLASS"), row, 4);
                historyTable.setValueAt(sqlResultsAccountHistory.getString("STATUS"), row, 5);

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
