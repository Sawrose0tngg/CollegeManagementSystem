package data.entry;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTextField;
import database.handler.CreationDatabase;
import database.handler.DatabaseHandler;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import login.system.Registration;
import main.admin.controller.MainAdminController;
import manage.staff.RunStaff;
import manage.student.RunStudent;
import manage.teacher.RunTeacher;
import modules.CourseModel;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class DataEntryController implements Initializable {

    DatabaseHandler handler = DatabaseHandler.getInstance();
    Statement stmt;

    @FXML
    private StackPane stackPane;
    @FXML
    private VBox overflowContainer;
    @FXML
    private JFXButton btnLogOut, btnEdit;
    @FXML
    private MaterialDesignIconView btnEditProfile, iconClose, iconMax, iconMin;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label lblMenu;
    @FXML
    private AnchorPane holderPane;
    public static AnchorPane holdePane;
    @FXML
    private JFXTextField txtCourse, txtDuration, txtSearch, txtSubject, txtSearchSubject, txtFull, txtPass, txtTheory, txtPractical;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXButton btnAdd, btnRemove, btnViewAll, btnshowAll;
    @FXML
    private TableView<CourseModel> courseTable;
    @FXML
    private TableColumn<CourseModel, String> colSno, colDuration, colSemester, colCourse, colFull, colPass, colTheory, colPractical;
    @FXML
    private JFXComboBox<String> comboCourse, comboSemester;
    @FXML
    private TableColumn<CourseModel, String> colSno1, colCourse1, colSemester1, colSubject1;
    @FXML
    private JFXComboBox<String> comboCourse1, comboSemester1;

    private ObservableList<String> semester;
    @FXML
    private TableView<CourseModel> subjectTable;
    @FXML
    private JFXButton btnAddSubject;
    @FXML
    private JFXButton btnRemoveSubject;
    public static StackPane rPane;
    public static JFXDialog dialog;
    Stage stage;
    Stage stage1 = new Stage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        intiCol();
        rPane = stackPane;
        holdePane = holderPane;
        openMenus();
        fillCourseTable("select * from course");
        fillSubjectTable("select * from subject");
        setDepartmentsToCombo();
        settingCombo();
    }

    @FXML
    private void logOff(ActionEvent event) throws Exception {
        stage = (Stage) iconClose.getScene().getWindow();
        stage.close();
        new Registration().start(stage1);
    }

    @FXML
    private void mouseClickAction(MouseEvent event) {
        if (event.getSource() == iconClose) {
            AlertMaker.showSimpleAlert("Note", "Please Logout");
        } else if (event.getSource() == iconMax) {
            stage = (Stage) iconMax.getScene().getWindow();
            stage.setFullScreen(!stage.isFullScreen());
        } else if (event.getSource() == iconMin) {
            stage = (Stage) iconMin.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    @FXML
    private void addCourse(ActionEvent event) {
        Connection conn = CreationDatabase.setupDatabse();
        String flag = "0";
        if (checkBox.isSelected()) {
            flag = "1";
        }
        String query = "select courseList from course where courseList = '" + txtCourse.getText().toUpperCase() + "'";
        ResultSet rs = CreationDatabase.execQuery(query);
        try {
            if (rs.next()) {
                String sql = "update course set courseDuration='" + Integer.parseInt(txtDuration.getText()) + "', flag='" + Integer.parseInt(flag) + "' where courseList = '" + txtCourse.getText().toUpperCase() + "'";
                if (CreationDatabase.execAction(sql)) {
                    System.out.println("Added Successfully...");
                    fillCourseTable("select * from course");
                    handler.setupSemesterTable(txtDuration.getText(), Integer.parseInt(flag));
                    clearFields();
                }
                rs.close();
            } else {
                String sql = "insert into course(courseList,courseDuration,flag) values('" + txtCourse.getText().toUpperCase() + "','" + Integer.parseInt(txtDuration.getText()) + "','" + Integer.parseInt(flag) + "')";
                try {
                    stmt = conn.createStatement();
                    stmt.execute(sql);
                    fillCourseTable("select * from course");
                    AlertMaker.showSimpleAlert("Success", "Successfully Saved");

                    handler.setupSemesterTable(txtDuration.getText(), Integer.parseInt(flag));
                    clearFields();
                    conn.close();
                    stmt.close();
                } catch (SQLException ex) {
                    AlertMaker.showErrorMessage(ex);
                }
            }
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        setDepartmentsToCombo();
    }

    @FXML
    private void removeCourse(ActionEvent event) {
        ResultSet rs = null, rs1 = null;
        int counter = 0;
        String sql = "select * from course where courseList = '" + txtCourse.getText().toUpperCase() + "'";
        rs = CreationDatabase.execQuery(sql);
        try {
            if (rs.next()) {
                int val1 = rs.getInt("courseDuration");
                int val2 = rs.getInt("flag");
                counter = new MainAdminController().setRequestData("select * from course where courseDuration='" + val1 + "' and flag = '" + val2 + "'");
                if (counter == 1) {
                    if (val2 == 1) {
                        CreationDatabase.execAction("drop table " + val1 + "semester");
                    } else if (val2 == 0) {
                        CreationDatabase.execAction("drop table " + val1 + "year");
                    }
                }
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

        String query = "delete from course where courseList = '" + txtCourse.getText().toUpperCase() + "'";
        if (CreationDatabase.execAction(query)) {
            fillCourseTable("select * from course");

            ResultSet rs2 = CreationDatabase.execQuery("select course from subject where course = '" + txtCourse.getText().toUpperCase() + "'");
            try {
                if (rs2.next()) {
                    String query1 = "delete from subject where course = '" + txtCourse.getText().toUpperCase() + "'";
                    if (CreationDatabase.execAction(query1)) {
                        AlertMaker.showSimpleAlert("Success", "Successfully Deleted");
                    }
                } else {
                    AlertMaker.showSimpleAlert("Success", "Successfully Deleted");
                }
                rs2.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
            clearFields();
        }
        fillSubjectTable("select * from subject");
        setDepartmentsToCombo();
    }

    @FXML
    private void searchCourse() {
        fillCourseTable("select * from course where courseList = '" + txtSearch.getText().toUpperCase() + "'");
    }

    @FXML
    private void addSubject(ActionEvent event) {
        String value1 = comboCourse.getSelectionModel().getSelectedItem();
        String value2 = comboSemester.getSelectionModel().getSelectedItem();
        double full = 100.0, pass = 40.0, th = 60, pr = 40;
        Connection conn = CreationDatabase.setupDatabse();

        String query = "select * from subject where subjectName = '" + txtSubject.getText() + "'";
        ResultSet rs = CreationDatabase.execQuery(query);
        try {
            if (rs.next()) {
                String sql = "update subject set subjectName = '" + txtSubject.getText() + "', fullMark = '" + txtFull.getText() + "', passMark = '" + txtPass.getText() + "', theory = '" + txtTheory.getText() + "', practical = '" + txtPractical.getText() + "' where course = '" + value1 + "' and semester = '" + value2 + "' and subjectName = '" + txtSubject.getText().toUpperCase() + "'";
                if (CreationDatabase.execAction(sql)) {
                    fillSubjectTable("select * from subject");
                    AlertMaker.showSimpleAlert("Success", "Successfully Addedd");
                }
                rs.close();
            } else {
                String sql = "insert into subject(course,semester,subjectName, fullMark, passMark, theory, practical) values('" + value1 + "','" + value2 + "','" + txtSubject.getText().toUpperCase() + "', '" + full + "', '" + pass + "', '" + th + "', '" + pr + "')";
                try {
                    stmt = conn.createStatement();
                    stmt.execute(sql);
                    fillSubjectTable("select * from subject");
                    AlertMaker.showSimpleAlert("Success", "Successfully Added");
                    clearFields();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    AlertMaker.showErrorMessage(ex);
                }
            }
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

    }

    @FXML
    private void removeSubject(ActionEvent event) {

        String query = "delete from subject where course = '" + comboCourse.getSelectionModel().getSelectedItem() + "' and semester = '" + comboSemester.getSelectionModel().getSelectedItem() + "' and subjectName = '" + txtSubject.getText().toUpperCase() + "'";
        if (CreationDatabase.execAction(query)) {
            System.out.println("Deleted Successfully");
            fillSubjectTable("select * from subject");
            clearFields();
        }
    }

    @FXML
    private void searchSubject() {
        fillSubjectTable("select * from subject where subjectName = '" + txtSearchSubject.getText().toUpperCase() + "'");
    }

    @FXML
    private void showAllCourse(ActionEvent event) {
        fillCourseTable("select * from course");
        clearFields();
    }

    @FXML
    private void showAllSubject(ActionEvent event) {
        fillSubjectTable("select * from subject");
        clearFields();
    }

    @FXML
    private void tableClickAction(MouseEvent event) {

        if (event.getSource() == courseTable) {
            if (event.getClickCount() == 2) {

                CourseModel model = courseTable.getSelectionModel().getSelectedItem();
                String sql = "select * from course where courseList = '" + model.getCourse() + "'";
                ResultSet rs = CreationDatabase.execQuery(sql);
                try {
                    if (rs.next()) {
                        txtCourse.setText(rs.getString("courseList"));
                        txtDuration.setText(rs.getString("courseDuration"));
                        if (rs.getInt("flag") == 1) {
                            checkBox.setSelected(true);
                        } else {
                            checkBox.setSelected(false);
                        }
                    }
                    rs.close();
                } catch (SQLException ex) {
                    AlertMaker.showErrorMessage(ex);
                }
            }
        } else if (event.getSource() == subjectTable) {
            if (event.getClickCount() == 2) {
                CourseModel model = subjectTable.getSelectionModel().getSelectedItem();
                String sql = "select * from subject where subjectName = '" + model.getDuration() + "' and course = '" + model.getCourse() + "' and semester = '" + model.getSemester() + "'";
                ResultSet rs = CreationDatabase.execQuery(sql);
                try {
                    if (rs.next()) {
                        comboCourse.setValue(rs.getString("course"));
                        comboSemester.setValue(rs.getString("semester"));
                        txtSubject.setText(rs.getString("subjectName"));
                        txtFull.setText(rs.getString("fullMark"));
                        txtPass.setText(rs.getString("passMark"));
                        txtTheory.setText(rs.getString("theory"));
                        txtPractical.setText(rs.getString("practical"));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    AlertMaker.showErrorMessage(ex);
                }
            }
        }
    }

    private void intiCol() {

        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colSno.setCellValueFactory(new PropertyValueFactory<>("sno"));
        colFull.setCellValueFactory(new PropertyValueFactory<>("fullMark"));
        colPass.setCellValueFactory(new PropertyValueFactory<>("passMark"));
        colTheory.setCellValueFactory(new PropertyValueFactory<>("theoryMark"));
        colPractical.setCellValueFactory(new PropertyValueFactory<>("practicalMark"));

        colCourse1.setCellValueFactory(new PropertyValueFactory<>("course"));
        colSubject1.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colSemester1.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colSno1.setCellValueFactory(new PropertyValueFactory<>("sno"));

    }

    public void fillCourseTable(String sql) {
        ObservableList<CourseModel> list = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery(sql);
        int counter = 1;
        try {
            while (rs.next()) {
                String cou = rs.getString("courseList");
                int dur = rs.getInt("courseDuration");
                String sem;

                if (rs.getInt("flag") == 1) {
                    sem = String.valueOf(dur * 2);
                } else {
                    sem = "-";
                }
                list.add(new CourseModel(cou, String.valueOf(dur), sem, String.valueOf(counter++)));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        courseTable.getItems().setAll(list);
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

    private void openMenus() {
        JFXPopup popup = new JFXPopup();
        popup.setContent(overflowContainer);
        popup.setPopupContainer(stackPane);
        popup.setSource(lblMenu);
        lblMenu.setOnMouseClicked((MouseEvent e) -> {
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -10, 40);
        });
    }

    private void setDepartmentsToCombo() {

        String query = "SELECT courseList FROM course";
        ObservableList<String> course = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery(query);
        try {
            while (rs.next()) {
                String name = rs.getString("courseList");
                course.add(name);
            }
            comboCourse.getItems().clear();
            comboCourse1.getItems().clear();
            comboCourse.getItems().addAll(course);
            comboCourse1.getItems().addAll(course);
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
    }

    private void clearFields() {
        txtCourse.setText(null);
        txtDuration.setText(null);
        checkBox.setSelected(false);
        txtSearch.setText(null);
        comboSemester.setValue(null);
        comboCourse.setValue(null);
        txtSubject.setText(null);
        txtSearchSubject.setText(null);
        txtFull.setText(null);
        txtPass.setText(null);
        txtTheory.setText(null);
        txtPractical.setText(null);
    }

    public void settingCombo() {
        comboCourse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String sql = "select * from course where courseList = '" + newValue + "'";
            ResultSet rs = CreationDatabase.execQuery(sql);
            try {
                if (rs.next()) {
                    if (rs.getInt("flag") == 1) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query = "select semesterList from " + rs.getInt("courseDuration") + "semester";

                        ResultSet rs1 = null;
                        rs1 = CreationDatabase.execQuery(query);
                        while (rs1.next()) {
                            String name = rs1.getString("semesterList");
                            semester.add(name);
                        }
                        comboSemester.setItems(semester);
                        rs1.close();
                    } else if (rs.getInt("flag") == 0) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query = "select yearList from " + rs.getInt("courseDuration") + "year";

                        ResultSet rs1 = null;
                        rs1 = CreationDatabase.execQuery(query);
                        while (rs1.next()) {
                            String name = rs1.getString("yearList");
                            semester.add(name);
                        }
                        comboSemester.setItems(semester);
                        rs1.close();
                    }
                }
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        });

        comboCourse1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String sql = "select * from course where courseList = '" + newValue + "'";
            ResultSet rs = CreationDatabase.execQuery(sql);
            try {
                if (rs.next()) {
                    if (rs.getInt("flag") == 1) {
                        comboSemester1.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query = "select semesterList from " + rs.getInt("courseDuration") + "semester";

                        ResultSet rs1 = null;
                        rs1 = CreationDatabase.execQuery(query);
                        while (rs1.next()) {
                            String name = rs1.getString("semesterList");
                            semester.add(name);
                        }
                        comboSemester1.setItems(semester);
                        rs1.close();
                    } else if (rs.getInt("flag") == 0) {
                        comboSemester1.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query = "select yearList from " + rs.getInt("courseDuration") + "year";

                        ResultSet rs1 = null;
                        rs1 = CreationDatabase.execQuery(query);
                        while (rs1.next()) {
                            String name = rs1.getString("yearList");
                            semester.add(name);
                        }
                        comboSemester1.setItems(semester);
                        rs1.close();
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
            fillSubjectTable("select * from subject where course = '" + newValue + "'");
            clearFields();
        });

        comboSemester1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            fillSubjectTable("select * from subject where course = '" + comboCourse1.getSelectionModel().getSelectedItem() + "' and semester = '" + newValue + "'");
            clearFields();
        });
    }

    @FXML
    private void openStudent(MouseEvent event) throws Exception {
        new RunStudent().start(stage1);
        stage = (Stage) iconClose.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void openTeacher(MouseEvent event) throws Exception {
        new RunTeacher().start(stage1);
        stage = (Stage) iconClose.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void openStaff(MouseEvent event) throws Exception {
        new RunStaff().start(stage1);
        stage = (Stage) iconClose.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void editProfileAction(ActionEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        rPane.toFront();
        dialog = new JFXDialog(rPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/common/function/AdminProfile.fxml"));
            dialogLayout.setActions(anchorPane);
            dialog.show();
        } catch (IOException ex) {
            Logger.getLogger(DataEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void sentToBack(MouseEvent event) {
        rPane.toBack();
    }

}
