/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.system;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import common.function.Common;
import database.handler.CreationDatabase;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class RegisterController implements Initializable {

    CreationDatabase createdb = new CreationDatabase();
    Connection conn;
    @FXML
    private JFXRadioButton radioMale, radioFemale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private MaterialDesignIconView lblClose;
    @FXML
    private JFXToggleButton btnToggle;
    @FXML
    private JFXTextField txtFirst, txtLast, txtEmail, txtContact, txtAnswer;
    @FXML
    private JFXPasswordField txtPassword, txtRePassword;
    @FXML
    private JFXComboBox<String> comboAdmin, comboQuestion;
    @FXML
    private Label lblLogin;
    WritableImage image;
    int flag;
    @FXML
    private ImageView imgProfile;

    public RegisterController() {
        this.conn = CreationDatabase.setupDatabse();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboAdmin.getItems().addAll("Data Entry", "Account", "Library");
        comboQuestion.getItems().addAll("What is your grandfather name?", "What is the name of your first school?", "Which is your favorite game?", "What is the name of your pet?");
    }

    @FXML
    private void mouseClick(MouseEvent event) {
        Stage stage;
        if (event.getSource() == lblClose) {
            stage = (Stage) lblClose.getScene().getWindow();
            stage.close();
        } else if (event.getSource() == lblLogin) {
            stage = (Stage) lblLogin.getScene().getWindow();
            stage.close();
            new Common().loadWindow("/login/system/Login.fxml");
        }
    }

    @FXML
    private void saveAdmin(ActionEvent event) {
        Blob blob = null;

        if (txtFirst.getText() != null && txtLast.getText() != null && txtEmail.getText() != null && txtContact.getText() != null && txtAnswer.getText() != null
                && txtPassword.getText() != null && txtRePassword.getText() != null && comboQuestion.getValue() != null && flag == 1) {

            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imgProfile.getImage(), null), "png", byteOutput);
                blob = conn.createBlob();
                blob.setBytes(1, byteOutput.toByteArray());
            } catch (SQLException | IOException ex) {
                AlertMaker.showErrorMessage(ex, "Error", "Please Upload Your Photo??");
            }

            if (txtPassword.getText().equals(txtRePassword.getText())) {
                String pass = null;
                if (txtPassword.getText().length() < 16) {
                    pass = DigestUtils.sha512Hex(txtPassword.getText());
                } else {
                    pass = txtPassword.getText();
                }
                String sql = "insert into admindetails(firstName,lastName,email,contact,password,adminType,gender,question,answer,photo) values(?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst = null;
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, txtFirst.getText());
                    pst.setString(2, txtLast.getText());
                    pst.setString(3, txtEmail.getText().toLowerCase());
                    pst.setString(4, txtContact.getText());
                    pst.setString(5, pass);
                    if (btnToggle.isSelected()) {
                        pst.setString(6, "MAIN");
                    } else if (comboAdmin.getValue() != null) {
                        pst.setString(6, comboAdmin.getSelectionModel().getSelectedItem());
                    } else {
                        AlertMaker.showErrorMessage("Error", "Please Choose Any One Admin???");
                    }
                    pst.setString(7, getGender());
                    pst.setString(8, comboQuestion.getSelectionModel().getSelectedItem());
                    pst.setString(9, txtAnswer.getText().toLowerCase());
                    pst.setBlob(10, blob);

                    pst.execute();
                    clearInputs();
                    AlertMaker.showSimpleAlert("Success", "Successfully Add New Admin");
                } catch (SQLException ex) {
                    AlertMaker.showErrorMessage(ex, "Error", "Login Failed??");
                } finally {
                    try {
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                AlertMaker.showErrorMessage("Error", "Password Doesn't Match???");
            }
            try {
                byteOutput.close();
                conn.close();
            } catch (IOException | SQLException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            AlertMaker.showErrorMessage("Error", "Please Fill All Fields???");
        }
    }

    @FXML
    private void uploadPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.png", "*.gif"));
        //show open dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                image = SwingFXUtils.toFXImage(bufferedImage, null);
                Rectangle clip = new Rectangle(imgProfile.getFitWidth(), imgProfile.getFitHeight());
                imgProfile.setImage(image);
                imgProfile.setClip(clip);
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                WritableImage img = imgProfile.snapshot(parameters, null);
                imgProfile.setClip(null);
                imgProfile.setImage(img);
                flag = 1;
            } catch (IOException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        }
    }

    private String getGender() {
        String ge = "";
        if (radioMale.isSelected()) {
            ge = "MALE";
        } else if (radioFemale.isSelected()) {
            ge = "FEMALE";
        }
        return ge;
    }

    private void clearInputs() {
        txtFirst.setText(null);
        txtLast.setText(null);
        txtEmail.setText(null);
        txtContact.setText(null);
        txtPassword.setText(null);
        txtRePassword.setText(null);
        comboAdmin.setValue(null);
        radioMale.setSelected(false);
        radioFemale.setSelected(false);
        comboQuestion.setValue(null);
        txtAnswer.setText(null);
        imgProfile.setImage(null);
        btnToggle.setSelected(false);
    }

    @FXML
    private void adminToggleAction(ActionEvent event) {
        if (btnToggle.isSelected()) {
            comboAdmin.setDisable(true);
            comboAdmin.setValue(null);
        } else {
            comboAdmin.setDisable(false);
        }
    }

}
