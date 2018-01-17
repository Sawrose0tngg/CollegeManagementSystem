package library.management;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.sun.prism.impl.Disposer;
import database.handler.CreationDatabase;
import database.handler.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import modules.BookModel;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class BookController implements Initializable {

    @FXML
    private JFXTextField txtName, txtAuthor, txtPublisher, txtNumber, txtSearch;
    @FXML
    private JFXComboBox<String> comboCourse, comboSemester, comboCourse1, comboSemester1;
    private ObservableList<String> course, semester;
    @FXML
    private JFXTextField txtId;
    private static double progress1 = 0;
    private static double progress2 = 0;
    private static double progress3 = 0;
    private static double progress4 = 0;
    private static double progress5 = 0;
    private static double progress6 = 0;
    @FXML
    private TableView<?> bookTable;
    @FXML
    private TableColumn<?, ?> colSno, colId, colName, colAuthor, colPublisher, colNumber, colCourse, colSemester, colEdit, colDelete;

    public static TableView table;
    public static TableColumn coledit, coldelete;
    public static StackPane rPane;
    @FXML
    private StackPane rootPane;
    public static String cacheId = null;
    public static JFXDialog dialog;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rPane = rootPane;
        table = bookTable;
        coledit = colEdit;
        coldelete = colDelete;
        settingCombo();
        initCol();
        updateProgress();
        loadData("select * from bookdb");
    }

    @FXML
    private void saveBooks(ActionEvent event) {
        Connection conn = CreationDatabase.setupDatabse();
        PreparedStatement pst = null;
        String sql = "insert into bookDb(bookName,course,semester,authorName,publisher,bookNumber,bookID) values(?,?,?,?,?,?,?)";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText().toLowerCase());
            pst.setString(2, comboCourse.getSelectionModel().getSelectedItem());
            pst.setString(3, comboSemester.getSelectionModel().getSelectedItem());
            pst.setString(4, txtAuthor.getText().toLowerCase());
            pst.setString(5, txtPublisher.getText().toLowerCase());
            pst.setString(6, txtNumber.getText());
            pst.setString(7, txtId.getText());

            pst.executeUpdate();
            clearInput();
            AlertMaker.showSimpleAlert("Success", "Successfully Saved");
            new HomeController().getLibraryDetails();
            conn.close();
            pst.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
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
            rs.close();
            comboCourse.getItems().addAll(course);
            comboCourse1.getItems().addAll(course);
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
                        ResultSet rs2 = CreationDatabase.execQuery(query1);
                        while (rs2.next()) {
                            String name = rs2.getString("semesterList");
                            semester.add(name);
                        }
                        comboSemester.setItems(semester);
                    } else if (rs1.getInt("flag") == 0) {
                        comboSemester.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query2 = "select yearList from " + rs1.getInt("courseDuration") + "year";
                        ResultSet rs3 = CreationDatabase.execQuery(query2);
                        while (rs3.next()) {
                            String name = rs3.getString("yearList");
                            semester.add(name);
                        }
                        comboSemester.setItems(semester);
                    }
                }
                rs1.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        });

        comboCourse1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData("select * from bookdb where course = '" + newValue + "'");
            String sql = "select * from course where courseList = '" + newValue + "'";
            ResultSet rs1 = CreationDatabase.execQuery(sql);
            try {
                if (rs1.next()) {
                    if (rs1.getInt("flag") == 1) {
                        comboSemester1.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query1 = "select semesterList from " + rs1.getInt("courseDuration") + "semester";
                        ResultSet rs2 = CreationDatabase.execQuery(query1);
                        while (rs2.next()) {
                            String name = rs2.getString("semesterList");
                            semester.add(name);
                        }
                        comboSemester1.setItems(semester);
                    } else if (rs1.getInt("flag") == 0) {
                        comboSemester1.setValue(null);
                        semester = FXCollections.observableArrayList();
                        String query2 = "select yearList from " + rs1.getInt("courseDuration") + "year";
                        ResultSet rs3 = CreationDatabase.execQuery(query2);
                        while (rs3.next()) {
                            String name = rs3.getString("yearList");
                            semester.add(name);
                        }
                        comboSemester1.setItems(semester);
                    }
                }
                rs1.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        });

    }

    private void updateProgress() {

        txtName.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress1 = 1.0;
                } else {
                    progress1 = 0.0;
                }
            } catch (Exception e) {
                progress1 = 0.0;

            }
            double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6);
            fillBId(sum);
        });

        comboCourse.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (newValue != null) {
                    progress2 = 1.0;
                } else {
                    progress2 = 0.0;
                }
            } catch (Exception e) {
                progress2 = 0.0;

            }
            double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6);
            fillBId(sum);
        });

        comboSemester.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData("select * from bookdb where course = '" + comboCourse.getSelectionModel().getSelectedItem() + "' and semester = '" + newValue + "'");

            try {
                if (newValue != null) {
                    progress3 = 1.0;
                } else {
                    progress3 = 0.0;
                }
            } catch (Exception e) {
                progress3 = 0.0;
            }
            double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6);
            fillBId(sum);
        });

        comboSemester1.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData("select * from bookdb where course = '" + comboCourse1.getSelectionModel().getSelectedItem() + "' and semester = '" + newValue + "'");
        });

        txtAuthor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress4 = 1.0;
                } else {
                    progress4 = 0.0;
                }
            } catch (Exception e) {
                progress4 = 0.0;

            }
            double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6);
            fillBId(sum);
        });

        txtPublisher.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress5 = 1.0;
                } else {
                    progress5 = 0.0;
                }
            } catch (Exception e) {
                progress5 = 0.0;

            }
            double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6);
            fillBId(sum);
        });

        txtNumber.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (!newValue.isEmpty()) {
                    progress6 = 1.0;
                } else {
                    progress6 = 0.0;
                }
            } catch (Exception e) {
                progress6 = 0.0;

            }
            double sum = (progress1 + progress2 + progress3 + progress4 + progress5 + progress6);
            fillBId(sum);
        });

    }

    private void fillBId(double sum) {

        if (sum == 6) {
            String idName;
            String query = "select max(id) from bookDb";
            ResultSet rs = CreationDatabase.execQuery(query);
            try {
                if (rs.next()) {
                    idName = "BOO" + String.valueOf(rs.getInt(1) + 1);
                    txtId.setText(idName);
                }
                rs.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        } else {
            txtId.setText(null);
        }

    }

    private void clearInput() {

        txtName.setText(null);
        comboCourse.setValue(null);
        comboSemester.setValue(null);
        txtAuthor.setText(null);
        txtPublisher.setText(null);
        txtNumber.setText(null);
        txtId.setText(null);
    }

    @FXML
    private void searchBook(ActionEvent event) {
        comboCourse1.setValue(null);
        comboSemester1.setValue(null);
        loadData("select * from bookdb where bookID = '" + txtSearch.getText().toUpperCase() + "'");
    }

    @FXML
    private void showAll(ActionEvent event) {
        comboCourse1.setValue(null);
        comboSemester1.setValue(null);
        txtSearch.setText(null);
        loadData("select * from bookdb");
    }

    public void loadData(String sql) {
        Statement stmt = null;
        Connection conn = CreationDatabase.setupDatabse();
        table.setItems(null);
        ObservableList<BookModel> list = FXCollections.observableArrayList();

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            try {
                while (rs.next()) {
                    count++;
                    String id = rs.getString("bookID");
                    String na = rs.getString("bookName");
                    String co = rs.getString("course");
                    String se = rs.getString("semester");
                    String em = rs.getString("authorName");
                    String add = rs.getString("publisher");
                    String mo = rs.getString("bookNumber");

                    list.add(new BookModel(String.valueOf(count), id, na, co, se, em, add, mo));
                }
                rs.close();
                conn.close();
                stmt.close();
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }

        table.setItems(list);

        coledit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {

                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });
        coledit.setCellFactory(new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {
            @Override
            public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> param) {

                return new CreateButtonAction("Edit");
            }
        });

        coldelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Disposer.Record, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Disposer.Record, Boolean> param) {

                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });
        coldelete.setCellFactory(new Callback<TableColumn<Disposer.Record, Boolean>, TableCell<Disposer.Record, Boolean>>() {
            @Override
            public TableCell<Disposer.Record, Boolean> call(TableColumn<Disposer.Record, Boolean> param) {
                return new CreateButtonAction("Delete");
            }
        });
    }

    private void initCol() {

        colSno.setCellValueFactory(new PropertyValueFactory<>("sno"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));

    }

    @FXML
    private void rootPaneClick(MouseEvent event) {

        rootPane.toBack();
        dialog.close();
    }

    private static class CreateButtonAction extends TableCell<Disposer.Record, Boolean> {

        Statement stmt;
        Connection conn = CreationDatabase.setupDatabse();
        DatabaseHandler handler = DatabaseHandler.getInstance();

        JFXButton btn;

        CreateButtonAction(String name) {

            btn = new JFXButton(name);
            if ("Edit".equals(name)) {
                btn.setStyle("-fx-background-color:  rgb(0, 170, 207); -fx-text-fill: #fff");
            } else if ("Delete".equals(name)) {
                btn.setStyle("-fx-background-color: #F87951; -fx-text-fill: #fff");
            }

            btn.setOnAction((ActionEvent event) -> {
                BookModel model = (BookModel) CreateButtonAction.this.getTableView().getItems().get(CreateButtonAction.this.getIndex());

                if (name.equals("Edit")) {
                    String query = "select * from bookdb where bookID = '" + model.getId() + "'";
                    ResultSet rs = CreationDatabase.execQuery(query);
                    try {
                        if (rs.next()) {
                            JFXDialogLayout dialogLayout = new JFXDialogLayout();
                            rPane.toFront();
                            dialog = new JFXDialog(rPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
                            try {
                                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/library/management/BookEdit.fxml"));
                                dialogLayout.setStyle("-fx-background-image: url("
                                        + "'/image/collections/background.jpg'"
                                        + "); "
                                        + "-fx-background-size: cover;");
                                dialogLayout.setActions(anchorPane);
                                dialog.show();
                            } catch (IOException ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            BookEditController.textName.setText(model.getName());
                            BookEditController.combCourse.setValue(model.getCourse());
                            BookEditController.combSemester.setValue(model.getSemester());
                            BookEditController.textAuthor.setText(model.getAuthor());
                            BookEditController.textPublisher.setText(model.getPublisher());
                            BookEditController.textNumber.setText(model.getNumber());
                            cacheId = model.getId();
                        }
                        rs.close();
                    } catch (SQLException ex) {
                        AlertMaker.showErrorMessage(ex);
                    }
                } else if (name.equals("Delete")) {
                    String query = "delete from bookdb where bookID = '" + model.getId() + "'";
                    handler = DatabaseHandler.getInstance();
                    String confirm = AlertMaker.confirmMessage("Confirmation", "Are you sure want to delete");
                    if (confirm.equals("YES") && handler.execAction(query)) {
                        new BookController().loadData("select * from bookdb");
                        AlertMaker.showSimpleAlert("Success", "Successfully Deleted");
                    }
                }
            });

        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(btn);
            } else {
                setGraphic(null);
            }
        }

    }
}
