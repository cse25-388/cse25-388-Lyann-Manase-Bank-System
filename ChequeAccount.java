package model;
public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;

    //constructor for new cheque account objects with employer details
    public ChequeAccount(String accountNumber, double initialDeposit, String companyName, String companyAddress, String branch) {
        super(accountNumber, initialDeposit, branch); //calls the constructor of the parent class Account
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    @Override 
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            this.balance -= amount; //decreases the account's balance by the withdrawn amount
         recordTransaction(amount, "Withdrawal"); // creates a record for withdrawals made
            return true; //successful account balance change
        }
        return false; //rejects the command and returns a failed operation to the system
    }

    //getter methods for company details
    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }
}
