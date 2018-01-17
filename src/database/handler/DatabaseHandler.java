package database.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private final String jdbcDriver = "com.mysql.jdbc.Driver";
    private final String dbName = "schoolmanagement";
    private final String dbUrl = "jdbc:mysql://localhost:3306/schoolmanagement?createDatabaseIfNotExist=true";
    private final String dbUser = "root";
    private final String dbPass = "";
    private static Connection conn = null;
    private final PreparedStatement pst = null;
    private final ResultSet rs = null;
    private Statement stmt = null;

    public DatabaseHandler() {
        createConnection();
        setupAdminTable();
        setupApproveAdminTable();
        setupCourseTable();
        setupSubjectTable();
        setupStudentTable();
        setupTeacherTable();
        setupStaffTypeTable();
        setupStaffTable();
        setupBookTable();
        setupMemberTable();
        setupLibrarySettingTable();
        setupIssueTable();
        setupFeeSettingTable();
        setupStudentFeeTable();
        setupTeachSalarySettingTable();
        setupTeacherSlaryTable();
        setupStaffSalarySettingTable();
        setupStaffSlaryTable();
        setupNotificationTable();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    void createConnection() {
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            System.out.println("Created...");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error");
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    void setupStudentTable() {
        String TABLE_NAME = "studentdetails";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	firstName varchar(200),\n"
                    + "	lastName varchar(200),\n"
                    + "	birth varchar(50),\n"
                    + "	admission varchar(50),\n"
                    + "	course varchar(100),\n"
                    + "	semester varchar(100),\n"
                    + "	gender varchar(50),\n"
                    + "	guardianName varchar(200),\n"
                    + "	guardianContact varchar(20),\n"
                    + "	mobile varchar(20),\n"
                    + "	email varchar(200) unique,\n"
                    + "	location varchar(200),\n"
                    + "	photo blob,\n"
                    + "	studentId varchar(100) unique"
                    + " )";
            stmt.execute(sql);
            System.out.println("Successfully Created Student Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupTeacherTable() {
        String TABLE_NAME = "teacherdetails";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	firstName varchar(200),\n"
                    + "	lastName varchar(200),\n"
                    + "	birth varchar(50),\n"
                    + "	joinDate varchar(50),\n"
                    + "	course varchar(100),\n"
                    + "	semester varchar(100),\n"
                    + "	gender varchar(50),\n"
                    + "	subject varchar(200),\n"
                    + "	mobile varchar(20),\n"
                    + "	email varchar(200) unique,\n"
                    + "	location varchar(200),\n"
                    + "	photo blob,\n"
                    + "	teacherId varchar(100) unique"
                    + " )";
            stmt.execute(sql);
            System.out.println("Successfully Created Teacher Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupAdminTable() {
        String TABLE_NAME = "admindetails";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	firstName varchar(200),\n"
                    + "	lastName varchar(200),\n"
                    + "	email varchar(200) unique,\n"
                    + "	contact varchar(15),\n"
                    + "	password varchar(200),\n"
                    + "	adminType varchar(20),\n"
                    + "	gender varchar(50),\n"
                    + "	question varchar(200),\n"
                    + "	answer varchar(100),\n"
                    + "	photo blob"
                    + " )";
            stmt.execute(sql);
            String query = "select * from admindetails";
            ResultSet rs = execQuery(query);
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                String email = "admin";
                String password = DigestUtils.sha512Hex("admin");
                String type = "MAIN";
                stmt.executeUpdate("insert into admindetails(email,password,adminType) values('" + email + "','" + password + "', '" + type + "')");
            }
            System.out.println("Successfully Created Admin Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupApproveAdminTable() {
        String TABLE_NAME = "approveadmin";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	email varchar(200) unique,\n"
                    + "	password varchar(200)"
                    + " )";
            stmt.execute(sql);
            ResultSet rs = execQuery("select * from approveadmin");
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count == 0) {
                String email = "admin";
                String password = DigestUtils.sha512Hex("admin");
                stmt.executeUpdate("insert into approveadmin(email,password) values('" + email + "','" + password + "')");
            }
            System.out.println("Successfully Created Approve Admin Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupStaffTypeTable() {
        String TABLE_NAME = "staffcategory";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	typeName varchar(100) unique,\n"
                    + "	shift varchar(100),\n"
                    + "	time varchar(50)"
                    + " )";
            stmt.execute(sql);
            System.out.println("Successfully Created Staff Type Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void setupNotificationTable() {
        String TABLE_NAME = "notify";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	adminType varchar(100) unique,\n"
                    + "	message varchar(500)"
                    + " )";
            stmt.execute(sql);
            System.out.println("Successfully Created Staff Type Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setupStaffTable() {
        String TABLE_NAME = "staffdetails";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "	id int(100) primary key not null auto_increment,\n"
                    + "	firstName varchar(200),\n"
                    + "	lastName varchar(200),\n"
                    + "	birth varchar(50),\n"
                    + "	joinDate varchar(50),\n"
                    + "	staffType varchar(100),\n"
                    + "	workShift varchar(50),\n"
                    + "	workTime varchar(50),\n"
                    + "	gender varchar(100),\n"
                    + "	mobile varchar(20),\n"
                    + "	email varchar(200) unique,\n"
                    + "	location varchar(200),\n"
                    + "	photo blob,\n"
                    + "	staffId varchar(100) unique"
                    + " )";
            stmt.execute(sql);
            System.out.println("Successfully Created Staff Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setupSemesterTable(String dur, int flg) {
        if (flg == 1) {
            String TABLE_NAME = dur + "semester";
            try {
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "	id int(10) primary key auto_increment,\n"
                        + "	semesterList varchar(200) unique, \n"
                        + " fId int(10)"
                        + " )";
                stmt.execute(sql);
                for (int i = 1; i <= (Integer.parseInt(dur) * 2); i++) {
                    String string;
                    switch (i) {
                        case 1:
                            string = "st";
                            break;
                        case 2:
                            string = "nd";
                            break;
                        case 3:
                            string = "rd";
                            break;
                        default:
                            string = "th";
                            break;
                    }
                    String sem = String.valueOf(i) + string + " Semester";
                    int foreignId = 0;
                    String query = "insert into " + TABLE_NAME + " (semesterList,fId) values('" + sem + "', '" + foreignId + "')";
                    try {
                        stmt.executeUpdate(query);
                    } catch (Exception e) {
                        System.out.println("All ready created...");
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (flg == 0) {
            String TABLE_NAME = dur + "year";
            try {
                stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + " id int(10) primary key auto_increment,\n"
                        + " yearList varchar(200) unique, \n"
                        + " fId int(10)"
                        + " )";
                stmt.execute(sql);
                for (int i = 1; i <= Integer.parseInt(dur); i++) {
                    String string;
                    switch (i) {
                        case 1:
                            string = "st";
                            break;
                        case 2:
                            string = "nd";
                            break;
                        case 3:
                            string = "rd";
                            break;
                        default:
                            string = "th";
                            break;
                    }
                    String sem = String.valueOf(i) + string + " Year";
                    int foreignId = 0;
                    String query = "insert into " + TABLE_NAME + " (yearList,fId) values('" + sem + "', '" + foreignId + "')";
                    try {
                        stmt.executeUpdate(query);
                    } catch (Exception e) {
                        System.out.println("All ready created...");
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Successfully Created Semester/Year...");
    }

    void setupCourseTable() {
        String TABLE_NAME = "course";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "courseList varchar(200) unique,\n"
                    + "courseDuration int(10),\n"
                    + "flag int(10)"
                    + " )";
            stmt.execute(sql);

            System.out.println("Successfully Created Course Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupSubjectTable() {
        String TABLE_NAME = "subject";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "course varchar(200) ,\n"
                    + "semester varchar(200),\n"
                    + "subjectName varchar(200),\n"
                    + "fullMark double,\n"
                    + "passMark double,\n"
                    + "theory double,\n"
                    + "practical double"
                    + " )";
            stmt.execute(sql);

            System.out.println("Successfully Created Subject Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupBookTable() {
        String TABLE_NAME = "bookDb";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "bookName varchar(200) ,\n"
                    + "course varchar(100) ,\n"
                    + "semester varchar(100),\n"
                    + "authorName varchar(200),\n"
                    + "publisher varchar(200),\n"
                    + "bookNumber int(10),\n"
                    + "bookID varchar(100)"
                    + " )";
            stmt.execute(sql);

            System.out.println("Successfully Created Subject Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupMemberTable() {
        String TABLE_NAME = "librarymember";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "studentID varchar(200) unique,\n"
                    + "memberID varchar(200) unique"
                    + " )";
            stmt.execute(sql);

            System.out.println("Successfully Created Subject Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupIssueTable() {
        String TABLE_NAME = "issue";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "memberID varchar(200),\n"
                    + "bookID varchar(200),\n"
                    + "date varchar(100),\n"
                    + "renew int(10)"
                    + " )";
            stmt.execute(sql);

            System.out.println("Successfully Created Issue Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupLibrarySettingTable() {
        String TABLE_NAME = "librarysetting";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "noFine int(10) ,\n"
                    + "fine double ,\n"
                    + "numBook int(10)"
                    + " )";

            stmt.execute(sql);
            String noFine = "15", fine = "0.25", numBook = "2";
            String query1 = "select * from librarysetting";
            ResultSet rs = execQuery(query1);
            if (!rs.next()) {
                String query = "insert into librarysetting(noFine,fine,numBook) values('" + noFine + "','" + fine + "','" + numBook + "')";
                try {
                    stmt.executeUpdate(query);
                } catch (Exception e) {
                    System.out.println("All ready created...");
                }
            } else {
                System.out.println("Already Inserted...");
            }
            System.out.println("Successfully Created Library Setting Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupFeeSettingTable() {
        String TABLE_NAME = "feesetting";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "course varchar(100) unique,\n"
                    + "amount double"
                    + " )";

            stmt.execute(sql);

            System.out.println("Successfully Created Fee Setting Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupTeachSalarySettingTable() {
        String TABLE_NAME = "tsalarysetting";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "course varchar(100) ,\n"
                    + "subject varchar(100) unique,\n"
                    + "amount double"
                    + " )";

            stmt.execute(sql);

            System.out.println("Successfully Created Teacher Salary Setting Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupStaffSalarySettingTable() {
        String TABLE_NAME = "ssalarysetting";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "category varchar(100) ,\n"
                    + "shift varchar(100) ,\n"
                    + "hour int(10),\n"
                    + "amount double"
                    + " )";

            stmt.execute(sql);

            System.out.println("Successfully Created Teacher Salary Setting Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupStudentFeeTable() {
        String TABLE_NAME = "studentfee";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "studentID varchar(100) unique,\n"
                    + "totalAmount double,\n"
                    + "totalPay double,\n"
                    + "payDate varchar(50)"
                    + " )";

            stmt.execute(sql);

            System.out.println("Successfully Created Student Fee Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupTeacherSlaryTable() {
        String TABLE_NAME = "teachersalary";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "teacherID varchar(100) unique,\n"
                    + "totalPay double,\n"
                    + "recentPay double,\n"
                    + "payDate varchar(50),\n"
                    + "dueAmount double"
                    + " )";

            stmt.execute(sql);

            System.out.println("Successfully Created Student Fee Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupStaffSlaryTable() {
        String TABLE_NAME = "staffsalary";
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + "id int(10) primary key auto_increment,\n"
                    + "staffID varchar(100) unique,\n"
                    + "totalPay double,\n"
                    + "recentPay double,\n"
                    + "payDate varchar(50),\n"
                    + "dueAmount double"
                    + " )";

            stmt.execute(sql);

            System.out.println("Successfully Created Student Fee Database");
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setupStudentMarksTable(String name, String sub[]) {
        String TABLE_NAME = name;
        try {
            stmt = conn.createStatement();
            String sql = null;
            if (sub[0] == null) {
                System.out.println("Please Add Subjects.");
            } else if (sub[1] == null) {
                sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "id int(10) primary key auto_increment,\n"
                        + "sid varchar(200) ,\n"
                        + "course varchar(100),\n"
                        + "semester varchar(100),\n"
                        + sub[0] + "Th varchar(100),\n"
                        + sub[0] + "Pr varchar(100),\n"
                        + "remarks varchar(200)"
                        + " )";
            } else if (sub[2] == null) {
                sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "id int(10) primary key auto_increment,\n"
                        + "sid varchar(200) ,\n"
                        + "course varchar(100),\n"
                        + "semester varchar(100),\n"
                        + sub[0] + "Th varchar(100),\n"
                        + sub[0] + "Pr varchar(100),\n"
                        + sub[1] + "Th varchar(100),\n"
                        + sub[1] + "Pr varchar(100),\n"
                        + "remarks varchar(200)"
                        + " )";
            } else if (sub[3] == null) {
                sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "id int(10) primary key auto_increment,\n"
                        + "sid varchar(200) ,\n"
                        + "course varchar(100),\n"
                        + "semester varchar(100),\n"
                        + sub[0] + "Th varchar(100),\n"
                        + sub[0] + "Pr varchar(100),\n"
                        + sub[1] + "Th varchar(100),\n"
                        + sub[1] + "Pr varchar(100),\n"
                        + sub[2] + "Th varchar(100),\n"
                        + sub[2] + "Pr varchar(100),\n"
                        + "remarks varchar(200)"
                        + " )";
            } else if (sub[4] == null) {
                sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "id int(10) primary key auto_increment,\n"
                        + "sid varchar(200) ,\n"
                        + "course varchar(100),\n"
                        + "semester varchar(100),\n"
                        + sub[0] + "Th varchar(100),\n"
                        + sub[0] + "Pr varchar(100),\n"
                        + sub[1] + "Th varchar(100),\n"
                        + sub[1] + "Pr varchar(100),\n"
                        + sub[2] + "Th varchar(100),\n"
                        + sub[2] + "Pr varchar(100),\n"
                        + sub[3] + "Th varchar(100),\n"
                        + sub[3] + "Pr varchar(100),\n"
                        + "remarks varchar(200)"
                        + " )";
            } else if (sub[5] == null) {

                sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "id int(10) primary key auto_increment,\n"
                        + "sid varchar(200) ,\n"
                        + "course varchar(100),\n"
                        + "semester varchar(100),\n"
                        + sub[0] + "Th varchar(100),\n"
                        + sub[0] + "Pr varchar(100),\n"
                        + sub[1] + "Th varchar(100),\n"
                        + sub[1] + "Pr varchar(100),\n"
                        + sub[2] + "Th varchar(100),\n"
                        + sub[2] + "Pr varchar(100),\n"
                        + sub[3] + "Th varchar(100),\n"
                        + sub[3] + "Pr varchar(100),\n"
                        + sub[4] + "Th varchar(100),\n"
                        + sub[4] + "Pr varchar(100),\n"
                        + "remarks varchar(200)"
                        + " )";
            } else {
                sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                        + "id int(10) primary key auto_increment,\n"
                        + "sid varchar(200) ,\n"
                        + "course varchar(100),\n"
                        + "semester varchar(100),\n"
                        + sub[0] + "Th varchar(100),\n"
                        + sub[0] + "Pr varchar(100),\n"
                        + sub[1] + "Th varchar(100),\n"
                        + sub[1] + "Pr varchar(100),\n"
                        + sub[2] + "Th varchar(100),\n"
                        + sub[2] + "Pr varchar(100),\n"
                        + sub[3] + "Th varchar(100),\n"
                        + sub[3] + "Pr varchar(100),\n"
                        + sub[4] + "Th varchar(100),\n"
                        + sub[4] + "Pr varchar(100),\n"
                        + sub[5] + "Th varchar(100),\n"
                        + sub[5] + "Pr varchar(100),\n"
                        + "remarks varchar(200)"
                        + " )";

            }
            stmt.execute(sql);

            System.out.println("Successfully Created Student Mark Database");
        } catch (SQLException ex) {
            System.out.println("No Subject Choosen");
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
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

    public boolean execAction(String qu) {
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
