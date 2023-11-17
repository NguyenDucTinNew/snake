package com.example;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class menugamecontroller {

    @FXML
    private ImageView grassbackgound;

    @FXML
    private ImageView logogame;

    
    @FXML
    void btn1player(ActionEvent event) throws IOException  {
           
     FXMLLoader loader = new FXMLLoader(getClass().getResource("pregame.fxml"));
        Parent parent = loader.load();
        pregamecontroller pregameController = loader.getController();
        pregameController.initialize();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        Stage stage = (Stage) grassbackgound.getScene().getWindow();
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    void btn2player(ActionEvent event) {

    }

}
