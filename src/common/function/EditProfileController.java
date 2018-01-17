/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.function;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
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
public class EditProfileController implements Initializable {

    DatabaseHandler handler = DatabaseHandler.getInstance();
    CreationDatabase createdb = new CreationDatabase();
    Connection conn = createdb.setupDatabse();
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
    @FXML
    private AnchorPane parentPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtNewPassword.setDisable(true);
        txtRePassword.setDisable(true);
        comboAdmin.getItems().addAll("Data Entry", "Account", "Library");
        comboQuestion.getItems().addAll("What is your grandfather name?", "What is the name of your first school?", "Which is your favorite game?", "What is the name of your pet?");
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
                Logger.getLogger(AdminProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) throws SQLException {
        Blob blob = null;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imageProfile.getImage(), null), "png", byteOutput);
            blob = conn.createBlob();
            blob.setBytes(1, byteOutput.toByteArray());
        } catch (SQLException | IOException ex) {
            Logger.getLogger(AdminProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (flag == 1) {
            String query = "update admindetails set photo=? where email = ?";
            try {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setBlob(1, blob);
                pst.setString(2, LoginController.emailID);
                pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(AdminProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String adminStr = null;
        if (toggleMain.isSelected()) {
            adminStr = "MAIN";
        } else {
            adminStr = comboAdmin.getSelectionModel().getSelectedItem();
        }

        if (txtNewPassword.isDisable() == false && txtRePassword.isDisable() == false && LoginController.passwordID.equals(txtPassword.getText())) {
            if (txtNewPassword.getText().equals(txtRePassword.getText())) {
                if (adminStr != null) {
                    if (adminStr.equals("MAIN") || !adminStr.equals(LoginController.adminCategory)) {
                        String sql = "update admindetails set firstName='" + txtFirst.getText() + "',lastName='" + txtLast.getText() + "',email='" + txtEmail.getText() + "',contact='" + txtContact.getText() + "',password='" + DigestUtils.sha512Hex(txtNewPassword.getText()) + "',adminType='" + adminStr + "',gender='" + getGender() + "',question='" + comboQuestion.getSelectionModel().getSelectedItem() + "',answer='" + txtAnswer.getText() + "' where email = '" + LoginController.emailID + "'";
                        String query = "delete from approveadmin where email = '" + LoginController.emailID + "'";
                        if (handler.execAction(query) && handler.execAction(sql)) {
                            AlertMaker.showSimpleAlert("Success", "Successfully Save Changes");
                        } else {
                            AlertMaker.showErrorMessage("Error", "Unable To Save Changes");
                        }
                    } else {
                        String sql = "update admindetails set firstName='" + txtFirst.getText() + "',lastName='" + txtLast.getText() + "',email='" + txtEmail.getText() + "',contact='" + txtContact.getText() + "',password='" + DigestUtils.sha512Hex(txtNewPassword.getText()) + "',gender='" + getGender() + "',question='" + comboQuestion.getSelectionModel().getSelectedItem() + "',answer='" + txtAnswer.getText() + "' where email = '" + LoginController.emailID + "'";
                        String query = "update approveadmin set email = '" + txtEmail.getText().toLowerCase() + "', password = '" + DigestUtils.sha512Hex(txtNewPassword.getText()) + "' where email = '" + LoginController.emailID + "'";
                        if (handler.execAction(query) && handler.execAction(sql)) {
                            AlertMaker.showSimpleAlert("Success", "Successfully Save Changes");
                        } else {
                            AlertMaker.showErrorMessage("Error", "Unable To Save Changes");
                        }
                    }
                } else {
                    AlertMaker.showErrorMessage("Error", "Please Selct Admin Type");
                }
            } else {
                AlertMaker.showErrorMessage("Error", "Password Doesn't Match???");
            }
        } else if (txtNewPassword.isDisable() == true && txtRePassword.isDisable() == true && LoginController.passwordID.equals(txtPassword.getText())) {
            if (adminStr != null) {
                if (adminStr.equals("MAIN") || !adminStr.equals(LoginController.adminCategory)) {
                    String sql = "update admindetails set firstName='" + txtFirst.getText() + "',lastName='" + txtLast.getText() + "',email='" + txtEmail.getText() + "',contact='" + txtContact.getText() + "',adminType='" + adminStr + "',gender='" + getGender() + "',question='" + comboQuestion.getSelectionModel().getSelectedItem() + "',answer='" + txtAnswer.getText() + "' where email = '" + LoginController.emailID + "'";
                    String query = "delete from approveadmin where email = '" + LoginController.emailID + "'";
                    if (handler.execAction(query) && handler.execAction(sql)) {
                        AlertMaker.showSimpleAlert("Success", "Successfully Save Changes");
                    } else {
                        AlertMaker.showErrorMessage("Error", "Unable To Save Changes");
                    }
                } else {
                    String sql = "update admindetails set firstName='" + txtFirst.getText() + "',lastName='" + txtLast.getText() + "',email='" + txtEmail.getText() + "',contact='" + txtContact.getText() + "',gender='" + getGender() + "',question='" + comboQuestion.getSelectionModel().getSelectedItem() + "',answer='" + txtAnswer.getText() + "' where email = '" + LoginController.emailID + "'";
                    String query = "update approveadmin set email = '" + txtEmail.getText().toLowerCase() + "' where email = '" + LoginController.emailID + "'";
                    if (handler.execAction(query) && handler.execAction(sql)) {
                        AlertMaker.showSimpleAlert("Success", "Successfully Save Changes");
                    } else {
                        AlertMaker.showErrorMessage("Error", "Unable To Save Changes");
                    }
                }
            } else {
                AlertMaker.showErrorMessage("Error", "Please Selct Admin Type");
            }
        } else {
            AlertMaker.showErrorMessage("Error", "You have enter worng password???");
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

        String sql = "select * from admindetails where email='" + LoginController.emailID + "'";
        ResultSet rs = handler.execQuery(sql);
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
                    ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
                    BufferedImage read = ImageIO.read(bis);
                    Image img = SwingFXUtils.toFXImage(read, null);
                    imageProfile.setImage(img);
                }
            }
        } catch (SQLException | IOException ex) {
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

    @FXML
    private void clearFields(ActionEvent event) {
        loadData();
    }

}
