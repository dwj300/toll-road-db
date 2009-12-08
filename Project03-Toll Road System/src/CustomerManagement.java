import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

public class CustomerManagement extends JApplet implements ActionListener
{
    private final int JFRAME_WIDTH = 600;
    private final int JFRAME_HEIGHT = 120;

    Connection dbConnection;
    
    Statement sqlStatementNames;
    Statement sqlStatementTransmitters;
    Statement sqlStatementAccountBalance;
    Statement sqlStatementUpdateAccountBalance;

    ResultSet sqlResultsNames;
    ResultSet sqlResultsTransmitters;
    ResultSet sqlResultsAccountBalance;

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private JFrame jfCustWindow;

    private JPanel jpCustRecords;
    private JPanel jpAddMoney;
    private JPanel jpButtons;
    private JPanel jpMain;

    private FlowLayout flMain;

    private JComboBox jcbCustNames;
    private JComboBox jcbCustTransmitters;

    private JLabel jlCustName;
    private JLabel jlCustTransmitter;
    private JLabel jlCustAccountBalanceLabel;
    private JLabel jlCustAccountBalance;
    private JLabel jlAddMoney;

    private JTextField jtfAddMoney;

    private JButton jbSubmit;
    private JButton jbCancel;

    DecimalFormat currencyFormat = new DecimalFormat("$###,###.00");

    private String strAddMoney;
    private String strCurrentBalance;

    public CustomerManagement()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);
            sqlStatementNames = dbConnection.createStatement();
            sqlStatementTransmitters = dbConnection.createStatement();
            sqlStatementAccountBalance = dbConnection.createStatement();
        }
        catch(InstantiationException ie)
        {
            
        }
        catch(IllegalAccessException iae)
        {

        }
        catch(ClassNotFoundException cnfe)
        {

        }
        catch(SQLException sqle)
        {

        }
    }

    @Override
    public void init()
    {
        jfCustWindow = new JFrame("Customer Management");

        jpCustRecords = new JPanel();
        jpAddMoney = new JPanel();
        jpButtons = new JPanel();
        jpMain = new JPanel();

        flMain = new FlowLayout();

        jcbCustNames = new JComboBox();
        jcbCustNames.setMaximumRowCount(3);
        jcbCustNames.addActionListener(this);
        jcbCustTransmitters = new JComboBox();
        jcbCustTransmitters.setMaximumRowCount(3);
        jcbCustTransmitters.addActionListener(this);

        jlCustName = new JLabel("Select Customer: ");
        jlCustTransmitter = new JLabel("Select Transmitter: ");
        jlCustAccountBalanceLabel = new JLabel("Account Balance: ");
        jlCustAccountBalance = new JLabel("");
        jlAddMoney = new JLabel("Add Money To Transmitter: ");

        jtfAddMoney = new JTextField(7);
        jtfAddMoney.setText("$00.00");

        jbSubmit = new JButton("Submit");
        jbSubmit.addActionListener(this);
        jbCancel = new JButton("Cancel");
        jbCancel.addActionListener(this);

        jpCustRecords.setLayout(flMain);
        jpCustRecords.add(jlCustName);
        jpCustRecords.add(jcbCustNames);
        jpCustRecords.add(jlCustTransmitter);
        jpCustRecords.add(jcbCustTransmitters);
        jpCustRecords.add(jlCustAccountBalanceLabel);
        jpCustRecords.add(jlCustAccountBalance);

        jpAddMoney.setLayout(flMain);
        jpAddMoney.add(jlAddMoney);
        jpAddMoney.add(jtfAddMoney);

        jpButtons.setLayout(flMain);
        jpButtons.add(jbSubmit);
        jpButtons.add(jbCancel);

        jpMain.setLayout(flMain);
        jpMain.add(jpCustRecords);
        jpMain.add(jpAddMoney);
        jpMain.add(jpButtons);

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

        strAddMoney = "";
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
                    sqlResultsAccountBalance = sqlStatementAccountBalance.executeQuery("select account_balance from transmitters where transmitter_id = " + jcbCustTransmitters.getSelectedItem().toString());
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

        if(e.getSource() == jbSubmit)
        {
            strAddMoney = jtfAddMoney.getText();
            if(strAddMoney.charAt(0) == '$')
            {
                strAddMoney = strAddMoney.substring(1);
            }
            Double dblwAddMoney = new Double(strAddMoney);

            strCurrentBalance = jlCustAccountBalance.getText();
            if(strCurrentBalance.charAt(0) == '$')
            {
                strCurrentBalance = strCurrentBalance.substring(1);
            }
            Double dblwCurrentBalance = new Double(strCurrentBalance);

            double dblNewAccountBalance = dblwAddMoney.doubleValue() + dblwCurrentBalance.doubleValue();

            try
            {
                sqlStatementUpdateAccountBalance.execute("update transmitters set account_balance = " + dblNewAccountBalance + " where transmitter_id = " + jcbCustTransmitters.getSelectedItem().toString());
            }
            catch(SQLException sqle)
            {

            }
        }
    }
}
