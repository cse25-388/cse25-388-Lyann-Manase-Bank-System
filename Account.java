package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account{
    private String accountNumber;
    protected double balance;
    private String branch;
    private List <Transaction> transactionHistory;

    //constructor
    public Account (String accountNumber, double initialDeposit , String branch){
        this.accountNumber = accountNumber;
        this.balance = initialDeposit;
        this.branch = branch;
        this.transactionHistory = new ArrayList<>();
        recordTransaction(initialDeposit, "Initial Deposit"); //records the account creation deposit 
    }

    public void deposit(double amount){
        if (amount > 0){
            this.balance += amount; //increases the account's balance by the deposited amount
            recordTransaction(amount, "Deposit"); // creates deposit transaction record
        } 
    }

    public void deposit(double amount, String type){
        if (amount > 0){
            this.balance += amount; //increases the account's balance by the deposited amount
            recordTransaction(amount, type); // creates deposit transaction record
        } 
    }

    public void updateBalance( double amount){
        this.balance = amount; //increases the account's balance by the deposited amount
        recordTransaction(amount, "Deposit"); // creates deposit transaction record
    }
   
    public boolean withdraw(double amount){
        if (amount > 0 && balance >= amount){
        this.balance = amount; 
        recordTransaction(amount, "Withdrawal");// creates a record for withdrawals made
        return true; //successful account balance change
        }
        return false; //rejects the command and returns a failed operation to the system
    }

    //method to create transaction objects
    protected void recordTransaction(double amount, String type){
        Transaction tc = new Transaction(amount, type);// new transaction object
        transactionHistory.add(tc); // adds the transaction object into the history log list
    }

    public String getAccountNumber(){ // gets unique text account numbers
        return accountNumber;
    }
    
    public double getBalance(){
        return balance;
    }

    public String getBranch(){
        return branch;
    }

    public List <Transaction> getTransactionHistory(){
        return transactionHistory;
    }
}