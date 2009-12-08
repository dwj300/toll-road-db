import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import org.sourceforge.jcalendarbutton.JCalendarButton;

public class JCalendarExample extends JApplet
{
    private final int JFRAME_WIDTH = 400;
    private final int JFRAME_HEIGHT = 400;

    private JFrame jfWindow;

    private JPanel jpMain;

    private FlowLayout flMain;

    private JCalendarButton jcbCalendar;

    public JCalendarExample()
    {

    }

    @Override
    public void init()
    {
        jfWindow = new JFrame("JCalendar Example");

        jpMain = new JPanel();

        flMain = new FlowLayout();

        jpMain.setLayout(flMain);
        jcbCalendar = new JCalendarButton();

        jpMain.add(jcbCalendar);

        jfWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jfWindow.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        jfWindow.setVisible(true);
        jfWindow.setResizable(false);
        jfWindow.add(jpMain);
        setSize(0,0);

    }
}
