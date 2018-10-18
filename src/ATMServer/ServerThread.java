package ATMServer;

import com.accountType.Account;

import java.io.*;

import java.net.Socket;

public class ServerThread implements Runnable{
    private Socket client=null;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //获取Socket的输出流，用来发送数据到服务端
            ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
            //获取Socket的输入流，用来接收从服务端发送过来的数据
            ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
            TransObject object=(TransObject)ois.readObject();
            String requestType=object.getRequestType();
            switch (requestType){
                case "注册":
                    if(!ATMServer.registration(object.getFromPassword(),object.getFromName(),object.getFromIdNum(),
                            object.getFromEmail(),object.getFromAccountType())){
                        object.setFromName("null");
                    }
                    oos.writeObject(object);
                    break;
                case "登录":
                    if (ATMServer.userLogin(object.getFromName(),object.getFromPassword())==1){
                        object.setFromName("null");
                        oos.writeObject(object);
                    }else if (ATMServer.userLogin(object.getFromName(),object.getFromPassword())==2){
                        String name=object.getFromName();
                        object.setAccount(ATMServer.bank.login(name,object.getFromPassword())); //isOnline=false;
                        object.setFromName("true");
                        oos.writeObject(object);

                        ATMServer.bank.getAccounts().get(name).setIsOnline(true);
                        ATMServer.bank.upDate();  //更新账户的登录状态
                    }else {
                        object.setFromName("false");
                        oos.writeObject(object);
                    }
                    break;
                case "业务":
                    break;
                case "下线":
                    break;
            }

            ois.close();
            oos.close();
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
