package model;
public class SavingsAccount extends Account implements InterestPayable {

    private static final double INTEREST_RATE = 0.0005; //interest rate for savings account

    
    //constructor for new savings account objects
    public SavingsAccount(String accountNumber, double initialDeposit, String branch) {
        super(accountNumber, initialDeposit, branch); //calls the constructor of the parent class Account
        
    }

    @Override 
    public double calculateInterest(){
        return this.balance * INTEREST_RATE; //calculates interest based on the current balance and the interest rate
            }

            @Override
    public void payInterest(){
        double interest = calculateInterest(); //calculates interest based on the current balance and the interest rate
        this.balance += interest; //updates the account balance with the computed interest
        recordTransaction(interest, "Interest Paid"); // creates a record for interest payments made
    }
    @Override
    public boolean withdraw(double amount){
        //logs an error message to the user explaining that withdrawals are not allowed for savings accounts
        System.out.println("Withdrawals are not allowed for savings accounts.");
       
        //blocks the withdrawal operation and returns a failed operation to the system
        return false;

    }
}