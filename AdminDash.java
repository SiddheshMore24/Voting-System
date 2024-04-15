import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AdminDash extends JFrame implements ActionListener {
    JButton createPollButton;
    JButton historyButton;
    JButton endPollButton;
    JButton backButton;

    public AdminDash(String pollName) {
        this.setTitle("Hey! Admin");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.getContentPane().setBackground(new Color(135, 206, 235)); // Set sky blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel headlineLabel = new JLabel("Hey! Admin", JLabel.CENTER);
        headlineLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headlineLabel.setForeground(Color.BLACK); // Set black text color
        this.add(headlineLabel, gbc);

        gbc.gridy++;
        createPollButton = new JButton("Create Poll");
        createPollButton.addActionListener(this);
        createPollButton.setForeground(Color.BLACK); // Set black text color
        this.add(createPollButton, gbc);

        gbc.gridy++;
        historyButton = new JButton("History");
        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new HistoryPage();
                setVisible(false);
            }
        });
        historyButton.setForeground(Color.BLACK); // Set black text color
        this.add(historyButton, gbc);

        gbc.gridy++;
        endPollButton = new JButton("End Poll");
        endPollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(AdminDash.this,
                        "Are you sure you want to view the result?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    // Proceed to view the result
                    new ResultPage(pollName);
                    setVisible(false);
                }
            }
        });
        endPollButton.setForeground(Color.BLACK); // Set black text color
        this.add(endPollButton, gbc);

        gbc.gridy++;
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Hide the current frame (Admin Dashboard)
                new AdminLoginPage().setVisible(true); // Show the admin login page
            }
        });
        backButton.setForeground(Color.BLACK); // Set black text color
        this.add(backButton, gbc);

        setBounds(460, 200, 600, 400);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createPollButton) {
            setVisible(false);
            new CreatePollPage();
        }
    }
}
