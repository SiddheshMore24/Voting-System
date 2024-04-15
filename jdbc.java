import java.sql.*;

public class jdbc {
    public static void main(String[] args) {
        //Class is already a pre-defined class in Java which has for name method in which we upload the driver.
        try {
            //Driver uploading
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //Getting the connection....
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.30.157:1521/xe", "system", "admin");
//@192.168.30.157
            if(con!= null) {
                System.out.println("Connection Successfull😊😊😊");

                //Creating Table
                //Statement smt = con.createStatement();
                //smt.executeUpdate("create table emp(eno NUMBER PRIMARY KEY, name VARCHAR(12), sal NUMBER)");

                //con.close(); //Close the connection due to security purposes.
                //System.out.println("Executed successfully...");
            }
            else {
                System.out.println("Error");
            }
        }
        catch(ClassNotFoundException e) {
            System.out.println("Class Not Found Exception");
        }
        catch (SQLException e) {
            System.out.println("SQL Exception...Connection not established...");
        }

        catch(Exception e) {
            System.out.println(e);
        }
    }
}