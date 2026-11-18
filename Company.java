package model;

public class Company extends Customer {
    private String registrationNumber;
     

    //constructor
    public Company(int customerId, String name, String address, String registrationNumber) {
        super(customerId, name, address);
        this.registrationNumber = registrationNumber;
    }

    //getter method
    public String getRegistrationNumber() {
        return registrationNumber;
    }
}