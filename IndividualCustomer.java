package model;
public class IndividualCustomer extends Customer {
    private String surname;
    private boolean isEmployed;
    private String employer;
    //constructor
    public IndividualCustomer(int customerId, String name, String surname, String address, String employer) {
        super(customerId, name, address);
        this.surname = surname;
        this.isEmployed = (employer != null && !employer.trim().isEmpty());
        this.employer = employer;
    }

    //getter methods
    public boolean canOpenChequeAccount() {
        return isEmployed;
    }

     public String getSurname() {
        return surname;
    }

    public String getEmployer() {
        return employer;
    }
}
