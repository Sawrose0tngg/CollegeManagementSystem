/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account.management;

import common.function.Common;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sawrose Tamang
 */
public class RunAccount extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/account/management/Dashboard.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.initModality(Modality.APPLICATION_MODAL);
        
        root.setStyle(
                "-fx-background-image: url("
                + "'/image/collections/background.jpg'"
                + "); "
                + "-fx-background-size: cover;"
        );
        
        Scene scene = new Scene(root);
        new Common().dragNode(root, stage);
        stage.setScene(scene);
        
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
