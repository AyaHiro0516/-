package com.dao;

import com.ATMServer.ServerStart;
import com.accountType.Account;

import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;

public class AccountDAOFileImpl implements AccountDAO{
    public final File idFile=new File("F:/test/id.txt");
    public final File dataFile=new File("F:/test/data.txt");

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
            ServerStart.bank.setAccounts((TreeMap<String,Account>)ois.readObject());
            ServerStart.bank.setAccountsNum(ServerStart.bank.getAccounts().size());
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
    public void upDate(){
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try{
            fos=new FileOutputStream(dataFile.toString());
            oos=new ObjectOutputStream(fos);
            oos.writeObject(ServerStart.bank.getAccounts());
            ServerStart.bank.setAccountsNum(ServerStart.bank.getAccounts().size());
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
