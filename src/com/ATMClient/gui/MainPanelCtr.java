package com.ATMClient.gui;

import com.ATMClient.ClientStart;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainPanelCtr {
    @FXML
    private Button Btn_register;
    @FXML
    private Button Btn_login;

    public void Registration(){
        try {
            ClientStart.initRegisterPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void Login(){
        try {
            ClientStart.initLoginPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
