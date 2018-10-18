package com.gui;

import com.MainApp;
import com.accountType.CreditAccount;
import com.accountType.LoanCreditAccount;
import com.accountType.LoanSavingAccount;
import com.exceptionType.ATMException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
    private ChoiceBox selectBox;
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

    public ChoiceBox getSelectBox() {
        return selectBox;
    }

    public void setSelectBox(ChoiceBox selectBox) {
        this.selectBox = selectBox;
    }

    public void submition(){
        String username=usernameText.getText();
        String mode=(String) selectBox.getValue();
        try {
            Double amount=new Double(amountTextField.getText());
            if (amount<0){
                amountTextField.clear();
                statusText.setText("输入有误！");
                return;
            }
            switch (mode){  //判断操作类型
                case "存款":
                    MainApp.bank.deposit(username,amount);
                    break;
                case "取款":
                    MainApp.bank.withdraw(username,amount);
                    break;
                case "转账":
                    String transname=transnameTextField.getText();
                    MainApp.bank.transfer(username,transname,amount);
                    break;
                case "借贷":
                    MainApp.bank.requestLoan(username,amount);
                    break;
                case "还贷":
                    MainApp.bank.payLoan(username,amount);
                    break;
            }
            MainApp.bank.upDate();
            statusText.setText("操作成功！");
        }catch (ATMException e ){
            e.printStackTrace();
            amountTextField.clear();
            statusText.setText("操作失败！");
        }catch (NumberFormatException e){
            //e.printStackTrace();
            amountTextField.clear();
            statusText.setText("输入有误！");
        }
        amountTextField.clear();
        //更新面板信息
        String accountType=MainApp.bank.getAccounts().get(username).getAccountType();
        balanceText.setText(MainApp.bank.getAccounts().get(username).getBalance()+"");
        switch (accountType){
            case "SavingAccount":
                //do nothing
                break;
            case "CreditAccount":
                CreditAccount account=(CreditAccount)MainApp.bank.getAccounts().get(username);
                ceilingText.setText(account.getCeiling()+"");
                break;
            case "LoanSavingAccount":
                LoanSavingAccount account1=(LoanSavingAccount)MainApp.bank.getAccounts().get(username);
                loanText.setText(account1.getLoan()+"");
                break;
            case "LoanCreditAccount":
                LoanCreditAccount account2=(LoanCreditAccount)MainApp.bank.getAccounts().get(username);
                ceilingText.setText(account2.getCeiling()+"");
                loanText.setText(account2.getLoan()+"");
                break;
        }

    }

    public void backward(){
        try{
            MainApp.initMainPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
