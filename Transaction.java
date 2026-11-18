package model;

import java.util.Date; 

public class Transaction {
    private int transactionID;
    private double amount;
    private String type;
    private Date date;

    //constructor used to create new trasaction objects
    public Transaction(int transactionID, double amount, String type){
        this.transactionID = transactionID;
        this.amount = amount;
        this.type = type;
    }
    
    public Transaction(double amount2, String type2) {
        //TODO Auto-generated constructor stub
    }

    public void recordTransaction(){
        System.out.println("Transaction ID: " + transactionID + "\nAmount: BWP " + amount + "\nType: " + type + "\nDate: " + date);
    }

    //getter methods
    public int getTransactionID(){
        return transactionID;
    }

    public double getAmount(){
        return amount;
    }

    public String getType(){
        return type;
    }

    public Date getDate(){
        return date;
    }
}
