/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.system;

import account.management.RunAccount;
import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import common.function.Common;
import data.entry.RunMain;
import database.handler.CreationDatabase;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import library.management.RunLibrary;
import main.admin.controller.RunMainAdmin;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class LoginController implements Initializable {

    @FXML
    private Label lblForget, lblSignup;
    @FXML
    private MaterialDesignIconView lblClose;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField txtPassword;
    public static String emailID = null, passwordID = null, adminCategory = null;
    Stage stage1;
    public static MaterialDesignIconView iClose;
    @FXML
    private JFXSpinner lblSpinner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iClose = lblClose;
        lblSpinner.setVisible(false);
    }

    @FXML
    private void mouseClick(MouseEvent event) throws IOException {
        if (event.getSource() == lblClose) {
            stage1 = (Stage) lblClose.getScene().getWindow();
            stage1.close();
        } else if (event.getSource() == lblSignup) {
            stage1 = (Stage) lblSignup.getScene().getWindow();
            stage1.close();
            new Common().loadWindow("/login/system/Register.fxml");
        } else if (event.getSource() == lblForget) {
            stage1 = (Stage) lblSignup.getScene().getWindow();
            stage1.close();
            new Common().loadWindow("/login/system/ForgetPassword.fxml");
        }
    }

    @FXML
    private void loginAdmin(ActionEvent event) {

        lblSpinner.setVisible(true);
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.seconds(5));
        pauseTransition.setOnFinished(ev -> {
            try {
                completeLogin();
            } catch (Exception ex) {
                AlertMaker.showErrorMessage(ex);
            }
        });
        pauseTransition.play();

    }

    private void completeLogin() throws Exception {

        Stage stage = new Stage();
        String sql = "select approveadmin.*,admindetails.adminType from approveadmin inner join admindetails on approveadmin.email = admindetails.email where approveadmin.email = '" + txtEmail.getText().toLowerCase() + "' and approveadmin.password = '" + DigestUtils.sha512Hex(txtPassword.getText()) + "'";

        ResultSet rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                switch (rs.getString("adminType")) {
                    case "MAIN":
                        emailID = rs.getString("email");
                        passwordID = txtPassword.getText();
                        new RunMainAdmin().start(stage);
                        break;
                    case "Data Entry":
                        emailID = rs.getString("email");
                        passwordID = txtPassword.getText();
                        adminCategory = rs.getString("adminType");
                        new RunMain().start(stage);
                        break;
                    case "Library":
                        emailID = rs.getString("email");
                        passwordID = txtPassword.getText();
                        adminCategory = rs.getString("adminType");
                        new RunLibrary().start(stage);
                        break;
                    case "Account":
                        emailID = rs.getString("email");
                        passwordID = txtPassword.getText();
                        adminCategory = rs.getString("adminType");
                        new RunAccount().start(stage);
                        break;
                    default:
                        break;
                }
                stage1 = (Stage) lblClose.getScene().getWindow();
                stage1.close();
            } else {
                AlertMaker.showErrorMessage("Error", "Email And Password Doesn't Match!!!");
            }
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

    }
}
