package database.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class CreationDatabase {

    public final static String jdbcDriver = "com.mysql.jdbc.Driver";
    public final static String dbName = "schoolmanagement";
    public final static  String dbUrl = "jdbc:mysql://localhost:3306/schoolmanagement";
    public final static String dbUser = "root";
    public final static String dbPass = "";
    public static Statement stmt;
    public static Connection conn = null;

    public static Connection setupDatabse() {
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        return conn;
    }

    public static ResultSet execQuery(String query) {
        ResultSet result;
        conn = setupDatabse();
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }

    public static boolean execAction(String qu) {
        conn = setupDatabse();
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }

}
