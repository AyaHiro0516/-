package com.ATMClient.gui;

import com.accountType.TransObject;
import com.ATMClient.ClientStart;
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

    public void submition() {
        String username=usernameText.getText();
        if (!username.equals(lastUsername)){
            inputTimes=3;
            lastUsername=username;
        }
        String password=passwordText.getText();
        if (username.equals("") || password.equals("")){
            statusText.setText("账户或密码不能为空！");
            return;
        }
        try {
            client=new Socket("192.168.43.79",8888);  //客户端连接
            oos=new ObjectOutputStream(client.getOutputStream());
            ois=new ObjectInputStream(client.getInputStream());

            TransObject object=new TransObject("登录");
            object.setFromName(username);
            object.setFromPassword(password);
            oos.writeObject(object);

            TransObject getObject=(TransObject)ois.readObject();
            if (getObject.getStatus().equals("null")){
                statusText.setText("无该学生信息！");
            }else if(getObject.getStatus().equals("false") && inputTimes>0){
                statusText.setText("密码错误！"+"你还有 "+(inputTimes--)+" 次机会。");
            }else if (getObject.getStatus().equals("true") && inputTimes>=0){
                if (getObject.getAccount().getIsOnline()){
                    statusText.setText("该账号已在其他客户端上线！");
                }else ClientStart.initBusinessPanel(getObject.getAccount());
            }else {
                ClientStart.initMainPanel();
            }

            client.close();
        }catch (IOException e){
            statusText.setText("连接服务器失败！");
        }catch (ClassNotFoundException e){
            statusText.setText("写入账户失败！");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void backward(){
        try {
            ClientStart.initMainPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
