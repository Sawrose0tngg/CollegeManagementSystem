/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.admin.controller;

import common.function.Common;
import database.handler.DatabaseHandler;
import javafx.application.Application;
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
public class RunMainAdmin extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main/admin/controller/MainAdmin.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        new Common().dragNode(root,stage);
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
