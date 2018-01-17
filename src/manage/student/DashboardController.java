package manage.student;

import collection.alert.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private JFXButton btnLogOut, btnEdit;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private MaterialDesignIconView iconClose, iconMin, iconMax;
    @FXML
    private AnchorPane leftBorderPane;
    @FXML
    private JFXDrawer drawer;

    public static AnchorPane home, add, view, result, edit, holdePane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        holdePane = holderPane;
        openMenus();
        sideBarAnimation();
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

    void setNode(Node node) {
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

    private void createPages() {
        try {
            home = FXMLLoader.load(getClass().getResource("/manage/student/Home.fxml"));
            add = FXMLLoader.load(getClass().getResource("/manage/student/AddStudent.fxml"));
            view = FXMLLoader.load(getClass().getResource("/manage/student/OverView.fxml"));
            result = FXMLLoader.load(getClass().getResource("/manage/student/ViewResult.fxml"));
            edit = FXMLLoader.load(getClass().getResource("/common/function/EditProfile.fxml"));
            
            setNode(home);
        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex);
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

    private void sideBarAnimation() {
        borderPane.setLeft(null);
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/manage/student/DrawerContent.fxml"));

            drawer.setSidePane(box);

            HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
            transition.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                transition.setRate(transition.getRate() * -1);
                transition.play();
                if (drawer.isShown()) {
                    drawer.close();
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.millis(300),
                            ae -> {
                                borderPane.setLeft(null);
                            }));
                    timeline.play();
                } else {
                    drawer.open();
                    borderPane.setLeft(leftBorderPane);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void editProfile(ActionEvent event) {
        setNode(edit);
    }

}
