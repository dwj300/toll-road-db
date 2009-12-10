
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
    private final String[] COLUMN_NAMES = {"Exit Name", "Profit", "Prequency of Car", "Frequency of Truck", "Frequency of Transmitter", "Frequency of Ticket"};

    private final int JFRAME_WIDTH = 475;
    private final int JFRAME_HEIGHT = 600;

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private Connection dbConnection;

    private Statement transactionsStatement;

    private ResultSet transactionsResults;

    private String transactionsQuerry;



    private JFrame window;

    private JPanel tablePanel;
    private JPanel topPanel;
    private JPanel mainPanel;

    private BorderLayout tableLayout;
    private FlowLayout topLayout;
    private BorderLayout appletLayout;

    private JTable reportTable;

    private Object[][] data = new Object[20][7];


    public TollReport()
    {
        startDBConnection();
    }

    @Override
    public void init()
    {
        window = new JFrame("Toll Report");

        tablePanel = new JPanel();
        topPanel = new JPanel();
        mainPanel = new JPanel();

        tableLayout = new BorderLayout();
        topLayout = new FlowLayout();
        appletLayout = new BorderLayout();

        tablePanel.setLayout(tableLayout);
        topPanel.setLayout(topLayout);
        mainPanel.setLayout(appletLayout);

        reportTable = new JTable(data, COLUMN_NAMES);

        tablePanel.add(reportTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(reportTable, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        window.setVisible(true);
        window.setResizable(false);

        window.add(mainPanel);
        setSize(0,0);
        update();
    }

    private void startDBConnection()
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
    {
        int maxExit = 0;
        int row = 0;
        double car = 0;
        double truck = 0;
        double transmitters = 0;
        double tickets = 0;
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
                transactionsQuerry = "select TRANSACTION_ID from transactions where exit_id = " + currentExit;
                transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                            

                if(transactionsResults.next() == true)
                {
                   
                    transactionsQuerry = "select EXIT_NUMBER A from EXITS where exit_id = " + currentExit;
                    
                    transactionsResults = transactionsStatement.executeQuery(transactionsQuerry);
                    transactionsResults.next();
                    reportTable.setValueAt(transactionsResults.getString("A"), row, 0);
                                        
                    transactionsQuerry = "select sum(AMOUNT_PAID) A from transactions where exit_id = " + currentExit;
                    transactionsResults.next();
                    reportTable.setValueAt(transactionsResults.getDouble("A"), row, 1);
             
                    transactionsQuerry = "select count(*) A from transactions where class = 'Car' and exit_id = " + currentExit;
                    transactionsResults.next();
                    car = transactionsResults.getInt("A");

                    transactionsQuerry = "select count(*) A from transactions where class = 'Truck' and exit_id = " + currentExit;
                    transactionsResults.next();
                    truck = transactionsResults.getInt("A");

                    reportTable.setValueAt((car / (car + truck) + "%"), row, 2);
                    reportTable.setValueAt((truck / (car + truck) + "%"), row, 3);
                   
                    transactionsQuerry = "select count(*) A from transactions where payment_type = 'transmitters' and exit_id = " + currentExit;
                    transactionsResults.next();
                    transmitters = transactionsResults.getInt("A");

                    transactionsQuerry = "select count(*) A from transactions where payment_type = 'tickets' and exit_id = " + currentExit;
                    transactionsResults.next();
                    tickets = transactionsResults.getInt("A");

                    reportTable.setValueAt((transmitters / (transmitters + tickets) + "%"), row, 2);
                    reportTable.setValueAt((tickets / (transmitters + tickets) + "%"), row, 3);

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
