package com.ATMServer;

import com.ATMServer.core.Bank;
import com.ATMServer.core.ServerThread;
import com.ATMServer.gui.ServerGuiCtr;
import com.accountType.Account;
import com.exceptionType.RegisterException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerStart extends Application{
    private static Stage stage;
    public static Bank bank=Bank.getBank();
    public static String clientInfo;
    public static boolean flag;
    public static ServerSocket server;
    public static long connectTimes;
    public static ListView<String> listView;
    public static TextField textField;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        initMainPanel();
    }

    public static void main(String[] args) throws IOException {
        launch(args);

    }
    public static void work() {
        Platform.runLater(()->textField.setText("运行中"));
        try {
            //服务端在20006端口监听客户端请求的TCP连接
            server = new ServerSocket(8888);  //1
            Socket client = null;
            bank.readData();
            //设置所有账户下线
            Collection<Account> col=bank.getAccounts().values();
            for(Account account:col){
                account.setIsOnline(false);
            }

            System.out.println(bank.getAccounts().toString());
            //通过调用Executors类的静态方法，创建一个ExecutorService实例
            //ExecutorService接口是Executor接口的子接口
            Executor service = Executors.newFixedThreadPool(50);
            while(flag){
                //等待客户端的连接
                try{
                    client = server.accept();   //运行到这里会线程阻塞  一直等待下一个客户端连接
                }catch (IOException e){
                    System.out.println("客户端连接失败");
                }

                if (flag){
                    clientInfo=client.toString();
                    connectTimes++;
                    //子线程更新UI线程的操作
                    Platform.runLater(()->listView.getItems().add(clientInfo));
                    System.out.println(client.toString()+"与客户端连接成功！");
                    System.out.println(bank.getAccounts().toString());
                    service.execute(new ServerThread(client));
                }else {
                    try {
                        client.close();
                    }catch (IOException e2) {
                        System.out.println("客户端断开连接失败");
                    }
                }

            }
            Platform.runLater(()->textField.setText("关闭"));
            connectTimes=0;
            server.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("创建客户端失败");
            Platform.runLater(()->textField.setText("错误"));
        }

    }
    public static void initMainPanel() throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(ServerStart.class.getResource("gui/ServerGui.fxml"));
        AnchorPane Panel=loader.load();
        Scene scene=new Scene(Panel);
        stage.setTitle("ATM终端");
        stage.setScene(scene);
        stage.resizableProperty().setValue(false);
        stage.show();

        ServerGuiCtr ctr=loader.getController();
        listView=ctr.getClientInfo();
        textField=ctr.getStatusTextField();
        textField.setText("关闭");
    }
    public static boolean registration(String password,String name, String idnum,
                                       String email, String adress, String accountType) throws RegisterException{
        if (bank.getAccounts().containsKey(name)){
            return false;
        }else {
            bank.getAccounts().put(name,bank.register(password,name,idnum,email,adress,accountType));
            bank.addAccount(bank.getAccounts().get(name).getId(),password,name,idnum,email,adress,accountType);
            return true;
        }
    }
    public  static int userLogin(String name, String password){
        if (!bank.getAccounts().containsKey(name)){
            return 1;
        }else if (bank.getAccounts().get(name).getPassword().equals(password)){
            return 2;
        }else return 3;
    }
}
