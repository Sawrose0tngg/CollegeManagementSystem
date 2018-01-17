/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manage.student;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Sawrose Tamang
 */
public class DrawerContentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openHome(ActionEvent event) {
        new DashboardController().setNode(DashboardController.home);
    }

    @FXML
    private void addStudent(ActionEvent event) {
        new DashboardController().setNode(DashboardController.add);
    }

    @FXML
    private void updateStudent(ActionEvent event) {
        new DashboardController().setNode(DashboardController.view);
    }

    @FXML
    private void deleteStudent(ActionEvent event) {
        new DashboardController().setNode(DashboardController.view);
    }

    @FXML
    private void viewStudent(ActionEvent event) {
        new DashboardController().setNode(DashboardController.view);
    }

    @FXML
    private void studentResult(ActionEvent event) {
        new DashboardController().setNode(DashboardController.result);
    }
    
}
