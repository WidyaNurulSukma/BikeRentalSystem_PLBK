// BikeStation.java - Restructured sesuai OCL
import java.util.*;

public class BikeStation {
    private String location;
    private Set<BikeCategory> bikeCategories;
    private Set<RentalRecord> activeRentals;
    private Set<RentalRecord> completedRentals;
    private Set<RentalRecord> rentalRecords; // All records
    
    public BikeStation(String location) {
        this.location = location;
        this.bikeCategories = new HashSet<>();
        this.activeRentals = new HashSet<>();
        this.completedRentals = new HashSet<>();
        this.rentalRecords = new HashSet<>();
    }
    
    public void addBikeCategory(BikeCategory category) {
        bikeCategories.add(category);
    }
    
    public void addRentalRecord(RentalRecord record) {
        rentalRecords.add(record);
        if ("active".equals(record.getStatus())) {
            activeRentals.add(record);
        } else if ("completed".equals(record.getStatus())) {
            completedRentals.add(record);
        }
    }
    
    public void moveToCompleted(RentalRecord record) {
        activeRentals.remove(record);
        completedRentals.add(record);
    }
    
    public RentalRecord findRentalRecord(String rentalId) {
        return rentalRecords.stream()
                           .filter(record -> record.getRentalId().equals(rentalId))
                           .findFirst()
                           .orElse(null);
    }
    
    public BikeCategory findBikeCategory(String categoryName) {
        return bikeCategories.stream()
                            .filter(category -> category.getName().equals(categoryName))
                            .findFirst()
                            .orElse(null);
    }
    
    // Method sesuai OCL
    public boolean isRentalValid(String rentalId) {
        return activeRentals.stream()
                           .anyMatch(r -> r.getRentalId().equals(rentalId));
    }
    
    // Getters
    public String getLocation() { return location; }
    public Set<BikeCategory> getBikeCategories() { return new HashSet<>(bikeCategories); }
    public Set<RentalRecord> getActiveRentals() { return new HashSet<>(activeRentals); }
    public Set<RentalRecord> getCompletedRentals() { return new HashSet<>(completedRentals); }
    public Set<RentalRecord> getRentalRecords() { return new HashSet<>(rentalRecords); }
}
