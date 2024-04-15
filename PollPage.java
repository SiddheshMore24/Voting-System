import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PollPage extends JFrame {
    private JList<String> representativeList;
    private JButton voteButton;
    private String pollName;

    public PollPage(int a, String pollName) {
        setTitle("Select Your Representative");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());

        // Set background color
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Use custom font for the title
        Font titleFont = new Font("Arial", Font.BOLD, 20);
        JLabel titleLabel = new JLabel("Select Your Representative");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(50, 50, 50)); // Set font color

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Fetch representative names from the database
        List<String> representatives = fetchRepresentativesFromDB();
        representativeList = new JList<>(representatives.toArray(new String[0]));
        representativeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set visible row count for the JList
        representativeList.setVisibleRowCount(10);

        // Create a JScrollPane with adjusted preferred size
        JScrollPane scrollPane = new JScrollPane(representativeList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        voteButton = new JButton("Vote");
        voteButton.setFont(new Font("Arial", Font.BOLD, 16));
        voteButton.setBackground(new Color(50, 150, 250));
        voteButton.setForeground(Color.WHITE);
        voteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedRepresentative = representativeList.getSelectedValue();
                if (selectedRepresentative != null) {
                    JOptionPane.showMessageDialog(null, "You have voted for: " + selectedRepresentative);

                    // Update the vote count in the database
                    updateVoteCount(selectedRepresentative);
                    if (a != 0)
                        new VoterLoginPage(a, pollName);
                    else
                        new AdminDash(pollName);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a representative.");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(voteButton, gbc);

        add(panel, BorderLayout.CENTER);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to fetch representative names from the database
    private List<String> fetchRepresentativesFromDB() {
        List<String> representatives = new ArrayList<>();
        String DB_URL = "jdbc:oracle:thin:@192.168.30.157:1521/xe";
        String DB_USER = "system";
        String DB_PASSWORD = "admin";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT Name FROM Representative")) {
            while (resultSet.next()) {
                representatives.add(resultSet.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while fetching representatives from the database.");
        }
        return representatives;
    }

    // Method to update the vote count in the database
    private void updateVoteCount(String representativeName) {
        String DB_URL = "jdbc:oracle:thin:@192.168.30.157:1521/xe";
        String DB_USER = "system";
        String DB_PASSWORD = "admin";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Representative SET VoteCount = VoteCount + 1 WHERE Name = ?")) {
            preparedStatement.setString(1, representativeName);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while updating vote count for the representative.");
        }
    }
}
