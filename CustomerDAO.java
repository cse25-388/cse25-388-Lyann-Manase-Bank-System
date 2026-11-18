package dao;

import model.Company;
import model.Customer;
import model.IndividualCustomer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Saves a new Individual or Company customer and their login PIN into the database
    public void addCustomer(Customer customer, String pin) throws SQLException {
        String sql = "INSERT OR IGNORE INTO customers "
                   + "(customer_id, customer_type, name, surname, address, employer_or_reg, pin) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customer.getCustomerId());

            // Check if the customer is an Individual or a Company to save the right details
            if (customer instanceof IndividualCustomer) {
                IndividualCustomer ind = (IndividualCustomer) customer;
                pstmt.setString(2, "INDIVIDUAL");
                pstmt.setString(3, ind.getName());
                pstmt.setString(4, ind.getSurname());
                pstmt.setString(5, ind.getAddress());
                pstmt.setString(6, ind.getEmployer());
            } else if (customer instanceof Company) {
                Company comp = (Company) customer;
                pstmt.setString(2, "COMPANY");
                pstmt.setString(3, comp.getName());
                pstmt.setString(4, null);
                pstmt.setString(5, comp.getAddress());
                pstmt.setString(6, comp.getRegistrationNumber());
            }

            pstmt.setString(7, pin);
            pstmt.executeUpdate();
        }
    }

    // Checks if a customer ID and PIN match a record in the database for login
    public Customer authenticate(int customerId, String pin) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ? AND pin = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();

            // If a matching customer is found, build and return the Customer object
            if (rs.next()) {
                String type = rs.getString("customer_type");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String extra = rs.getString("employer_or_reg");

                if ("INDIVIDUAL".equals(type)) {
                    String surname = rs.getString("surname");
                    return new IndividualCustomer(customerId, name, surname, address, extra);
                } else {
                    return new Company(customerId, name, address, extra);
                }
            }
        }
        return null; // Returns null if login details are incorrect
    }

    // Retrieves a list of all customers stored in the database
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String type = rs.getString("customer_type");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String extra = rs.getString("employer_or_reg");

                if ("INDIVIDUAL".equals(type)) {
                    list.add(new IndividualCustomer(id, name, rs.getString("surname"), address, extra));
                } else {
                    list.add(new Company(id, name, address, extra));
                }
            }
        }
        return list;
    }
}