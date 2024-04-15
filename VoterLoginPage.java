import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VoterLoginPage extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private int numberOfVoters;
    private String pollName;
    private String[] representatives;
    private Connection connection;

    private static final String JDBC_URL = "jdbc:oracle:thin:@192.168.30.157:1521/xe";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "admin";

    public VoterLoginPage(int numberOfVoters, String pollName) {
        this.numberOfVoters = numberOfVoters;
      this.pollName=pollName;
        setTitle("Voter Login");
        setSize(600, 400); // Set initial size to be larger
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(173, 216, 230)); // Light Blue Color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;



        JLabel titleLabel = new JLabel("Voter Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set Title Font
        titleLabel.setForeground(Color.BLACK);  // Set Title Color

        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        signupButton = new JButton("Signup");

        panel.add(titleLabel, gbc);
        gbc.gridy++;
        panel.add(nameLabel, gbc);
        gbc.gridx++;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(emailLabel, gbc);
        gbc.gridx++;
        panel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx++;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(loginButton, gbc);
        gbc.gridx++;
        panel.add(signupButton, gbc);

        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call method to handle login
                handleLogin();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call method to handle signup
                handleSignup();
            }
        });

        setBounds(460,200,600,400);
        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Voter WHERE email = ? AND password = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                navigateToPollPage();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Login failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void navigateToPollPage() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                if(numberOfVoters>0)
                {
                    new PollPage(numberOfVoters-1,pollName);
                }else
                {
                    new AdminDash(pollName);
                }

            }
        });
        setVisible(false);
    }

    private void handleSignup() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Voter (name, email, password) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Signup Successful!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public static void main(String[] args) {
//        new VoterLoginPage(1,"");
//    }
}
