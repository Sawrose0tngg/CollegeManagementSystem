package account.management;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextArea;
import database.handler.CreationDatabase;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import login.system.LoginController;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class HomeController implements Initializable {

    @FXML
    private HBox groupHolder;
    @FXML
    private StackPane deptStackPane;
    @FXML
    private JFXListView<?> departmentList;
    @FXML
    private Button btnAddDepartment;
    @FXML
    private Group groupStudent, groupTeacher, groupStaff;
    @FXML
    private Label lblStudentAmt, lblTeacherAmt, lblStaffAmt;
    @FXML
    private Label lblName, lblFullName, lblEmail, lblMobile, lblAdmin;
    @FXML
    private ImageView imageProfile;
    @FXML
    private JFXTextArea txtNotify;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getProfileFill();
        getNotifyFill();
        setRipples();
        getAmountTotal();
    }

    @FXML
    private void addDepartment(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "More Content On Staff Department", "Staff Department", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setRipples() {
        JFXRippler rippler1 = new JFXRippler(groupStudent);
        JFXRippler rippler2 = new JFXRippler(groupTeacher);
        JFXRippler rippler3 = new JFXRippler(groupStaff);
        rippler1.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler2.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler3.setMaskType(JFXRippler.RipplerMask.RECT);

        rippler1.setRipplerFill(Paint.valueOf("#1564C0"));
        rippler2.setRipplerFill(Paint.valueOf("#00AACF"));
        rippler3.setRipplerFill(Paint.valueOf("#00B3A0"));

        groupHolder.getChildren().addAll(rippler1, rippler2, rippler3);
    }

    private void getAmountTotal() {

        String sql1 = "select * from studentfee";
        String sql2 = "select * from teachersalary";
        String sql3 = "select * from staffsalary";
        ResultSet rs1 = null, rs2 = null, rs3 = null;
        rs1 = CreationDatabase.execQuery(sql1);
        rs2 = CreationDatabase.execQuery(sql2);
        rs3 = CreationDatabase.execQuery(sql3);

        double sum1 = 0.0, sum2 = 0.0, sum3 = 0.0;
        try {
            while (rs1.next()) {
                sum1 += Double.parseDouble(rs1.getString("totalPay"));
            }
            rs1.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs2.next()) {
                sum2 += Double.parseDouble(rs2.getString("totalPay"));
            }
            rs2.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs3.next()) {
                sum3 += Double.parseDouble(rs3.getString("totalPay"));
            }
            rs3.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lblStudentAmt.setText(String.valueOf(sum1));
        lblTeacherAmt.setText(String.valueOf(sum2));
        lblStaffAmt.setText(String.valueOf(sum3));

    }

    @FXML
    private void editProfile(ActionEvent event) {
        new DashboardController().setNode(DashboardController.edit);
    }

    private void getProfileFill() {
        ByteArrayInputStream bis = null;
        String sql = "select * from admindetails where email = '" + LoginController.emailID + "'";
        ResultSet rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                lblName.setText(rs.getString("firstName") + " " + rs.getString("lastName"));
                lblEmail.setText(rs.getString("email"));
                lblFullName.setText(rs.getString("firstName") + " " + rs.getString("lastName"));
                lblMobile.setText(rs.getString("contact"));
                lblAdmin.setText(rs.getString("adminType") + " Admin");
                byte[] byteData = rs.getBytes("photo");
                bis = new ByteArrayInputStream(byteData);
                BufferedImage read = ImageIO.read(bis);
                Image image = SwingFXUtils.toFXImage(read, null);
                imageProfile.setImage(image);
                bis.close();
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    private void getNotifyFill() {

        String sql = "select message from notify where adminType = '" + LoginController.adminCategory + "'";
        ResultSet rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                txtNotify.setText(rs.getString("message"));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
}
