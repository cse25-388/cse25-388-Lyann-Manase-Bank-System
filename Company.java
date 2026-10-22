public class Company extends Customer {
    private String registrationNumber;
     

    //constructor
    public Company(String name, String address, String registrationNumber) {
        super(name, address);
        this.registrationNumber = registrationNumber;
    }

    //getter method
    public String getRegistrationNumber() {
        return registrationNumber;
    }
}