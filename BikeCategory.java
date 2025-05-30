// BikeCategory.java

import java.util.Set;
import java.util.HashSet;

/**
 * Kelas untuk mengelompokkan sepeda berdasarkan kategori dan menghitung biaya sewa.
 */
public class BikeCategory {
    private String name;
    private Currency hourlyRate;
    private Set<Bike> bikes;
    
    public BikeCategory(String name, Currency hourlyRate) {
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.bikes = new HashSet<>();
    }
    
    /**
     * Menghitung biaya sewa berdasarkan durasi jam.
     * @param hours jumlah jam yang disewa, harus > 0
     * @return total biaya sewa sebagai Currency
     */
    public Currency calculateRentalCost(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }
        return hourlyRate.multiply(hours);
    }
    
    public void addBike(Bike bike) {
        bikes.add(bike);
        bike.setCategory(this);
    }
    
    public void removeBike(Bike bike) {
        bikes.remove(bike);
        bike.setCategory(null);
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Currency getHourlyRate() {
        return hourlyRate;
    }
    
    public void setHourlyRate(Currency hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
    /**
     * Mengembalikan salinan set untuk menghindari modifikasi langsung.
     */
    public Set<Bike> getBikes() {
        return new HashSet<>(bikes);
    }
}
