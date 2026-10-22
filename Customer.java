public class Customer{
    private String name;
    private String address;
    private List <Account> accounts;

    //constructor
    public Customer(String name, String address){
        this.name = name;
        this.address = address;
        this.accounts = new ArrayList<>();
    }
 
    //method link account to the customer
    public void addAccount(Account account){
        accounts.add(account);
    }

    //getter methods
    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public List<Account> getAccounts(){
        return accounts;
    }
}