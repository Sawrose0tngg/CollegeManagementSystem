package library.management;

import common.function.Common;
import database.handler.DatabaseHandler;
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
public class RunLibrary extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/management/Dashboard.fxml"));
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
//       new Thread(() -> {
//            DatabaseHandler.getInstance();
//        }).start();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
