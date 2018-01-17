/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.system;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import common.function.Common;
import database.handler.CreationDatabase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class ForgetPasswordController implements Initializable {

    @FXML
    private AnchorPane headerPane;
    @FXML
    private MaterialDesignIconView lblClose;
    @FXML
    private JFXTextField txtEmail, txtAnswer;
    @FXML
    private JFXComboBox<String> comboQuestion;
    @FXML
    private JFXPasswordField txtPassword, txtCPassword;
    @FXML
    private Label lblLogin;
    @FXML
    private FontAwesomeIconView iconBack;
    @FXML
    private AnchorPane bodyPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboQuestion.getItems().addAll("What is your grandfather name?", "What is the name of your first school?", "Which is your favorite game?", "What is the name of your pet?");
        settingVisibility(true);

        txtEmail.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ResultSet rs = CreationDatabase.execQuery("select email,question from admindetails where email = '" + newValue + "'");
            try {
                if (rs.next()) {
                    settingVisibility(false);
                    comboQuestion.setValue(rs.getString("question"));
                } else {
                    comboQuestion.setValue(null);
                    settingVisibility(true);
                }
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        });
    }

    @FXML
    private void mouseClick(MouseEvent event) {
        Stage stage;
        if (event.getSource() == lblClose) {
            stage = (Stage) lblClose.getScene().getWindow();
            stage.close();
        } else if (event.getSource() == lblLogin || event.getSource() == iconBack) {
            stage = (Stage) lblLogin.getScene().getWindow();
            stage.close();
            new Common().loadWindow("/login/system/Login.fxml");
        }
    }

    @FXML
    private void changePassword(ActionEvent event) throws SQLException {

        String sql = "select * from admindetails where email = '" + txtEmail.getText().toLowerCase() + "' and question = '" + comboQuestion.getSelectionModel().getSelectedItem() + "' and answer = '" + txtAnswer.getText().toLowerCase() + "'";
        ResultSet rs = CreationDatabase.execQuery(sql);
        if (rs.next()) {
            if (txtPassword.getText().equals(txtCPassword.getText())) {
                String pass = null;
                if (txtPassword.getText().length() < 16) {
                    pass = DigestUtils.sha512Hex(txtPassword.getText());
                } else {
                    pass = txtPassword.getText();
                }
                if (CreationDatabase.execAction("update admindetails set password = '" + pass + "' where email = '" + txtEmail.getText().toLowerCase() + "' and question = '" + comboQuestion.getSelectionModel().getSelectedItem() + "' and answer = '" + txtAnswer.getText().toLowerCase() + "'")) {
                    headerPane.setStyle("-fx-background-color: #00A65A");
                    bodyPane.setStyle("-fx-background-color: #b0e4cc");
                    AlertMaker.showSimpleAlert("Success", "Successfully Changed Password");
                } else {
                    headerPane.setStyle("-fx-background-color:  #F39C12");
                    bodyPane.setStyle("-fx-background-color: #ffe4bc");
                    AlertMaker.showErrorMessage("Error", "Unable to Make Changes???");
                }
            } else {
                headerPane.setStyle("-fx-background-color: #F39C12");
                bodyPane.setStyle("-fx-background-color: #ffe4bc");
                AlertMaker.showErrorMessage("Error", "Password Doesn't Match???");
            }
        } else {
            headerPane.setStyle("-fx-background-color: #DD4B39");
            bodyPane.setStyle("-fx-background-color: #e4c4c1");
            AlertMaker.showErrorMessage("Error", "You Have Enter Wrong Information???");
        }
    }

    public void settingVisibility(boolean status) {
        comboQuestion.setDisable(status);
        txtAnswer.setDisable(status);
        txtPassword.setDisable(status);
        txtCPassword.setDisable(status);
    }

}
