import java.util.*;

class vehicle {
    private String licensePlate;
    private String color;
    private double pricePerDay;
    private boolean isRented;
    private LinkedList<Rental> rentalHistory;
    // Store last 5 rentals

    public vehicle(String licensePlate, String color, double pricePerDay) {
        this.licensePlate = licensePlate;
        this.color = color;
        this.pricePerDay = pricePerDay;
        this.isRented = false;
        this.rentalHistory = new LinkedList<>();
    }
//Getters for the variables of vehicle
    public String getLicensePlate() { return licensePlate; }
    public String getColor() { return color; }
    public double getPricePerDay() { return pricePerDay; }
    public boolean isRented() { return isRented; }

    public void rentVehicle(Rental rental) {
        this.isRented = true;
        if (rentalHistory.size() == 5) {
            rentalHistory.removeFirst();
            // Remove the oldest rental if history exceeds 5
        }
        rentalHistory.add(rental);
    }

    public void returnVehicle() {
        this.isRented = false;
    }

    public double calculateRentalPrice(int days) {
        return days * pricePerDay;
    }

    public List<Rental> getRentalHistory() {
        return Collections.unmodifiableList(rentalHistory);
    }
}







class Rental {
    private vehicle vehicle;
    private Customer customer;
    private int rentalDays;
    private Date rentalDate;
    private double totalCost;

    public Rental(Vehicle vehicle, Customer customer, int rentalDays) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDays = rentalDays;
        this.rentalDate = new Date();
        this.totalCost = vehicle.calculateRentalPrice(rentalDays);
    }

    public Vehicle getVehicle() { return vehicle; }
    public Customer getCustomer() { return customer; }
    public int getRentalDays() { return rentalDays; }
    public Date getRentalDate() { return rentalDate; }
    public double getTotalCost() { return totalCost; }
}

class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public boolean rentVehicle(String licensePlate, Customer customer, int rentalDays) {
        vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle != null && !vehicle.isRented()) {
            Rental rental = new Rental(vehicle, customer, rentalDays);
            vehicle.rentVehicle(rental);
            rentals.add(rental);
            return true;
        }
        return false;  // Vehicle not available
    }

    public void returnVehicle(String licensePlate) {
        vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle != null && vehicle.isRented()) {
            vehicle.returnVehicle();
        }
    }

    private vehicle findVehicleByLicensePlate(String licensePlate) {
        return vehicles.stream()
                .filter(vehicle -> vehicle.getLicensePlate().equals(licensePlate))
                .findFirst()
                .orElse(null);
    }

    public List<Rental> getRentalHistory(String licensePlate) {
        Vehicle vehicle = findVehicleByLicensePlate(licensePlate);
        if (vehicle != null) {
            return vehicle.getRentalHistory();
        }
        return Collections.emptyList();
    }
}

public class Main {
    public static void main(String[] args) {
        RentalSystem rentalSystem = new RentalSystem();

        vehicle car1 = new Car("ABC123", "Red", 40.0);
        vehicle truck1 = new Truck("XYZ789", "Blue", 80.0);
        vehicle motorcycle1 = new Motorcycle("MNO456", "Black", 20.0);

        rentalSystem.addVehicle(car1);
        rentalSystem.addVehicle(truck1);
        rentalSystem.addVehicle(motorcycle1);

        Customer customer1 = new Customer("John Doe", "123 Elm Street", 30);
        rentalSystem.addCustomer(customer1);

        rentalSystem.rentVehicle("ABC123", customer1, 3);
        rentalSystem.rentVehicle("XYZ789", customer1, 1);

        System.out.println("Rental History for Car ABC123:");
        for (Rental rental : rentalSystem.getRentalHistory("ABC123")) {
            System.out.println("Date: " + rental.getRentalDate() + ", Total Cost: $" + rental.getTotalCost());
        }
    }
}
