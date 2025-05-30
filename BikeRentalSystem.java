// BikeRentalSystem.java - Updated untuk menggunakan struktur baru
import java.util.*;

public class BikeRentalSystem {
    private CustomerService customerService;
    private BikeStationService bikeStationService;
    private Scanner scanner;
    
    public BikeRentalSystem() {
        this.customerService = new CustomerService();
        this.bikeStationService = new BikeStationService("Main Station", customerService);
        this.scanner = new Scanner(System.in);
        initializeTestData();
    }
    
    private void initializeTestData() {
        // Create bike categories with Currency
        BikeCategory mountainBike = new BikeCategory("Mountain Bike", new Currency(15000, "IDR"));
        BikeCategory roadBike = new BikeCategory("Road Bike", new Currency(12000, "IDR"));
        BikeCategory cityBike = new BikeCategory("City Bike", new Currency(10000, "IDR"));
        
        // Add categories to station
        bikeStationService.getStation().addBikeCategory(mountainBike);
        bikeStationService.getStation().addBikeCategory(roadBike);
        bikeStationService.getStation().addBikeCategory(cityBike);
        
        // Add bikes to categories
        mountainBike.addBike(new Bike("MTB001", mountainBike));
        mountainBike.addBike(new Bike("MTB002", mountainBike));
        roadBike.addBike(new Bike("RDB001", roadBike));
        roadBike.addBike(new Bike("RDB002", roadBike));
        cityBike.addBike(new Bike("CTB001", cityBike));
        cityBike.addBike(new Bike("CTB002", cityBike));
    }
    
    public void displayMainMenu() {
        System.out.println("\n=== SISTEM PENYEWAAN SEPEDA ===");
        System.out.println("1. Lihat Sepeda Tersedia");
        System.out.println("2. Daftar Pelanggan");
        System.out.println("3. Sewa Sepeda");
        System.out.println("4. Kembalikan Sepeda");
        System.out.println("5. Lihat Riwayat Rental");
        System.out.println("6. Kelola Inventaris (Admin)");
        System.out.println("7. Keluar");
        System.out.print("Pilih menu: ");
    }
    
    public void viewAvailableBikes() {
        System.out.println("\n=== SEPEDA TERSEDIA ===");
        
        for (BikeCategory category : bikeStationService.getStation().getBikeCategories()) {
            int available = bikeStationService.getBikeAvailability(category.getName());
            System.out.printf("Kategori: %s - Tersedia: %d sepeda - Tarif: %s/jam%n",
                category.getName(), available, category.getHourlyRate());
            
            category.getBikes().stream()
                    .filter(bike -> "Available".equals(bike.getStatus()))
                    .forEach(bike -> System.out.printf("  - %s%n", bike.getBikeId()));
        }
    }
    
