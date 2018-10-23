package com.ATMClient.gui;

import com.accountType.TransObject;
import com.ATMClient.ClientStart;
import com.accountType.CreditAccount;
import com.accountType.LoanCreditAccount;
import com.accountType.LoanSavingAccount;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BusinessPanelCtr {
    @FXML
    private Button Btn_submit;
    @FXML
    private Button Btn_backward;
    @FXML
    private Text idText;
    @FXML
    private Text usernameText;
    @FXML
    private Text balanceText;
    @FXML
    private Text ceilingText;
    @FXML
    private Text loanText;
    @FXML
    private Text transnameText;
    @FXML
    private TextField amountTextField;
    @FXML
    private TextField transnameTextField;
    @FXML
    private ChoiceBox<String> selectBox;
    @FXML
    private Text statusText;

    //客户端连接
    private Socket client=null;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos=null;

    public Text getIdText() {
        return idText;
    }

    public void setIdText(Text idText) {
        this.idText = idText;
    }

    public Text getUsernameText() {
        return usernameText;
    }

    public void setUsernameText(Text usernameText) {
        this.usernameText = usernameText;
    }

    public Text getBalanceText() {
        return balanceText;
    }

    public void setBalanceText(Text balanceText) {
        this.balanceText = balanceText;
    }

    public Text getCeilingText() {
        return ceilingText;
    }

    public void setCeilingText(Text ceilingText) {
        this.ceilingText = ceilingText;
    }

    public Text getLoanText() {
        return loanText;
    }

    public void setLoanText(Text loanText) {
        this.loanText = loanText;
    }

    public Text getTransnameText() {
        return transnameText;
    }

    public void setTransnameText(Text transnameText) {
        this.transnameText = transnameText;
    }

    public TextField getAmountTextField() {
        return amountTextField;
    }

    public void setAmountTextField(TextField amountTextField) {
        this.amountTextField = amountTextField;
    }

    public TextField getTransnameTextField() {
        return transnameTextField;
    }

    public void setTransnameTextField(TextField transnameTextField) {
        this.transnameTextField = transnameTextField;
    }

    public ChoiceBox<String> getSelectBox() {
        return selectBox;
    }

    public void setSelectBox(ChoiceBox<String> selectBox) {
        this.selectBox = selectBox;
    }

    public void submition() {
        String username=usernameText.getText();
        String mode=selectBox.getValue();
        try {
            Double amount=new Double(amountTextField.getText());
            if (amount<0){
                amountTextField.clear();
                statusText.setText("输入有误！");
                return;
            }
            client=new Socket("192.168.43.79",8888);  //客户端连接
            oos=new ObjectOutputStream(client.getOutputStream());
            ois=new ObjectInputStream(client.getInputStream());

            TransObject object=new TransObject("业务");
            object.setFromName(username);
            object.setAmount(amountTextField.getText());
            object.setToName(transnameTextField.getText());
            object.setBusinessType(mode);
            oos.writeObject(object);

            TransObject getObject=(TransObject)ois.readObject();
            if (getObject.getStatus().equals("null")){
                statusText.setText("操作失败！");
                amountTextField.clear();
            }else {
                statusText.setText("操作成功！");
                //更新面板信息
                amountTextField.clear();
                String accountType=getObject.getFromAccountType();
                balanceText.setText(getObject.getAccount().getBalance()+"");
                switch (accountType){
                    case "SavingAccount":
                        //do nothing
                        break;
                    case "CreditAccount":
                        CreditAccount account1=(CreditAccount)getObject.getAccount();
                        ceilingText.setText(account1.getCeiling()+"");
                        break;
                    case "LoanSavingAccount":
                        LoanSavingAccount account2=(LoanSavingAccount)getObject.getAccount();
                        loanText.setText(account2.getLoan()+"");
                        break;
                    case "LoanCreditAccount":
                        LoanCreditAccount account3=(LoanCreditAccount)getObject.getAccount();
                        ceilingText.setText(account3.getCeiling()+"");
                        loanText.setText(account3.getLoan()+"");
                        break;
                }
            }

            client.close();
        }catch (IOException e){
            statusText.setText("连接服务器失败！");
        }catch (ClassNotFoundException e ){
            statusText.setText("写入账户失败！");
            e.printStackTrace();
        }catch (NumberFormatException e){
            amountTextField.clear();
            statusText.setText("输入有误！");
        }

    }

    public void backward(){
        try{
            client=new Socket("192.168.43.79",8888);  //客户端连接
            oos=new ObjectOutputStream(client.getOutputStream());
            ois=new ObjectInputStream(client.getInputStream());
            TransObject object=new TransObject("下线");
            object.setFromName(usernameText.getText());
            oos.writeObject(object);

            TransObject getObject=(TransObject)ois.readObject();
            client.close();
            ClientStart.initMainPanel();
        }catch (IOException e){
            //e.printStackTrace();
            statusText.setText("连接服务器失败！");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
