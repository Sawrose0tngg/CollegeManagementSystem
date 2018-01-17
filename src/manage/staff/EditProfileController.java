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
public class EditProfileController implements Initializable {

    @FXML
    private AnchorPane parentPane;
    @FXML
    private JFXTextField txtFname, txtLname, txtMobile, txtEmail, txtLocation, txtStaffID;
    @FXML
    private JFXRadioButton rdMale, rdFemale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private JFXButton btnSave, btnUpload;
    @FXML
    private ProgressBar progressPersonal;
    @FXML
    private Label lblComplete;
    @FXML
    private ImageView imgProfile;

    public static JFXTextField fname, lname, mobile, email, location, stfid;
    public static JFXRadioButton male, female;
    public static DatePicker birth, join;
    public static JFXComboBox<String> ctype, cshift, ctime;
    public static ImageView photo;
    private ObservableList<String> course, semester, subject;
    WritableImage image;
    int flag;

    private static double progress1 = 0;
    private static double progress2 = 0;
    private static double progress3 = 0;
    private static double progress4 = 0;
    private static double progress5 = 0;
    private static double progress6 = 0;
    private static double progress7 = 0;
    private static double progress8 = 0;
    private static double progress9 = 0;
    private static double progress10 = 0;
    private static double progress11 = 0;
    private static double progress12 = 0;

    @FXML
    private DatePicker joinDate, birthDate;
    @FXML
    private JFXComboBox<String> comboStaff, comboShift, comboTime;
    private ObservableList<String> typeList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fname = txtFname;
        lname = txtLname;
        mobile = txtMobile;
        email = txtEmail;
        location = txtLocation;
        stfid = txtStaffID;
        male = rdMale;
        female = rdFemale;
        birth = birthDate;
        join = joinDate;
        ctype = comboStaff;
        cshift = comboShift;
        ctime = comboTime;
        photo = imgProfile;
        flag = 0;
        setDepartmentsToCombo();
        updateProgress(1);
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
                Rectangle clip = new Rectangle(imgProfile.getFitWidth(), imgProfile.getFitHeight());
                imgProfile.setImage(image);
                imgProfile.setClip(clip);
                SnapshotParameters parameters = new SnapshotParameters();
                parameters.setFill(Color.TRANSPARENT);
                WritableImage img = imgProfile.snapshot(parameters, null);
                imgProfile.setClip(null);
                imgProfile.setImage(img);
                flag = 1;
                updateProgress(1);
            } catch (IOException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        }
    }

    @FXML
    private void saveTeacher(ActionEvent event) {

        Connection conn = CreationDatabase.setupDatabse();
        PreparedStatement pst = null;

        Date bDate = Date.valueOf(birthDate.getValue());
        Date jDate = Date.valueOf(joinDate.getValue());

        Blob blob = null;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imgProfile.getImage(), null), "png", byteOutput);
            blob = conn.createBlob();
            blob.setBytes(1, byteOutput.toByteArray());
            byteOutput.close();
            conn.close();
        } catch (IOException | SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

        if (flag == 1) {
            String query = "update staffdetails set photo=? where staffId = ?";
            try {
                pst = conn.prepareStatement(query);
                pst.setBlob(1, blob);
                pst.setString(2, stfid.getText());
                pst.execute();
                pst.close();
                conn.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        }
        String sql = "update staffdetails set firstName='" + txtFname.getText().trim() + "',lastName='" + txtLname.getText().trim() + "',"
                + "birth='" + bDate.toString() + "',joinDate='" + jDate.toString() + "',staffType='" + comboStaff.getSelectionModel().getSelectedItem() + "',"
                + "workShift='" + comboShift.getSelectionModel().getSelectedItem() + "',gender='" + getGender() + "',"
                + "workTime='" + comboTime.getSelectionModel().getSelectedItem() + "',"
                + "mobile='" + txtMobile.getText().trim() + "',email='" + txtEmail.getText().toLowerCase().trim() + "',"
                + "location='" + txtLocation.getText() + "' where staffId='" + txtStaffID.getText() + "'";

        if (CreationDatabase.execAction(sql)) {
            new OverViewController().loadData("select * from staffdetails");
            AlertMaker.showSimpleAlert("Success", "Successfully Changed");
            new HomeController().getStaffNumber();
        }

    }

    private void updateProgress(int temp) {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

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
        }
    }

    private void setDepartmentsToCombo() {
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
            AlertMaker.showErrorMessage(ex);
        }
        comboShift.getItems().addAll("Morning", "Day", "Evening");
        comboTime.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
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

    private void fillTecherId(double sum) {
        if (sum >= 1) {
            txtStaffID.setVisible(true);
        } else {
            txtStaffID.setVisible(false);
        }

    }

}
