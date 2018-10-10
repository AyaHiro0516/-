package com.gui;

import com.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginPanelCtr {
    @FXML
    private Button Btn_submit;
    @FXML
    private Button Btn_backward;
    @FXML
    private TextField usernameText;
    @FXML
    private TextField passwordText;
    @FXML
    private Text statusText;
    private int inputTimes=3;
    private String lastUsername="null";
    public void submition(){
        String username=usernameText.getText();
        if (!username.equals(lastUsername)){
            inputTimes=3;
            lastUsername=username;
        }
        String password=passwordText.getText();
        if (!MainApp.bank.getAccounts().containsKey(username)){
            statusText.setText("无该学生信息！");
        }else if(!MainApp.bank.getAccounts().get(username).getPassword().equals(password) && inputTimes>0){
            statusText.setText("密码错误！"+"你还有 "+(inputTimes--)+" 次机会。");
        }else if (MainApp.bank.getAccounts().get(username).getPassword().equals(password) && inputTimes>=0){
            try{
                MainApp.initBusinessPanel(username);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try{
                MainApp.initMainPanel();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void backward(){
        try {
            MainApp.initMainPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
