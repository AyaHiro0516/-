package com.dao;

import com.ATMServer.core.Bank;
import com.accountType.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;
import java.util.TreeMap;

public class AccountDAOJSONImpl implements AccountDAO{
    private static File idFile = null;
    private static File dataFile = null;
    private static URL url=AccountDAOJSONImpl.class.getResource("/config/ATMSystem.xml");
    static { //加载配置文件
        try{
            SAXReader reader=new SAXReader();
            Document doc=reader.read(url);
            Element root=doc.getRootElement();
            Element elem=root.element("DAOJSON");
            idFile=new File(elem.elementText("idFile"));
            dataFile=new File(elem.elementText("dataFile"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void upDate(){
        try {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            Gson gson = builder.create();
            FileWriter writer = new FileWriter(dataFile);
            writer.write(gson.toJson(Bank.getBank().getAccounts()));
            Bank.getBank().setAccountsNum(Bank.getBank().getAccounts().size());
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TreeMap<String,Object> getAccountMap(){
        TreeMap<String,Object> map=new TreeMap<>();
        try {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            Gson gson = builder.create();
            BufferedReader buf = new BufferedReader(new FileReader(dataFile));
            Type typeToken = new TypeToken< TreeMap<String,Object > >() {}.getType();
            map=gson.fromJson(buf, typeToken);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public long returnId() {
        Long id=new Long("0");
        try {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            Gson gson = builder.create();
            BufferedReader buf = new BufferedReader(new FileReader(idFile));
            id=gson.fromJson(buf,Long.class);
            FileWriter writer = new FileWriter(idFile);
            writer.write(gson.toJson(id+1));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void getAllAccount() {
        try {
            BufferedReader buf = new BufferedReader(new FileReader(dataFile));
            TreeMap<String,Object> map=getAccountMap();          //从json里读取的Map
            TreeMap<String,Account> accountMap=new TreeMap<>();  //要赋值给bank的Map

            JsonElement root=new JsonParser().parse(buf);  //获取根节点
            JsonObject details = root.getAsJsonObject();   //获取Account节点
            Set<String> keys=map.keySet();
            for (String userName:keys) {
                JsonElement accInfo = details.get(userName);
                JsonObject infoField=accInfo.getAsJsonObject(); //获取Account里的属性节点

                long userId=infoField.get("id").getAsLong();
                String passWord=infoField.get("password").getAsString();
                String name=infoField.get("name").getAsString();
                String personId=infoField.get("personId").getAsString();
                String email=infoField.get("email").getAsString();
                String adress=infoField.get("adress").getAsString();
                double balance=infoField.get("balance").getAsDouble();
                String accountType=infoField.get("accountType").getAsString();
                switch (accountType){
                    case "SavingAccount":
                        Account savingacc=new SavingAccount().setAccountType(accountType);
                        savingacc.setId(userId);
                        savingacc.setPassword(passWord);
                        savingacc.setName(name);
                        savingacc.setPersonId(personId);
                        savingacc.setEmail(email);
                        savingacc.setAdress(adress);
                        savingacc.setBalance(balance);
                        accountMap.put(name,savingacc);
                        break;
                    case "CreditAccount":
                        Account creditacc=new CreditAccount().setAccountType(accountType);
                        creditacc.setId(userId);
                        creditacc.setPassword(passWord);
                        creditacc.setName(name);
                        creditacc.setPersonId(personId);
                        creditacc.setEmail(email);
                        creditacc.setAdress(adress);
                        creditacc.setBalance(balance);
                        ((CreditAccount)creditacc).setCeiling(infoField.get("ceiling").getAsDouble());
                        accountMap.put(name,creditacc);
                        break;
                    case "LoanSavingAccount":
                        Account lsavingacc=new LoanSavingAccount().setAccountType(accountType);
                        lsavingacc.setId(userId);
                        lsavingacc.setPassword(passWord);
                        lsavingacc.setName(name);
                        lsavingacc.setPersonId(personId);
                        lsavingacc.setEmail(email);
                        lsavingacc.setAdress(adress);
                        lsavingacc.setBalance(balance);
                        ((LoanSavingAccount)lsavingacc).setLoan(infoField.get("loan").getAsDouble());
                        accountMap.put(name,lsavingacc);
                        break;
                    case "LoanCreditAccount":
                        Account lcreditacc=new LoanCreditAccount().setAccountType(accountType);
                        lcreditacc.setId(userId);
                        lcreditacc.setPassword(passWord);
                        lcreditacc.setName(name);
                        lcreditacc.setPersonId(personId);
                        lcreditacc.setEmail(email);
                        lcreditacc.setAdress(adress);
                        lcreditacc.setBalance(balance);
                        ((LoanCreditAccount)lcreditacc).setCeiling(infoField.get("ceiling").getAsDouble());
                        ((LoanCreditAccount)lcreditacc).setLoan(infoField.get("loan").getAsDouble());
                        accountMap.put(name,lcreditacc);
                        break;
                }
            }
            Bank.getBank().setAccounts(accountMap);
            Bank.getBank().setAccountsNum(Bank.getBank().getAccounts().size());
        }catch (Exception e){
            e.printStackTrace();
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
