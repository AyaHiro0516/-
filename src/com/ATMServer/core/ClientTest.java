package com.ATMServer.core;

import com.accountType.TransObject;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {  //完成注册功能

    public static void main(String[] args) throws IOException{
        Socket client=new Socket("127.0.0.1",20006);
        //获取Socket的输出流，用来发送数据到服务端
        ObjectOutputStream oos=new ObjectOutputStream(client.getOutputStream());
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
        Scanner in=new Scanner(System.in);
        System.out.println("请输入账号名");
        String name=in.next();
        System.out.println("请输入账号id");
        String idnum=in.next();
        TransObject object=new TransObject("注册");
        object.setFromName(name);
        object.setFromIdNum(idnum);
        object.setFromAccountType("SavingAccount");
        oos.writeObject(object);
        try{
            TransObject object1=(TransObject)ois.readObject();
            if (!object1.getStatus().equals("null")){
                System.out.println(object1.getAccount().toString());
            }else {
                System.out.println("账号已存在");
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
