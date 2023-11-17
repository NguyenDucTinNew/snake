package com.example;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TestController {

    /**
     * @param event
     */
    @FXML
    private TextField tftTitle;
     
    @FXML
    void btnonClick(ActionEvent event) throws IOException  {
     Stage testwindown = (Stage)tftTitle.getScene().getWindow();
     String title = tftTitle.getText();
     testwindown.setTitle(title);
    }

}