package com.ATMServer.gui;


import com.ATMServer.ServerStart;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerGuiCtr {
    @FXML
    private ListView<String> clientInfo;
    @FXML
    private ToggleButton Btn_start;
    @FXML
    private ToggleButton Btn_end;
    @FXML
    private Button Btn_exit;
    @FXML
    private TextField statusTextField;

    public TextField getStatusTextField() {
        return statusTextField;
    }

    public void setStatusTextField(TextField statusTextField) {
        this.statusTextField = statusTextField;
    }

    public ListView<String> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ListView<String> clientInfo) {
        this.clientInfo = clientInfo;
    }
    public void start() throws IOException{
        ServerStart.flag=true;
        new Thread(()-> ServerStart.work()).start();
    }
    public void end() throws IOException{
        ServerStart.flag=false;
        if (ServerStart.connectTimes==0){
            System.exit(0);
        }
    }
    public void exitApp(){
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(ServerStart.class.getResource("gui/exitWarning.fxml"));
            AnchorPane Panel=loader.load();

            Scene scene=new Scene(Panel);
            Stage stage=new Stage();
            stage.setTitle("警告！");
            stage.setScene(scene);
            stage.resizableProperty().setValue(false);
            stage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
