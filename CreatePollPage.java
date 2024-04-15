import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreatePollPage extends JFrame {

    private JLabel titleLabel;
    private JLabel pollNameLabel;
    private JTextField pollNameField;
    private JLabel representativeLabel;
    private JTextField representativeField;
    private JButton addRepresentativeButton; // Changed to avatar button
    private JButton startPollButton;
    private DefaultListModel<String> representativeListModel;
    private JList<String> representativeList;
    private JLabel numberOfVotersLabel;
    private JTextField numberOfVotersField;
    public static int a = 3;
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.30.157:1521/xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "admin";


    public CreatePollPage() {
        setTitle("Create Poll");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(230, 240, 250)); // Set background color to light blue

        titleLabel = new JLabel("Create Poll", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

        pollNameLabel = new JLabel("Poll Name:");
        add(pollNameLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 5), 0, 0));

        pollNameField = new JTextField(15);
        add(pollNameField, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 10), 0, 0));

        representativeLabel = new JLabel("Representative Name:");
        add(representativeLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 5), 0, 0));

        representativeField = new JTextField(15);
        add(representativeField, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 10), 0, 0));

        // Avatar button to add representative
        addRepresentativeButton = new JButton(new ImageIcon(getCircularAvatarIcon(20, Color.GREEN, Color.WHITE)));
        addRepresentativeButton.setContentAreaFilled(false);
        addRepresentativeButton.setBorderPainted(false);
        addRepresentativeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRepresentative();
            }
        });
        add(addRepresentativeButton, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 10, 10), 0, 0));

        numberOfVotersLabel = new JLabel("Number of Voters:");
        add(numberOfVotersLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 5), 0, 0));

        numberOfVotersField = new JTextField(15);
        add(numberOfVotersField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 10, 10), 0, 0));

        startPollButton = new JButton("Post Poll");
        startPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPoll();
            }
        });
        add(startPollButton, new GridBagConstraints(0, 4, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

        // Title for the list of representatives
        JLabel representativeListTitle = new JLabel("List of Representatives");
        add(representativeListTitle, new GridBagConstraints(0, 5, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0));

        representativeListModel = new DefaultListModel<>();
        representativeList = new JList<>(representativeListModel);
        JScrollPane scrollPane = new JScrollPane(representativeList);
        add(scrollPane, new GridBagConstraints(0, 6, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

        setBounds(460, 200, 700, 500); // Adjusted frame size
        setVisible(true);
    }

    private void addRepresentative() {
        String representativeName = representativeField.getText().trim();
        if (!representativeName.isEmpty()) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Representative (Name) VALUES (?)")) {
                preparedStatement.setString(1, representativeName);
                preparedStatement.executeUpdate();
                representativeListModel.addElement(representativeName);
                representativeField.setText("");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while adding representative.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a representative name.");
        }
    }

    private void startPoll() {
        if (representativeListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one representative.");
        } else {
            String pollName = pollNameField.getText().trim();
            if (pollName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a poll name.");
            } else {
                String numberOfVotersText = numberOfVotersField.getText().trim();
                if (numberOfVotersText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter the number of voters.");
                } else {
                    try {
                        int numberOfVoters = Integer.parseInt(numberOfVotersText);
                        if (numberOfVoters <= 0) {
                            JOptionPane.showMessageDialog(this, "Number of voters must be greater than zero.");
                        } else {
                            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Poll (poll_name, representatives_count, representatives_list) VALUES (?, ?, ?)")) {
                                preparedStatement.setString(1, pollName);
                                preparedStatement.setInt(2, representativeListModel.getSize());
                                StringBuilder representativesList = new StringBuilder();
                                for (int i = 0; i < representativeListModel.getSize(); i++) {
                                    representativesList.append(representativeListModel.getElementAt(i));
                                    if (i < representativeListModel.getSize() - 1) {
                                        representativesList.append(", ");
                                    }
                                }
                                preparedStatement.setString(3, representativesList.toString());
                                preparedStatement.executeUpdate();
                                JOptionPane.showMessageDialog(this, "Poll '" + pollName + "' Posted!");
                                setVisible(false);
                                new VoterLoginPage(numberOfVoters, pollNameField.getText());
                            } catch (SQLException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(this, "Error occurred while posting the poll.");
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid number for the number of voters.");
                    }
                }
            }
        }
    }

    // Method to create circular avatar icon
    private Image getCircularAvatarIcon(int size, Color bgColor, Color textColor) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2d.setColor(bgColor);
        g2d.fill(new Ellipse2D.Double(0, 0, size, size));

        // Plus sign
        g2d.setColor(textColor);
        g2d.setStroke(new BasicStroke(3)); // Adjust stroke width for bolder lines
        int plusSize = (int) (size * 0.6);
        int plusOffset = (size - plusSize) / 2;
        g2d.drawLine(plusOffset, size / 2, plusOffset + plusSize, size / 2);
        g2d.drawLine(size / 2, plusOffset, size / 2, plusOffset + plusSize);

        g2d.dispose();
        return img;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CreatePollPage();
            }
        });
    }
}
