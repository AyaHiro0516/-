package com.dao;

import com.accountType.Account;

import java.util.TreeMap;

public interface AccountDao {
    TreeMap<String,Account> getAllAccount() throws Exception;
    void addAccount(String password, String name, String personId, String email, String acType) throws Exception;
    void updateBalance(String name, double amount) throws Exception;
    void updateCeiling(String name, double amount) throws Exception;
    void updateLoan(String name, double amount) throws Exception;
}
