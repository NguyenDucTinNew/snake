package com.example;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.Gamecontroller;

public class usercrud {
     @FXML
    private TextField nameField;
   
    private String username;
    public void setUsername(String username) {
        nameField.setText(username);
    }

    public String getUsername()
    {
        return username;
    }
    
    
    //move nó sang màn hình chạy game
      @FXML
    private void handlePlayButtonAction(ActionEvent event) {
    username = nameField.getText();
    Gamecontroller  gamecontroller = new Gamecontroller(username);
    gamecontroller.startgame();;
}
     
}
