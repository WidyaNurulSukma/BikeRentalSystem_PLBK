# Sistem Penyewaan Sepeda (Bike Rental System)

Sistem penyewaan sepeda berbasis Java yang menerapkan konsep Object-Oriented Programming dengan implementasi OCL (Object Constraint Language) untuk validasi dan constraint checking.

## 👥 Anggota Kelompok
- **Widya Nurul Sukma** - 2208107010054
- **Pryta Rosela** - 2208107010046

## 📋 Deskripsi Proyek

Sistem ini merupakan aplikasi console untuk mengelola penyewaan sepeda yang memungkinkan:
- Registrasi dan manajemen pelanggan
- Pengelolaan kategori sepeda dengan tarif berbeda
- Proses penyewaan dan pengembalian sepeda
- Tracking riwayat rental
- Manajemen inventaris sepeda

## 🏗️ Arsitektur Sistem

### Struktur Class Utama

1. **BikeRentalSystem** - Class utama yang mengatur alur aplikasi
2. **BikeStation** - Mengelola stasiun sepeda dan inventaris
3. **BikeCategory** - Kategori sepeda dengan tarif per jam
4. **Bike** - Entity sepeda individual
5. **Customer** - Data pelanggan
6. **RentalRecord** - Catatan penyewaan
7. **Currency** - Type untuk mata uang

### Interface dan Service

- **ICustomerMgt** - Interface untuk manajemen pelanggan
- **IBikeStationMgt** - Interface untuk manajemen stasiun sepeda
- **ReturnInterface** - Interface khusus untuk pengembalian sepeda
- **CustomerService** - Service layer untuk operasi pelanggan
- **BikeStationService** - Service layer untuk operasi stasiun

## 🔍 OCL (Object Constraint Language) - Penjelasan Mendalam

OCL merupakan bahasa formal yang digunakan untuk mendefinisikan constraint dan aturan bisnis dalam sistem. Dalam proyek ini, OCL diimplementasikan untuk memastikan integritas data dan logika bisnis yang konsisten.

### File OCL: BikeRentalSystem.ocl

#### 1. Constraint untuk `getBikeAvailability`
```ocl
context IBikeStationMgt::getBikeAvailability(category: String): Integer
pre:
    bikeStation.bikeCategories->exists(c: BikeCategory | c.name = category)
post:
    let theCategory: BikeCategory = bikeStation.bikeCategories->
        select(c: BikeCategory | c.name = category)->asSequence()->first() in
    result = theCategory.bikes->select(b: Bike | b.status = 'Available')->size()
```

**Penjelasan:**
- **Precondition**: Memastikan kategori yang diminta benar-benar ada dalam sistem
- **Postcondition**: Menghitung jumlah sepeda yang tersedia dalam kategori tertentu
- **Implementasi**: Method ini mengembalikan jumlah sepeda dengan status "Available"

#### 2. Constraint untuk `rentBike`
```ocl
context IBikeStationMgt::rentBike(category: String, hours: Integer): String
pre:
    bikeStation.bikeCategories->exists(c: BikeCategory | c.name = category) and
    hours > 0 and
    bikeStation.bikeCategories->select(c: BikeCategory | c.name = category)->first().bikes->
        select(b: Bike | b.status = 'Available')->size() > 0
post:
    result <> null and
    bikeStation.rentalRecords->exists(r: RentalRecord | r.rentalId = result)
```

**Penjelasan:**
- **Precondition**: 
  - Kategori sepeda harus valid
  - Durasi harus positif
  - Minimal ada satu sepeda tersedia
- **Postcondition**: 
  - Mengembalikan rental ID yang tidak null
  - Record rental baru tersimpan dalam sistem
  - Status sepeda berubah menjadi "Rented"

#### 3. Constraint untuk `returnBike`
```ocl
context ReturnInterface::returnBike(rentalId: String): Boolean
pre:
    bikeStation.rentalRecords->exists(r: RentalRecord | r.rentalId = rentalId) and
    bikeStation.rentalRecords->select(r: RentalRecord | r.rentalId = rentalId)->
        asSequence()->first().allocation <> null
post:
    result = true and
    let theRecord: RentalRecord = bikeStation.rentalRecords->
        select(r: RentalRecord | r.rentalId = rentalId)->asSequence()->first() in
    let theBike: Bike = theRecord.allocation@pre in
    theBike.status = 'Available'
```

**Penjelasan:**
- **Precondition**: Rental ID harus valid dan sepeda belum dikembalikan
- **Postcondition**: Sepeda kembali ke status "Available" dan masuk ke daftar completed rentals

#### 4. Constraint untuk `registerCustomer`
```ocl
context ICustomerMgt::registerCustomer(name: String, phone: String, email: String): CustomerId
pre:
    name <> '' and phone <> '' and email <> '' and
    email.indexOf('@') > 0 and
    not customer->exists(c: Customer | c.email = email)
post:
    result <> null and
    customer->exists(c: Customer | c.id = result) and
    customer->size() = customer@pre->size() + 1
```

**Penjelasan:**
- **Precondition**: 
  - Semua parameter tidak boleh kosong
  - Email harus valid (mengandung @)
  - Email harus unik
