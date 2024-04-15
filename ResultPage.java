import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ResultPage extends JFrame {

    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.30.157:1521/xe";
    private static final String USER = "system";
    private static final String PASS = "admin";

    public ResultPage(String pollName) {
        super("Election Results");

        ArrayList<Result> results = fetchData();

        if (!results.isEmpty()) {
            Collections.sort(results, Comparator.comparingInt(Result::getVoteCount).reversed());

            JPanel centerPanel = new JPanel(new GridLayout(2, 1));

            String winnerText;
            if (checkForTie(results)) {
                StringBuilder tieNames = new StringBuilder("Tie between ");
                for (Result result : results) {
                    if (result.getVoteCount() == results.get(0).getVoteCount()) {
                        tieNames.append(result.getName()).append(", ");
                    }
                }
                tieNames.setLength(tieNames.length() - 2);
                winnerText = tieNames.toString();
            } else {
                String winnerName = results.get(0).getName();
                winnerText = "Winner: " + winnerName;
            }
            JLabel winnerLabel = new JLabel(winnerText);
            winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
            winnerLabel.setHorizontalAlignment(JLabel.CENTER);
            centerPanel.add(winnerLabel);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Vote Count");
            for (Result result : results) {
                model.addRow(new Object[]{result.getName(), result.getVoteCount()});
            }

            JTable table = new JTable(model);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            centerPanel.add(scrollPane);

            add(centerPanel, BorderLayout.CENTER);
        } else {
            JOptionPane.showMessageDialog(null, "No results found!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        JButton smallButton = new JButton("Close Poll");
        smallButton.setPreferredSize(new Dimension(80, 30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(smallButton);

        add(buttonPanel, BorderLayout.SOUTH);

        smallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData(pollName);
                dispose();
            }
        });

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Set background color
        getContentPane().setBackground(new Color(240, 240, 240));
    }

    private ArrayList<Result> fetchData() {
        ArrayList<Result> results = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT NAME, VOTECOUNT FROM Representative ORDER BY VOTECOUNT DESC";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String name = rs.getString("NAME");
                int voteCount = rs.getInt("VOTECOUNT");
                results.add(new Result(name, voteCount));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private void deleteData(String pollName) {
        Connection conn = null;
        PreparedStatement insertStatement = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String selectWinnerSql = "SELECT NAME FROM Representative ORDER BY VOTECOUNT DESC FETCH FIRST 1 ROWS ONLY";
            ResultSet winnerResultSet = stmt.executeQuery(selectWinnerSql);

            String winnerName = null;
            if (winnerResultSet.next()) {
                winnerName = winnerResultSet.getString("NAME");
            }

            String insertHistorySql = "INSERT INTO History (poll_name, winner) VALUES (?, ?)";
            insertStatement = conn.prepareStatement(insertHistorySql);
            insertStatement.setString(1, pollName);
            if (checkForTie(fetchData())) {
                insertStatement.setString(2, "Tie");
            } else {
                insertStatement.setString(2, winnerName);
            }
            insertStatement.executeUpdate();

            String deleteRepresentativeSql = "DELETE FROM Representative";
            stmt.executeUpdate(deleteRepresentativeSql);

            JOptionPane.showMessageDialog(null, "Poll closed successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);

            new AdminDash("");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error occurred while closing poll.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkForTie(ArrayList<Result> results) {
        int firstVoteCount = results.get(0).getVoteCount();
        for (Result result : results) {
            if (result.getVoteCount() != firstVoteCount) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ResultPage("");
            }
        });
    }
}

class Result {
    private String name;
    private int voteCount;

    public Result(String name, int voteCount) {
        this.name = name;
        this.voteCount = voteCount;
    }

    public String getName() {
        return name;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
