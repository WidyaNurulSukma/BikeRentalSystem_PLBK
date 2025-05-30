// RentalDetails.java - Class untuk detail rental
import java.time.LocalDateTime;

public class RentalDetails {
    private String rentalId;
    private int hours;
    private LocalDateTime rentalDate;
    private String bikeCategory;
    private Currency totalCost;
    private String status;
    
    public RentalDetails(String rentalId, int hours, LocalDateTime rentalDate, 
                        String bikeCategory, Currency totalCost, String status) {
        this.rentalId = rentalId;
        this.hours = hours;
        this.rentalDate = rentalDate;
        this.bikeCategory = bikeCategory;
        this.totalCost = totalCost;
        this.status = status;
    }
    
    // Getters
    public String getRentalId() { return rentalId; }
    public int getHours() { return hours; }
    public LocalDateTime getRentalDate() { return rentalDate; }
    public String getBikeCategory() { return bikeCategory; }
    public Currency getTotalCost() { return totalCost; }
    public String getStatus() { return status; }
}
