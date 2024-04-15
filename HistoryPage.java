import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class HistoryPage extends JFrame {

    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.30.157:1521/xe";
    private static final String USER = "system";
    private static final String PASS = "admin";

    public HistoryPage() {
        super("Poll History");
        getContentPane().setBackground(new Color(135, 206, 235)); // Set sky blue background

        ArrayList<History> historyList = fetchHistory();

        if (!historyList.isEmpty()) {
            JPanel centerPanel = new JPanel(new GridLayout(1, 1));
            centerPanel.setBackground(new Color(135, 206, 235)); // Set sky blue background

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Poll Name");
            model.addColumn("Winner Name");
            for (History entry : historyList) {
                model.addRow(new Object[]{entry.getPollName(), entry.getWinnerName()});
            }

            JTable table = new JTable(model);
            table.setBackground(Color.WHITE); // Set white background for the table
            table.setForeground(Color.BLACK); // Set black text color for the ctable
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            table.setDefaultRenderer(Object.class, centerRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            centerPanel.add(scrollPane);

            add(centerPanel, BorderLayout.CENTER);
        } else {
            JOptionPane.showMessageDialog(null, "No history found!", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new HistoryPage();
    }
    private ArrayList<History> fetchHistory() {
        ArrayList<History> historyList = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT poll_name, winner FROM History ORDER BY poll_name";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String pollName = rs.getString("poll_name");
                String winnerName = rs.getString("winner");
                historyList.add(new History(pollName, winnerName));
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

        return historyList;
    }


    class History {
        private String pollName;
        private String winnerName;

        public History(String pollName, String winnerName) {
            this.pollName = pollName;
            this.winnerName = winnerName;
        }

        public String getPollName() {
            return pollName;
        }

        public String getWinnerName() {
            return winnerName;
        }
    }
}
