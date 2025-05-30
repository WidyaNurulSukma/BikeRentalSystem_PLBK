// CustomerId.java - Type untuk ID customer
public class CustomerId {
    private String id;
    
    public CustomerId(String id) {
        this.id = id;
    }
    
    public String getId() { return id; }
    
    @Override
    public String toString() { return id; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CustomerId that = (CustomerId) obj;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
