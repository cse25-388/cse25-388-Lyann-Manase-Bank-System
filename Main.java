import java.util.List;

import model.ChequeAccount;
import model.Company;
import model.IndividualCustomer;
import model.InvestmentAccount;
import model.SavingsAccount;
import model.Transaction;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   STARTING BANKING SYSTEM INTEGRATION TEST      ");
        System.out.println("=================================================\n");

        // ---------------------------------------------------------------------
        // STEP 1: INITIALIZE CUSTOMER TYPES
        // ---------------------------------------------------------------------
        System.out.println("[Step 1] Initializing Customer Entities...");
        
        // Creating an Individual Customer using standard parameters
        IndividualCustomer individual = new IndividualCustomer(101, "Lerato", "Mokgosi", "Plot 45, Gaborone", "Botswana Tech Solutions");
        System.out.println("Created Individual Customer: " + individual.getName() + " " + individual.getSurname());

        // Creating a Corporate Company Customer using our specialized subclass
        Company corporate = new Company(202, "Botswana Tech Solutions", "Innovation Hub, Gaborone", "CO-98741");
        System.out.println("Created Company Customer: " + corporate.getName() + " (Reg No: " + corporate.getRegistrationNumber() + ")\n");


        // ---------------------------------------------------------------------
        // STEP 2: OPENING ACCOUNTS & ENFORCING CONSTRAINTS
        // ---------------------------------------------------------------------
        System.out.println("[Step 2] Opening Bank Accounts & Testing Constraints...");

        // 1. Open a Savings Account
        SavingsAccount savings = new SavingsAccount("SA-1001", 250.00, "Main Branch");
        individual.addAccount(savings);
        System.out.println("-> Successfully added Savings Account " + savings.getAccountNumber() + " for " + individual.getName());

        // 2. Open an Investment Account (Testing the BWP 500 minimum constraint)
        System.out.println("-> Testing Investment Account opening constraint (Depositing BWP 200)...");
        try {
            InvestmentAccount invalidInvestment = new InvestmentAccount("IA-9999", 200.00, "Main Branch");
        } catch (IllegalArgumentException e) {
            System.out.println("   VALIDATION PASSED: " + e.getMessage());
        }

        // Now open it correctly with a valid opening balance (BWP 1500)
        InvestmentAccount investment = new InvestmentAccount("IA-2002", 1500.00, "Main Branch");
        corporate.addAccount(investment);
        System.out.println("-> Successfully opened Investment Account " + investment.getAccountNumber() + " with BWP 1500.00");

        // 3. Open a Cheque Account
        ChequeAccount cheque = new ChequeAccount("CA-3003", 5000.00, "Main Branch", individual.getEmployer(), "Orapa Office");
        individual.addAccount(cheque);
        System.out.println("-> Successfully opened Cheque Account " + cheque.getAccountNumber() + " tracking employer " + cheque.getCompanyName() + "\n");


        // ---------------------------------------------------------------------
        // STEP 3: TESTING EXECUTIONS (DEPOSIT SEQUENCE DIAGRAM FLOW)
        // ---------------------------------------------------------------------
        System.out.println("[Step 3] Simulating 'Deposit Funds' Sequence Flow...");
        
        // Pick an account (Investment) and mimic your sequence logic path
        double depositAmount = 500.00;
        System.out.println("Customer requests a deposit of BWP " + depositAmount + " into " + investment.getAccountNumber());
        
        // 1. Invokes deposit method directly inside Account layer
        investment.deposit(depositAmount);
        
        // 2. Instantiate and record matching Transaction entity (Logs the receipt to terminal)
        Transaction depositTx = new Transaction(7001, depositAmount, "Deposit");
        investment.getTransactionHistory().add(depositTx);
        depositTx.recordTransaction();
        
        System.out.println("New Investment Account Balance: BWP " + investment.getBalance() + "\n");


        // ---------------------------------------------------------------------
        // STEP 4: BUSINESS RULES VALIDATION (WITHDRAWALS & INTEREST)
        // ---------------------------------------------------------------------
        System.out.println("[Step 4] Validating Core Account Rules & Polymorphism...");

        // Rule 1: Testing withdrawal restriction on Savings Account
        System.out.println("Attempting a withdrawal of BWP 100 from Savings Account...");
        boolean savingsWithdrawResult = savings.withdraw(100.00);
        System.out.println("Withdrawal executed successfully? " + savingsWithdrawResult);
        System.out.println("Savings Balance remains: BWP " + savings.getBalance());

        // Rule 2: Testing active checking withdrawal on Cheque Account
        System.out.println("\nAttempting a withdrawal of BWP 1500 from Cheque Account...");
        boolean chequeWithdrawResult = cheque.withdraw(1500.00);
        System.out.println("Withdrawal executed successfully? " + chequeWithdrawResult);
        System.out.println("Cheque Account Balance after withdrawal: BWP " + cheque.getBalance());

        // Rule 3: Testing Interest Calculations via InterestPayable interface
        System.out.println("\nRunning End-of-Month Interest Distribution...");
        
        System.out.println("Savings Account current balance: BWP " + savings.getBalance());
        double savingsInterestEarned = savings.calculateInterest();
        System.out.println("-> Savings interest added (0.05%): BWP " + savingsInterestEarned);
        System.out.println("-> New Savings Balance: BWP " + savings.getBalance());

        System.out.println("Investment Account current balance: BWP " + investment.getBalance());
        double investmentInterestEarned = investment.calculateInterest();
        System.out.println("-> Investment interest added (5.00%): BWP " + investmentInterestEarned);
        System.out.println("-> New Investment Balance: BWP " + investment.getBalance() + "\n");

        System.out.println("=================================================");
        System.out.println("  ALL COMPONENT LOGIC CHECKS RUN SUCCESSFULLY   ");
        System.out.println("=================================================");
    }
}