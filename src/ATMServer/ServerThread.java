package ATMServer;

import com.exceptionType.ATMException;

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
                    }else {
                        object.setFromName("false");
                        oos.writeObject(object);
                    }
                    break;
                case "业务":
                    String mode=object.getBusinessType();
                    try{
                        switch (mode){
                            case "存款": ATMServer.bank.deposit(object.getFromName(),new Double(object.getAmount()));
                                break;
                            case "取款": ATMServer.bank.withdraw(object.getFromName(),new Double(object.getAmount()));
                                break;
                            case "转账": ATMServer.bank.transfer(object.getFromName(),object.getToName(),new Double(object.getAmount()));
                                break;
                            case "借贷": ATMServer.bank.requestLoan(object.getFromName(),new Double(object.getAmount()));
                                break;
                            case "还贷": ATMServer.bank.payLoan(object.getFromName(),new Double(object.getAmount()));
                                break;
                        }
                        ATMServer.bank.upDate();
                        object.setAccount(ATMServer.bank.getAccounts().get(object.getFromName()));
                        object.setFromAccountType(ATMServer.bank.getAccounts().get(object.getFromName()).getAccountType());
                    }catch (ATMException atmE){
                        object.setFromName("null");
                    }
                    oos.writeObject(object);
                    break;
                case "下线":
                    ATMServer.bank.getAccounts().get(object.getFromName()).setIsOnline(false);
                    break;
            }

            ois.close();
            oos.close();
            client.close();
        }catch (Exception e){
            //do something
        }
    }
}
