package com.dao;

public interface AccountDAO {
    long returnId();
    void getAllAccount();
    void addAccount(long userId, String password, String name, String personId, String email, String adress, String acType);
    void upDateBalance(String name, double amount);
    void upDateCeiling(String name, double amount);
    void upDateLoan(String name, double amount);
}
