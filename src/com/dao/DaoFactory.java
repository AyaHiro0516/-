package com.dao;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;

public class DaoFactory {
    private static URL url=DaoFactory.class.getResource("/config/ATMSystem.xml");
    public static String getHost(){
        String host=null;
        try{
            SAXReader reader=new SAXReader();
            Document doc=reader.read(url);
            Element root=doc.getRootElement();
            Element elem=root.element("Socket");
            host=elem.elementText("host");
        }catch (Exception e){
            e.printStackTrace();
        }
        return host;
    }

    public static String getPort(){
        String port=null;
        try{
            SAXReader reader=new SAXReader();
            Document doc=reader.read(url);
            Element root=doc.getRootElement();
            Element elem=root.element("Socket");
            port=elem.elementText("port");
        }catch (Exception e){
            e.printStackTrace();
        }
        return port;
    }

    public static String getDAOType(){
        String type=null;
        try{
            SAXReader reader=new SAXReader();
            Document doc=reader.read(url);
            Element root=doc.getRootElement();
            Element elem=root.element("DAOType");
            type=elem.getText();
        }catch (Exception e){
            e.printStackTrace();
        }
        return type;
    }

    public static AccountDAO getAccountDAO(String type){
        switch (type){
            case "JDBCImpl":
                return new AccountDAOJDBCImpl();
            case "FileImpl":
                return new AccountDAOFileImpl();
            case "XMLImpl":
                return new AccountDAOXMLImpl();
            case "JSONImpl":
                return new AccountDAOJSONImpl();
            default:
                return null;
        }
    }

}