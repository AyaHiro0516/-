package com.dao;

import com.ATMServer.core.Bank;
import com.accountType.Account;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.TreeMap;

public class AccountDAOFileImpl implements AccountDAO{
    private static File idFile = null;
    private static File dataFile = null;
    private static URL url=AccountDAOFileImpl.class.getResource("/config/ATMSystem.xml");
    static { //加载配置文件
        try{
            SAXReader reader=new SAXReader();
            Document doc=reader.read(url);
            Element root=doc.getRootElement();
            Element elem=root.element("DAOFile");
            idFile=new File(elem.elementText("idFile"));
            dataFile=new File(elem.elementText("dataFile"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void upDate(){
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try{
            fos=new FileOutputStream(dataFile.toString());
            oos=new ObjectOutputStream(fos);
            oos.writeObject(Bank.getBank().getAccounts());
            Bank.getBank().setAccountsNum(Bank.getBank().getAccounts().size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                oos.close();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public long returnId() {
        long id=0;
        Writer w=null;
        BufferedWriter bw=null;
        try {
            Scanner fin=new Scanner(idFile);
            id=fin.nextLong();
            w=new FileWriter(idFile.toString());
            bw=new BufferedWriter(w);
            bw.write(id+1+"");
            bw.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bw.close();
                w.close();
            } catch (IOException E) {
                E.printStackTrace();
            }
        }
        return id;
    }

    @Override
    public void getAllAccount() {
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        try{
            fis=new FileInputStream(dataFile.toString());
            ois=new ObjectInputStream(fis);
            Bank.getBank().setAccounts((TreeMap<String,Account>)ois.readObject());
            Bank.getBank().setAccountsNum(Bank.getBank().getAccounts().size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                ois.close();
                fis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addAccount(long userId, String password, String name, String personId, String email, String adress, String acType) {
        upDate();
    }

    @Override
    public void upDateBalance(String name, double amount) {
        upDate();
    }

    @Override
    public void upDateCeiling(String name, double amount) {
        upDate();
    }

    @Override
    public void upDateLoan(String name, double amount) {
        upDate();
    }
}
