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
    private final String[] COLUMN_NAMES = {"Start exit", "End exit", "Date",
                                           "Payment", "Class", "Status",
                                           "Customer"};

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private final int APPLET_WIDTH = 600;
    private final int APPLET_HEIGHT = 400;

    private int rowCounter;

    private Connection dbConnection;

    private Statement tripsStatement;

    private ResultSet tripsResults;

    private String tripsQuerry;

    private JButton startButton;
    private JButton endButton;
    private JButton depositButton;
    private JButton statementButton;
    private JButton tollReportButton;

    private BorderLayout appletLayout;
    private BorderLayout tablePanelLayout;
    private FlowLayout buttonPanelLayout;

    private JPanel buttonPanel;
    private JPanel tablePanel;

    private JTable resultsTable;

    private Object[][] data = new Object[20][7];

    @Override
    public void init()
    {
        setSize(APPLET_WIDTH, APPLET_HEIGHT);

        startButton = new JButton("Start trip");
        endButton = new JButton("End trip");
        depositButton = new JButton("Deposit money");
        statementButton = new JButton("Account Statement");
        tollReportButton = new JButton("Toll Report");

        appletLayout = new BorderLayout();
        tablePanelLayout = new BorderLayout();
        buttonPanelLayout = new FlowLayout();

        buttonPanel = new JPanel();
        tablePanel = new JPanel();

        resultsTable = new JTable(data, COLUMN_NAMES);

        setLayout(appletLayout);
        buttonPanel.setLayout(buttonPanelLayout);
        tablePanel.setLayout(tablePanelLayout);

        startButton.addActionListener(this);
        endButton.addActionListener(this);
        depositButton.addActionListener(this);
        statementButton.addActionListener(this);
        tollReportButton.addActionListener(this);

        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(statementButton);
        buttonPanel.add(tollReportButton);

        tablePanel.add(resultsTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(resultsTable, BorderLayout.CENTER);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        startDBConnection();
        update();
    }

    private void startDBConnection()
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
    {
        rowCounter = 0;
        tripsQuerry = "";

        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER, DBPASS);
            tripsStatement = dbConnection.createStatement();
            tripsQuerry = "select (select EXIT_NUMBER from EXITS where EXIT_ID = t.START_EXIT_ID) START_EXIT, (select EXIT_NUMBER from EXITS where EXIT_ID = t.END_EXIT_ID) END_EXIT, DATE, PAYMENT_TYPE, CLASS, STATUS, (select NAME from CUSTOMERS c where CUSTOMER_ID = (select CUSTOMER_ID from TRANSMITTERS where TRANSMITTER_ID = t.TRANSMITTER_ID)) CUSTOMER  from TRIPS t";
            tripsResults = tripsStatement.executeQuery(tripsQuerry);
            
            while(tripsResults.next())
            {
                resultsTable.setValueAt(tripsResults.getString("START_EXIT"), rowCounter, 0);
                resultsTable.setValueAt(tripsResults.getString("END_EXIT"), rowCounter, 1);
                resultsTable.setValueAt(tripsResults.getString("DATE"), rowCounter, 2);
                resultsTable.setValueAt(tripsResults.getString("PAYMENT_TYPE"), rowCounter, 3);
                resultsTable.setValueAt(tripsResults.getString("CLASS"), rowCounter, 4);
                resultsTable.setValueAt(tripsResults.getString("STATUS"), rowCounter, 5);
                resultsTable.setValueAt(tripsResults.getString("CUSTOMER"), rowCounter, 6);

                rowCounter++;
            }

            tripsResults.close();
            tripsStatement.close();
            dbConnection.close();
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
    {
        if(e.getSource() == startButton)
        {
            StartTrip start = new StartTrip();
            start.init();
        }
        else if(e.getSource() == endButton)
        {
            EndTrip end = new EndTrip();
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
    public void destroy()
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