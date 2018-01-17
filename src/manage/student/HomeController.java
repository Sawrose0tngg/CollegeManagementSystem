package manage.student;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.prism.impl.Disposer.Record;
import common.function.CreateButton;
import database.handler.CreationDatabase;
import database.handler.DatabaseHandler;
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
import modules.CourseModel;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class HomeController implements Initializable {

    DatabaseHandler handler;

    public static JFXTextField courseYear, courseName;
    public static JFXCheckBox checkBox;
    public static Label label;
    public static StackPane rootP;
    public static TableView table;
    public static TableColumn coledit, coldelete;
    public static Label lblT, lblR, lblM, lblF;

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
    private TableView<CourseModel> courseTable;
    @FXML
    private TableColumn<CourseModel, String> colCourse, colDuration, colSemester;
    @FXML
    private TableColumn colEdit, colDelete;
    @FXML
    private Label lblTotal, lblRegister, lblMale, lblFemale;
    @FXML
    private Label lblName, lblFullName, lblEmail, lblMobile, lblAdmin;
    @FXML
    private ImageView imageProfile;
    @FXML
    private JFXTextArea txtNotify;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblT = lblTotal;
        lblR = lblRegister;
        lblM = lblMale;
        lblF = lblFemale;
        rootP = rootPane;
        table = courseTable;
        coledit = colEdit;
        coldelete = colDelete;
        getProfileFill();
        getNotifyFill();
        intiCol();
        fillTable();
        setRipples();
        getAmountTotal();
    }

    @FXML
    public void addDepartment(MouseEvent event) {

        if (event.getSource() == btnAddDepartment) {
            popupDialog();
        } else {
            rootP.toBack();
        }
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

    private void intiCol() {
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));

    }

    public void fillTable() {
        ObservableList<CourseModel> list = FXCollections.observableArrayList();

        ResultSet rs = CreationDatabase.execQuery("select * from course");
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
                list.add(new CourseModel(cou, String.valueOf(dur), sem));
            }
            rs.close();
        } catch (SQLException ex) {
            AlertMaker.showErrorMessage(ex);
        }
        table.getItems().setAll(list);

        coledit.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> param) {

                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });
        coledit.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> param) {
                return new CreateButton("Edit");
            }
        });

        coldelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Record, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> param) {

                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });
        coldelete.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
            @Override
            public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> param) {
                return new CreateButton("Delete");
            }
        });

    }

    public void popupDialog() {

        courseName = new JFXTextField();
        courseName.setPromptText("Course Name");
        courseName.setLabelFloat(true);
        courseName.setPrefSize(150, 50);
        courseName.setPadding(new Insets(20, 5, 10, 5));
        courseName.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        courseYear = new JFXTextField();
        courseYear.setPromptText("Duration of Course (Number of Years)");
        courseYear.setLabelFloat(true);
        courseYear.setPrefSize(150, 50);
        courseYear.setPadding(new Insets(10, 5, 10, 5));
        courseYear.setStyle("-fx-font-size:13px; -fx-font-weight:bold;-fx-text-fill:#00B3A0");

        label = new Label("Semester Type");
        label.setStyle("-fx-font-size:14px;-fx-font-weight:bold;-fx-text-fill:#00B3A0");
        checkBox = new JFXCheckBox();
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setPrefSize(200, 50);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label, checkBox);

        // Heading text
        Text t = new Text("Add New Department");
        t.setStyle("-fx-font-size:14px;");

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setHeading(t);
        rootP.toFront();
        JFXDialog dialog = new JFXDialog(rootP, dialogLayout, JFXDialog.DialogTransition.CENTER);
        // close button
        JFXButton closeButton = new JFXButton("Cancel");
        closeButton.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;");
        //Add button
        JFXButton addBtn = new JFXButton("Add");
        addBtn.setStyle("-fx-button-type: RAISED;-fx-background-color: rgb(77,102,204);-fx-font-size: 14px;-fx-text-fill: WHITE;"
                + "");
        closeButton.setOnAction((ActionEvent event1) -> {
            dialog.close();
        });
        addBtn.setOnAction((ActionEvent event1) -> {
            String flag = "0";
            if (checkBox.isSelected()) {
                flag = "1";
            }
            String query = "select courseList from course where courseList = '" + courseName.getText().toUpperCase() + "'";
            Statement stmt;
            Connection conn = CreationDatabase.setupDatabse();
            ResultSet rs = CreationDatabase.execQuery(query);
            try {
                if (rs.next()) {
                    String sql = "update course set courseDuration='" + Integer.parseInt(courseYear.getText()) + "', flag='" + Integer.parseInt(flag) + "' where courseList = '" + courseName.getText() + "'";
                    if (CreationDatabase.execAction(sql)) {
                        fillTable();
                        AlertMaker.showSimpleAlert("Success", "Successfully Changed");
                        handler.setupSemesterTable(courseYear.getText(), Integer.parseInt(flag));
                    }
                    rs.close();
                } else {
                    String sql = "insert into course(courseList,courseDuration,flag) values('" + courseName.getText().toUpperCase() + "','" + Integer.parseInt(courseYear.getText()) + "','" + Integer.parseInt(flag) + "')";
                    try {
                        stmt = conn.createStatement();
                        stmt.execute(sql);
                        stmt.close();
                        conn.close();
                        fillTable();
                        handler.setupSemesterTable(courseYear.getText(), Integer.parseInt(flag));
                        AlertMaker.showSimpleAlert("Success", "Successfully Saved");
                    } catch (SQLException ex) {
                        AlertMaker.showErrorMessage(ex);
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
        box.getChildren().addAll(addBtn, closeButton);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPrefSize(400, 150);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(courseName, courseYear, hbox, box);

        dialogLayout.setActions(vbox);
        dialog.show();
    }

    public void getAmountTotal() {

        ResultSet rs1 = CreationDatabase.execQuery("select * from studentdetails");
        ResultSet rs2 = CreationDatabase.execQuery("select gender from studentdetails");

        int count = 0, countm = 0, countf = 0;
        try {
            while (rs1.next()) {
                count++;
                recentAddStudent();
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

    private void recentAddStudent() {
        int counter = 0;
        java.util.Date dat1 = null, dat2;
        String sql1 = "select * from studentdetails";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String month = "2628002880";
        double milsec1 = 0.0, milsec2 = 0.0;
        dat2 = new java.util.Date();

        ResultSet rs1 = CreationDatabase.execQuery(sql1);
        try {
            while (rs1.next()) {
                dat1 = Date.valueOf(rs1.getString("admission"));
                try {
                    java.util.Date date1 = df.parse(dat1.toString());
                    milsec1 = date1.getTime();
                    milsec2 = dat2.getTime();
                } catch (ParseException ex) {
                    AlertMaker.showErrorMessage(ex);
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
