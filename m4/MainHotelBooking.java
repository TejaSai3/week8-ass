import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

abstract class Room {
    private String roomNumber;

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public abstract double calculatePrice(long numberOfNights);
    public abstract String getRoomType();
}

class StandardRoom extends Room {
    private static final double NIGHTLY_RATE = 100.0;

    public StandardRoom(String roomNumber) {
        super(roomNumber);
    }

    @Override
    public double calculatePrice(long numberOfNights) {
        return numberOfNights * NIGHTLY_RATE;
    }

    @Override
    public String getRoomType() {
        return "Standard Room";
    }
}

class DeluxeRoom extends Room {
    private static final double NIGHTLY_RATE = 200.0;

    public DeluxeRoom(String roomNumber) {
        super(roomNumber);
    }

    @Override
    public double calculatePrice(long numberOfNights) {
        return numberOfNights * NIGHTLY_RATE;
    }

    @Override
    public String getRoomType() {
        return "Deluxe Room";
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

class Reservation {
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private boolean isCancelled = false;

    public Reservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        long nights = checkOutDate.toEpochDay() - checkInDate.toEpochDay();
        this.totalPrice = room.calculatePrice(nights);
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    public boolean overlapsWith(LocalDate start, LocalDate end) {
        if (isCancelled) return false;
        return checkInDate.isBefore(end) && start.isBefore(checkOutDate);
    }
}

class HotelBookingService {
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber().equals(room.getRoomNumber()) && res.overlapsWith(checkIn, checkOut)) {
                return false;
            }
        }
        return true;
    }

    public Reservation createReservation(Customer customer, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!isRoomAvailable(room, checkIn, checkOut)) {
            System.out.println(room.getRoomType() + " " + room.getRoomNumber() + " is not available from " + checkIn + " to " + checkOut + ".");
            return null;
        }

        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(reservation);
        System.out.println("Reservation confirmed for " + customer.getName() + ", " + 
                           room.getRoomType() + " " + room.getRoomNumber() + 
                           " (" + checkIn + " to " + checkOut + "). Price: $" + reservation.getTotalPrice() + ".");
        return reservation;
    }

    public void cancelReservation(Reservation reservation, LocalDate cancellationDate) {
        if (reservation == null || reservation.isCancelled()) {
            return;
        }
        if (cancellationDate.isBefore(reservation.getCheckInDate())) {
            reservation.cancel();
            System.out.println("Reservation for " + reservation.getCustomer().getName() + ", " + 
                               reservation.getRoom().getRoomType() + " " + reservation.getRoom().getRoomNumber() + 
                               " (" + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate() + ") cancelled successfully.");
        } else {
            System.out.println("Cancellation failed. Cancellation deadline passed.");
        }
    }
}

public class MainHotelBooking {
    public static void main(String[] args) {
        HotelBookingService bookingService = new HotelBookingService();

        Room room101 = new StandardRoom("101");
        Room room201 = new DeluxeRoom("201");
        bookingService.addRoom(room101);
        bookingService.addRoom(room201);

        Customer customerA = new Customer("Customer A");
        Customer customerB = new Customer("Customer B");
        Customer customerC = new Customer("Customer C");

        LocalDate jan1 = LocalDate.of(2026, 1, 1);
        LocalDate jan3 = LocalDate.of(2026, 1, 3);
        LocalDate jan5 = LocalDate.of(2026, 1, 5);
        LocalDate jan7 = LocalDate.of(2026, 1, 7);

        if (bookingService.isRoomAvailable(room101, jan1, jan5)) {
            System.out.println("Standard Room 101 is available from " + jan1 + " to " + jan5 + ".");
        }

        Reservation resA = bookingService.createReservation(customerA, room101, jan1, jan5);
        Reservation resB = bookingService.createReservation(customerB, room101, jan3, jan7);

        bookingService.cancelReservation(resA, LocalDate.of(2025, 12, 31));

        LocalDate feb10 = LocalDate.of(2026, 2, 10);
        LocalDate feb12 = LocalDate.of(2026, 2, 12);
        Reservation resC = bookingService.createReservation(customerC, room201, feb10, feb12);
    }
}