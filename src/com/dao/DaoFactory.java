package com.dao;

public class DaoFactory {
    public static AccountDao getAccountDao_mysql(){
        return new AccountDao_mysql();
    }
}