- **Postcondition**: 
  - Customer ID tidak null
  - Customer baru tersimpan
  - Jumlah customer bertambah satu

#### 5. Constraint untuk `calculateRentalCost`
```ocl
context BikeCategory::calculateRentalCost(hours: Integer): Currency
pre:
    hours > 0
post:
    result = self.hourlyRate * hours
```

**Penjelasan:**
- **Precondition**: Durasi harus positif
- **Postcondition**: Biaya = tarif per jam × durasi

### Implementasi OCL dalam Java

Setiap constraint OCL diimplementasikan dalam kode Java dengan:
1. **Validasi Precondition**: Menggunakan `if` statements dan `throw` exceptions
2. **Logic Implementation**: Implementasi logika bisnis sesuai spesifikasi
3. **Postcondition Verification**: Memastikan hasil sesuai dengan yang diharapkan

Contoh implementasi:
```java
@Override
public int getBikeAvailability(String category) {
    // OCL Precondition validation
    BikeCategory bikeCategory = station.findBikeCategory(category);
    if (bikeCategory == null) {
        throw new IllegalArgumentException("Invalid bike category");
    }
    
    // OCL Postcondition implementation
    return (int) bikeCategory.getBikes().stream()
                             .filter(bike -> "Available".equals(bike.getStatus()))
                             .count();
}
```

## 🚀 Cara Menjalankan Aplikasi

### Prerequisites
- Java Development Kit (JDK) 8 atau lebih tinggi
- IDE Java (IntelliJ IDEA, Eclipse, atau VS Code)

### Langkah-langkah:

1. **Clone atau download** semua file Java ke dalam satu direktori
2. **Compile** semua file Java:
   ```bash
   javac *.java
   ```
3. **Jalankan** aplikasi:
   ```bash
   java BikeRentalSystem
   ```

## 📱 Fitur Aplikasi

### Menu Utama
1. **Lihat Sepeda Tersedia** - Menampilkan daftar sepeda berdasarkan kategori
2. **Daftar Pelanggan** - Registrasi pelanggan baru
3. **Sewa Sepeda** - Proses penyewaan sepeda
4. **Kembalikan Sepeda** - Pengembalian sepeda yang disewa
5. **Lihat Riwayat Rental** - History rental pelanggan
6. **Kelola Inventaris (Admin)** - Manajemen sepeda dan validasi rental
7. **Keluar** - Exit aplikasi

### Kategori Sepeda Default
- **Mountain Bike** - IDR 15.000/jam
- **Road Bike** - IDR 12.000/jam  
- **City Bike** - IDR 10.000/jam

## 🎯 Fitur Unggulan

### 1. Validasi OCL Terintegrasi
Setiap operasi bisnis divalidasi menggunakan constraint OCL untuk memastikan:
- Data konsisten
- Aturan bisnis terpenuhi
- Error handling yang proper

### 2. Manajemen Mata Uang
- Type `Currency` untuk handling mata uang
- Calculation otomatis biaya rental
- Support untuk denda keterlambatan

### 3. Status Tracking
- **Available**: Sepeda siap disewa
- **Rented**: Sepeda sedang disewa
- **Maintenance**: Sepeda dalam perbaikan

### 4. Rental Record Management
- Active rentals tracking
- Completed rentals history
- Customer rental history

## 🔧 Struktur Data

### Relationship Antar Class
```
BikeStation (1) ---> (*) BikeCategory
BikeCategory (1) ---> (*) Bike
Customer (1) ---> (*) RentalRecord
RentalRecord (*) ---> (1) Bike
BikeStation (1) ---> (*) RentalRecord
```

### Status Flow
```
Bike: Available → Rented → Available
RentalRecord: active → completed
```

## 🧪 Testing OCL Constraints

Aplikasi menyediakan method `demonstrateOCLConstraints()` untuk testing:
- Calculate rental cost validation
- Customer registration validation  
- Bike availability checking
- Rental validation

## 🔒 Exception Handling

Sistem menangani berbagai jenis error:
- **IllegalArgumentException**: Parameter tidak valid
- **IllegalStateException**: State tidak valid untuk operasi
- **RuntimeException**: Error umum dalam proses bisnis

## 📚 Konsep OOP yang Diterapkan

1. **Encapsulation**: Private fields dengan getter/setter
2. **Inheritance**: Interface implementation
3. **Polymorphism**: Method overriding dalam interfaces
4. **Abstraction**: Service layer abstraction

## 🔄 Future Enhancements

- GUI Implementation
- Database Integration
- Payment Gateway Integration
- GPS Tracking untuk sepeda
- Mobile App Support
- Advanced Reporting

## 📝 Lisensi

Proyek ini dibuat untuk keperluan akademis sebagai tugas mata kuliah.

---

**Catatan**: Sistem ini mengimplementasikan OCL constraints secara eksplisit untuk memastikan integritas data dan validasi aturan bisnis yang ketat. Setiap constraint OCL diterjemahkan ke dalam kode Java dengan validasi precondition dan postcondition yang sesuai.
