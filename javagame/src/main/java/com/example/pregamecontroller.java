package com.example;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class pregamecontroller {
    private int mapindex;
    private String username;

    public void setUsername(String username) {
        tftusername.setText(username);
    }

    public String getusername() {
        return username;
    }
   
    @FXML
    private ImageView imgmap1;

    private BooleanProperty isImgMap1Clicked = new SimpleBooleanProperty(false);

    @FXML
    private ImageView imgmap2;

    private BooleanProperty isImgMap2Clicked = new SimpleBooleanProperty(false);

    @FXML
    private ImageView imgmap3;

    private BooleanProperty isImgMap3Clicked = new SimpleBooleanProperty(false);

    @FXML
    private Label lboptionmap;

    @FXML
    private Label lbusername;

    @FXML
    private TextField tftusername;

    @FXML
    private Pane paneboder1;

    @FXML
    private Pane paneboder2;

    @FXML
    private Pane paneboder3;

    @FXML
    void btnhandleonclickstart(ActionEvent event) {
        username = tftusername.getText();
        Gamecontroller gamecontroller = new Gamecontroller(username, mapindex);
        gamecontroller.startgame();
    }

    @FXML
    void btnimgsgetmapid10(MouseEvent event) {
        handlePaneClick(paneboder1, isImgMap1Clicked, 10);
    }

    @FXML
    void btnimgsgetmapid11(MouseEvent event) {
        handlePaneClick(paneboder2, isImgMap2Clicked, 11);
    }

    @FXML
    void btnimgsgetmapid12(MouseEvent event) {
        handlePaneClick(paneboder3, isImgMap3Clicked, 12);
    }

    private void handlePaneClick(Pane pane, BooleanProperty isClicked, int mapIndex) {
        if (!isClicked.get()) {
            this.mapindex = mapIndex;
            pane.setStyle("-fx-border-color: yellow; -fx-border-width: 6px; -fx-border-style: solid;");
            isClicked.set(true);
    
            // Ẩn border của các Pane khác
            if (pane != paneboder1) {
                paneboder1.setStyle("");
                isImgMap1Clicked.set(false);
            }
            if (pane != paneboder2) {
                paneboder2.setStyle("");
                isImgMap2Clicked.set(false);
            }
            if (pane != paneboder3) {
                paneboder3.setStyle("");
                isImgMap3Clicked.set(false);
            }
        } else {
            this.mapindex = 0;
            pane.setStyle("");
            isClicked.set(false);
        }
        this.mapindex = mapIndex;
    }

    @FXML
    public void initialize() {
        paneboder1.setOnMouseClicked(event -> handlePaneClick(paneboder1, isImgMap1Clicked, 10));
        paneboder2.setOnMouseClicked(event -> handlePaneClick(paneboder2, isImgMap2Clicked, 11));
        paneboder3.setOnMouseClicked(event -> handlePaneClick(paneboder3, isImgMap3Clicked, 12));
    }
}