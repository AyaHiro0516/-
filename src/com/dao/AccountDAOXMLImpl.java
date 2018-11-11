package com.dao;

import com.ATMServer.core.Bank;
import com.accountType.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.TreeMap;

public class AccountDAOXMLImpl implements AccountDAO {
    private static String xmlpos="F:\\IntelliJ IDEA Projects\\OOP\\ATMSystem\\config\\AccountData.xml";
    public void writeXML(Document doc){
        try {
            FileOutputStream out = new FileOutputStream(xmlpos);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            XMLWriter writer = new XMLWriter(out,format);
            writer.write(doc);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public long returnId() {
        long id=0;
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(new File(xmlpos));
            Element root=doc.getRootElement();
            Element elem=root.element("usableId");
            id=new Long(elem.getText());
            elem.setText(id+1+"");
            writeXML(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void getAllAccount() {
        TreeMap<String,Account> map=new TreeMap<>();
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(new File(xmlpos));
            Element root=doc.getRootElement();
            List<Element> savingacc=root.element("SavingAccount").elements("Account");
            for(Element e :savingacc){
                Account account=new SavingAccount().setAccountType("SavingAccount");
                long userId=new Long(e.elementText("userId"));        account.setId(userId);
                String passWord=e.elementText("passWord");            account.setPassword(passWord);
                String name=e.elementText("userName");                account.setName(name);
                String personId=e.elementText("personId");            account.setPersonId(personId);
                String email=e.elementText("email");                  account.setEmail(email);
                String adress=e.elementText("adress");                account.setAdress(adress);
                double balance=new Double(e.elementText("balance"));  account.setBalance(balance);
                map.put(name,account);
            }

            List<Element> creditacc=root.element("CreditAccount").elements("Account");
            for(Element e :creditacc){
                Account account=new CreditAccount().setAccountType("CreditAccount");
                long userId=new Long(e.elementText("userId"));        account.setId(userId);
                String passWord=e.elementText("passWord");            account.setPassword(passWord);
                String name=e.elementText("userName");                account.setName(name);
                String personId=e.elementText("personId");            account.setPersonId(personId);
                String email=e.elementText("email");                  account.setEmail(email);
                String adress=e.elementText("adress");                account.setAdress(adress);
                double balance=new Double(e.elementText("balance"));  account.setBalance(balance);
                double ceiling=new Double(e.elementText("ceiling"));  ((CreditAccount)account).setCeiling(ceiling);
                map.put(name,account);
            }

            List<Element> lsavingacc=root.element("LoanSavingAccount").elements("Account");
            for(Element e :lsavingacc){
                Account account=new LoanSavingAccount().setAccountType("LoanSavingAccount");
                long userId=new Long(e.elementText("userId"));        account.setId(userId);
                String passWord=e.elementText("passWord");            account.setPassword(passWord);
                String name=e.elementText("userName");                account.setName(name);
                String personId=e.elementText("personId");            account.setPersonId(personId);
                String email=e.elementText("email");                  account.setEmail(email);
                String adress=e.elementText("adress");                account.setAdress(adress);
                double balance=new Double(e.elementText("balance"));  account.setBalance(balance);
                double loan=new Double(e.elementText("loan"));        ((LoanSavingAccount)account).setLoan(loan);
                map.put(name,account);
            }

            List<Element> lcreditacc=root.element("LoanCreditAccount").elements("Account");
            for(Element e :lcreditacc){
                Account account=new LoanCreditAccount().setAccountType("LoanCreditAccount");
                long userId=new Long(e.elementText("userId"));        account.setId(userId);
                String passWord=e.elementText("passWord");            account.setPassword(passWord);
                String name=e.elementText("userName");                account.setName(name);
                String personId=e.elementText("personId");            account.setPersonId(personId);
                String email=e.elementText("email");                  account.setEmail(email);
                String adress=e.elementText("adress");                account.setAdress(adress);
                double balance=new Double(e.elementText("balance"));  account.setBalance(balance);
                double ceiling=new Double(e.elementText("ceiling"));  ((LoanCreditAccount)account).setCeiling(ceiling);
                double loan=new Double(e.elementText("loan"));        ((LoanCreditAccount)account).setLoan(loan);
                map.put(name,account);
            }
            Bank.getBank().setAccounts(map);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void addAccount(long userId, String password, String name, String personId, String email, String adress, String acType) {
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(new File(xmlpos));
            Element root=doc.getRootElement();
            Element elem=root.element(acType).addElement("Account");
            elem.addAttribute("name",name);
            elem.addElement("userId").setText(userId+"");
            elem.addElement("passWord").setText(password);
            elem.addElement("userName").setText(name);
            elem.addElement("personId").setText(personId);
            elem.addElement("email").setText(email);
            elem.addElement("adress").setText(adress);
            elem.addElement("balance").setText("0");
            switch (acType){
                case "CreditAccount":
                    elem.addElement("ceiling").setText("0");
                    break;
                case "LoanSavingAccount":
                    elem.addElement("loan").setText("0");
                    break;
                case "LoanCreditAccount":
                    elem.addElement("ceiling").setText("0");
                    elem.addElement("loan").setText("0");
                    break;
            }
            writeXML(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void upDateBalance(String name, double amount) {
        String acType= Bank.getBank().getAccounts().get(name).getAccountType();
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(new File(xmlpos));
            Element root=doc.getRootElement();
            Element elem=root.element(acType).element("Account");
            double balance=new Double(elem.elementText("balance"));
            elem.element("balance").setText(amount+balance+"");
            writeXML(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void upDateCeiling(String name, double amount) {
        String acType= Bank.getBank().getAccounts().get(name).getAccountType();
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(new File(xmlpos));
            Element root=doc.getRootElement();
            Element elem=root.element(acType).element("Account");
            double ceiling=new Double(elem.elementText("ceiling"));
            elem.element("ceiling").setText(amount+ceiling+"");
            writeXML(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void upDateLoan(String name, double amount) {
        String acType= Bank.getBank().getAccounts().get(name).getAccountType();
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(new File(xmlpos));
            Element root=doc.getRootElement();
            Element elem=root.element(acType).element("Account");
            double loan=new Double(elem.elementText("loan"));
            elem.element("loan").setText(amount+loan+"");
            writeXML(doc);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
