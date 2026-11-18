package model;

import java.util.ArrayList;
import java.util.List;


public class Customer{
    private int customerId;
    private String name;
    private String address;
    private List <Account> accounts;

    //constructor
    public Customer(int customerId, String name, String address){
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.accounts = new ArrayList<>();
    }
 
    //method link account to the customer
    public void addAccount(Account account){
        accounts.add(account);
    }

    //getter methods
public int getCustomerId(){
        return customerId;
    }
    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public List<Account> getAccounts(){
        return accounts;
    }
}