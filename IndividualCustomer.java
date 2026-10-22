public class IndividualCustomer extends Customer {
    private String surname;
    private boolean isEmployed;

    //constructor
    public IndividualCustomer(String name, String address, String surname, boolean isEmployed) {
        super(name, address);
        this.surname = surname;
        this.isEmployed = isEmployed;
    }

    //getter methods
    public boolean canOpenChequeAccount() {
        return isEmployed;
    }

     public String getSurname() {
        return surname;
    }
}
