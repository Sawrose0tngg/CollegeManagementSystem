/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.admin.controller;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import database.handler.CreationDatabase;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import login.system.Registration;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class MainAdminController implements Initializable {

    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnWidgets;
    @FXML
    private JFXButton btnProfile;

    public static AnchorPane home, info, operate, profile, holdePane;
    @FXML
    private AnchorPane holderPane;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private Label lblRequest;
    public static Label lblR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        holdePane = holderPane;
        lblR = lblRequest;
        createPages();
        loadRequest();
    }

    private void createPages() {
        try {
            home = FXMLLoader.load(getClass().getResource("/main/admin/controller/Home.fxml"));
            info = FXMLLoader.load(getClass().getResource("/main/admin/controller/AdminInfo.fxml"));
            operate = FXMLLoader.load(getClass().getResource("/main/admin/controller/AdminOperate.fxml"));
            profile = FXMLLoader.load(getClass().getResource("/main/admin/controller/Profile.fxml"));

            setNode(home);
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    public void setNode(Node node) {
        holdePane.getChildren().clear();
        holdePane.getChildren().add((Node) node);

        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    @FXML
    private void openHome(ActionEvent event) {
        setNode(home);
    }

    @FXML
    private void openWidgets(ActionEvent event) {
        setNode(operate);
    }

    @FXML
    private void openAdmins(ActionEvent event) {
        setNode(info);
    }

    @FXML
    private void openProfile(ActionEvent event) {
        setNode(profile);
    }

    @FXML
    private void logOff(ActionEvent event) throws Exception {
        Stage stage;
        Stage stage1 = new Stage();
        stage = (Stage) btnProfile.getScene().getWindow();
        stage.close();
        new Registration().start(stage1);
    }

    public void loadRequest() {
        lblR.setText(String.valueOf(setRequestData("select email from admindetails where email not in (select email from approveadmin)")));
    }

    public int setRequestData(String sql) {
        CreationDatabase createdb = new CreationDatabase();
        Connection conn = CreationDatabase.setupDatabse();
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            try {
                while (rs.next()) {
                    count++;
                }
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
            stmt.close();
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

        try {
            conn.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        return count;
    }

}
