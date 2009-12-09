import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
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
    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private JButton startButton;
    private JButton endButton;
    private JButton depositButton;

    private BorderLayout appletLayout;
    private BorderLayout tablePanelLayout;
    private FlowLayout buttonPanelLayout;

    private JPanel buttonPanel;
    private JPanel tablePanel;

    private JTable resultsTable;

    private String[] columnNames = {"Start exit", "End exit", "Date", "Payment",
                                    "Class"};
    private Object[][] data = new Object[15][5];

    @Override
    public void init()
    {
        startButton = new JButton("Start trip");
        endButton = new JButton("End trip");
        depositButton = new JButton("Deposit money");

        appletLayout = new BorderLayout();
        tablePanelLayout = new BorderLayout();
        buttonPanelLayout = new FlowLayout();

        buttonPanel = new JPanel();
        tablePanel = new JPanel();

        resultsTable = new JTable(data, columnNames);

        this.setLayout(appletLayout);
        buttonPanel.setLayout(buttonPanelLayout);
        tablePanel.setLayout(tablePanelLayout);

        startButton.addActionListener(this);
        endButton.addActionListener(this);
        depositButton.addActionListener(this);


        buttonPanel.add(startButton, FlowLayout.LEFT);
        buttonPanel.add(endButton, FlowLayout.CENTER);
        buttonPanel.add(depositButton, FlowLayout.RIGHT);

        tablePanel.add(resultsTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(resultsTable, BorderLayout.CENTER);
        
        add(buttonPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        update();

    }

    public void update()
    {
        String tempQuerry = "";
        ResultSet results;
        int row = 0;
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            Connection myConnection = DriverManager.getConnection(DBURL , DBUSER, DBPASS);
            Statement stmt = myConnection.createStatement();
            tempQuerry = "select * from TRIPS";
            results = stmt.executeQuery(tempQuerry);
            while(results.next())
            {
                resultsTable.setValueAt(results.getString("START_EXIT"), row, 0);
                resultsTable.setValueAt(results.getString("END_EXIT"), row, 1);
                resultsTable.setValueAt(results.getString("DATE"), row, 2);
                resultsTable.setValueAt(results.getString("PAYMENT_TYPE"), row, 3);
                resultsTable.setValueAt(results.getString("CLASS"), row, 4);

                row++;
            }

            results.close();
            stmt.close();
            myConnection.close();
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("A ClassNotFoundException has occurred.");
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

        }
        else if(e.getSource() == endButton)
        {

        }
        else if(e.getSource() == depositButton)
        {
            CustomerManagement deposit = new CustomerManagement();
            deposit.init();
        }
    }
}