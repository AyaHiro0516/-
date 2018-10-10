package com.gui;

import com.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainPanelCtr {
    @FXML
    private Button Btn_register;
    @FXML
    private Button Btn_login;

    public void Registration(){
        try {
            MainApp.initRegisterPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void Login(){
        try {
            MainApp.initLoginPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
