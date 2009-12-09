import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EndTrip extends JApplet implements ActionListener
{
    private final int JFRAME_WIDTH = 400;
    private final int JFRAME_HEIGHT = 400;

    private Connection dbConnection;

    private Statement sqlStatementGetTripsUnderway;
    private Statement sqlStatementGetTripsCBNPaid;

    private ResultSet sqlResultsGetTripsUnderway;
    private ResultSet sqlResultsGetTripsCBNPaid;

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private JFrame jfTripWindow;

    private JPanel jpMain;
    private JPanel jpSelectTrip;
    private JPanel jpPaymentInfo;
    private JPanel jpChangeStatus;
    private JPanel jpSubmit;

    private FlowLayout flMain;
    private GridBagLayout gblMain;
    private GridBagConstraints gbcMain;

    private JComboBox jcbSelectTrip;
    private JComboBox jcbChangeStatus;

    private JLabel jlSelectTrip;
    private JLabel jlMilesTraveled;
    private JLabel jlMilesTraveledValue;
    private JLabel jlPaymentType;
    private JLabel jlPaymentTypeValue;
    private JLabel jlVehicleType;
    private JLabel jlVehicleTypeValue;
    private JLabel jlTotalDue;
    private JLabel jlTotalDueValue;
    private JLabel jlChargedTo;
    private JLabel jlName;
    private JLabel jlTransmitterID;
    private JLabel jlTransmitterIDValue;
    private JLabel jlTransmitterCurrentBalance;
    private JLabel jlTransmitterCurrentBalanceValue;
    private JLabel jlTransmitterNewBalance;
    private JLabel jlTransmitterNewBalanceValue;
    private JLabel jlChangeStatus;

    private JButton jbSubmit;

    private String[] strTripStatus = {"Underway", "Completed But Not Paid", "Paid"};

    public void EndTrip()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);
            sqlStatementGetTripsUnderway = dbConnection.createStatement();
            sqlStatementGetTripsCBNPaid = dbConnection.createStatement();
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
        jfTripWindow = new JFrame("End Trip");

        jpMain = new JPanel();
        jpSelectTrip = new JPanel();
        jpPaymentInfo = new JPanel();
        jpChangeStatus = new JPanel();
        jpSubmit = new JPanel();

        flMain = new FlowLayout();
        gblMain = new GridBagLayout();
        gbcMain = new GridBagConstraints();
        gbcMain.weightx = 100;
        gbcMain.weighty = 200;

        jcbSelectTrip = new JComboBox();
        jcbSelectTrip.setMaximumRowCount(3);
        jcbSelectTrip.addActionListener(this);
        jcbChangeStatus = new JComboBox(strTripStatus);
        jcbChangeStatus.setMaximumRowCount(3);
        jcbChangeStatus.addActionListener(this);

        jlSelectTrip = new JLabel("Select Trip: ");
        jlMilesTraveled = new JLabel("Miles Traveled: ");
        jlMilesTraveledValue = new JLabel("");
        jlPaymentType = new JLabel("Payment Type: ");
        jlPaymentTypeValue = new JLabel("");
        jlVehicleType = new JLabel("Vehicle Type: ");
        jlVehicleTypeValue = new JLabel("");
        jlTotalDue = new JLabel("Total Due: ");
        jlTotalDueValue = new JLabel("");
        jlChargedTo = new JLabel("Charged: ");
        jlName = new JLabel();
        jlTransmitterID = new JLabel("Transmitter ID: ");
        jlTransmitterIDValue = new JLabel("");
        jlTransmitterCurrentBalance = new JLabel("Current Balance: ");
        jlTransmitterCurrentBalanceValue = new JLabel("");
        jlTransmitterNewBalance = new JLabel("New Balance: ");
        jlTransmitterNewBalanceValue = new JLabel("");
        jlChangeStatus = new JLabel("Change Status");

        jbSubmit = new JButton("Submit");
        jbSubmit.addActionListener(this);

        jpSelectTrip.setLayout(flMain);
        jpSelectTrip.add(jlSelectTrip);
        jpSelectTrip.add(jcbSelectTrip);

        jpPaymentInfo.setLayout(gblMain);

        gbcMain.anchor = GridBagConstraints.NORTH;
        gbcMain.ipady = 20;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlMilesTraveled, gbcMain);
        jpPaymentInfo.add(jlMilesTraveled);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlMilesTraveledValue, gbcMain);
        jpPaymentInfo.add(jlMilesTraveledValue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlVehicleType, gbcMain);
        jpPaymentInfo.add(jlVehicleType);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlVehicleTypeValue, gbcMain);
        jpPaymentInfo.add(jlVehicleTypeValue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 3;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTotalDue, gbcMain);
        jpPaymentInfo.add(jlTotalDue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 3;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTotalDueValue, gbcMain);
        jpPaymentInfo.add(jlTotalDueValue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 4;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlPaymentType, gbcMain);
        jpPaymentInfo.add(jlPaymentType);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 4;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlPaymentTypeValue, gbcMain);
        jpPaymentInfo.add(jlPaymentTypeValue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 5;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlChargedTo, gbcMain);
        jpPaymentInfo.add(jlChargedTo);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 5;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlName, gbcMain);
        jpPaymentInfo.add(jlName);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 2;
        gbcMain.gridy = 5;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterID, gbcMain);
        jpPaymentInfo.add(jlTransmitterID);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 3;
        gbcMain.gridy = 5;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterIDValue, gbcMain);
        jpPaymentInfo.add(jlTransmitterIDValue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 6;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterCurrentBalance, gbcMain);
        jpPaymentInfo.add(jlTransmitterCurrentBalance);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 2;
        gbcMain.gridy = 6;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterCurrentBalanceValue, gbcMain);
        jpPaymentInfo.add(jlTransmitterCurrentBalanceValue);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 7;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterNewBalance, gbcMain);
        jpPaymentInfo.add(jlTransmitterNewBalance);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 2;
        gbcMain.gridy = 7;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterNewBalanceValue, gbcMain);
        jpPaymentInfo.add(jlTransmitterNewBalanceValue);

        jpChangeStatus.setLayout(flMain);
        jpChangeStatus.add(jlChangeStatus);
        jpChangeStatus.add(jcbChangeStatus);

        jpSubmit.setLayout(flMain);
        jpSubmit.add(jbSubmit);
        
        jpMain.setLayout(gblMain);
        
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpSelectTrip, gbcMain);
        jpMain.add(jpSelectTrip);
        
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpPaymentInfo, gbcMain);
        jpMain.add(jpPaymentInfo);
        
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 2;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpChangeStatus, gbcMain);
        jpMain.add(jpChangeStatus);
        
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 2;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpSubmit, gbcMain);
        jpMain.add(jpSubmit);

        jfTripWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfTripWindow.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        jfTripWindow.setVisible(true);
        jfTripWindow.setResizable(false);
        jfTripWindow.add(jpMain);
        setSize(0,0);

        try
        {
            String query = "select trip_id, date, payment_type, class, (select exit_number from exits where t.start_exit_id = exit_id) start_exit, (select nearest_town from exits where t.start_exit_id = exit_id) start_nearest_town, (select exit_number from exits where t.end_exit_id = exit_id) end_exit, (select nearest_town from exits where t.end_exit_id = exit_id) end_nearest_town, transmitter_id, (select name from customers where customer_id = (select customer_id from transmitters where t.transmitter_id = transmitter_id)) name, (select account_balance from transmitters where t.transmitter_id = transmitter_id) account_balance from trips t where status = 'Completed But Not Paid'";
            sqlResultsGetTripsCBNPaid = sqlStatementGetTripsCBNPaid.executeQuery(query);


            while(sqlResultsGetTripsCBNPaid.next())
            {
               jcbSelectTrip.addItem("Trip " + sqlResultsGetTripsCBNPaid.getInt("trip_id") + " on " + sqlResultsGetTripsCBNPaid.getString("date") + " from " + sqlResultsGetTripsCBNPaid.getString("start_exit") + ":" + sqlResultsGetTripsCBNPaid.getString("start_nearest_town"));
            }

            jcbSelectTrip.removeAllItems();
            sqlResultsGetTripsUnderway = sqlStatementGetTripsUnderway.executeQuery("select * from trips where status = 'Underway'");
            while(sqlResultsGetTripsUnderway.next())
            {
               jcbSelectTrip.addItem(sqlResultsGetTripsUnderway.getInt("trip_id"));
            }
        }
        catch(SQLException sqle)
        {

        }
    }

    public void actionPerformed(ActionEvent e)
    {

    }
}