    public void registerCustomer() {
        System.out.println("\n=== DAFTAR PELANGGAN ===");
        
        try {
            System.out.print("Masukkan nama: ");
            String name = scanner.nextLine().trim();
            System.out.print("Masukkan nomor telepon: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Masukkan email: ");
            String email = scanner.nextLine().trim();
            
            CustomerId customerId = customerService.registerCustomer(name, phone, email);
            System.out.println("Pelanggan berhasil didaftarkan dengan ID: " + customerId);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void rentBike() {
        System.out.println("\n=== PENYEWAAN SEPEDA ===");
        
        try {
            // Show available bikes by category
            viewAvailableBikes();
            
            // Get customer information
            System.out.print("\nMasukkan nama: ");
            String name = scanner.nextLine().trim();
            System.out.print("Masukkan nomor telepon: ");
            String phone = scanner.nextLine().trim();
            
            Customer customer = customerService.findCustomer(name, phone);
            if (customer == null) {
                System.out.print("Pelanggan tidak ditemukan. Masukkan email untuk mendaftar: ");
                String email = scanner.nextLine().trim();
                CustomerId customerId = customerService.registerCustomer(name, phone, email);
                customer = customerService.findCustomerById(customerId.getId());
            }
            
            // Get rental details
            System.out.print("Pilih ID sepeda: ");
            String bikeId = scanner.nextLine().trim();
            System.out.print("Durasi sewa (jam): ");
            int duration = Integer.parseInt(scanner.nextLine().trim());
            
            // Process rental
            RentalRecord record = bikeStationService.rentBike(customer, bikeId, duration);
            System.out.printf("\nPenyewaan berhasil!%n");
            System.out.printf("Kode sewa: %s%n", record.getRentalId());
            System.out.printf("Total biaya: %s%n", record.getTotalCost());
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void returnBike() {
        System.out.println("\n=== PENGEMBALIAN SEPEDA ===");
        
        try {
            System.out.print("Masukkan kode sewa: ");
            String rentalId = scanner.nextLine().trim();
            
            boolean success = bikeStationService.returnBike(rentalId);
            if (success) {
                System.out.println("Pengembalian berhasil!");
            } else {
                System.out.println("Gagal memproses pengembalian. Periksa kode sewa.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void viewCustomerRentals() {
        System.out.println("\n=== RIWAYAT RENTAL ===");
        
        try {
            System.out.print("Masukkan Customer ID: ");
            String customerId = scanner.nextLine().trim();
            
            Set<RentalDetails> rentals = customerService.getCustomerRentals(customerId);
            
            if (rentals.isEmpty()) {
                System.out.println("Tidak ada riwayat rental untuk pelanggan ini.");
            } else {
                System.out.println("Riwayat Rental:");
                for (RentalDetails rental : rentals) {
                    System.out.printf("- %s: %s (%d jam) - %s - Status: %s%n",
                        rental.getRentalId(),
                        rental.getBikeCategory(),
                        rental.getHours(),
                        rental.getTotalCost(),
                        rental.getStatus());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void manageInventory() {
        System.out.println("\n=== KELOLA INVENTARIS (ADMIN) ===");
        
        // Display inventory summary
        Map<String, Object> summary = bikeStationService.getInventorySummary();
        System.out.printf("Ringkasan Inventaris:%n");
        System.out.printf("Total sepeda: %d%n", summary.get("total"));
        System.out.printf("Tersedia: %d%n", summary.get("available"));
        System.out.printf("Disewa: %d%n", summary.get("rented"));
        System.out.printf("Perbaikan: %d%n", summary.get("maintenance"));
        
        System.out.println("\nValidasi Rental Aktif:");
        System.out.print("Masukkan Rental ID untuk validasi: ");
        String testRentalId = scanner.nextLine().trim();
        boolean isValid = bikeStationService.getStation().isRentalValid(testRentalId);
        System.out.println("Rental ID " + testRentalId + " valid: " + isValid);
    }
    
     public void run() {
        System.out.println("Selamat datang di Sistem Penyewaan Sepeda!");
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        viewAvailableBikes();
                        break;
                    case 2:
                        registerCustomer();
                        break;
                    case 3:
                        rentBike();
                        break;
                    case 4:
                        returnBike();
                        break;
                    case 5:
                        viewCustomerRentals();
                        break;
                    case 6:
                        manageInventory();
                        break;
                    case 7:
                        System.out.println("Terima kasih telah menggunakan sistem kami!");
                        running = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
                
                if (running) {
                    System.out.print("\nTekan Enter untuk melanjutkan...");
                    scanner.nextLine();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Harap masukkan angka.");
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    // Main method to run the application
    public static void main(String[] args) {
        BikeRentalSystem system = new BikeRentalSystem();
        system.run();
    }
    
    // Additional helper methods for testing OCL constraints
    public void demonstrateOCLConstraints() {
        System.out.println("\n=== DEMONSTRASI OCL CONSTRAINTS ===");
        
        try {
            // Test 1: Calculate rental cost
            BikeCategory testCategory = new BikeCategory("Test Category", new Currency(10000, "IDR"));
            Currency cost = testCategory.calculateRentalCost(3);
            System.out.println("Test 1 - Calculate rental cost (3 hours): " + cost);
            
            // Test 2: Register customer with validation
            CustomerId id = customerService.registerCustomer("John Doe", "08123456789", "john@example.com");
            System.out.println("Test 2 - Register customer: " + id);
            
            // Test 3: Check bike availability
            int availability = bikeStationService.getBikeAvailability("Mountain Bike");
            System.out.println("Test 3 - Mountain Bike availability: " + availability);
            
            // Test 4: Validate rental
            boolean isValid = bikeStationService.getStation().isRentalValid("RNT0001");
            System.out.println("Test 4 - Validate rental RNT0001: " + isValid);
            
        } catch (Exception e) {
            System.out.println("OCL Constraint error: " + e.getMessage());
        }
    }
}
