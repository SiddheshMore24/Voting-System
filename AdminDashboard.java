import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard extends JFrame {
    private JButton createPollButton;
    private JButton endPollButton;
    private JButton historyButton;
    private JButton logoutButton;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Title label
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create poll button
        createPollButton = new JButton("Create Poll");
        customizeButton(createPollButton);
        createPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to create poll page
            }
        });
        buttonPanel.add(createPollButton);

        // End poll button
        endPollButton = new JButton("End Poll");
        customizeButton(endPollButton);
        endPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to end poll page
            }
        });
        buttonPanel.add(endPollButton);

        // History button
        historyButton = new JButton("View History");
        customizeButton(historyButton);
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to poll history page
            }
        });
        buttonPanel.add(historyButton);

        // Logout button
        logoutButton = new JButton("Logout");
        customizeButton(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to login page
            }
        });
        buttonPanel.add(logoutButton);

        add(buttonPanel);
    }

    private void customizeButton(JButton button) {
        button.setPreferredSize(new Dimension(200, 30));
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }
}
