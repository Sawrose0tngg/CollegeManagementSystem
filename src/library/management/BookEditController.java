package library.management;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.handler.CreationDatabase;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class BookEditController implements Initializable {

    @FXML
    private JFXTextField txtName, txtAuthor, txtPublisher, txtNumber;
    public static JFXTextField textName, textAuthor, textPublisher, textNumber;
    @FXML
    private JFXComboBox<String> comboCourse, comboSemester;
    public static JFXComboBox<String> combCourse, combSemester;
    private ObservableList<String> course, semester;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textName = txtName;
        textAuthor = txtAuthor;
        textPublisher = txtPublisher;
        textNumber = txtNumber;
        combCourse = comboCourse;
        combSemester = comboSemester;
        settingCombo();
    }

    @FXML
    private void saveBook(ActionEvent event) {
        String sql = "update bookdb set bookName='" + txtName.getText().toLowerCase() + "',course='" + comboCourse.getSelectionModel().getSelectedItem() + "',semester='" + comboSemester.getSelectionModel().getSelectedItem() + "',authorName='" + txtAuthor.getText() + "',publisher='" + txtPublisher.getText() + "',bookNumber='" + txtNumber.getText() + "' where bookID ='" + BookController.cacheId + "' ";
        if (CreationDatabase.execAction(sql)) {
            new BookController().loadData("select * from bookdb");
            AlertMaker.showSimpleAlert("Success", "Successfully Updated");
            BookController.dialog.close();

        }
    }

    private void settingCombo() {

        String query = "SELECT courseList FROM course";
        course = FXCollections.observableArrayList();
        semester = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery(query);
        try {
            while (rs.next()) {
                String name = rs.getString("courseList");
                course.add(name);
            }
            comboCourse.getItems().addAll(course);
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        comboCourse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            String sql = "select * from course where courseList = '" + newValue + "'";
            ResultSet rs1 = CreationDatabase.execQuery(sql);
            try {
                if (rs1.next()) {
                    if (rs1.getInt("flag") == 1) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query1 = "select semesterList from " + rs1.getInt("courseDuration") + "semester";
                        ResultSet rs2 = null;
                        rs2 = CreationDatabase.execQuery(query1);
                        while (rs2.next()) {
                            String name = rs2.getString("semesterList");
                            semester.add(name);
                        }
                        comboSemester.setItems(semester);
                        rs2.close();
                    } else if (rs1.getInt("flag") == 0) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query2 = "select yearList from " + rs1.getInt("courseDuration") + "year";
                        ResultSet rs3 = null;
                        rs3 = CreationDatabase.execQuery(query2);
                        while (rs3.next()) {
                            String name = rs3.getString("yearList");
                            semester.add(name);
                        }
                        comboSemester.setItems(semester);
                        rs3.close();
                    }
                }
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        });

    }

    @FXML
    private void closeDialog(ActionEvent event) {
        BookController.rPane.toBack();
        BookController.dialog.close();
    }
}
