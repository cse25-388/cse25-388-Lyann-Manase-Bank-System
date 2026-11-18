package controller;

import dao.CustomerDAO;
import model.Customer;
import java.sql.SQLException;

public class LoginController {
    // Connects the controller to the Customer database table
    private final CustomerDAO customerDAO = new CustomerDAO();

    // Validates login inputs and checks the database for a matching customer
    public Customer login(String customerIdText, String pin) throws IllegalArgumentException, SQLException {
        // 1. Check that neither field was left blank
        if (customerIdText == null || customerIdText.trim().isEmpty() || pin == null || pin.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID and PIN are required.");
        }

        // 2. Convert the typed Customer ID text into a whole number
        int id;
        try {
            id = Integer.parseInt(customerIdText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Customer ID must be a valid number.");
        }

        // 3. Ask CustomerDAO to verify the ID and PIN in the SQLite database
        Customer customer = customerDAO.authenticate(id, pin.trim());
        if (customer == null) {
            throw new IllegalArgumentException("Invalid Customer ID or PIN.");
        }

        return customer; // Returns the logged-in customer if credentials match
    }
}
