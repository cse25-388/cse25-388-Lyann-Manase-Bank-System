package model;
public class InvestmentAccount extends Account implements InterestPayable {
     
    private static final double MINIMUM_DEPOSIT = 500.00; //minimum deposit amount for investment accounts
      private static final double INTEREST_RATE = 0.05; //interest rate for investment accounts

    //constructor for new investment account objects
    public InvestmentAccount(String accountNumber, double initialDeposit, String branch) {
        super(accountNumber, validateInitialDeposit(initialDeposit), branch); //calls the constructor of the parent class Account
    }

    //static method to enforce the minimum deposit amount for investment accounts
    private static double validateInitialDeposit(double amount) {
        if (amount < MINIMUM_DEPOSIT) {
            //stops code execution and throws error message if deposit is less than the minimum required amount
            throw new IllegalArgumentException("Initial deposit must be at least P" + MINIMUM_DEPOSIT);
        }
        return amount; //returns the valid deposit amount if it meets the minimum requirement
    }
     @Override 
    public double calculateInterest() {
        return this.balance * INTEREST_RATE; //calculates interest based on the current balance and the interest rate
    }
    @Override 
    public void payInterest(){
        //computes interest of 5% on the current balance
        double Interest = this.balance * INTEREST_RATE;

        updateBalance(this.balance + Interest); //updates the account balance with the computed interest
    }
}
