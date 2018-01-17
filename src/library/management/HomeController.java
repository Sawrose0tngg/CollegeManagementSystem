package library.management;

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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import login.system.LoginController;
import main.admin.controller.MainAdminController;

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
    private Pane circleImage;
    @FXML
    private Label lblLibMember, lblLibBook, lblBookIssue, lblBookAvailable;
    public static Label lblM, lblB, lblI, lblA;
    @FXML
    private Group groupMember, groupBook, groupIssue, groupAvailable;
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
        lblM = lblLibMember;
        lblB = lblLibBook;
        lblI = lblBookIssue;
        lblA = lblBookAvailable;
        getProfileFill();
        getNotifyFill();
        setRipples();
        getLibraryDetails();
    }

    @FXML
    private void addDepartment(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "More Content On Library Department", "Library Department", JOptionPane.INFORMATION_MESSAGE);

    }

    @FXML
    private void editProfile(ActionEvent event) {
        new DashboardController().setNode(DashboardController.edit);
    }

    private void setRipples() {
        JFXRippler rippler1 = new JFXRippler(groupMember);
        JFXRippler rippler2 = new JFXRippler(groupBook);
        JFXRippler rippler3 = new JFXRippler(groupIssue);
        JFXRippler rippler4 = new JFXRippler(groupAvailable);
        rippler1.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler2.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler3.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler4.setMaskType(JFXRippler.RipplerMask.RECT);

        rippler1.setRipplerFill(Paint.valueOf("#1564C0"));
        rippler2.setRipplerFill(Paint.valueOf("#00AACF"));
        rippler3.setRipplerFill(Paint.valueOf("#00B3A0"));
        rippler4.setRipplerFill(Paint.valueOf("#F87951"));

        groupHolder.getChildren().addAll(rippler1, rippler2, rippler3, rippler4);
    }

    public void getLibraryDetails() {

        int countB = 0;
        int countT = new MainAdminController().setRequestData("select memberID from librarymember");
        int countI = new MainAdminController().setRequestData("select * from issue");

        ResultSet rs = CreationDatabase.execQuery("select * from bookdb");
        try {
            while (rs.next()) {
                countB += Integer.parseInt(rs.getString("bookNumber"));
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lblM.setText(String.valueOf(countT));
        lblB.setText(String.valueOf(countB + countI));
        lblI.setText(String.valueOf(countI));
        lblA.setText(String.valueOf(countB));

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
