package manage.staff;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.sun.prism.impl.Disposer;
import database.handler.CreationDatabase;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import modules.StudentModel;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class OverViewController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private JFXComboBox<String> comboStaff, comboShift;
    @FXML
    private TableView<StudentModel> staffTable;
    @FXML
    private TableColumn<StudentModel, String> colSno, colId, colName, colType, colShift, colEmail, colAddress, colMobile, colJoin, colGender, colEdit, colDelete;
    @FXML
    private StackPane rootPane;
    public static TableView table;
    public static TableColumn coledit, coldelete;
    private ObservableList<String> typeList;
    public static StackPane rPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rPane = rootPane;
        table = staffTable;
        coledit = colEdit;
        coldelete = colDelete;
        settingCombo();
        intiCol();
        loadData("select * from staffdetails");
        settingComboAction();
    }

    @FXML
    private void searchStaff(ActionEvent event) {
        loadData("select * from staffdetails where staffId = '" + txtSearch.getText().toUpperCase() + "' or email = '" + txtSearch.getText() + "' or firstName = '" + txtSearch.getText() + "' or lastName = '" + txtSearch.getText() + "'");
    }

    @FXML
    private void showAll(ActionEvent event) {
        loadData("select * from staffdetails");
        comboStaff.setValue(null);
        comboShift.setValue(null);
        txtSearch.setText(null);
    }

    public void loadData(String sql) {
        Statement stmt;
        Connection conn = CreationDatabase.setupDatabse();

        table.setItems(null);
        ObservableList<StudentModel> list = FXCollections.observableArrayList();

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            try {
                while (rs.next()) {
                    count++;
                    String id = rs.getString("staffId");
                    String na = rs.getString("firstName") + " " + rs.getString("lastName");
                    String co = rs.getString("staffType");
                    String se = rs.getString("workShift");
                    String em = rs.getString("email");
                    String add = rs.getString("location");
                    String mo = rs.getString("mobile");
                    String adm = rs.getString("joinDate");
                    String ge = rs.getString("gender");

                    list.add(new StudentModel(String.valueOf(count), id, na, co, se, em, add, mo, adm, ge));
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

    private void settingCombo() {

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
    }

    public void settingComboAction() {

        comboStaff.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData("select * from staffdetails where staffType = '" + newValue + "'");
        });

        comboShift.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            loadData("select * from staffdetails where staffType = '" + comboStaff.getSelectionModel().getSelectedItem() + "' and workShift ='" + newValue + "'");
        });
    }

    private void intiCol() {

        colSno.setCellValueFactory(new PropertyValueFactory<>("sno"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("course"));
        colShift.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        colJoin.setCellValueFactory(new PropertyValueFactory<>("admission"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

    }

    @FXML
    private void bringToBack(MouseEvent event) {
        rootPane.toBack();
    }

    private static class CreateButtonAction extends TableCell<Disposer.Record, Boolean> {

        JFXButton btn;
        public static JFXDialog dialog;

        public CreateButtonAction(String name) {
            btn = new JFXButton(name);
            if ("Edit".equals(name)) {
                btn.setStyle("-fx-background-color:  rgb(0, 170, 207); -fx-text-fill: #fff");
            } else if ("Delete".equals(name)) {
                btn.setStyle("-fx-background-color: #F87951; -fx-text-fill: #fff");
            }
            MaterialDesignIconView iconClose = new MaterialDesignIconView();
            iconClose.setGlyphName("CLOSE_CIRCLE");
            iconClose.setSize("23");
            iconClose.setStyle("-fx-fill: #fff;");
            iconClose.setCursor(Cursor.HAND);
            iconClose.setOnMouseClicked((MouseEvent event) -> {
                CreateButtonAction.dialog.close();
                OverViewController.rPane.toBack();
            });

            btn.setOnAction((ActionEvent event) -> {
                StudentModel model = (StudentModel) CreateButtonAction.this.getTableView().getItems().get(CreateButtonAction.this.getIndex());

                if (name.equals("Edit")) {
                    String query = "select * from staffdetails where staffId = '" + model.getId() + "'";
                    ResultSet rs = CreationDatabase.execQuery(query);
                    try {
                        if (rs.next()) {
                            JFXDialogLayout dialogLayout = new JFXDialogLayout();
                            dialogLayout.setHeading(iconClose);
                            rPane.toFront();
                            dialog = new JFXDialog(rPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
                            try {
                                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/manage/staff/EditProfile.fxml"));
                                dialogLayout.setStyle("-fx-background-image: url("
                                        + "'/image/collections/background.jpg'"
                                        + "); "
                                        + "-fx-background-size: cover;");
                                dialogLayout.setActions(anchorPane);
                                dialog.show();
                            } catch (IOException ex) {
                                AlertMaker.showErrorMessage(ex);
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            Date dat1 = Date.valueOf(rs.getString("birth"));
                            Date dat2 = Date.valueOf(rs.getString("joinDate"));

                            EditProfileController.fname.setText(rs.getString("firstName"));
                            EditProfileController.lname.setText(rs.getString("lastName"));
                            EditProfileController.birth.setValue(dat1.toLocalDate());
                            EditProfileController.join.setValue(dat2.toLocalDate());
                            EditProfileController.ctype.setValue(rs.getString("staffType"));
                            EditProfileController.cshift.setValue(rs.getString("workShift"));
                            EditProfileController.ctime.setValue(rs.getString("workTime"));
                            EditProfileController.mobile.setText(rs.getString("mobile"));
                            EditProfileController.email.setText(rs.getString("email"));
                            EditProfileController.location.setText(rs.getString("location"));
                            EditProfileController.stfid.setText(rs.getString("staffId"));
                            if ("MALE".equals(rs.getString("gender"))) {
                                EditProfileController.male.setSelected(true);
                            } else if ("FEMALE".equals(rs.getString("gender"))) {
                                EditProfileController.female.setSelected(true);
                            }
                            byte[] byteData = rs.getBytes("photo");
                            ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
                            BufferedImage read = ImageIO.read(bis);
                            Image image = SwingFXUtils.toFXImage(read, null);
                            EditProfileController.photo.setImage(image);
                            rs.close();
                        }
                    } catch (IOException | SQLException ex) {
                        AlertMaker.showErrorMessage(ex);
                    }
                } else if (name.equals("Delete")) {
                    String query = "delete from staffdetails where staffId = '" + model.getId() + "'";
                    String confirm = AlertMaker.confirmMessage("Confirmation", "Are you sure want to delete");

                    if (confirm.equals("YES") && CreationDatabase.execAction(query)) {
                        new OverViewController().loadData("select * from staffdetails");
                        AlertMaker.showSimpleAlert("Success", "Successfully Deleted");
                        new HomeController().getStaffNumber();
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
