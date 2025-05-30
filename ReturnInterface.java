// Interfaces.java

import java.util.Set;
import java.util.List;

/**
 * Mengelola data customer
 */
interface ICustomerMgt {
    Customer findCustomer(String name, String phone);
    void addCustomer(Customer customer);
    boolean isExistingCustomer(String name, String phone);
    
    // Methods sesuai OCL
    Set<RentalDetails> getCustomerRentals(String customerId);
    CustomerId registerCustomer(String name, String phone, String email);
}

/**
 * Mengelola sepeda dan rental di station
 */
interface IBikeStationMgt {
    List<Bike> getAvailableBikes();
    RentalRecord rentBike(Customer customer, String bikeId, int duration); // Legacy method
    boolean returnBike(String rentalId);
    void addBike(Bike bike);
    void removeBike(String bikeId);
    void updateBikeStatus(String bikeId, String status);
    
    // Methods sesuai OCL
    int getBikeAvailability(String category);
    String rentBike(String category, int hours); // OCL method
}

/**
 * Interface khusus untuk return bike
 */
interface ReturnInterface {
    boolean returnBike(String rentalId);
}
