package com.dao;

import com.accountType.Account;

import java.util.TreeMap;

public class AccountDao_mysql implements AccountDao {
    @Override
    public TreeMap<String, Account> getAllAccount() throws Exception {
        return null;
    }

    @Override
    public void addAccount(String password, String name, String personId, String email, String acType) throws Exception {

    }

    @Override
    public void updateBalance(String name, double amount) throws Exception {

    }

    @Override
    public void updateCeiling(String name, double amount) throws Exception {

    }

    @Override
    public void updateLoan(String name, double amount) throws Exception {

    }
}
