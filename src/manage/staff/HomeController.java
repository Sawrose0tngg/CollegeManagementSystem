package manage.staff;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.prism.impl.Disposer;
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
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.TableCell;
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
import javafx.util.Callback;
import javax.imageio.ImageIO;
import login.system.LoginController;
import modules.StaffTypeModel;

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
    public static StackPane rPane;
    @FXML
    private TableColumn<StaffTypeModel, String> colSno, colType, colShift, colHours, colEdit, colDelete;
    @FXML
    private TableView<StaffTypeModel> staffTypeTable;
    @FXML
    private Label lblTotalStaff, lblFemaleStaff, lblRecentStaff, lblMaleStaff;

    public static TableColumn coledit, coldelete;
    static JFXComboBox<String> comboTime, comboShift;
    static JFXTextField txtStaffType;
    public static TableView<StaffTypeModel> table;
    public static Label lblT, lblR, lblM, lblF;
    @FXML
    private Label lblName, lblFullName, lblEmail, lblMobile, lblAdmin;
    @FXML
    private ImageView imageProfile;
    @FXML
    private JFXTextArea txtNotify;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblT = lblTotalStaff;
        lblR = lblRecentStaff;
        lblF = lblFemaleStaff;
        lblM = lblMaleStaff;
        txtStaffType = new JFXTextField();
        comboTime = new JFXComboBox();
        comboShift = new JFXComboBox();
        comboShift.getItems().addAll("Morning", "Day", "Evening");
        comboTime.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        comboTime.setVisibleRowCount(6);
        coledit = colEdit;
        coldelete = colDelete;
        rPane = rootPane;
        table = staffTypeTable;
        getProfileFill();
        getNotifyFill();
        initCol();
        fillStaffTypeTable("select * from staffcategory");
        setRipples();
        getStaffNumber();
    }

    @FXML
    private void addDepartment(MouseEvent event) {
        if (event.getSource() == btnAddDepartment) {
            clearFields();
            popUpFields();
        } else {
            rootPane.toBack();
        }
    }

    public void popUpFields() {
        txtStaffType.setPromptText("Staff Type");
        txtStaffType.setLabelFloat(true);
        txtStaffType.setPrefSize(150, 50);
        txtStaffType.setPadding(new Insets(20, 5, 10, 5));
        txtStaffType.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        comboShift.setPromptText("Staff Shift");
        comboShift.setPrefSize(400, 50);
        comboShift.setPadding(new Insets(20, 5, 10, 5));
        comboShift.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        comboTime.setPromptText("Staff Time");
        comboTime.setPrefSize(400, 50);
        comboTime.setPadding(new Insets(20, 5, 10, 5));
        comboTime.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        // Heading text
        Text t = new Text("Add New Subject");
        t.setStyle("-fx-font-size:14px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(t);
        rPane.toFront();
        JFXDialog dialog = new JFXDialog(rPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
        // close button
        JFXButton closeButton = new JFXButton("Dismiss");
        closeButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");
        //Add button
        JFXButton addBtn = new JFXButton("Add");
        addBtn.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;"
                + "");
        closeButton.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });
        addBtn.setOnAction((ActionEvent event1) -> {

            String value1 = comboShift.getSelectionModel().getSelectedItem();
            String value2 = comboTime.getSelectionModel().getSelectedItem();
            double full = 100.0, pass = 40.0, th = 60, pr = 40;
            Statement stmt;
            Connection conn = CreationDatabase.setupDatabse();

            String query = "select * from staffcategory where typeName = '" + txtStaffType.getText().toUpperCase() + "'";
            ResultSet rs = CreationDatabase.execQuery(query);
            try {
                if (rs.next()) {
                    String sql = "update staffcategory set typeName = '" + txtStaffType.getText().toUpperCase() + "', shift = '" + value1 + "', time = '" + value2 + "' where typeName = '" + txtStaffType.getText().toUpperCase() + "' ";
                    if (CreationDatabase.execAction(sql)) {
                        fillStaffTypeTable("select * from staffcategory");
                        AlertMaker.showSimpleAlert("Success", "Successfully Changed");
                    }
                    rs.close();
                } else {
                    String sql = "insert into staffcategory(typeName,shift,time) values('" + txtStaffType.getText().toUpperCase() + "','" + value1 + "','" + value2 + "')";
                    try {
                        stmt = conn.createStatement();
                        stmt.execute(sql);
                        fillStaffTypeTable("select * from staffcategory");
                        AlertMaker.showSimpleAlert("Success", "Successfully Saved");
                        conn.close();
                        stmt.close();
                    } catch (SQLException ex) {
                        AlertMaker.showErrorMessage(ex);
                    }
                }
            } catch (SQLException ex) {
                AlertMaker.showErrorMessage(ex);
            }

            dialog.close();
            clearFields();
        });
        HBox box = new HBox();
        box.setSpacing(30);
        box.setPrefSize(200, 50);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().addAll(addBtn, closeButton);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPrefSize(400, 150);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(txtStaffType, comboShift, comboTime, box);

        dialogLayout.setActions(vbox);

        dialog.show();

    }

    public void fillStaffTypeTable(String sql) {
        ObservableList<StaffTypeModel> list = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery(sql);
        int counter = 1;
        try {
            while (rs.next()) {
                String type = rs.getString("typeName");
                String shift = rs.getString("shift");
                String time = rs.getString("time");

                list.add(new StaffTypeModel(String.valueOf(counter++), type, shift, time));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        table.getItems().setAll(list);

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
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("time"));

    }

    void clearFields() {
        txtStaffType.setText(null);
        comboShift.setValue(null);
        comboTime.setValue(null);
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

    private static class CreateButtonAction extends TableCell<Disposer.Record, Boolean> {

        JFXButton btn;
        public static JFXDialog dialog;

        CreateButtonAction(String name) {

            btn = new JFXButton(name);
            if ("Edit".equals(name)) {
                btn.setStyle("-fx-background-color:  rgb(0, 170, 207); -fx-text-fill: #fff");
            } else if ("Delete".equals(name)) {
                btn.setStyle("-fx-background-color: #F87951; -fx-text-fill: #fff");
            }

            btn.setOnAction((ActionEvent event) -> {
                StaffTypeModel model = (StaffTypeModel) CreateButtonAction.this.getTableView().getItems().get(CreateButtonAction.this.getIndex());

                if (name.equals("Edit")) {
                    String query = "select * from staffcategory where typeName = '" + model.getType().toUpperCase() + "'";
                    ResultSet rs = CreationDatabase.execQuery(query);
                    try {
                        if (rs.next()) {
                            new HomeController().popUpFields();
                            txtStaffType.setText(rs.getString("typeName"));
                            comboShift.setValue(rs.getString("shift"));
                            comboTime.setValue(rs.getString("time"));
                        }
                        rs.close();
                    } catch (SQLException ex) {
                        AlertMaker.showErrorMessage(ex);
                    }
                } else if (name.equals("Delete")) {
                    String query = "delete from staffcategory where typeName = '" + model.getType().toUpperCase() + "'";
                    String confirm = AlertMaker.confirmMessage("Confirmation", "Are you sure want to delete");
                    if (confirm.equals("YES") && CreationDatabase.execAction(query)) {
                        new HomeController().fillStaffTypeTable("select * from staffcategory");
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

    public void getStaffNumber() {

        String sql1 = "select * from staffdetails";
        String sql2 = "select gender from staffdetails";

        ResultSet rs1 = CreationDatabase.execQuery(sql1);
        ResultSet rs2 = CreationDatabase.execQuery(sql2);

        int count = 0, countm = 0, countf = 0;
        try {
            while (rs1.next()) {
                count++;
                recentAddStaff();
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (rs2.next()) {
                if (rs2.getString("gender").equals("MALE")) {
                    countm++;
                } else if (rs2.getString("gender").equals("FEMALE")) {
                    countf++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lblT.setText(String.valueOf(count));
        lblM.setText(String.valueOf(countm));
        lblF.setText(String.valueOf(countf));

    }

    private void recentAddStaff() {
        int counter = 0;
        java.util.Date dat1 = null, dat2;
        String sql1 = "select * from staffdetails";
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
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
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
