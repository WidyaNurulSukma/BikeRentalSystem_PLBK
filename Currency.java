// Currency.java - Type untuk mata uang
public class Currency {
    private double amount;
    private String code; // IDR, USD, etc.
    
    public Currency(double amount, String code) {
        this.amount = amount;
        this.code = code;
    }
    
    public Currency multiply(int multiplier) {
        return new Currency(amount * multiplier, code);
    }
    
    // Getters and Setters
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    @Override
    public String toString() {
        return String.format("%.0f %s", amount, code);
    }
}
