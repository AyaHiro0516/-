package com.dao;

import com.ATMServer.core.Bank;
import com.accountType.*;

import java.sql.*;
import java.util.TreeMap;

public class AccountDAOJDBCImpl implements AccountDAO {
    static final String DB_URL = "jdbc:mysql://localhost:3306/setraining?useSSL=false";
    static final String driver = "com.mysql.jdbc.Driver";
    static final String USER = "root";
    static final String PASS = "bxy0516";
    static Connection conn=null;
    static PreparedStatement ps=null;
    static ResultSet rs=null;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }
    public static void closeAll(){
        try {
            rs.close();
            ps.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public long returnId() {
        long id=0;
        try {
            Class.forName(driver);
            conn=getConnection();
            ps=conn.prepareStatement("SELECT userId FROM usableId");
            rs=ps.executeQuery();
            while (rs.next()){
                id=rs.getLong("userId");
            }
            ps=conn.prepareStatement("UPDATE usableId SET userId=?");
            ps.setObject(1,id+1);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
        return id;
    }

    @Override
    public void getAllAccount() {
        TreeMap<String,Account> map=new TreeMap<>();
        try {
            Class.forName(driver);
            conn=getConnection();
            ps=conn.prepareStatement("SELECT * FROM SavingAccount");
            rs=ps.executeQuery();
            while (rs.next()){
                Account account=new SavingAccount().setAccountType("SavingAccount");
                long userId=rs.getLong("userId");           account.setId(userId);
                String passWord=rs.getString("passWord");   account.setPassword(passWord);
                String name=rs.getString("userName");           account.setName(name);
                String personId=rs.getString("personId");   account.setPersonId(personId);
                String email=rs.getString("email");         account.setEmail(email);
                String adress=rs.getString("adress");       account.setAdress(adress);
                double balance=rs.getDouble("balance");     account.setBalance(balance);
                map.put(name,account);
            }

            ps=conn.prepareStatement("SELECT * FROM CreditAccount");
            rs=ps.executeQuery();
            while (rs.next()){
                Account account=new CreditAccount().setAccountType("CreditAccount");
                long userId=rs.getLong("userId");           account.setId(userId);
                String passWord=rs.getString("passWord");   account.setPassword(passWord);
                String name=rs.getString("userName");           account.setName(name);
                String personId=rs.getString("personId");   account.setPersonId(personId);
                String email=rs.getString("email");         account.setEmail(email);
                String adress=rs.getString("adress");       account.setAdress(adress);
                double balance=rs.getDouble("balance");     account.setBalance(balance);
                double ceiling=rs.getDouble("ceiling");     ((CreditAccount)account).setCeiling(ceiling);
                map.put(name,account);
            }

            ps=conn.prepareStatement("SELECT * FROM LoanSavingAccount");
            rs=ps.executeQuery();
            while (rs.next()){
                Account account=new LoanSavingAccount().setAccountType("LoanSavingAccount");
                long userId=rs.getLong("userId");           account.setId(userId);
                String passWord=rs.getString("passWord");   account.setPassword(passWord);
                String name=rs.getString("userName");           account.setName(name);
                String personId=rs.getString("personId");   account.setPersonId(personId);
                String email=rs.getString("email");         account.setEmail(email);
                String adress=rs.getString("adress");       account.setAdress(adress);
                double balance=rs.getDouble("balance");     account.setBalance(balance);
                double loan=rs.getDouble("loan");           ((LoanSavingAccount)account).setLoan(loan);
                map.put(name,account);
            }

            ps=conn.prepareStatement("SELECT * FROM LoanCreditAccount");
            rs=ps.executeQuery();
            while (rs.next()){
                Account account=new LoanCreditAccount().setAccountType("LoanCreditAccount");
                long userId=rs.getLong("userId");           account.setId(userId);
                String passWord=rs.getString("passWord");   account.setPassword(passWord);
                String name=rs.getString("userName");           account.setName(name);
                String personId=rs.getString("personId");   account.setPersonId(personId);
                String email=rs.getString("email");         account.setEmail(email);
                String adress=rs.getString("adress");       account.setAdress(adress);
                double balance=rs.getDouble("balance");     account.setBalance(balance);
                double ceiling=rs.getDouble("ceiling");     ((LoanCreditAccount)account).setCeiling(ceiling);
                double loan=rs.getDouble("loan");           ((LoanCreditAccount)account).setLoan(loan);
                map.put(name,account);
            }

            Bank.getBank().setAccounts(map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }

    }
    @Override
    public void addAccount(long userId, String password, String name, String personId, String email, String adress, String acType) {
        try {
            Class.forName(driver);
            conn=getConnection();
            String sql="INSERT INTO "+acType+" (userId,passWord,userName,personId,email,adress,balance) VALUES (?,?,?,?,?,?,?)";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,userId);
            ps.setObject(2,password);
            ps.setObject(3,name);
            ps.setObject(4,personId);
            ps.setObject(5,email);
            ps.setObject(6,adress);
            ps.setObject(7,0);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }
    @Override
    public void upDateBalance(String name, double amount) {
        String acType=Bank.getBank().getAccounts().get(name).getAccountType();  //可优化
        try {
            Class.forName(driver);
            conn=getConnection();
            String sql="SELECT balance FROM "+acType+" WHERE userName=?";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,name);
            rs=ps.executeQuery();
            double balance=0;
            while (rs.next()){
                balance=rs.getDouble("balance");
            }
            sql="UPDATE "+acType+" SET balance=? WHERE userName=?";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,balance+amount);
            ps.setObject(2,name);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }
    @Override
    public void upDateCeiling(String name, double amount) {
        String acType=Bank.getBank().getAccounts().get(name).getAccountType();
        try {
            Class.forName(driver);
            conn=getConnection();
            String sql="SELECT ceiling FROM "+acType+" WHERE userName=?";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,name);
            rs=ps.executeQuery();
            double ceiling=0;
            while (rs.next()){
                ceiling=rs.getDouble("ceiling");
            }
            sql="UPDATE "+acType+" SET ceiling=? WHERE userName=?";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,ceiling+amount);
            ps.setObject(2,name);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }
    @Override
    public void upDateLoan(String name, double amount) {
        String acType=Bank.getBank().getAccounts().get(name).getAccountType();
        try {
            Class.forName(driver);
            conn=getConnection();
            String sql="SELECT loan FROM "+acType+" WHERE userName=?";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,name);
            rs=ps.executeQuery();
            double loan=0;
            while (rs.next()){
                loan=rs.getDouble("loan");
            }
            sql="UPDATE "+acType+" SET loan=? WHERE userName=?";
            ps=conn.prepareStatement(sql);
            ps.setObject(1,loan+amount);
            ps.setObject(2,name);
            ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeAll();
        }
    }
}
