import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class StartTrip extends JApplet implements ActionListener
{
    private final int JFRAME_WIDTH = 800;
    private final int JFRAME_HEIGHT = 200;

    Connection dbConnection;
    
    Statement sqlStatementGetExits;
    Statement sqlStatementGetTransmitter;
    Statement sqlStatementGetTripIDvalue;
    Statement sqlStatementInsertTrip;
    Statement sqlStatementGetStartExitID;
    Statement sqlStatementGetEndExitID;
    
    ResultSet sqlResultsGetExits;
    ResultSet sqlResultsGetTransmitter;
    ResultSet sqlResultsGetTripIDvalue;
    ResultSet sqlResultsGetStartExitID;
    ResultSet sqlResultsGetEndExitID;

    private final String DBURL = "jdbc:derby://localhost:1527/Toll-Road-DB";
    private final String DBUSER = "root";
    private final String DBPASS = "root";    
    
    private JFrame jfTripWindow;
    
    private JPanel jpMain;
    private JPanel jpExits;
    private JPanel jpPaymentType;
    private JPanel jpVehicleClass;
    private JPanel jpSubmit;

    private TitledBorder tbPaymentType;
    private TitledBorder tbVehicleClass;
    
    private FlowLayout flMain;
    private GridBagLayout gblMain;
    private GridBagConstraints gbcMain;
    
    private JComboBox jcbStartExit;
    private JComboBox jcbEndExit;
    private JComboBox jcbStatus;
    private JComboBox jcbTransmitterID;
    
    private JLabel jlTripID;
    private JLabel jlTripIDvalue;
    private JLabel jlDate;
    private JLabel jlStartExit;
    private JLabel jlEndExit;
    private JLabel jlTransmitterID;
    private JLabel jlTripStatus;
    private JLabel jlSubmitStatus;
    
    private JTextField jtfDate;
    
    private JRadioButton jrbTicket;
    private JRadioButton jrbTransmitter;
    private ButtonGroup bgPaymentType;
    
    private JRadioButton jrbCar;
    private JRadioButton jrbTruck;
    private ButtonGroup bgVehicleClass;
    
    private JButton jbSubmit;

    private String[] strTripStatus = {"Underway", "Completed But Not Paid", "Paid"};

    private int dbTrip_ID;
    private String dbDate;
    private String dbStart_Exit;
    private int dbStart_Exit_ID;
    private int dbEnd_Exit_ID;
    private String dbEnd_Exit;
    private String dbPayment_Type;
    private String dbTransmitter_IDstring;
    private int dbTransmitter_ID;
    private String dbVehicle_Class;
    private String dbTrip_Status;

    public StartTrip()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            dbConnection = DriverManager.getConnection(DBURL , DBUSER , DBPASS);
            sqlStatementGetExits = dbConnection.createStatement();
            sqlStatementGetTransmitter = dbConnection.createStatement();
            sqlStatementGetTripIDvalue = dbConnection.createStatement();
            sqlStatementInsertTrip = dbConnection.createStatement();
            sqlStatementGetStartExitID = dbConnection.createStatement();
            sqlStatementGetEndExitID = dbConnection.createStatement();
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
        jfTripWindow = new JFrame("Start Trip");
        
        jpMain = new JPanel();
        jpExits = new JPanel();
        jpPaymentType = new JPanel();
        jpVehicleClass = new JPanel();
        jpSubmit = new JPanel();

        tbPaymentType = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Payment Type");
        tbPaymentType.setTitleJustification(TitledBorder.LEFT);
        jpPaymentType.setBorder(tbPaymentType);

        tbVehicleClass = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Vehicle Class");
        tbVehicleClass.setTitleJustification(TitledBorder.LEFT);
        jpVehicleClass.setBorder(tbVehicleClass);

        flMain = new FlowLayout();
        gblMain = new GridBagLayout();
        gbcMain = new GridBagConstraints();
        gbcMain.weightx = 100;
        gbcMain.weighty = 100;

        jcbStartExit = new JComboBox();
        jcbStartExit.setMaximumRowCount(3);
        jcbStartExit.addActionListener(this);
        jcbEndExit = new JComboBox();
        jcbEndExit.setMaximumRowCount(3);
        jcbEndExit.addActionListener(this);
        jcbStatus = new JComboBox(strTripStatus);
        jcbStatus.setMaximumRowCount(3);
        jcbStatus.addActionListener(this);
        jcbTransmitterID = new JComboBox();
        jcbTransmitterID.setMaximumRowCount(3);
        jcbTransmitterID.addActionListener(this);

        jlTripID = new JLabel("Trip ID: ");
        jlTripIDvalue = new JLabel("");
        jlDate = new JLabel("Date: ");
        jlStartExit = new JLabel("Start Exit: ");
        jlEndExit = new JLabel("End Exit: ");
        jlTransmitterID = new JLabel("Transmitter ID: ");
        jlTripStatus = new JLabel("Trip Status: ");
        jlSubmitStatus = new JLabel("");

        jtfDate = new JTextField(10);
        jtfDate.setText("yyyy-mm-dd");

        jrbTicket = new JRadioButton("Ticket", true);
        jrbTicket.addActionListener(this);
        jrbTransmitter = new JRadioButton("Transmitter");
        jrbTransmitter.addActionListener(this);
        bgPaymentType = new ButtonGroup();
        bgPaymentType.add(jrbTicket);
        bgPaymentType.add(jrbTransmitter);

        jrbCar = new JRadioButton("Car", true);
        jrbCar.addActionListener(this);
        jrbTruck = new JRadioButton("Truck");
        jrbTruck.addActionListener(this);
        bgVehicleClass = new ButtonGroup();
        bgVehicleClass.add(jrbCar);
        bgVehicleClass.add(jrbTruck);

        jbSubmit = new JButton("Submit");
        jbSubmit.addActionListener(this);

        jpExits.setLayout(flMain);
        jpExits.add(jlTripID);
        jpExits.add(jlTripIDvalue);
        jpExits.add(jlDate);
        jpExits.add(jtfDate);
        jpExits.add(jlStartExit);
        jpExits.add(jcbStartExit);
        jpExits.add(jlEndExit);
        jpExits.add(jcbEndExit);

        jpPaymentType.setLayout(gblMain);

        gbcMain.ipadx = 20;
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 2;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jrbTicket, gbcMain);
        jpPaymentType.add(jrbTicket);

        gbcMain.ipadx = 20;
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 2;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jrbTransmitter, gbcMain);
        jpPaymentType.add(jrbTransmitter);

        gbcMain.ipadx = 20;
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 1;
        gbcMain.gridy = 2;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jcbTransmitterID, gbcMain);
        jpPaymentType.add(jcbTransmitterID);

        gbcMain.ipadx = 20;
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 2;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTransmitterID, gbcMain);
        jpPaymentType.add(jlTransmitterID);

        jpVehicleClass.setLayout(gblMain);

        gbcMain.ipadx = 20;
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jrbCar, gbcMain);
        jpVehicleClass.add(jrbCar);

        gbcMain.ipadx = 20;
        gbcMain.anchor = GridBagConstraints.WEST;
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 2;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jrbTruck, gbcMain);
        jpVehicleClass.add(jrbTruck);

        jpSubmit.setLayout(gblMain);

        gbcMain.ipady = 50;
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlTripStatus, gbcMain);
        jpSubmit.add(jlTripStatus);

        gbcMain.ipady = 0;
        gbcMain.gridx = 1;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jcbStatus, gbcMain);
        jpSubmit.add(jcbStatus);

        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jbSubmit, gbcMain);
        jpSubmit.add(jbSubmit);

        gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jlSubmitStatus, gbcMain);
        jpSubmit.add(jlSubmitStatus);

        jpMain.setLayout(gblMain);

        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        gbcMain.gridwidth = 3;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpExits, gbcMain);
        jpMain.add(jpExits);

        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpPaymentType, gbcMain);
        jpMain.add(jpPaymentType);

        gbcMain.gridx = 1;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 1;
        gbcMain.gridheight = 1;
        gblMain.setConstraints(jpVehicleClass, gbcMain);
        jpMain.add(jpVehicleClass);

        gbcMain.gridx = 2;
        gbcMain.gridy = 1;
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

        dbTrip_ID = 0;
        dbDate = "";
        dbStart_Exit = "";
        dbEnd_Exit = "";
        dbStart_Exit_ID = 0;
        dbEnd_Exit_ID = 0;
        dbPayment_Type = "Ticket";
        dbTransmitter_IDstring = "NULL";
        dbTransmitter_ID = 0;
        dbVehicle_Class = "Car";
        dbTrip_Status = "Underway";

        try
        {
            jlTripIDvalue.setText("");
            sqlResultsGetTripIDvalue = sqlStatementGetTripIDvalue.executeQuery("select max(trip_id) trip_id from trips");
            while(sqlResultsGetTripIDvalue.next())
            {
               jlTripIDvalue.setText(new Integer(sqlResultsGetTripIDvalue.getInt("trip_id") + 1).toString());
            }
        }
        catch(SQLException sqle)
        {

        }

        try
        {
            jcbStartExit.removeAllItems();
            jcbEndExit.removeAllItems();
            sqlResultsGetExits = sqlStatementGetExits.executeQuery("select exit_number, nearest_town from exits");
            while(sqlResultsGetExits.next())
            {
                jcbStartExit.addItem(sqlResultsGetExits.getString("exit_number") + " : " + sqlResultsGetExits.getString("nearest_town"));
                jcbEndExit.addItem(sqlResultsGetExits.getString("exit_number") + " : " + sqlResultsGetExits.getString("nearest_town"));
            }
        }
        catch(SQLException sqle)
        {

        }

        try
        {
            jcbTransmitterID.removeAllItems();
            sqlResultsGetTransmitter = sqlStatementGetTransmitter.executeQuery("select transmitter_id, (select name from customers where customer_id = t.customer_id) name from transmitters t");            
            while(sqlResultsGetTransmitter.next())
            {
                jcbTransmitterID.addItem(sqlResultsGetTransmitter.getString("transmitter_id") + " : " + sqlResultsGetTransmitter.getString("name"));
            }
        }
        catch(SQLException sqle)
        {

        }
    }

    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == jbSubmit)
        {
            String newTrip = "";
            dbTrip_ID = new Integer(jlTripIDvalue.getText());
            dbDate = jtfDate.getText();
            if(dbTransmitter_IDstring == "NULL" && dbStart_Exit_ID != dbEnd_Exit_ID && dbDate != "yyyy-mm-dd")
            {
                newTrip = "insert into trips (trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class) " +
                "values (" + dbTrip_ID + ", " + dbStart_Exit_ID + ", " + dbEnd_Exit_ID + ", '" + dbDate + "', '" + dbPayment_Type + "', "
                + dbTransmitter_IDstring + ", '" + dbTrip_Status + "', '" + dbVehicle_Class + "')";
            }
            else if (dbStart_Exit_ID != dbEnd_Exit_ID && dbDate != "yyyy-mm-dd")
            {
                newTrip = "insert into trips (trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class) " +
                "values (" + dbTrip_ID + ", " + dbStart_Exit_ID + ", " + dbEnd_Exit_ID + ", '" + dbDate + "', '" + dbPayment_Type + "', "
                + dbTransmitter_ID + ", '" + dbTrip_Status + "', '" + dbVehicle_Class + "')";
            }

            try
            {
                sqlStatementInsertTrip.execute(newTrip);
            }
            catch(SQLException sqle)
            {

            }
            if(dbStart_Exit_ID != dbEnd_Exit_ID && dbDate != "yyyy-mm-dd")
            {
                jlSubmitStatus.setText("Successful!");
                jlTripIDvalue.setText(new Integer(dbTrip_ID + 1).toString());
                jtfDate.setText("yyyy-mm-dd");
                jcbStartExit.setSelectedIndex(0);
                jcbEndExit.setSelectedIndex(0);
                jrbTicket.setSelected(true);
                jcbTransmitterID.setSelectedIndex(0);
                jrbCar.setSelected(true);
                jcbStatus.setSelectedIndex(0);
            }
            else if(dbStart_Exit_ID == dbEnd_Exit_ID && dbDate != "yyyy-mm-dd")
            {
                jlSubmitStatus.setText("Cannot have same exits.");
            }
            else if(dbDate == "yyyy-mm-dd" && dbStart_Exit_ID != dbEnd_Exit_ID)
            {
                jlSubmitStatus.setText("Invalid Date");
            }
            else if(dbStart_Exit_ID == dbEnd_Exit_ID && dbDate == "yyyy-mm-dd")
            {
                jlSubmitStatus.setText("Cannot have same exits. Invalid date.");
            }
        }

        if(e.getSource() == jcbStartExit)
        {
            dbStart_Exit = jcbStartExit.getSelectedItem().toString();
            dbStart_Exit = dbStart_Exit.substring(0, dbStart_Exit.indexOf(" "));
            try
            {
                sqlResultsGetStartExitID = sqlStatementGetStartExitID.executeQuery("select exit_id from exits where exit_number = '" + dbStart_Exit + "'");
                while(sqlResultsGetStartExitID.next())
                {
                    dbStart_Exit_ID = new Integer(sqlResultsGetStartExitID.getString("exit_id")).intValue();
                }
            }
            catch(SQLException sqle)
            {

            }
        }

        if(e.getSource() == jcbEndExit)
        {
            dbEnd_Exit = jcbEndExit.getSelectedItem().toString();
            dbEnd_Exit = dbEnd_Exit.substring(0, dbEnd_Exit.indexOf(" "));
            try
            {
                sqlResultsGetEndExitID = sqlStatementGetEndExitID.executeQuery("select exit_id from exits where exit_number = '" + dbEnd_Exit + "'");
                while(sqlResultsGetEndExitID.next())
                {
                    dbEnd_Exit_ID = new Integer(sqlResultsGetEndExitID.getString("exit_id")).intValue();
                }
            }
            catch(SQLException sqle)
            {

            }
        }

        if(e.getSource() == jrbTicket)
        {
            dbPayment_Type = "Ticket";
            dbTransmitter_IDstring = "NULL";
        }

        if(e.getSource() == jrbTransmitter || e.getSource() == jcbTransmitterID)
        {
            if(jrbTransmitter.isSelected())
            {
                dbPayment_Type = "Transmitter";
                dbTransmitter_IDstring = jcbTransmitterID.getSelectedItem().toString();
                dbTransmitter_IDstring = dbTransmitter_IDstring.substring(0, dbTransmitter_IDstring.indexOf(" "));
                dbTransmitter_ID = new Integer(dbTransmitter_IDstring).intValue();
            }
        }

        if(e.getSource() == jrbCar)
        {
            dbVehicle_Class = "Car";
        }

        if(e.getSource() == jrbTruck)
        {
            dbVehicle_Class = "Truck";
        }

        if(e.getSource() == jcbStatus)
        {
            dbTrip_Status = jcbStatus.getSelectedItem().toString();
        }

        if(e.getSource() == jcbStartExit)
        {
            dbStart_Exit = jcbStartExit.getSelectedItem().toString();
        }
    }
}
