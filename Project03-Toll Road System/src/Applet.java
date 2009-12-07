import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTable;

public class Applet extends JApplet implements ActionListener
{
    private JButton startButton;
    private JButton endButton;
    private JButton depositButton;

    private JTable resultsTable;

    private String[] columnNames = {"Start exit", "End exit", "Date", "Payment",
                                    "Class"};

    @Override
    public void init()
    {
        startButton = new JButton("Start trip");
        endButton = new JButton("End trip");
        depositButton = new JButton("Deposit money");

        resultsTable = new JTable();

    }

    public void actionPerformed(ActionEvent e)
    {

    }

}
