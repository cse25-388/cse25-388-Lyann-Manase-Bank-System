package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Location and name of the SQLite database file that will be created in your project folder
    private static final String URL = "jdbc:sqlite:banking_system.db";

    // Opens and returns a live connection to the SQLite database
    public static Connection getConnection() throws SQLException {
        try{
            // Load the SQLite JDBC driver from lib folder
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver not found", e);
        }
        return DriverManager.getConnection(URL);
    }

    // Automatically creates the required database tables if they do not exist yet
    public static void initializeDatabase() {
        // SQL statement to create the 'customers' table (stores both Individuals and Companies)
        String createCustomers = "CREATE TABLE IF NOT EXISTS customers ("
                + "customer_id INTEGER PRIMARY KEY, "
                + "customer_type TEXT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "surname TEXT, "
                + "address TEXT NOT NULL, "
                + "employer_or_reg TEXT, "
                + "pin TEXT NOT NULL);";

        // SQL statement to create the 'accounts' table linked to a customer via customer_id
        String createAccounts = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_number TEXT PRIMARY KEY, "
                + "customer_id INTEGER NOT NULL, "
                + "account_type TEXT NOT NULL, "
                + "balance REAL NOT NULL, "
                + "branch TEXT NOT NULL, "
                + "company_name TEXT, "
                + "company_address TEXT, "
                + "FOREIGN KEY (customer_id) REFERENCES customers(customer_id));";

        // SQL statement to create the 'transactions' table linked to an account via account_number
        String createTransactions = "CREATE TABLE IF NOT EXISTS transactions ("
                + "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "account_number TEXT NOT NULL, "
                + "amount REAL NOT NULL, "
                + "type TEXT NOT NULL, "
                + "tx_date TEXT NOT NULL, "
                + "FOREIGN KEY (account_number) REFERENCES accounts(account_number));";

        // Try-with-resources automatically closes the connection and statement after running the SQL
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createCustomers);
            stmt.execute(createAccounts);
            stmt.execute(createTransactions);
            System.out.println("Database tables initialized successfully.");
        } catch (SQLException e) {
            // Prints error details if anything goes wrong while connecting or creating tables
            e.printStackTrace();
        }
    }
}