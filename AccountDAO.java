package dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Saves a new Savings, Investment, or Cheque account into the database
    public void createAccount(Account account, int customerId) throws SQLException {
        String sql = "INSERT OR IGNORE INTO accounts "
                   + "(account_number, customer_id, account_type, balance, branch, company_name, company_address) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountNumber());
            pstmt.setInt(2, customerId);
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());

            // Check the account subclass so we store the right account type and employer info
            if (account instanceof SavingsAccount) {
                pstmt.setString(3, "SAVINGS");
                pstmt.setString(6, null);
                pstmt.setString(7, null);
            } else if (account instanceof InvestmentAccount) {
                pstmt.setString(3, "INVESTMENT");
                pstmt.setString(6, null);
                pstmt.setString(7, null);
            } else if (account instanceof ChequeAccount) {
                ChequeAccount cheque = (ChequeAccount) account;
                pstmt.setString(3, "CHEQUE");
                pstmt.setString(6, cheque.getCompanyName());
                pstmt.setString(7, cheque.getCompanyAddress());
            }

            pstmt.executeUpdate();
        }
    }

    // Loads all accounts belonging to a specific customer from the database
    public List<Account> getAccountsByCustomerId(int customerId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String accNum = rs.getString("account_number");
                String type = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                String branch = rs.getString("branch");

                if ("SAVINGS".equals(type)) {
                    accounts.add(new SavingsAccount(accNum, balance, branch));
                } else if ("INVESTMENT".equals(type)) {
                    accounts.add(new InvestmentAccount(accNum, balance, branch));
                } else if ("CHEQUE".equals(type)) {
                    String compName = rs.getString("company_name");
                    String compAddr = rs.getString("company_address");
                    accounts.add(new ChequeAccount(accNum, balance, branch, compName, compAddr));
                }
            }
        }
        return accounts;
    }

    // Updates an account's balance in the database after a deposit, withdrawal, or interest payment
    public void updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            pstmt.executeUpdate();
        }
    }

    // Records a transaction receipt into the 'transactions' table
    public void logTransaction(String accountNumber, double amount, String type) throws SQLException {
        String sql = "INSERT INTO transactions (account_number, amount, type, tx_date) "
                   + "VALUES (?, ?, ?, datetime('now'))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNumber);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
        }
    }

    // Inserts sample customers and accounts into the database on first startup (10+ records required)
    public static void seedSampleData() {
        try {
            CustomerDAO cDao = new CustomerDAO();
            AccountDAO aDao = new AccountDAO();

            // Skip seeding if the database already has customers
            if (!cDao.getAllCustomers().isEmpty()) {
                return;
            }

            // Create 5 sample customers (4 Individuals + 1 Company)
            IndividualCustomer c1 = new IndividualCustomer(101, "Thabo", "Molefe", "Plot 453, Gaborone", "Debswana");
            IndividualCustomer c2 = new IndividualCustomer(102, "Kabo", "Kgosi", "Phakalane, Gaborone", "Mascom");
            IndividualCustomer c3 = new IndividualCustomer(103, "Lesego", "Ndlovu", "Block 8, Gaborone", "Orange Botswana");
            IndividualCustomer c4 = new IndividualCustomer(104, "Mpho", "Seretse", "Francistown", "");
            Company c5 = new Company(201, "Botswana Tech Solutions", "Innovation Hub, Gaborone", "CO-98741");

            cDao.addCustomer(c1, "1234");
            cDao.addCustomer(c2, "1234");
            cDao.addCustomer(c3, "1234");
            cDao.addCustomer(c4, "1234");
            cDao.addCustomer(c5, "1234");

            // Create 8 sample accounts linked to those customers
            aDao.createAccount(new SavingsAccount("SA-1001", 1200.00, "Main Branch"), 101);
            aDao.createAccount(new ChequeAccount("CA-1002", 5000.00, "Main Branch", "Debswana", "Orapa"), 101);
            aDao.createAccount(new InvestmentAccount("IA-1003", 2500.00, "Game City"), 102);
            aDao.createAccount(new ChequeAccount("CA-1004", 8400.00, "Game City", "Mascom", "Gaborone"), 102);
            aDao.createAccount(new SavingsAccount("SA-1005", 600.00, "Airport Junction"), 103);
            aDao.createAccount(new SavingsAccount("SA-1006", 350.00, "Francistown"), 104);
            aDao.createAccount(new InvestmentAccount("IA-2001", 15000.00, "Main Branch"), 201);
            aDao.createAccount(new ChequeAccount("CA-2002", 42000.00, "Main Branch", "Botswana Tech Solutions", "Innovation Hub"), 201);

            System.out.println("Sample records seeded into database successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}