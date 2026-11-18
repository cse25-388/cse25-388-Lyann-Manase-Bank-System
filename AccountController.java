package controller;

import dao.AccountDAO;
import model.*;
import java.sql.SQLException;
import java.util.List;

public class AccountController {
    // Connects the controller to the Accounts and Transactions database tables
    private final AccountDAO accountDAO = new AccountDAO();

    // Fetches all bank accounts belonging to the logged-in customer
    public List<Account> loadCustomerAccounts(int customerId) throws SQLException {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    // Handles depositing money into an account and saving the new balance to SQLite
    public void deposit(Account account, String amountText) throws Exception {
        double amount = parsePositiveAmount(amountText);

        // Update balance in the Java Model object
        account.deposit(amount);

        // Save the updated balance and transaction receipt to SQLite
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        accountDAO.logTransaction(account.getAccountNumber(), amount, "Deposit");
    }

    // Handles withdrawing money while enforcing Savings Account restrictions
    public void withdraw(Account account, String amountText) throws Exception {
        double amount = parsePositiveAmount(amountText);

        // Enforce business rule: Savings Accounts do not allow withdrawals
        if (account instanceof SavingsAccount) {
            throw new IllegalStateException("Withdrawals are strictly forbidden on Savings Accounts.");
        }

        // Attempt withdrawal using the Model's logic
        boolean success = account.withdraw(amount);
        if (!success) {
            throw new IllegalStateException("Insufficient funds for this withdrawal.");
        }

        // Save the updated balance and transaction receipt to SQLite
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        accountDAO.logTransaction(account.getAccountNumber(), amount, "Withdrawal");
    }

    // Calculates and pays monthly interest on Savings or Investment accounts
    public double applyMonthlyInterest(Account account) throws Exception {
        // Check if the selected account implements InterestPayable
        if (!(account instanceof InterestPayable)) {
            throw new IllegalStateException("Cheque Accounts do not earn monthly interest.");
        }

        InterestPayable payable = (InterestPayable) account;
        double interestEarned = payable.calculateInterest();

        // Add interest to the account balance in the Model
        payable.payInterest();

        // Save the updated balance and interest receipt to SQLite
        accountDAO.updateBalance(account.getAccountNumber(), account.getBalance());
        accountDAO.logTransaction(account.getAccountNumber(), interestEarned, "Monthly Interest");

        return interestEarned;
    }

    // Opens a brand-new account for the customer and saves it to SQLite
    public void openNewAccount(Customer customer, String type, String accNumber, String depositText,
                               String branch, String companyName, String companyAddr) throws Exception {
        if (accNumber == null || accNumber.trim().isEmpty() || branch == null || branch.trim().isEmpty()) {
            throw new IllegalArgumentException("Account Number and Branch are required.");
        }

        double initialDeposit = parsePositiveAmount(depositText);
        Account newAccount;

        // Create the matching Account subclass based on the user's choice
        if ("Savings".equals(type)) {
            newAccount = new SavingsAccount(accNumber.trim(), initialDeposit, branch.trim());
        } else if ("Investment".equals(type)) {
            // InvestmentAccount constructor automatically enforces the BWP 500.00 minimum
            newAccount = new InvestmentAccount(accNumber.trim(), initialDeposit, branch.trim());
        } else if ("Cheque".equals(type)) {
            // Check employment rule for Individual Customers
            if (customer instanceof IndividualCustomer) {
                IndividualCustomer ind = (IndividualCustomer) customer;
                if (!ind.canOpenChequeAccount()) {
                    throw new IllegalStateException("Customer must be employed to open a Cheque Account.");
                }
            }
            newAccount = new ChequeAccount(accNumber.trim(), initialDeposit, branch.trim(), companyName, companyAddr);
        } else {
            throw new IllegalArgumentException("Please select a valid account type.");
        }

        // Save the new account and initial deposit receipt to SQLite
        accountDAO.createAccount(newAccount, customer.getCustomerId());
        accountDAO.logTransaction(newAccount.getAccountNumber(), initialDeposit, "Initial Deposit");
    }

    // Helper method to make sure typed amounts are valid positive numbers
    private double parsePositiveAmount(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter an amount.");
        }
        try {
            double val = Double.parseDouble(text.trim());
            if (val <= 0) {
                throw new IllegalArgumentException("Amount must be greater than BWP 0.00.");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid numeric amount.");
        }
    }
}
