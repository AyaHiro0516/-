package com.gui;

import ATMServer.TransObject;
import com.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

    //客户端连接
    private Socket client=null;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos=null;

    public void submition() throws IOException{
        String username=usernameText.getText();
        if (!username.equals(lastUsername)){
            inputTimes=3;
            lastUsername=username;
        }
        String password=passwordText.getText();

        client=new Socket("127.0.0.1",20006);  //客户端连接
        oos=new ObjectOutputStream(client.getOutputStream());
        ois=new ObjectInputStream(client.getInputStream());

        TransObject object=new TransObject("登录");
        object.setFromName(username);
        object.setFromPassword(password);
        oos.writeObject(object);

        try{
            TransObject getObject=(TransObject)ois.readObject();
            if (getObject.getFromName().equals("null")){
                statusText.setText("无该学生信息！");
            }else if(getObject.getFromName().equals("false") && inputTimes>0){
                statusText.setText("密码错误！"+"你还有 "+(inputTimes--)+" 次机会。");
            }else if (getObject.getFromName().equals("true") && inputTimes>=0){
                if (getObject.getAccount().getIsOnline()==true){
                    statusText.setText("该账号已在其他客户端上线！");
                }else MainApp.initBusinessPanel(getObject.getAccount());
            }else {
                MainApp.initMainPanel();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        client.close();
    }
    public void backward(){
        try {
            MainApp.initMainPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
