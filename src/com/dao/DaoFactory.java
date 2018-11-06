package com.dao;

public class DaoFactory {
    public static AccountDAO getAccountDAO(String type){
        switch (type){
            case "JDBCImpl":
                return new AccountDAOJDBCImpl();
            case "FileImpl":
                return new AccountDAOFileImpl();
            default:
                return null;
        }
    }

}