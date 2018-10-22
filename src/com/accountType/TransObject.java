package com.accountType;

import com.accountType.Account;

import java.io.Serializable;

public class TransObject implements Serializable{  //打包类
    private String RequestType;
    private String BusinessType;
    private String fromAccountType;
    private String fromName;
    private String fromIdNum;
    private String fromPassword;
    private String fromEmail;
    private String fromAdress;
    private String toName;
    private String amount;
    private String status;
    private Account account=null;
    public TransObject() {
    }

    public TransObject(String requestType) {
        RequestType = requestType;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromIdNum() {
        return fromIdNum;
    }

    public void setFromIdNum(String fromIdNum) {
        this.fromIdNum = fromIdNum;
    }

    public String getFromPassword() {
        return fromPassword;
    }

    public void setFromPassword(String fromPassword) {
        this.fromPassword = fromPassword;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromAccountType() {
        return fromAccountType;
    }

    public void setFromAccountType(String fromAccountType) {
        this.fromAccountType = fromAccountType;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromAdress() {
        return fromAdress;
    }

    public void setFromAdress(String fromAdress) {
        this.fromAdress = fromAdress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
