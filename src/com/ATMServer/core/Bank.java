package com.ATMServer.core;

import com.accountType.*;
import com.exceptionType.*;


import java.io.*;
import java.util.Collection;
import java.util.TreeMap;

public class Bank{
    public static File dataFile=new File("F:/test/data.txt");
    private static Bank bank;
    private Bank(){
        this.accounts=new TreeMap<>();
        this.accountsNum=this.accounts.size();
    }
    public static Bank getBank(){
        if(bank==null){
            synchronized (Bank.class){
                if (bank==null){   //DCL双重检查锁机制
                    bank=new Bank();
                }
            }
        }
        return bank;
    }
    private TreeMap<String,Account> accounts; //DAO
    private int accountsNum;

    public TreeMap<String, Account> getAccounts() {
        return accounts;
    }

    public int getAccountsNum() {
        return accountsNum;
    }

    public void upDate(){   //DAO
        FileOutputStream fos=null;
        ObjectOutputStream oos=null;
        try{
            fos=new FileOutputStream(dataFile.toString());
            oos=new ObjectOutputStream(fos);
            oos.writeObject(this.accounts);
            this.accountsNum=this.accounts.size();
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

    public void readData(){  //DAO
        FileInputStream fis=null;
        ObjectInputStream ois=null;
        try{
            fis=new FileInputStream(dataFile.toString());
            ois=new ObjectInputStream(fis);
            this.accounts=(TreeMap<String,Account>) ois.readObject();
            this.accountsNum=this.accounts.size();
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

    public Account register(String password, String name, String personId, String email, String acType) throws RegisterException{
        switch (acType){
            case "SavingAccount":
                accountsNum++;
                return new SavingAccount(password, name, personId, email, 0).setAccountType("SavingAccount");
            case "CreditAccount":
                accountsNum++;
                return new CreditAccount(password, name, personId, email, 0).setAccountType("CreditAccount");
            case "LoanSavingAccount":
                accountsNum++;
                return new LoanSavingAccount(password, name, personId, email, 0).setAccountType("LoanSavingAccount");
            case "LoanCreditAccount":
                accountsNum++;
                return new LoanCreditAccount(password, name, personId, email, 0).setAccountType("LoanCreditAccount");
            default: throw new RegisterException("开户失败，未知账户类型。");
        }
    }

    public Account login(String name, String password) throws ATMException{  //DAO
        if (accounts.containsKey(name)){
            if (accounts.get(name).getPassword().equals(password)){
                return accounts.get(name);
            }else throw new LoginException("密码错误，登录失败。");
        }
        else throw new AccountNotFoundException("无该账户信息，登录失败。");
    }

    public Account deposit(String name, double amount) throws ATMException{  //DAO
        if (accounts.containsKey(name)){
            return accounts.get(name).deposit(amount);
        }
        else throw new AccountNotFoundException("无该账户信息，存款失败。");
    }

    public Account withdraw(String name, double amount) throws ATMException{  //DAO
        if (accounts.containsKey(name)){
            return accounts.get(name).withdraw(amount);
        }
        throw new AccountNotFoundException("无该账户信息，取款失败。");
    }
    public Account setCeiling(String name, double newceiling) throws ATMException{  //DAO
        if (accounts.containsKey(name)){
            if (accounts.get(name) instanceof CreditAccount){
                ((CreditAccount) accounts.get(name)).setCeiling(newceiling);
                return accounts.get(name);
            }
            if (accounts.get(name) instanceof LoanCreditAccount){
                ((LoanCreditAccount) accounts.get(name)).setCeiling(newceiling);
                return accounts.get(name);
            }
            throw new AccountNotFoundException("账户类型不匹配，设置失败。");
        }
        else throw new AccountNotFoundException("无该账户信息，设置失败。");
    }

    public boolean transfer(String from, String to, double amount) throws ATMException{  //DAO
        if (accounts.containsKey(from) && accounts.containsKey(to)){
            Account acFrom=accounts.get(from);
            Account acTo=accounts.get(to);
            if (acFrom.getBalance()>=amount){
                acFrom.setBalance(acFrom.getBalance()-amount);
                acTo.setBalance(acTo.getBalance()+amount);
                return true;
            }else {
                throw new BalanceNotEnoughException("转账者余额不足。");
            }
        }else if(!accounts.containsKey(from)){
            throw new AccountNotFoundException("无转出账户信息。");
        }else {
            throw new AccountNotFoundException("无转入账户信息。");
        }
    }

    public Account requestLoan(String name, double amount) throws ATMException{  //DAO
        if (accounts.containsKey(name)){
            if (accounts.get(name) instanceof LoanCreditAccount){
                ((LoanCreditAccount) accounts.get(name)).requestLoan(amount);
                return accounts.get(name);
            }
            if (accounts.get(name) instanceof LoanSavingAccount){
                ((LoanSavingAccount) accounts.get(name)).requestLoan(amount);
                return accounts.get(name);
            }
            throw new AccountNotFoundException("账户类型不匹配，贷款失败。");
        }
        else throw new AccountNotFoundException("无该账户信息，贷款失败。");
    }

    public Account payLoan(String name, double amount) throws ATMException{  //DAO
        if (accounts.containsKey(name)){
            if (accounts.get(name) instanceof LoanCreditAccount){
                ((LoanCreditAccount) accounts.get(name)).payLoan(amount);
                return accounts.get(name);
            }
            if (accounts.get(name) instanceof LoanSavingAccount){
                ((LoanSavingAccount) accounts.get(name)).payLoan(amount);
                return accounts.get(name);
            }
            throw new AccountNotFoundException("账户类型不匹配，还贷失败。");
        }
        else throw new AccountNotFoundException("无该账户信息，还贷失败。");
    }

    public double getSumOfBalance(){
        double sum=0;
        Collection<Account> col=accounts.values();
        for(Account account:col){
            sum+=account.getBalance();
        }
        return sum;
    }

    public double getSumOfCeiling(){
        double sum=0;
        Collection<Account> col=accounts.values();
        for(Account account:col){
            if (account instanceof CreditAccount){
                sum+=((CreditAccount) account).getCeiling();
            }
            if (account instanceof LoanCreditAccount){
                sum+=((LoanCreditAccount) account).getCeiling();
            }
        }
        return sum;
    }

    public double getSumOfLoan(){
        double sum=0;
        Collection<Account> col=accounts.values();
        for(Account account:col){
            if (account instanceof LoanCreditAccount){
                sum+=((LoanCreditAccount) account).getLoan();
            }
            if (account instanceof LoanSavingAccount){
                sum+=((LoanSavingAccount) account).getLoan();
            }
        }
        return sum;
    }

}
