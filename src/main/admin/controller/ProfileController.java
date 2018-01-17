/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.admin.controller;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import database.handler.CreationDatabase;
import database.handler.DatabaseHandler;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import login.system.LoginController;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class ProfileController implements Initializable {

    @FXML
    private JFXTextField txtFirst, txtLast, txtEmail, txtContact, txtAnswer;
    @FXML
    private JFXComboBox<String> comboAdmin, comboQuestion;
    @FXML
    private JFXRadioButton rdMale, rdFemale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private JFXPasswordField txtPassword, txtNewPassword, txtRePassword;
    @FXML
    private JFXToggleButton toggleMain;
    WritableImage image;
    int flag;
    @FXML
    private ImageView imageProfile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtNewPassword.setDisable(true);
        txtRePassword.setDisable(true);
        comboAdmin.getItems().addAll("Data Entry", "Account", "Library");
        comboQuestion.getItems().addAll("What is your grandfather name?", "What is the name of your first school's?", "Which is your favorite game?", "What is the name of your pet?");
        loadData();
    }

    @FXML
    private void uploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png", "*.gif"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                Rectangle clip = new Rectangle(imageProfile.getFitWidth(), imageProfile.getFitHeight());
                imageProfile.setImage(image);
                imageProfile.setClip(clip);
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                WritableImage img = imageProfile.snapshot(parameters, null);
                imageProfile.setClip(null);
                imageProfile.setImage(img);
                flag = 1;
            } catch (IOException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) throws SQLException {
        Blob blob = null;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        CreationDatabase createdb = new CreationDatabase();
        Connection conn = CreationDatabase.setupDatabse();
        PreparedStatement pst = null;

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imageProfile.getImage(), null), "png", byteOutput);
            blob = conn.createBlob();
            blob.setBytes(1, byteOutput.toByteArray());
        } catch (SQLException | IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        if (flag == 1) {
            String query = "update admindetails set photo=? where email = ?";
            try {
                pst = conn.prepareStatement(query);
                pst.setBlob(1, blob);
                pst.setString(2, LoginController.emailID);
                pst.execute();
                pst.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        }
        String adm = "MAIN";
        int count = new MainAdminController().setRequestData("select adminType from admindetails where adminType = '" + adm + "'");

        String adminStr = null;
        if (toggleMain.isSelected()) {
            adminStr = "MAIN";
        } else {
            adminStr = comboAdmin.getSelectionModel().getSelectedItem();
        }

        if (txtNewPassword.isDisable() == false && txtRePassword.isDisable() == false && LoginController.passwordID.equals(txtPassword.getText())) {
            if (txtNewPassword.getText().equals(txtRePassword.getText())) {
                if (adminStr != null) {
                    if (count > 1 || (count == 1) && adminStr.equals("MAIN")) {
                        String sql = "update admindetails set firstName='" + txtFirst.getText() + "',lastName='" + txtLast.getText() + "',email='" + txtEmail.getText() + "',contact='" + txtContact.getText() + "',password='" + DigestUtils.sha512Hex(txtNewPassword.getText()) + "',adminType='" + adminStr + "',gender='" + getGender() + "',question='" + comboQuestion.getSelectionModel().getSelectedItem() + "',answer='" + txtAnswer.getText() + "' where email = '" + LoginController.emailID + "'";
                        String query = "update approveadmin set email = '" + txtEmail.getText().toLowerCase() + "', password = '" + DigestUtils.sha512Hex(txtNewPassword.getText()) + "' where email = '" + LoginController.emailID + "'";
                        if (CreationDatabase.execAction(query) && CreationDatabase.execAction(sql)) {
                            AlertMaker.showSimpleAlert("Success", "Successfully Save Changes");
                        }
                    } else {
                        AlertMaker.showErrorMessage("Error", "Cannot Change Main-Admin To Admin");
                    }
                } else {
                    AlertMaker.showErrorMessage("Error", "Please Selct Admin Type");
                }
            } else {
                AlertMaker.showErrorMessage("Error", "Password Doesn't Match???");
            }
        } else if (txtNewPassword.isDisable() == true && txtRePassword.isDisable() == true && LoginController.passwordID.equals(txtPassword.getText())) {
            if (adminStr != null) {
                if (count > 1 || (count == 1) && adminStr.equals("MAIN")) {
                    String sql = "update admindetails set firstName='" + txtFirst.getText() + "',lastName='" + txtLast.getText() + "',email='" + txtEmail.getText() + "',contact='" + txtContact.getText() + "',adminType='" + adminStr + "',gender='" + getGender() + "',question='" + comboQuestion.getSelectionModel().getSelectedItem() + "',answer='" + txtAnswer.getText() + "' where email = '" + LoginController.emailID + "'";
                    String query = "update approveadmin set email = '" + txtEmail.getText().toLowerCase() + "' where email = '" + LoginController.emailID + "'";
                    if (CreationDatabase.execAction(query) && CreationDatabase.execAction(sql)) {
                        AlertMaker.showSimpleAlert("Success", "Successfully Save Changes");
                    }
                } else {
                    AlertMaker.showErrorMessage("Error", "Cannot Change Main-Admin To Admin");
                }
            } else {
                AlertMaker.showErrorMessage("Error", "Please Selct Admin Type");
            }
        } else {
            AlertMaker.showErrorMessage("Error", "You have enter worng password???");
        }
        try {
            byteOutput.close();
            conn.close();
        } catch (IOException ex) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getGender() {
        String ge = "";
        if (rdMale.isSelected()) {
            ge = "MALE";
        } else if (rdFemale.isSelected()) {
            ge = "FEMALE";
        }
        return ge;
    }

    private void loadData() {
        ByteArrayInputStream bis = null;
        String sql = "select * from admindetails where email='" + LoginController.emailID + "'";
        ResultSet rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                txtFirst.setText(rs.getString("firstName"));
                txtLast.setText(rs.getString("lastName"));
                txtEmail.setText(rs.getString("email"));
                txtContact.setText(rs.getString("contact"));
                if (rs.getString("adminType").equals("MAIN")) {
                    toggleMain.setSelected(true);
                    comboAdmin.setDisable(true);
                } else {
                    comboAdmin.setDisable(false);
                    comboAdmin.setValue(rs.getString("adminType"));
                }
                if (rs.getString("gender") == null) {
                    rdMale.setSelected(false);
                    rdFemale.setSelected(false);
                } else {
                    if (rs.getString("gender").equals("MALE")) {
                        rdMale.setSelected(true);
                    } else if (rs.getString("gender").equals("FEMALE")) {
                        rdFemale.setSelected(true);
                    }
                }
                comboQuestion.setValue(rs.getString("question"));
                txtAnswer.setText(rs.getString("answer"));
                if (rs.getBytes("photo") != null) {
                    byte[] byteData = rs.getBytes("photo");
                    bis = new ByteArrayInputStream(byteData);
                    BufferedImage read = ImageIO.read(bis);
                    Image img = SwingFXUtils.toFXImage(read, null);
                    imageProfile.setImage(img);
                    bis.close();
                }
            }
        } catch (SQLException | IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        try {
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    @FXML
    private void toggleAction(ActionEvent event) {
        if (toggleMain.isSelected()) {
            comboAdmin.setDisable(true);
            comboAdmin.setValue(null);
        } else {
            comboAdmin.setDisable(false);
        }
    }

    @FXML
    private void changePasswordAction(ActionEvent event) {
        txtNewPassword.setDisable(false);
        txtRePassword.setDisable(false);
    }

}
