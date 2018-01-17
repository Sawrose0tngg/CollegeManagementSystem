/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.admin.controller;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextArea;
import database.handler.CreationDatabase;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class HomeController implements Initializable {

    @FXML
    private HBox groupHolder;
    @FXML
    private Group groupMain, groupApprove, groupRequest;
    @FXML
    private Label lblMain, lblApprove, lblRequest;
    @FXML
    private JFXComboBox<String> comboAdmin;
    @FXML
    private JFXTextArea txtMessage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setRipples();
        loadData();
        comboAdmin.getItems().addAll("Data Entry", "Account", "Library");
    }

    private void setRipples() {
        JFXRippler rippler1 = new JFXRippler(groupMain);
        JFXRippler rippler2 = new JFXRippler(groupApprove);
        JFXRippler rippler3 = new JFXRippler(groupRequest);
        rippler1.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler2.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler3.setMaskType(JFXRippler.RipplerMask.RECT);

        rippler1.setRipplerFill(Paint.valueOf("#1564C0"));
        rippler2.setRipplerFill(Paint.valueOf("#00AACF"));
        rippler3.setRipplerFill(Paint.valueOf("#00B3A0"));

        groupHolder.getChildren().addAll(rippler1, rippler2, rippler3);
    }

    public void loadData() {
        lblMain.setText(String.valueOf(new MainAdminController().setRequestData("select admindetails.email from admindetails inner join approveadmin on admindetails.email = approveadmin.email where admindetails.adminType = 'MAIN'")));
        lblApprove.setText(String.valueOf(new MainAdminController().setRequestData("select email from admindetails where email in (select email from approveadmin)")));
        lblRequest.setText(String.valueOf(new MainAdminController().setRequestData("select email from admindetails where email not in (select email from approveadmin)")));
    }

    @FXML
    private void sendNotification(ActionEvent event) throws SQLException {
        CreationDatabase createdb = new CreationDatabase();
        Connection conn = CreationDatabase.setupDatabse();
        ResultSet rs = null;
        PreparedStatement pst = null;
        if (txtMessage.getText() != null && comboAdmin.getSelectionModel().getSelectedItem() != null) {
            String sql = "select message from notify where adminType = '" + comboAdmin.getSelectionModel().getSelectedItem() + "'";
            rs = CreationDatabase.execQuery(sql);
            if (rs.next()) {
                String query = "update notify set message = '" + txtMessage.getText() + "' where adminType = '" + comboAdmin.getSelectionModel().getSelectedItem() + "'";
                if (CreationDatabase.execAction(query)) {
                    AlertMaker.showSimpleAlert("Success", "Successfully Sent Message");
                } else {
                    AlertMaker.showErrorMessage("Error", "Unable To Send Message");
                }
            } else {
                String query = "insert into notify(adminType,message) values(?,?)";
                pst = conn.prepareStatement(query);
                pst.setString(1, comboAdmin.getSelectionModel().getSelectedItem());
                pst.setString(2, txtMessage.getText());
                pst.execute();
                AlertMaker.showSimpleAlert("Success", "Successfully Sent Message");
                pst.close();
            }
            rs.close();
        } else {
            AlertMaker.showErrorMessage("Error", "Please Fill All Fields");
        }
        conn.close();
    }

}
