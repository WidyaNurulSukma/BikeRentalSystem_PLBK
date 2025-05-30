// BikeStationService.java - Updated sesuai OCL
import java.time.LocalDateTime;
import java.util.*;

public class BikeStationService implements IBikeStationMgt, ReturnInterface {
    private BikeStation station; // 1 2 3 5 6 -> 4 =5
    private CustomerService customerService;
    private int rentalIdCounter = 1;
    
    public BikeStationService(String location, CustomerService customerService) {
        this.station = new BikeStation(location);
        this.customerService = customerService;
    }
    
    // Method sesuai OCL
    @Override
    public int getBikeAvailability(String category) {
        BikeCategory bikeCategory = station.findBikeCategory(category);
        if (bikeCategory == null) {
            throw new IllegalArgumentException("Invalid bike category");
        }
        
        return (int) bikeCategory.getBikes().stream()
                                 .filter(bike -> "Available".equals(bike.getStatus()))
                                 .count();
    }
    
    // Method sesuai OCL
    @Override
    public String rentBike(String category, int hours) {
        // Validasi prekondisi OCL
        BikeCategory bikeCategory = station.findBikeCategory(category);
        if (bikeCategory == null) {
            throw new IllegalArgumentException("Invalid bike category");
        }
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }
        
        List<Bike> availableBikes = bikeCategory.getBikes().stream()
                                               .filter(bike -> "Available".equals(bike.getStatus()))
                                               .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        if (availableBikes.isEmpty()) {
            throw new IllegalStateException("No bikes available in this category");
        }
        
        // Untuk implementasi sederhana, kita butuh customer default
        // Dalam aplikasi nyata, ini akan diintegrasikan dengan UI
        throw new UnsupportedOperationException("This method requires customer selection - use rentBike(Customer, String, int) instead");
    }
    
    // Legacy method - kept for compatibility
    @Override
    public RentalRecord rentBike(Customer customer, String bikeId, int duration) {
        // Find bike from all categories
        Bike bike = null;
        for (BikeCategory category : station.getBikeCategories()) {
            bike = category.getBikes().stream()
                          .filter(b -> b.getBikeId().equals(bikeId))
                          .findFirst()
                          .orElse(null);
            if (bike != null) break;
        }
        
        if (bike == null || !"Available".equals(bike.getStatus())) {
            throw new RuntimeException("Sepeda tidak tersedia");
        }
        
        // Add customer if not exists
        customerService.addCustomer(customer);
        
        // Create rental record
        String rentalId = generateRentalId();
        RentalRecord record = new RentalRecord(rentalId, customer, bike, duration);
        
        // Update bike status
        bike.setStatus("Rented");
        station.addRentalRecord(record);
        customer.addRentalRecord(record);
        
        return record;
    }
    
    // Method dari ReturnInterface sesuai OCL
    @Override
    public boolean returnBike(String rentalId) {
        // Validasi prekondisi OCL
        RentalRecord record = station.findRentalRecord(rentalId);
        if (record == null || record.getAllocation() == null) {
            return false;
        }
        
        // Calculate late fee if applicable
        LocalDateTime returnTime = LocalDateTime.now();
        Currency lateFee = record.calculateLateFee(returnTime);
        
        // Update statuses - memenuhi postcondition OCL
        record.getAllocation().setStatus("Available");
        record.setStatus("completed");
        // allocation tidak di-reset agar historis tetap tersedia
        // record.setAllocation(null); // Dihapus untuk menjaga history
        
        // Move to completed rentals
        station.moveToCompleted(record);
        
        System.out.println("Sepeda berhasil dikembalikan.");
        if (lateFee.getAmount() > 0) {
            System.out.println("Denda keterlambatan: " + lateFee);
        }
        
        return true;
    }
    
    @Override
    public List<Bike> getAvailableBikes() {
        List<Bike> allBikes = new ArrayList<>();
        for (BikeCategory category : station.getBikeCategories()) {
            allBikes.addAll(category.getBikes().stream()
                                   .filter(bike -> "Available".equals(bike.getStatus()))
                                   .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
        }
        return allBikes;
    }
    
    public List<Bike> getAvailableBikesByCategory(String categoryName) {
        BikeCategory category = station.findBikeCategory(categoryName);
        if (category == null) {
            return new ArrayList<>();
        }
        
        return category.getBikes().stream()
                      .filter(bike -> "Available".equals(bike.getStatus()))
                      .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    @Override
    public void addBike(Bike bike) {
        BikeCategory category = bike.getCategory();
        if (!station.getBikeCategories().contains(category)) {
            station.addBikeCategory(category);
        }
        category.addBike(bike);
    }
    
    @Override
    public void removeBike(String bikeId) {
        for (BikeCategory category : station.getBikeCategories()) {
            category.getBikes().removeIf(b -> 
                b.getBikeId().equals(bikeId) && "Available".equals(b.getStatus()));
        }
    }
    
    @Override
    public void updateBikeStatus(String bikeId, String status) {
        for (BikeCategory category : station.getBikeCategories()) {
            category.getBikes().stream()
                    .filter(bank -> bank.getBikeId().equals(bikeId))
                    .findFirst()
                    .ifPresent(bike -> bike.setStatus(status));
        }
    }
    
    public Map<String, Object> getInventorySummary() {
        Map<String, Object> summary = new HashMap<>();
        List<Bike> allBikes = new ArrayList<>();
        
        for (BikeCategory category : station.getBikeCategories()) {
            allBikes.addAll(category.getBikes());
        }
        
        long totalBikes = allBikes.size();
        long availableBikes = allBikes.stream().filter(b -> "Available".equals(b.getStatus())).count();
        long rentedBikes = allBikes.stream().filter(b -> "Rented".equals(b.getStatus())).count();
        long maintenanceBikes = allBikes.stream().filter(b -> "Maintenance".equals(b.getStatus())).count();
        
        summary.put("total", totalBikes);
        summary.put("available", availableBikes);
        summary.put("rented", rentedBikes);
        summary.put("maintenance", maintenanceBikes);
        
        return summary;
    }
    
    private String generateRentalId() {
        return "RNT" + String.format("%04d", rentalIdCounter++);
    }
    
    public BikeStation getStation() {
        return station;
    }
}
