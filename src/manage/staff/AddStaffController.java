package manage.staff;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import database.handler.CreationDatabase;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class AddStaffController implements Initializable {

    @FXML
    private AnchorPane parentPane;
    @FXML
    private JFXTextField txtFname, txtLname, txtEmail, txtMobile, txtLocation, txtStafID;
    @FXML
    private JFXRadioButton rdMale, rdFemale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private ProgressBar progressPersonal;
    @FXML
    private Label lblComplete;
    @FXML
    private JFXButton btnClear, btnSave, btnUpload;
    @FXML
    private DatePicker joinDate, birthDate;
    @FXML
    private JFXComboBox<String> comboStaff, comboTime, comboShift;
    @FXML
    private ImageView imageProfile;

    WritableImage image;
    int flag;
    public static double progress1 = 0;
    public static double progress2 = 0;
    public static double progress3 = 0;
    public static double progress4 = 0;
    public static double progress5 = 0;
    public static double progress6 = 0;
    public static double progress7 = 0;
    public static double progress8 = 0;
    public static double progress9 = 0;
    public static double progress10 = 0;
    public static double progress11 = 0;
    public static double progress12 = 0;

    private ObservableList<String> typeList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        flag = 0;
        comboShift.getItems().addAll("Morning", "Day", "Evening");
        comboTime.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        setStaffTypeToCombo();
        updateProgress(0);
    }

    @FXML
    private void clearFields(ActionEvent event) {
        updateProgress(0);
        clearInput();
    }

    private void fillTecherId(double sum) {
        if (sum >= 1) {
            String idName;
            String query = "select max(id) from staffdetails";
            ResultSet rs = CreationDatabase.execQuery(query);
            try {
                if (rs.next()) {
                    idName = "STF" + String.valueOf(rs.getInt(1) + 1);
                    txtStafID.setText(idName);
                }
                rs.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        } else {
            txtStafID.setText(null);
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
                Rectangle clip = new Rectangle(imageProfile.getFitWidth(), imageProfile.getFitHeight());
                imageProfile.setImage(image);
                imageProfile.setClip(clip);
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                WritableImage img = imageProfile.snapshot(parameters, null);
                imageProfile.setClip(null);
                imageProfile.setImage(img);
                flag = 1;
                updateProgress(1);
            } catch (IOException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        }
    }

    @FXML
    private void saveStaff(ActionEvent event) {
        Connection conn = CreationDatabase.setupDatabse();
        PreparedStatement pst = null;
        Date bDate = Date.valueOf(birthDate.getValue());
        Date jDate = Date.valueOf(joinDate.getValue());
        Blob blob = null;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        String sql = "insert into staffdetails(firstName, lastName, birth, joinDate, staffType, workShift, workTime, gender, mobile, email, location, photo, staffId) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imageProfile.getImage(), null), "png", byteOutput);
            blob = conn.createBlob();
            blob.setBytes(1, byteOutput.toByteArray());
            byteOutput.close();
        } catch (SQLException | IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtFname.getText().trim());
            pst.setString(2, txtLname.getText().trim());
            pst.setString(3, bDate.toString());
            pst.setString(4, jDate.toString());
            pst.setString(5, comboStaff.getSelectionModel().getSelectedItem());
            pst.setString(6, comboShift.getSelectionModel().getSelectedItem());
            pst.setString(7, comboTime.getSelectionModel().getSelectedItem());
            pst.setString(8, getGender());
            pst.setString(9, txtMobile.getText().trim());
            pst.setString(10, txtEmail.getText().toLowerCase().trim());
            pst.setString(11, txtLocation.getText());
            pst.setBlob(12, blob);
            pst.setString(13, txtStafID.getText());
            pst.executeUpdate();

            pst.close();
            conn.close();
            clearInput();
            updateProgress(0);
            AlertMaker.showSimpleAlert("Success", "Saved Successfully");
            new HomeController().getStaffNumber();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
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

    private void updateProgress(int temp) {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        //progressPersonal.setProgress(0.00);       
        double sum_progress = progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12;

        txtFname.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress1 = 0.05;
                } else {
                    progress1 = 0.0;
                }
            } catch (Exception e) {
                progress1 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");

            fillTecherId(sum);
        });

        txtEmail.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress2 = 0.1;
                } else {
                    progress2 = 0.0;
                }
            } catch (Exception e) {
                progress2 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        txtLname.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress3 = 0.05;
                } else {
                    progress3 = 0.0;
                }
            } catch (Exception e) {
                progress3 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        txtMobile.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (newValue.length() > 1) {
                    progress4 = 0.1;
                } else {
                    progress4 = 0.0;
                }
            } catch (Exception e) {
                progress4 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        txtLocation.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress5 = 0.1;
                } else {
                    progress5 = 0.0;
                }
            } catch (Exception e) {
                progress5 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        rdMale.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                if (!oldValue) {
                    progress7 = 0.05;
                } else {
                    progress7 = 0.0;
                }
            } catch (Exception e) {
                progress7 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        rdFemale.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                if (!oldValue) {
                    progress7 = 0.05;
                } else {
                    progress7 = 0.0;
                }
            } catch (Exception e) {
                progress7 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        birthDate.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                if (newValue != null) {
                    progress8 = 0.1;
                } else {
                    progress8 = 0.0;
                }
            } catch (Exception e) {
                progress8 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        joinDate.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                if (newValue != null) {
                    progress9 = 0.1;
                } else {
                    progress9 = 0.0;
                }
            } catch (Exception e) {
                progress9 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        comboStaff.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress10 = 0.1;
                } else {
                    progress10 = 0.0;
                }
            } catch (Exception e) {
                progress10 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        comboShift.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (newValue != null) {
                    progress11 = 0.1;
                } else {
                    progress11 = 0.0;
                }
            } catch (Exception e) {
                progress11 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        comboTime.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (newValue != null) {
                    progress6 = 0.05;
                } else {
                    progress6 = 0.0;
                }
            } catch (Exception e) {
                progress6 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        });

        if (temp == 1) {
            progress12 = 0.1;
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        } else {
            progress12 = 0.0;
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillTecherId(sum);
        }

    }

    private void setStaffTypeToCombo() {

        String query = "SELECT typeName FROM staffcategory";
        typeList = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery(query);
        try {
            while (rs.next()) {
                String name = rs.getString("typeName");
                typeList.add(name);
            }
            rs.close();
            comboStaff.getItems().addAll(typeList);
        } catch (SQLException ex) {
            Logger.getLogger(AddStaffController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clearInput() {

        txtFname.setText(null);
        txtLname.setText(null);
        birthDate.setValue(null);
        joinDate.setValue(null);
        comboStaff.setValue(null);
        comboShift.setValue(null);
        comboTime.setValue(null);
        rdMale.setSelected(false);
        rdFemale.setSelected(false);
        txtMobile.setText(null);
        txtEmail.setText(null);
        txtLocation.setText(null);
        imageProfile.setImage(null);
        txtStafID.setText(null);

    }

}
