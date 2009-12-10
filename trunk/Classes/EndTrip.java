import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class EndTrip extends JApplet implements ActionListener
{
    private final int JFRAME_WIDTH = 500;
    private final int JFRAME_HEIGHT = 500;

    private final double TRUCK_PER_MILE = 0.25;
    private final double CAR_PER_MILE = 0.10;

    Connection dbConnection;

    Statement sqlStatementGetTripsUnderway;
    Statement sqlStatementGetTripsCBNPaid;
    Statement sqlStatementGetTripInfo;
    Statement sqlStatementGetTransmitterInfo;
    Statement sqlStatementGetExitInfo;
    Statement sqlStatementGetEndExits;
    Statement sqlStatementGetNewEndExit;
    Statement sqlStatementUpdateStatus;
    Statement sqlStatementUpdateTrips;
    Statement sqlStatementUpdateTransmitter;

    ResultSet sqlResultsGetTripsUnderway;
    ResultSet sqlResultsGetTripsCBNPaid;
    ResultSet sqlResultsGetTripInfo;
    ResultSet sqlResultsGetTransmitterInfo;
    ResultSet sqlResultsGetExitInfo;
    ResultSet sqlResultsGetEndExits;
    ResultSet sqlResultsGetNewEndExit;

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";

    private JFrame jfTripWindow;

    private JPanel jpMain;
    private JPanel jpSelectTrip;
    private JPanel jpSelectTripCurrent;
    private JPanel jpEndTrip;
    private JPanel jpPaymentInfo;
    private JPanel jpChangeStatus;
    private JPanel jpSubmit;

    private FlowLayout flMain;
    private GridBagLayout gblMain;
    private GridBagConstraints gbcMain;

    private JComboBox jcbSelectTrip;
    private JComboBox jcbChangeStatus;
    private JComboBox jcbSelectEndExit;

    private JLabel jlSelectTrip;
    private JLabel jlSelectEndExit;
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
    private JLabel jlStatus;

    private JButton jbSubmit;

    private String[] strTripStatus = {"Completed But Not Paid", "Paid"};

    private String dbTrip_ID;
    private String dbVehicle_Type;
    private String dbPayment_Type;
    private String dbCustomer_Name;
    private String dbStatus;
    private String dbNewStatus = "Completed But Not Paid";
    private int dbTransmitter_ID;
    private double dbCurrent_Balance;
    private double dbNew_Balance;
    private double dbTotalDue;

    private int startMileMarkerID;
    private int startMileMarker;
    private int endMileMarkerID;
    private int endMileMarker;
    private int milesTraveled;

    private String newEndExit;
    private int newEndExitID;

    private boolean dbTransmitter_IDboolean;

    private Applet applet;

    private DecimalFormat currencyFormat = new DecimalFormat("$###,###.00");

    public EndTrip(Applet applet)
    {
        this.applet = applet;
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);
            sqlStatementGetTripsUnderway = dbConnection.createStatement();
            sqlStatementGetTripsCBNPaid = dbConnection.createStatement();
            sqlStatementGetTransmitterInfo = dbConnection.createStatement();
            sqlStatementGetTripInfo = dbConnection.createStatement();
            sqlStatementGetExitInfo = dbConnection.createStatement();
            sqlStatementGetEndExits = dbConnection.createStatement();
            sqlStatementGetNewEndExit = dbConnection.createStatement();
            sqlStatementUpdateStatus = dbConnection.createStatement();
            sqlStatementUpdateTrips = dbConnection.createStatement();
            sqlStatementUpdateTransmitter = dbConnection.createStatement();
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
        jpSelectTripCurrent = new JPanel();
        jpEndTrip = new JPanel();
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
        jcbSelectEndExit = new JComboBox();
        jcbSelectEndExit.setMaximumRowCount(3);
        jcbSelectEndExit.addActionListener(this);

        jlSelectTrip = new JLabel("Select Trip: ");
        jlSelectEndExit = new JLabel("End Exit: ");
        jlMilesTraveled = new JLabel("Miles Traveled: ");
        jlMilesTraveledValue = new JLabel("");
        jlPaymentType = new JLabel("Payment Type: ");
        jlPaymentTypeValue = new JLabel("");
        jlVehicleType = new JLabel("Vehicle Type: ");
        jlVehicleTypeValue = new JLabel("");
        jlTotalDue = new JLabel("Total Due: ");
        jlTotalDueValue = new JLabel("");
        jlChargedTo = new JLabel("Charged To: ");
        jlName = new JLabel();
        jlTransmitterID = new JLabel("Transmitter ID: ");
        jlTransmitterIDValue = new JLabel("");
        jlTransmitterCurrentBalance = new JLabel("Current Balance: ");
        jlTransmitterCurrentBalanceValue = new JLabel("");
        jlTransmitterNewBalance = new JLabel("New Balance: ");
        jlTransmitterNewBalanceValue = new JLabel("");
        jlChangeStatus = new JLabel("Change Status:");
        jlStatus = new JLabel("");

        jbSubmit = new JButton("Submit");
        jbSubmit.addActionListener(this);

        jpSelectTripCurrent.setLayout(flMain);
        jpSelectTripCurrent.add(jlSelectTrip);
        jpSelectTripCurrent.add(jcbSelectTrip);

        jpEndTrip.setLayout(flMain);
        jpEndTrip.add(jlSelectEndExit);
        jpEndTrip.add(jcbSelectEndExit);
        jlSelectEndExit.setVisible(false);
        jcbSelectEndExit.setVisible(false);

        jpSelectTrip.setLayout(gblMain);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.ipady = 20;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpSelectTripCurrent, gbcMain);
        jpSelectTrip.add(jpSelectTripCurrent);

        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.ipady = 20;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpEndTrip, gbcMain);
        jpSelectTrip.add(jpEndTrip);

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
        gbcMain.gridwidth = 4;
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
        
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 3;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlStatus, gbcMain);
        jpMain.add(jlStatus);


        jfTripWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfTripWindow.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        jfTripWindow.setVisible(true);
        jfTripWindow.setResizable(false);
        jfTripWindow.add(jpMain);
        setSize(0,0);

        try
        {
            jcbSelectTrip.removeAllItems();
            sqlResultsGetTripsCBNPaid = sqlStatementGetTripsCBNPaid.executeQuery("select trip_id, date, (select exit_number from exits where t.start_exit_id = exit_id) start_exit, (select nearest_town from exits where t.start_exit_id = exit_id) start_nearest_town, (select exit_number from exits where t.end_exit_id = exit_id) end_exit, (select nearest_town from exits where t.end_exit_id = exit_id) end_nearest_town from trips t where status = 'Completed But Not Paid'");
            while(sqlResultsGetTripsCBNPaid.next())
            {
               jcbSelectTrip.addItem("Trip " + sqlResultsGetTripsCBNPaid.getInt("trip_id") + " on " + sqlResultsGetTripsCBNPaid.getString("date") + " from " + sqlResultsGetTripsCBNPaid.getString("start_exit") + " : " + sqlResultsGetTripsCBNPaid.getString("start_nearest_town") + " to " + sqlResultsGetTripsCBNPaid.getString("end_exit") + " : " + sqlResultsGetTripsCBNPaid.getString("end_nearest_town"));
            }

            sqlResultsGetTripsUnderway = sqlStatementGetTripsUnderway.executeQuery("select trip_id, date, (select exit_number from exits where t.start_exit_id = exit_id) start_exit, (select nearest_town from exits where t.start_exit_id = exit_id) start_nearest_town, (select exit_number from exits where t.end_exit_id = exit_id) end_exit, (select nearest_town from exits where t.end_exit_id = exit_id) end_nearest_town from trips t where status = 'Underway'");
            while(sqlResultsGetTripsUnderway.next())
            {
               jcbSelectTrip.addItem("Trip " + sqlResultsGetTripsUnderway.getInt("trip_id") + " on " + sqlResultsGetTripsUnderway.getString("date") + " from " + sqlResultsGetTripsUnderway.getString("start_exit") + " : " + sqlResultsGetTripsUnderway.getString("start_nearest_town"));
            }

        }
        catch(SQLException sqle)
        {

        }

        try
        {
            jcbSelectEndExit.removeAllItems();
            sqlResultsGetEndExits = sqlStatementGetEndExits.executeQuery("select exit_number, nearest_town from exits");
            while(sqlResultsGetEndExits.next())
            {
                jcbSelectEndExit.addItem(sqlResultsGetEndExits.getString("exit_number") + " : " + sqlResultsGetEndExits.getString("nearest_town"));
            }
        }
        catch(SQLException sqle)
        {

        }
    }

    @Override
    public void destroy()
    {
        applet.update();
        try
        {
            dbConnection.close();

            sqlStatementGetTripsUnderway.close();
            sqlStatementGetTripsCBNPaid.close();
            sqlStatementGetTripInfo.close();
            sqlStatementGetTransmitterInfo.close();
            sqlStatementGetExitInfo.close();
            sqlStatementGetEndExits.close();
            sqlStatementGetNewEndExit.close();
            sqlStatementUpdateStatus.close();
            sqlStatementUpdateTrips.close();
            sqlStatementUpdateTransmitter.close();

            sqlResultsGetTripsUnderway.close();
            sqlResultsGetTripsCBNPaid.close();
            sqlResultsGetTripInfo.close();
            sqlResultsGetTransmitterInfo.close();
            sqlResultsGetExitInfo.close();
            sqlResultsGetEndExits.close();
            sqlResultsGetNewEndExit.close();
        }
        catch (SQLException ex)
        {

        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == jcbSelectTrip)
        {
            try
            {
                dbTrip_ID = jcbSelectTrip.getSelectedItem().toString();
                dbTrip_ID = dbTrip_ID.substring(dbTrip_ID.indexOf('p')+2, dbTrip_ID.indexOf(" ", dbTrip_ID.indexOf('p')+2));
            }
            catch(NullPointerException npe)
            {

            }
            try
            {
                sqlResultsGetTripInfo = sqlStatementGetTripInfo.executeQuery("select payment_type, class, transmitter_id, start_exit_id, end_exit_id, status from trips where trip_id = " + dbTrip_ID);
                while(sqlResultsGetTripInfo.next())
                {                    
                    dbVehicle_Type = sqlResultsGetTripInfo.getString("class");
                    dbPayment_Type = sqlResultsGetTripInfo.getString("payment_type");
                    dbTransmitter_ID = sqlResultsGetTripInfo.getInt("transmitter_id");
                    dbTransmitter_IDboolean = sqlResultsGetTripInfo.wasNull();
                    startMileMarkerID = sqlResultsGetTripInfo.getInt("start_exit_id");
                    endMileMarkerID = sqlResultsGetTripInfo.getInt("end_exit_id");
                    dbStatus = sqlResultsGetTripInfo.getString("status");
                }
                if(!dbTransmitter_IDboolean)
                {
                    sqlResultsGetTransmitterInfo = sqlStatementGetTransmitterInfo.executeQuery("select account_balance, (select name from customers where t.customer_id = customer_id) name from transmitters t where transmitter_id = " + dbTransmitter_ID);
                    while(sqlResultsGetTransmitterInfo.next())
                    {
                       dbCurrent_Balance = sqlResultsGetTransmitterInfo.getDouble("account_balance");
                       dbCustomer_Name = sqlResultsGetTransmitterInfo.getString("name");
                    }
                }

                sqlResultsGetExitInfo = sqlStatementGetExitInfo.executeQuery("select (select mile_marker from exits where exit_id = " + startMileMarkerID + ") start_mile_marker, (select mile_marker from exits where exit_id = " + endMileMarkerID + ") end_mile_Marker from exits");
                while(sqlResultsGetExitInfo.next())
                {
                   startMileMarker = new Integer(sqlResultsGetExitInfo.getString("start_mile_marker")).intValue();
                   endMileMarker = new Integer(sqlResultsGetExitInfo.getString("end_mile_marker")).intValue();
                }

                milesTraveled = Math.abs(endMileMarker - startMileMarker);
                if(dbVehicle_Type.compareTo("Truck") == 0)
                {
                    dbTotalDue = milesTraveled * TRUCK_PER_MILE;
                }
                else if(dbVehicle_Type.compareTo("Car") == 0)
                {
                    dbTotalDue = milesTraveled * CAR_PER_MILE;
                }

                dbNew_Balance = dbCurrent_Balance - dbTotalDue;

                jlMilesTraveledValue.setText(new Integer(milesTraveled).toString() + " miles");
                jlVehicleTypeValue.setText(dbVehicle_Type);
                jlTotalDueValue.setText(currencyFormat.format(dbTotalDue));
                jlPaymentTypeValue.setText(dbPayment_Type);
                if(dbStatus.compareTo("Underway") == 0)
                {
                    jlSelectEndExit.setVisible(true);
                    jcbSelectEndExit.setVisible(true);
                }
                else if(dbStatus.compareTo("Completed But Not Paid") == 0)
                {
                    jlSelectEndExit.setVisible(false);
                    jcbSelectEndExit.setVisible(false);
                }
                if(!dbTransmitter_IDboolean)
                {
                    jlName.setText(dbCustomer_Name);
                    jlTransmitterIDValue.setText(new Integer(dbTransmitter_ID).toString());
                    jlTransmitterCurrentBalanceValue.setText(currencyFormat.format(dbCurrent_Balance));
                    jlTransmitterNewBalanceValue.setText(currencyFormat.format(dbNew_Balance));
                }
                else if(dbTransmitter_IDboolean)
                {
                    jlName.setText("");
                    jlTransmitterIDValue.setText("");
                    jlTransmitterCurrentBalanceValue.setText("");
                    jlTransmitterNewBalanceValue.setText("");
                }

            }
            catch(SQLException sqle)
            {

            }
        }

        else if(e.getSource() == jcbChangeStatus)
        {
            dbNewStatus = jcbChangeStatus.getSelectedItem().toString();
        }

        else if(e.getSource() == jbSubmit)
        {
            newEndExit = jcbSelectEndExit.getSelectedItem().toString();
            newEndExit = newEndExit.substring(0, newEndExit.indexOf(" "));
            try
            {             
                sqlResultsGetNewEndExit = sqlStatementGetNewEndExit.executeQuery("select exit_id from exits where exit_number = '" + newEndExit +"'");
                while(sqlResultsGetNewEndExit.next())
                {
                    newEndExitID = sqlResultsGetNewEndExit.getInt("exit_id");
                }

                // Transmitter
                if(!dbTransmitter_IDboolean)
                {
                    // The trip is completed, but not paid. I know the exit number
                    if(dbStatus.compareTo("Completed But Not Paid") == 0)
                    {
                        // And I want to pay it
                        if(dbNewStatus.compareTo("Paid") == 0)
                        {
                           try
                           {
                                //update transmitter with new balance
                                sqlStatementUpdateTransmitter.execute("update transmitters set account_balance = " + dbNew_Balance + " where transmitter_id = " + dbTransmitter_ID);
                                sqlStatementUpdateStatus.execute("update trips set status = '" + dbNewStatus + "' where trip_id = " + dbTrip_ID);
                                jlStatus.setText("Successfully Updated Transmitters and Trips, Paid");
                           }
                           catch(SQLException sqle)
                           {

                           }
                        }
                        else
                        {
                            jlStatus.setText("");
                        }
                    }
                    // The trip is underway, I don't know the exit number
                    else if(dbStatus.compareTo("Underway") == 0)
                    {
                        // And I want to complete it, but not pay it
                        if(dbNewStatus.compareTo("Completed But Not Paid") == 0)
                        {
                            sqlStatementUpdateStatus.execute("update trips set status = '" + dbNewStatus + "' where trip_id = " + dbTrip_ID);
                            sqlStatementUpdateTrips.execute("update trips set end_exit_id = " + newEndExitID + " where trip_id = " + dbTrip_ID);
                            jlStatus.setText("Successfully Updated Transmitter and Trips, Not Paid");
                        }
                        // And I want to complete it and pay it
                        else if(dbNewStatus.compareTo("Paid") == 0)
                        {
                            sqlStatementUpdateTrips.execute("update trips set end_exit_id = " + newEndExitID + " where trip_id = " + dbTrip_ID);
                            sqlStatementUpdateTransmitter.execute("update transmitters set account_balance = " + dbNew_Balance + " where transmitter_id = " + dbTransmitter_ID);
                            sqlStatementUpdateStatus.execute("update trips set status = '" + dbNewStatus + "' where trip_id = " + dbTrip_ID);
                            jlStatus.setText("Successfully Updated Transmitter and Trips, Paid");
                        }

                    }
                }
                // Ticket
                else if(dbTransmitter_IDboolean)
                {
                    // I know the exit number
                    if(dbStatus.compareTo("Completed But Not Paid") == 0)
                    {
                        if(dbNewStatus.compareTo("Paid") == 0)
                        {
                            sqlStatementUpdateStatus.execute("update trips set status = '" + dbNewStatus + "' where trip_id = " + dbTrip_ID);
                            jlStatus.setText("Successfully Updated Trips, Paid");
                        }
                        else
                        {
                            jlStatus.setText("");
                        }
                    }
                    // I need the exit number
                    else if(dbStatus.compareTo("Underway") == 0)
                    {
                        if(dbNewStatus.compareTo("Paid") == 0)
                        {
                            sqlStatementUpdateTrips.execute("update trips set end_exit_id = " + newEndExitID + " where trip_id = " + dbTrip_ID);
                            sqlStatementUpdateStatus.execute("update trips set status = '" + dbNewStatus + "' where trip_id = " + dbTrip_ID);
                            jlStatus.setText("Successfully Updated Trips, Paid");
                        }

                        else if(dbNewStatus.compareTo("Complete But Not Paid") == 0)
                        {
                            sqlStatementUpdateStatus.execute("update trips set status = '" + dbNewStatus + "' where trip_id = " + dbTrip_ID);
                            jlStatus.setText("Successfully Updated Trips, Not Paid");
                        }
                    }
                }
            }
            catch(SQLException sqle)
            {

            }
            catch(NullPointerException npe)
            {
                
            }

            try
            {
                jcbSelectTrip.removeAllItems();
                sqlResultsGetTripsCBNPaid = sqlStatementGetTripsCBNPaid.executeQuery("select trip_id, date, (select exit_number from exits where t.start_exit_id = exit_id) start_exit, (select nearest_town from exits where t.start_exit_id = exit_id) start_nearest_town, (select exit_number from exits where t.end_exit_id = exit_id) end_exit, (select nearest_town from exits where t.end_exit_id = exit_id) end_nearest_town from trips t where status = 'Completed But Not Paid'");
                while(sqlResultsGetTripsCBNPaid.next())
                {
                   jcbSelectTrip.addItem("Trip " + sqlResultsGetTripsCBNPaid.getInt("trip_id") + " on " + sqlResultsGetTripsCBNPaid.getString("date") + " from " + sqlResultsGetTripsCBNPaid.getString("start_exit") + " : " + sqlResultsGetTripsCBNPaid.getString("start_nearest_town") + " to " + sqlResultsGetTripsCBNPaid.getString("end_exit") + " : " + sqlResultsGetTripsCBNPaid.getString("end_nearest_town"));
                }

                sqlResultsGetTripsUnderway = sqlStatementGetTripsUnderway.executeQuery("select trip_id, date, (select exit_number from exits where t.start_exit_id = exit_id) start_exit, (select nearest_town from exits where t.start_exit_id = exit_id) start_nearest_town, (select exit_number from exits where t.end_exit_id = exit_id) end_exit, (select nearest_town from exits where t.end_exit_id = exit_id) end_nearest_town from trips t where status = 'Underway'");
                while(sqlResultsGetTripsUnderway.next())
                {
                   jcbSelectTrip.addItem("Trip " + sqlResultsGetTripsUnderway.getInt("trip_id") + " on " + sqlResultsGetTripsUnderway.getString("date") + " from " + sqlResultsGetTripsUnderway.getString("start_exit") + " : " + sqlResultsGetTripsUnderway.getString("start_nearest_town"));
                }

                jcbChangeStatus.setSelectedIndex(0);

                try
                {
                    jcbSelectTrip.setSelectedIndex(0);
                }
                catch(IllegalArgumentException iae)
                {
                    jlMilesTraveledValue.setText("");
                    jlVehicleTypeValue.setText("");
                    jlTotalDueValue.setText("");
                    jlPaymentTypeValue.setText("");
                    jlName.setText("");
                    jlTransmitterIDValue.setText("");
                    jlTransmitterCurrentBalanceValue.setText("");
                    jlTransmitterNewBalanceValue.setText("");
                    jlSelectEndExit.setVisible(false);
                    jcbSelectEndExit.setVisible(false);
                }

            }
            catch(SQLException sqle)
            {

            }
            applet.update();
        }
    }
}
