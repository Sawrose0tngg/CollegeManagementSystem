package account.management;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import login.system.Registration;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class DashboardController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane holderPane;
    @FXML
    private Label lblMenu;
    @FXML
    private VBox overflowContainer;
    @FXML
    private JFXButton btnLogOut, btnExit;
    @FXML
    private BorderPane borderPane;
    @FXML
    private MaterialDesignIconView iconClose, iconMax, iconMin;

    public static AnchorPane holdePane, student, staff, teacher, home, edit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        holdePane = holderPane;
        openMenus();
        createPages();

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

    //Set selected node to a content holder
    public void setNode(Node node) {
        holdePane.getChildren().clear();
        holdePane.getChildren().add((Node) node);

        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    //Load all fxml files to a cahce for swapping
    private void createPages() {
        try {
            home = FXMLLoader.load(getClass().getResource("/account/management/Home.fxml"));
            student = FXMLLoader.load(getClass().getResource("/account/management/StudentPage.fxml"));
            teacher = FXMLLoader.load(getClass().getResource("/account/management/TeacherPage.fxml"));
            staff = FXMLLoader.load(getClass().getResource("/account/management/StaffPage.fxml"));
            edit = FXMLLoader.load(getClass().getResource("/common/function/EditProfile.fxml"));
            //set up default node on page load
            setNode(home);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    Stage stage;

    @FXML
    private void logOff(ActionEvent event) throws Exception {
        Stage stage1 = new Stage();
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
    private void openHome(ActionEvent event) {
        setNode(home);
    }

    @FXML
    private void studentPage(ActionEvent event) {
        setNode(student);
    }

    @FXML
    private void teacherPage(ActionEvent event) {
        setNode(teacher);
    }

    @FXML
    private void staffPage(ActionEvent event) {
        setNode(staff);
    }

    @FXML
    private void editProfile(ActionEvent event) {
        setNode(edit);
    }

}
