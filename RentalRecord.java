// RentalRecord.java - Updated
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalRecord {
    private String rentalId;
    private Customer customer;
    private Bike allocation; // Bike yang dialokasikan
    private LocalDateTime timestamp;
    private int duration; // in hours
    private Currency totalCost;
    private String status; // "active", "completed", "cancelled"
    
    public RentalRecord(String rentalId, Customer customer, Bike bike, int duration) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.allocation = bike;
        this.duration = duration;
        this.timestamp = LocalDateTime.now();
        this.status = "active";
        calculateTotalCost();
    }
    
    private void calculateTotalCost() {
        this.totalCost = allocation.getCategory().calculateRentalCost(duration);
    }
    
    public Currency calculateLateFee(LocalDateTime returnTime) {
        long actualHours = ChronoUnit.HOURS.between(timestamp, returnTime);
        if (actualHours > duration) {
            long extraHours = actualHours - duration;
            Currency baseRate = allocation.getCategory().getHourlyRate();
            return new Currency(extraHours * baseRate.getAmount() * 1.5, baseRate.getCode());
        }
        return new Currency(0, allocation.getCategory().getHourlyRate().getCode());
    }
    
    // Getters and Setters
    public String getRentalId() { return rentalId; }
    public Customer getCustomer() { return customer; }
    public Bike getAllocation() { return allocation; }
    public void setAllocation(Bike allocation) { this.allocation = allocation; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getDuration() { return duration; }
    public Currency getTotalCost() { return totalCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
