package com.accountType;

import com.dao.AccountDAO;
import com.dao.AccountDAOFileImpl;
import com.dao.AccountDAOJDBCImpl;
import com.dao.DaoFactory;
import com.exceptionType.ATMException;

import java.io.*;
import java.util.Scanner;

abstract public class Account implements Serializable {
    private long id;
    private String password;
    private String name;
    private String personId;
    private String email;
    private String adress;
    private String accountType;
    private double balance;
    private boolean isOnline;
    private static AccountDAO dao= DaoFactory.getAccountDAO("JDBCImpl");

    private static long returnId() {
        if (dao instanceof AccountDAOFileImpl){
            return dao.returnId();
        }
        if (dao instanceof AccountDAOJDBCImpl){
            return dao.returnId();
        }
        return 0;
    }
    public Account() {
    }

    public Account(String password, String name, String personId, String email, double balance) {
        try{
            this.id = returnId();
            this.password = password;
            this.name = name;
            this.personId = personId;
            this.email = email;
            this.balance = balance;
            this.adress="null";
            this.isOnline=false;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public Account setAccountType(String accountType) {
        Account updateAC=this;
        updateAC.accountType=accountType;
        return updateAC;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    final public Account deposit(double amount){
        Account updateAC=this;
        updateAC.setBalance(this.balance+amount);
        return updateAC;
    }
    public abstract Account withdraw(double amount) throws ATMException;


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", personId='" + personId + '\'' +
                ", email='" + email + '\'' +
                ", adress='" + adress + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (Double.compare(account.balance, balance) != 0) return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (name != null ? !name.equals(account.name) : account.name != null) return false;
        if (personId != null ? !personId.equals(account.personId) : account.personId != null) return false;
        return email != null ? email.equals(account.email) : account.email == null;
    }

}