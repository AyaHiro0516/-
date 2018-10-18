package com.gui;

import com.MainApp;
import com.accountType.*;
import com.exceptionType.RegisterException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.TreeMap;

public class RegisterPanelCtr {
    @FXML
    private Button Btn_submit;
    @FXML
    private Button Btn_backward;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private PasswordField repasswordText;
    @FXML
    private TextField idnumText;
    @FXML
    private TextField emailText;
    @FXML
    private TextArea adressText;
    @FXML
    private ChoiceBox selectBox;
    @FXML
    private Text statusText;

    public ChoiceBox getSelectBox() {
        return selectBox;
    }

    public void setSelectBox(ChoiceBox selectBox) {
        this.selectBox = selectBox;
    }

    public void submition(){
        TreeMap<String,Account> map=MainApp.bank.getAccounts();
        String accountType=(String) selectBox.getValue();
        String username=usernameText.getText();
        String password=passwordText.getText();
        String repassword=repasswordText.getText();
        String idnum=idnumText.getText();
        String email=emailText.getText();
        String adress=adressText.getText();
        if (accountType.equals("") || username.equals("") || password.equals("") || repassword.equals("") ||
                idnum.equals("") || email.equals("")|| adress.equals("")){
            statusText.setText("有信息未填写！");
        }else if (map.containsKey(username) && map.get(username).getPersonId().equals(idnum)){
            statusText.setText("账号已存在");
        }else {
            if(password.equals(repassword)){  //此处逻辑要修改  先检查密码
                try{
                    map.put(username,MainApp.bank.register(password,username,idnum,email,accountType));
                    MainApp.bank.upDate();
                    selectBox.getSelectionModel().selectFirst();
                    usernameText.clear();
                    passwordText.clear();
                    repasswordText.clear();
                    idnumText.clear();
                    emailText.clear();
                    adressText.clear();
                    statusText.setText("账户添加成功！");
                }catch (RegisterException e){
                    e.printStackTrace();
                }
            }else {
                statusText.setText("密码不一致！");
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
