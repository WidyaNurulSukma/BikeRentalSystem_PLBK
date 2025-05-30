// Customer.java - Updated dengan id dan rentalRecords
import java.util.*;

public class Customer {
    private CustomerId id;
    private String name;
    private String phone;
    private String email;
    private Set<RentalRecord> rentalRecords;
    
    public Customer(CustomerId id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.rentalRecords = new HashSet<>();
    }
    
    public void addRentalRecord(RentalRecord record) {
        rentalRecords.add(record);
    }
    
    // Getters and Setters
    public CustomerId getId() { return id; }
    public void setId(CustomerId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<RentalRecord> getRentalRecords() { return new HashSet<>(rentalRecords); }
}
