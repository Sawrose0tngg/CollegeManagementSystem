package manage.teacher;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import database.handler.CreationDatabase;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import login.system.LoginController;
import modules.CourseModel;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class HomeController implements Initializable {

    @FXML
    private HBox groupHolder;
    @FXML
    private Group groupRegistered, groupTarget, groupGents, groupLadies;
    @FXML
    private StackPane deptStackPane;
    @FXML
    private Button btnAddDepartment;
    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<CourseModel> subjectTable;
    @FXML
    private TableColumn<CourseModel, String> colSno1, colCourse1, colSemester1, colSubject1, colFull, colPass, colTheory, colPractical;
    @FXML
    private Label lblTotalTeacher, lblRecentTeacher, lblMaleTeacher, lblFemaleTeacher;
    @FXML
    private Label lblName, lblAdmin, lblFullName, lblEmail, lblMobile;
    @FXML
    private ImageView imageProfile;
    @FXML
    private JFXTextArea txtNotify;

    public JFXComboBox<String> comboCourse, comboSemester;
    private ObservableList<String> semester;
    public JFXTextField txtSubject, txtFull, txtPass, txtTheory, txtPractical;
    public static Label lblT, lblR, lblM, lblF;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblT = lblTotalTeacher;
        lblR = lblRecentTeacher;
        lblM = lblMaleTeacher;
        lblF = lblFemaleTeacher;
        comboCourse = new JFXComboBox();
        comboSemester = new JFXComboBox();
        txtSubject = new JFXTextField();
        txtFull = new JFXTextField();
        txtPass = new JFXTextField();
        txtTheory = new JFXTextField();
        txtPractical = new JFXTextField();
        getProfileFill();
        getNotifyFill();
        intiCol();
        setRipples();
        setDepartmentsToCombo();
        settingCombo();
        fillSubjectTable("select * from subject");
        getTeacherNumber();
    }

    @FXML
    private void addDepartment(MouseEvent event) {
        if (event.getSource() == btnAddDepartment) {
            popUpDialog();
        } else {
            rootPane.toBack();
        }
    }

    public void popUpDialog() {
        comboCourse.setPromptText("Course");
        comboCourse.setPrefSize(400, 50);
        comboCourse.setPadding(new Insets(20, 5, 10, 5));
        comboCourse.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        comboSemester.setPromptText("Semester");
        comboSemester.setPrefSize(400, 50);
        comboSemester.setPadding(new Insets(20, 5, 10, 5));
        comboSemester.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        txtSubject.setPromptText("Subject");
        txtSubject.setLabelFloat(true);
        txtSubject.setPrefSize(150, 50);
        txtSubject.setPadding(new Insets(10, 5, 10, 5));
        txtSubject.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        txtFull.setPromptText("Full Marks");
        txtFull.setLabelFloat(true);
        txtFull.setPrefSize(150, 50);
        txtFull.setPadding(new Insets(10, 5, 10, 5));
        txtFull.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        txtPass.setPromptText("Pass Marks");
        txtPass.setLabelFloat(true);
        txtPass.setPrefSize(150, 50);
        txtPass.setPadding(new Insets(10, 5, 10, 5));
        txtPass.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        txtTheory.setPromptText("Theoritical Marks");
        txtTheory.setLabelFloat(true);
        txtTheory.setPrefSize(150, 50);
        txtTheory.setPadding(new Insets(10, 5, 10, 5));
        txtTheory.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        txtPractical.setPromptText("Practical Marks");
        txtPractical.setLabelFloat(true);
        txtPractical.setPrefSize(150, 50);
        txtPractical.setPadding(new Insets(10, 5, 10, 5));
        txtPractical.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        // Heading text
        Text t = new Text("Add New Subject");
        t.setStyle("-fx-font-size:14px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(t);
        rootPane.toFront();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        // close button
        JFXButton closeButton = new JFXButton("Dismiss");
        closeButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");
        //delete button
        JFXButton deleteButton = new JFXButton("Delete");
        deleteButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");
        //Add button
        JFXButton addBtn = new JFXButton("Add");
        addBtn.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;"
                + "");
        closeButton.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });
        deleteButton.setOnAction((ActionEvent event) -> {
            String sql = "delete from subject where course = '" + comboCourse.getSelectionModel().getSelectedItem() + "' and semester = '" + comboSemester.getSelectionModel().getSelectedItem() + "' and subjectName = '" + txtSubject.getText().toUpperCase() + "'";
            String confirm = AlertMaker.confirmMessage("Confirmation", "Are you sure want to delete");
            if (confirm.equals("YES") && CreationDatabase.execAction(sql)) {
                fillSubjectTable("select * from subject");
                AlertMaker.showSimpleAlert("Success", "Successfully Deleted");
                dialog.close();
            }
        });
        addBtn.setOnAction((ActionEvent event1) -> {
            Statement stmt;
            Connection conn = CreationDatabase.setupDatabse();

            String value1 = comboCourse.getSelectionModel().getSelectedItem();
            String value2 = comboSemester.getSelectionModel().getSelectedItem();
            double full = 100.0, pass = 40.0, th = 60, pr = 40;

            String query = "select * from subject where subjectName = '" + txtSubject.getText() + "'";
            ResultSet rs = CreationDatabase.execQuery(query);
            try {
                if (rs.next()) {
                    String sql = "update subject set subjectName = '" + txtSubject.getText() + "', fullMark = '" + txtFull.getText() + "', passMark = '" + txtPass.getText() + "', theory = '" + txtTheory.getText() + "', practical = '" + txtPractical.getText() + "' where course = '" + value1 + "' and semester = '" + value2 + "' and subjectName = '" + txtSubject.getText().toUpperCase() + "'";
                    if (CreationDatabase.execAction(sql)) {
                        fillSubjectTable("select * from subject");
                        AlertMaker.showSimpleAlert("Success", "SuccessFully Changed");
                    }
                    rs.close();
                } else {
                    String sql = "insert into subject(course,semester,subjectName, fullMark, passMark, theory, practical) values('" + value1 + "','" + value2 + "','" + txtSubject.getText().toUpperCase() + "', '" + full + "', '" + pass + "', '" + th + "', '" + pr + "')";
                    try {
                        stmt = conn.createStatement();
                        stmt.execute(sql);
                        fillSubjectTable("select * from subject");
                        AlertMaker.showSimpleAlert("Success", "SuccessFully Saved");
                    } catch (SQLException ex) {
                        Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
            dialog.close();
        });
        HBox box = new HBox();
        box.setSpacing(30);
        box.setPrefSize(200, 50);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(addBtn, deleteButton, closeButton);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPrefSize(400, 150);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(comboCourse, comboSemester, txtSubject, txtFull, txtPass, txtTheory, txtPractical, box);

        dialogLayout.setActions(vbox);
        dialog.show();
    }

    private void settingCombo() {

        comboCourse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String sql = "select * from course where courseList = '" + newValue + "'";
            ResultSet rs = CreationDatabase.execQuery(sql);
            ResultSet rs1 = null;
            try {
                if (rs.next()) {
                    if (rs.getInt("flag") == 1) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        rs1 = CreationDatabase.execQuery("select semesterList from " + rs.getInt("courseDuration") + "semester");
                        while (rs1.next()) {
                            String name = rs1.getString("semesterList");
                            semester.add(name);
                        }
                        rs1.close();
                        comboSemester.setItems(semester);
                    } else if (rs.getInt("flag") == 0) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        rs1 = CreationDatabase.execQuery("select yearList from " + rs.getInt("courseDuration") + "year");
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
        });
    }

    @FXML
    private void tableClickAction(MouseEvent event) throws SQLException {
        if (event.getClickCount() == 2) {
            CourseModel model = subjectTable.getSelectionModel().getSelectedItems().get(0);
            comboCourse.setValue(model.getCourse());
            comboSemester.setValue(model.getSemester());
            txtSubject.setText(model.getDuration());
            txtFull.setText(model.getFullMark());
            txtPass.setText(model.getPassMark());
            txtTheory.setText(model.getTheoryMark());
            txtPractical.setText(model.getPracticalMark());
            popUpDialog();
        }
    }

    public void fillSubjectTable(String sql) {
        ObservableList<CourseModel> list = FXCollections.observableArrayList();
        ResultSet rs = CreationDatabase.execQuery(sql);
        int counter = 1;
        try {
            while (rs.next()) {
                String cou = rs.getString("course");
                String dur = rs.getString("subjectName");
                String sem = rs.getString("semester");
                String ful = rs.getString("fullMark");
                String pas = rs.getString("passMark");
                String theo = rs.getString("theory");
                String prac = rs.getString("practical");

                list.add(new CourseModel(cou, dur, sem, String.valueOf(counter++), ful, pas, theo, prac));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        subjectTable.getItems().setAll(list);
    }

    private void setRipples() {
        JFXRippler rippler1 = new JFXRippler(groupRegistered);
        JFXRippler rippler2 = new JFXRippler(groupGents);
        JFXRippler rippler3 = new JFXRippler(groupLadies);
        JFXRippler rippler4 = new JFXRippler(groupTarget);
        rippler1.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler2.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler3.setMaskType(JFXRippler.RipplerMask.RECT);
        rippler4.setMaskType(JFXRippler.RipplerMask.RECT);

        rippler1.setRipplerFill(Paint.valueOf("#1564C0"));
        rippler2.setRipplerFill(Paint.valueOf("#00AACF"));
        rippler3.setRipplerFill(Paint.valueOf("#00B3A0"));
        rippler4.setRipplerFill(Paint.valueOf("#F87951"));

        groupHolder.getChildren().addAll(rippler1, rippler2, rippler3, rippler4);
    }

    private void setDepartmentsToCombo() {

        ObservableList<String> course = FXCollections.observableArrayList();
        ResultSet rs = CreationDatabase.execQuery("SELECT courseList FROM course");
        try {
            while (rs.next()) {
                String name = rs.getString("courseList");
                course.add(name);
            }
            rs.close();
            comboCourse.getItems().clear();
            comboCourse.getItems().addAll(course);
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    private void intiCol() {
        colCourse1.setCellValueFactory(new PropertyValueFactory<>("course"));
        colSubject1.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colSemester1.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colSno1.setCellValueFactory(new PropertyValueFactory<>("sno"));
        colFull.setCellValueFactory(new PropertyValueFactory<>("fullMark"));
        colPass.setCellValueFactory(new PropertyValueFactory<>("passMark"));
        colTheory.setCellValueFactory(new PropertyValueFactory<>("theoryMark"));
        colPractical.setCellValueFactory(new PropertyValueFactory<>("practicalMark"));

    }

    public void getTeacherNumber() {

        ResultSet rs1 = CreationDatabase.execQuery("select * from teacherdetails");
        ResultSet rs2 = CreationDatabase.execQuery("select gender from teacherdetails");

        int count = 0, countm = 0, countf = 0;
        try {
            while (rs1.next()) {
                count++;
                recentAddTeacher();
            }
            rs1.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        try {
            while (rs2.next()) {
                if (rs2.getString("gender").equals("MALE")) {
                    countm++;
                } else if (rs2.getString("gender").equals("FEMALE")) {
                    countf++;
                }
            }
            rs2.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        lblT.setText(String.valueOf(count));
        lblM.setText(String.valueOf(countm));
        lblF.setText(String.valueOf(countf));
    }

    private void recentAddTeacher() {
        int counter = 0;
        java.util.Date dat1 = null, dat2;
        String sql1 = "select * from teacherdetails";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String month = "2628002880";
        double milsec1 = 0.0, milsec2 = 0.0;
        dat2 = new java.util.Date();

        ResultSet rs1 = CreationDatabase.execQuery(sql1);
        try {
            while (rs1.next()) {
                dat1 = Date.valueOf(rs1.getString("joinDate"));
                try {
                    java.util.Date date1 = df.parse(dat1.toString());
                    milsec1 = date1.getTime();
                    milsec2 = dat2.getTime();
                } catch (ParseException ex) {
                    Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if ((milsec2 - milsec1) <= Double.parseDouble(month)) {
                    counter++;
                }
            }
            rs1.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        lblR.setText(String.valueOf(counter));
    }

    private void getProfileFill() {
        ByteArrayInputStream bis = null;
        String sql = "select * from admindetails where email = '" + LoginController.emailID + "'";
        ResultSet rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                lblName.setText(rs.getString("firstName") + " " + rs.getString("lastName"));
                lblEmail.setText(rs.getString("email"));
                lblFullName.setText(rs.getString("firstName") + " " + rs.getString("lastName"));
                lblMobile.setText(rs.getString("contact"));
                lblAdmin.setText(rs.getString("adminType") + " Admin");
                byte[] byteData = rs.getBytes("photo");
                bis = new ByteArrayInputStream(byteData);
                BufferedImage read = ImageIO.read(bis);
                Image image = SwingFXUtils.toFXImage(read, null);
                imageProfile.setImage(image);
                bis.close();
            }
            rs.close();
        } catch (SQLException | IOException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    private void getNotifyFill() {

        String sql = "select message from notify where adminType = '" + LoginController.adminCategory + "'";
        ResultSet rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                txtNotify.setText(rs.getString("message"));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }
}
