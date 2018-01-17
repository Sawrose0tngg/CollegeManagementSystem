package login.system;

import common.function.Common;
import database.handler.DatabaseHandler;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sawrose Tamang
 */
public class Registration extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/login/system/Login.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        new Common().dragNode(root, stage);
        stage.setScene(scene);
        
        stage.show();
        
        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
