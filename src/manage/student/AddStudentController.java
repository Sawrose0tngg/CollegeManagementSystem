package manage.student;

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
public class AddStudentController implements Initializable {

    @FXML
    private AnchorPane parentPane;
    @FXML
    private JFXTextField txtFname, txtLname, txtMobile, txtEmail, txtLocation, txtGContact, txtGName, txtSId;
    @FXML
    private JFXRadioButton rdMale, rdFemale;
    @FXML
    private ToggleGroup gender;
    @FXML
    private JFXButton btnClear, btnSave, btnUpload;
    @FXML
    private ProgressBar progressPersonal;
    @FXML
    private Label lblComplete;
    @FXML
    private DatePicker dateBirth, dateAdmission;
    @FXML
    private JFXComboBox<String> comboCourse, comboSemester;

    private ObservableList<String> course, semester;
    @FXML
    private ImageView imgProfile;
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
    private static double progress13 = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        flag = 0;
        setDepartmentsToCombo();
        updateProgress(0);
    }

    @FXML
    private void clearFields(ActionEvent event) {
        updateProgress(0);
        clearInput();
    }

    @FXML
    private void saveStudent(ActionEvent event) {
        Connection conn = CreationDatabase.setupDatabse();
        PreparedStatement pst = null;
        Date bDate = Date.valueOf(dateBirth.getValue());
        Date aDate = Date.valueOf(dateAdmission.getValue());

        Blob blob = null;
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

        String sql = "insert into studentdetails(firstName, lastName, birth, admission, course, semester, gender, guardianName, guardianContact, mobile, email, location, photo, studentId) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(imgProfile.getImage(), null), "png", byteOutput);
            blob = conn.createBlob();
            blob.setBytes(1, byteOutput.toByteArray());
            byteOutput.close();
        } catch (IOException | SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtFname.getText().trim());
            pst.setString(2, txtLname.getText().trim());
            pst.setString(3, bDate.toString());
            pst.setString(4, aDate.toString());
            pst.setString(5, comboCourse.getSelectionModel().getSelectedItem());
            pst.setString(6, comboSemester.getSelectionModel().getSelectedItem());
            pst.setString(7, getGender());
            pst.setString(8, txtGName.getText());
            pst.setString(9, txtGContact.getText().trim());
            pst.setString(10, txtMobile.getText().trim());
            pst.setString(11, txtEmail.getText().toLowerCase().trim());
            pst.setString(12, txtLocation.getText());
            pst.setBlob(13, blob);
            pst.setString(14, txtSId.getText());

            pst.executeUpdate();
            pst.close();
            conn.close();
            clearInput();
            updateProgress(0);
            new HomeController().getAmountTotal();
            AlertMaker.showSimpleAlert("Success", "Successfully Saved");

        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
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

    private void updateProgress(int temp) {
        DecimalFormat decimalFormat = new DecimalFormat("###.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        double sum_progress = progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13;

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
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");

            fillSId(sum);
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
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
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
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
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
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
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
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        txtGName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress6 = 0.05;
                } else {
                    progress6 = 0.0;
                }
            } catch (Exception e) {
                progress6 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        txtGContact.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress7 = 0.05;
                } else {
                    progress7 = 0.0;
                }
            } catch (Exception e) {
                progress7 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        rdMale.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                if (!oldValue) {
                    progress8 = 0.05;
                } else {
                    progress8 = 0.0;
                }
            } catch (Exception e) {
                progress8 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        rdFemale.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                if (!oldValue) {
                    progress8 = 0.05;
                } else {
                    progress8 = 0.0;
                }
            } catch (Exception e) {
                progress8 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        dateBirth.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                if (newValue != null) {
                    progress9 = 0.05;
                } else {
                    progress9 = 0.0;
                }
            } catch (Exception e) {
                progress9 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        dateAdmission.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            try {
                if (newValue != null) {
                    progress10 = 0.1;
                } else {
                    progress10 = 0.0;
                }
            } catch (Exception e) {
                progress10 = 0.0;
            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        comboCourse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String sql = "select * from course where courseList = '" + newValue + "'";
            ResultSet rs = CreationDatabase.execQuery(sql);
            ResultSet rs1 = null;
            try {
                if (rs.next()) {
                    if (rs.getInt("flag") == 1) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query = "select semesterList from " + rs.getInt("courseDuration") + "semester";

                        rs1 = CreationDatabase.execQuery(query);
                        while (rs1.next()) {
                            String name = rs1.getString("semesterList");
                            semester.add(name);
                        }
                        rs1.close();
                        comboSemester.setItems(semester);

                    } else if (rs.getInt("flag") == 0) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query = "select yearList from " + rs.getInt("courseDuration") + "year";

                        rs1 = CreationDatabase.execQuery(query);
                        while (rs1.next()) {
                            String name = rs1.getString("yearList");
                            semester.add(name);
                        }
                        rs1.close();
                        comboSemester.setItems(semester);
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
            try {
                if (!newValue.isEmpty()) {
                    progress11 = 0.1;
                } else {
                    progress11 = 0.0;
                }
            } catch (Exception e) {
                progress11 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        comboSemester.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (newValue != null) {
                    progress12 = 0.1;
                } else {
                    progress12 = 0.0;
                }
            } catch (Exception e) {
                progress12 = 0.0;

            }
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        });

        if (temp == 1) {
            progress13 = 0.1;
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        } else {
            progress13 = 0.0;
            double sum = (progress10 + progress1 + progress2 + progress3 + progress4 + progress5 + progress6 + progress7 + progress8 + progress9 + progress11 + progress12 + progress13);
            progressPersonal.setProgress(sum);
            lblComplete.setText(decimalFormat.format(sum * 100) + "% complete");
            fillSId(sum);
        }

    }

    private void fillSId(double sum) {
        if (sum >= 1) {
            String idName;
            String query = "select max(id) from studentdetails";
            ResultSet rs = CreationDatabase.execQuery(query);
            try {
                if (rs.next()) {
                    idName = "STD" + String.valueOf(rs.getInt(1) + 1);
                    txtSId.setText(idName);
                }
                rs.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        } else {
            txtSId.setText(null);
        }

    }

    private void setDepartmentsToCombo() {
        String query = "SELECT courseList FROM course";
        course = FXCollections.observableArrayList();
        semester = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery(query);
        try {
            while (rs.next()) {
                String name = rs.getString("courseList");
                course.add(name);
            }
            rs.close();
            comboCourse.getItems().addAll(course);
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

    private void clearInput() {
        txtFname.setText(null);
        txtLname.setText(null);
        dateBirth.setValue(null);
        dateAdmission.setValue(null);
        comboCourse.setValue(null);
        comboSemester.setValue(null);
        rdMale.setSelected(false);
        rdFemale.setSelected(false);
        txtGName.setText(null);
        txtGContact.setText(null);
        txtMobile.setText(null);
        txtEmail.setText(null);
        txtLocation.setText(null);
        imgProfile.setImage(null);
        txtSId.setText(null);
    }

}
