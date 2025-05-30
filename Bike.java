// Bike.java - Updated
public class Bike {
    private String bikeId;
    private String status; // "Available", "Rented", "Maintenance"
    private BikeCategory category;
    
    public Bike(String bikeId, BikeCategory category) {
        this.bikeId = bikeId;
        this.category = category;
        this.status = "Available";
    }
    
    // Getters and Setters
    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BikeCategory getCategory() { return category; }
    public void setCategory(BikeCategory category) { this.category = category; }
    
    public boolean isAvailable() {
        return "Available".equals(status);
    }
}
