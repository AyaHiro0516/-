package com;

import com.accountType.Account;
import com.accountType.CreditAccount;
import com.accountType.LoanCreditAccount;
import com.accountType.LoanSavingAccount;
import com.gui.BusinessPanelCtr;
import com.gui.RegisterPanelCtr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.Socket;

public class MainApp extends Application {
    private static Stage stage;
    public static File idFile=new File("F:/test/id.txt");
    public static File dataFile=new File("F:/test/data.txt");;
    public static Bank bank=Bank.getBank();
    public static Socket client=null;   //为客户端添加连接
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        bank.readData();
//            bank.getAccounts().put("user1",bank.register("null","user1","null","null","LoanCreditAccount"));
//            bank.getAccounts().put("user2",bank.register("null","user2","null","null","CreditAccount"));
//            bank.getAccounts().put("user3",bank.register("null","user3","null","null","SavingAccount"));
//            bank.getAccounts().put("user4",bank.register("null","user4","null","null","CreditAccount"));
//            Account account=bank.deposit("user1",100);
//            System.out.println(account.getBalance());
//            account=bank.setCeiling("user1",50);
//            account=bank.withdraw("user1",10);
//            bank.transfer("user1","user2",50);
//            bank.requestLoan("user1",100);
//            bank.payLoan("user1",20);
//        System.out.println(bank.login("user1","null").getBalance());
//        System.out.println(bank.login("user2","null").getBalance());
//        System.out.println(bank.login("user1","null").getAccountType());
//        System.out.println(bank.login("user2","null").getAccountType());
//        System.out.println(bank.getAccountsNum());
//        bank.upDate();
        initMainPanel();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    //初始化选择页面
    public static void initMainPanel() throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("gui/MainPanel.fxml"));
        AnchorPane Panel=loader.load();
        Scene scene=new Scene(Panel);
        stage.setTitle("ATM终端");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
    //进入登录页面
    public static void initLoginPanel() throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("gui/LoginPanel.fxml"));
        AnchorPane Panel=loader.load();
        Scene scene=new Scene(Panel);
        stage.setTitle("账户登录");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
    //进入注册页面
    public static void initRegisterPanel() throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("gui/RegisterPanel.fxml"));
        AnchorPane Panel=loader.load();

        RegisterPanelCtr ctr=loader.getController();
        ChoiceBox choiceBox=ctr.getSelectBox();
        choiceBox.getItems().addAll("SavingAccount","CreditAccount","LoanSavingAccount","LoanCreditAccount");
        choiceBox.getSelectionModel().selectFirst();

        Scene scene=new Scene(Panel);
        stage.setTitle("账户注册");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
    //进入业务页面
    public static void initBusinessPanel(Account account) throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("gui/BusinessPanel.fxml"));
        AnchorPane Panel=loader.load();

        BusinessPanelCtr ctr=loader.getController();
        Text idText=ctr.getIdText();
        Text usernameText=ctr.getUsernameText();
        Text balanceText=ctr.getBalanceText();
        Text ceilingText=ctr.getCeilingText();
        Text loanText=ctr.getLoanText();
        Text transnameText=ctr.getTransnameText();
        TextField transnameTextField=ctr.getTransnameTextField();
        ChoiceBox choiceBox=ctr.getSelectBox();

        transnameText.setVisible(false);
        transnameTextField.setVisible(false);
        choiceBox.getItems().addAll("存款","取款","转账");
        choiceBox.getSelectionModel().selectFirst();
        //隐藏接收账户窗口    当选中“转账”时才会出现
        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs,olds,news)->{
            if("转账".equals(news)){
                transnameText.setVisible(true);
                transnameTextField.setVisible(true);
            }else {
                transnameText.setVisible(false);
                transnameTextField.setVisible(false);
                transnameTextField.clear();
            }
        });

        idText.setText(account.getId()+"");
        usernameText.setText(account.getName());
        balanceText.setText(account.getBalance()+"");
        String accountType=account.getAccountType();
        switch (accountType){
            case "SavingAccount":
                ceilingText.setText("未开通");
                loanText.setText("未开通");
                break;
            case "CreditAccount":
                loanText.setText("未开通");
                CreditAccount account1=(CreditAccount)account;
                ceilingText.setText(account1.getCeiling()+"");
                break;
            case "LoanSavingAccount":
                ceilingText.setText("未开通");
                LoanSavingAccount account2=(LoanSavingAccount)account;
                loanText.setText(account2.getLoan()+"");
                choiceBox.getItems().addAll("借贷","还贷");
                break;
            case "LoanCreditAccount":
                LoanCreditAccount account3=(LoanCreditAccount)account;
                ceilingText.setText(account3.getCeiling()+"");
                loanText.setText(account3.getLoan()+"");
                choiceBox.getItems().addAll("借贷","还贷");
                break;
        }

        Scene scene=new Scene(Panel);
        stage.setTitle("业务窗口");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();
    }
}
