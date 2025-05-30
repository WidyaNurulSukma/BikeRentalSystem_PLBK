// CustomerService.java - Updated sesuai OCL
import java.util.*;
import java.util.stream.Collectors;

public class CustomerService implements ICustomerMgt {
    private Set<Customer> customers;
    private int customerIdCounter = 1;
    
    public CustomerService() {
        this.customers = new HashSet<>();
    }
    
    @Override
    public Customer findCustomer(String name, String phone) {
        return customers.stream()
                       .filter(customer -> customer.getName().equals(name) && 
                              customer.getPhone().equals(phone))
                       .findFirst()
                       .orElse(null);
    }
    
    public Customer findCustomerById(String customerId) {
        return customers.stream()
                       .filter(customer -> customer.getId().getId().equals(customerId))
                       .findFirst()
                       .orElse(null);
    }
    
    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }
    
    @Override
    public boolean isExistingCustomer(String name, String phone) {
        return findCustomer(name, phone) != null;
    }
    
    // Method sesuai OCL
    @Override
    public Set<RentalDetails> getCustomerRentals(String customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        
        return customer.getRentalRecords().stream()
                      .map(record -> new RentalDetails(
                          record.getRentalId(),
                          record.getDuration(),
                          record.getTimestamp(),
                          record.getAllocation().getCategory().getName(),
                          record.getTotalCost(),
                          record.getStatus()
                      ))
                      .collect(Collectors.toSet());
    }
    
    // Method sesuai OCL
    @Override
    public CustomerId registerCustomer(String name, String phone, String email) {
        // Validasi prekondisi OCL
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (email.indexOf('@') <= 0) {
            throw new IllegalArgumentException("Email must be valid");
        }
        if (customers.stream().anyMatch(c -> c.getEmail().equals(email))) {
            throw new IllegalArgumentException("Email must be unique");
        }
        
        CustomerId customerId = new CustomerId("CUST" + String.format("%04d", customerIdCounter++));
        Customer customer = new Customer(customerId, name, phone, email);
        customers.add(customer);
        
        return customerId;
    }
    
    public Set<Customer> getAllCustomers() {
        return new HashSet<>(customers);
    }
}
