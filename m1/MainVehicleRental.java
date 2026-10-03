import java.util.ArrayList;
import java.util.List;

abstract class Vehicle {
    private String id;
    private String model;
    private boolean isAvailable;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
        this.isAvailable = true;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract double calculateRentalCharge(int days);
}

class Sedan extends Vehicle {
    private static final double DAILY_RATE = 50.0;

    public Sedan(String id, String model) {
        super(id, model);
    }

    @Override
    public double calculateRentalCharge(int days) {
        return days * DAILY_RATE;
    }
}

class SUV extends Vehicle {
    private static final double DAILY_RATE = 80.0;

    public SUV(String id, String model) {
        super(id, model);
    }

    @Override
    public double calculateRentalCharge(int days) {
        return days * DAILY_RATE;
    }
}

class Truck extends Vehicle {
    private static final double DAILY_RATE = 120.0;

    public Truck(String id, String model) {
        super(id, model);
    }

    @Override
    public double calculateRentalCharge(int days) {
        return days * DAILY_RATE;
    }
}

class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Customer customer;
    private Vehicle vehicle;
    private int days;
    private double totalCharge;
    private boolean isActive;

    public Rental(Customer customer, Vehicle vehicle, int days) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.totalCharge = vehicle.calculateRentalCharge(days);
        this.isActive = true;
        vehicle.setAvailable(false);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public boolean isActive() {
        return isActive;
    }

    public void returnVehicle() {
        this.isActive = false;
        vehicle.setAvailable(true);
    }
}

class RentalSystem {
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public Rental rentVehicle(Customer customer, Vehicle vehicle, int days) {
        if (!vehicle.isAvailable()) {
            System.out.println(vehicle.getModel() + " is currently unavailable.");
            return null;
        }

        Rental rental = new Rental(customer, vehicle, days);
        rentals.add(rental);
        System.out.println(vehicle.getModel() + " rented successfully by " + customer.getName() + 
                           ". Rental charge: $" + rental.getTotalCharge() + ".");
        return rental;
    }

    public void returnVehicle(Rental rental) {
        if (rental != null && rental.isActive()) {
            rental.returnVehicle();
            System.out.println(rental.getVehicle().getModel() + " returned by " + rental.getCustomer().getName() + ".");
        }
    }
}

public class MainVehicleRental {
    public static void main(String[] args) {
        RentalSystem system = new RentalSystem();

        Vehicle sedanA = new Sedan("V101", "Sedan A");
        Vehicle suvB = new SUV("V102", "SUV B");

        system.addVehicle(sedanA);
        system.addVehicle(suvB);

        Customer customer1 = new Customer("Customer 1");
        Customer customer2 = new Customer("Customer 2");
        Customer customer3 = new Customer("Customer 3");

        Rental rental1 = system.rentVehicle(customer1, sedanA, 3);
        Rental rental2 = system.rentVehicle(customer2, sedanA, 2);

        system.returnVehicle(rental1);

        Rental rental3 = system.rentVehicle(customer3, suvB, 5);
    }
}