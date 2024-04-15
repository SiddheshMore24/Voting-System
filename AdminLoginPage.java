import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.IOException;

public class AdminLoginPage extends JFrame {
    private JTextField nameField;
    private JTextField organizationField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton actionButton;
    private JButton toggleButton;
    private boolean signupMode = false;
    private Connection connection;

    public AdminLoginPage() {
        setTitle("Admin Credentials");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.30.157:1521/xe", "system", "admin");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(173, 216, 230)); // Light Blue background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 5, 5); // Adjusted top inset
        gbc.anchor = GridBagConstraints.CENTER;

        // Title Label
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size and made it bold
        gbc.gridwidth = 2; // Spanning two columns
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        // Adding image to the top center
        ImageIcon imageIcon = new ImageIcon("your_image_path.jpg"); // Replace "your_image_path.jpg" with the actual path to your image
        JLabel imageLabel = new JLabel(imageIcon);
        gbc.gridwidth = 2; // Spanning two columns
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(imageLabel, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        JLabel nameLabel = new JLabel("Name:");
        JLabel organizationLabel = new JLabel("Organization Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        nameField = new JTextField(20);
        organizationField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        actionButton = new JButton("Login");
        toggleButton = new JButton("Create Account");

        panel.add(nameLabel, gbc);
        gbc.gridx++;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(organizationLabel, gbc);
        gbc.gridx++;
        panel.add(organizationField, gbc);
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

        panel.add(actionButton, gbc);
        gbc.gridx++;
        panel.add(toggleButton, gbc);

        actionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                if (signupMode) {
                    // Create account
                    String name = nameField.getText();
                    String organization = organizationField.getText();
                    if (createAccount(name, organization, email, password)) {
                        JOptionPane.showMessageDialog(null, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Login
                    if (authenticateUser(email, password)) {
                        // Login successful, open AdminDash
                        JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        AdminDash adminDash = new AdminDash("");
                        adminDash.setVisible(true);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        toggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleSignupMode();
            }
        });

        add(panel);
        setBounds(460, 200, 600, 400);
        setVisible(true);
    }

    private void toggleSignupMode() {
        if (signupMode) {
            setTitle("Admin Login");
            actionButton.setText("Login");
            toggleButton.setText("Create Account");
            clearFields();
        } else {
            setTitle("Admin Signup");
            actionButton.setText("Create Account");
            toggleButton.setText("Back to Login");
        }
        signupMode = !signupMode;
    }

    private boolean authenticateUser(String email, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT password FROM Admin WHERE email = ? AND password = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a row is returned, user exists and credentials are correct
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createAccount(String name, String organization, String email, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Admin (name, organization, email, password) VALUES (?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, organization);
            statement.setString(3, email);
            statement.setString(4, password);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // If rows affected, account created successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        nameField.setText("");
        organizationField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        new AdminLoginPage();
    }
}
