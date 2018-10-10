package com.accountType;

import com.exceptionType.ATMException;
import com.exceptionType.BalanceNotEnoughException;
import com.exceptionType.LoanException;

public class LoanSavingAccount extends SavingAccount implements Loanable{
    private double loan;

    public void setLoan(double loan) throws ATMException{
        if(loan<0){
            throw new LoanException("贷款额不能为负值，设置失败。");
        }
        else this.loan = loan;
    }

    public LoanSavingAccount() {
        super();
    }

    public LoanSavingAccount(String password, String name, String personId, String email, double balance) {
        super(password, name, personId, email, balance);
    }

    @Override
    public void requestLoan(double money) {
        this.loan+=money;
        this.setBalance(this.getBalance()+money);
    }

    @Override
    public void payLoan(double money) throws ATMException{
        if (this.getBalance()>=money){
            if (this.loan>=money){
                this.loan-=money;
                this.setBalance(this.getBalance()-money);
            }else throw new LoanException("还款额大于贷款额，还贷失败。");
        }else throw new BalanceNotEnoughException("余额不足，还贷失败。");
    }

    @Override
    public double getLoan() {
        return this.loan;
    }

    @Override
    public String toString() {
        return "LoanSavingAccount{ " +super.toString()+
                ", loan=" + loan +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        LoanSavingAccount that = (LoanSavingAccount) o;

        return Double.compare(that.loan, loan) == 0;
    }

}
