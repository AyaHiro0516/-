package com.ATMServer.core;

import com.ATMServer.ServerStart;
import com.accountType.TransObject;
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
                    if(!ServerStart.registration(object.getFromPassword(),object.getFromName(),object.getFromIdNum(),
                            object.getFromEmail(), object.getFromAdress(),object.getFromAccountType())){
                        object.setStatus("null");
                    }else {
                        object.setStatus("true");
                    }
                    oos.writeObject(object);
                    break;
                case "登录":
                    if (ServerStart.userLogin(object.getFromName(),object.getFromPassword())==1){
                        object.setStatus("null");
                        oos.writeObject(object);
                    }else if (ServerStart.userLogin(object.getFromName(),object.getFromPassword())==2){
                        object.setAccount(ServerStart.bank.getAccounts().get(object.getFromName())); //isOnline=false;
                        object.setStatus("true");
                        oos.writeObject(object);
                        ServerStart.bank.getAccounts().get(object.getFromName()).setIsOnline(true);
                    }else {
                        object.setStatus("false");
                        oos.writeObject(object);
                    }
                    break;
                case "业务":
                    String mode=object.getBusinessType();
                    try{
                        switch (mode){
                            case "存款":
                                ServerStart.bank.deposit(object.getFromName(),new Double(object.getAmount()));
                                ServerStart.bank.upDateBalance(object.getFromName(),new Double(object.getAmount()));
                                object.setStatus("true");
                                break;
                            case "取款":
                                ServerStart.bank.withdraw(object.getFromName(),new Double(object.getAmount()));
                                ServerStart.bank.upDateBalance(object.getFromName(),new Double("-"+object.getAmount()));
                                object.setStatus("true");
                                break;
                            case "转账":
                                if (ServerStart.bank.getAccounts().containsKey(object.getToName())   //先确认有没有接收账户
                                        && ServerStart.bank.getAccounts().get(object.getToName()).getIsOnline()){  //对方在线时不能转账
                                    object.setStatus("null");
                                } else {
                                    ServerStart.bank.transfer(object.getFromName(),object.getToName(),new Double(object.getAmount()));
                                    ServerStart.bank.upDateBalance(object.getFromName(),new Double("-"+object.getAmount()));
                                    ServerStart.bank.upDateBalance(object.getToName(),new Double(object.getAmount()));
                                    object.setStatus("true");
                                }
                                break;
                            case "借贷":
                                ServerStart.bank.requestLoan(object.getFromName(),new Double(object.getAmount()));
                                ServerStart.bank.upDateBalance(object.getFromName(),new Double(object.getAmount()));
                                ServerStart.bank.upDateLoan(object.getFromName(),new Double(object.getAmount()));
                                object.setStatus("true");
                                break;
                            case "还贷":
                                ServerStart.bank.payLoan(object.getFromName(),new Double(object.getAmount()));
                                ServerStart.bank.upDateBalance(object.getFromName(),new Double("-"+object.getAmount()));
                                ServerStart.bank.upDateLoan(object.getFromName(),new Double("-"+object.getAmount()));
                                object.setStatus("true");
                                break;
                        }
                        object.setAccount(ServerStart.bank.getAccounts().get(object.getFromName()));
                        object.setFromAccountType(ServerStart.bank.getAccounts().get(object.getFromName()).getAccountType());
                    }catch (ATMException atmE){
                        object.setStatus("null");
                    }finally {
                        oos.writeObject(object);
                    }
                    break;
                case "下线":
                    ServerStart.bank.getAccounts().get(object.getFromName()).setIsOnline(false);
                    object.setAccount(ServerStart.bank.getAccounts().get(object.getFromName()));
                    oos.writeObject(object);
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
