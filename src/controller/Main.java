package controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    @FXML
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass()
                .getResource("../view/User_Login.fxml"));
        primaryStage.setTitle("Atlanta BeltLine by Team_eleven");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
