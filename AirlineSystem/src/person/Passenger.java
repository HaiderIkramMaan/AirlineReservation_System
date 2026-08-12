package person;

import booking.Booking;
import flight.Flight;
import payment.Payment;
import seat.Seat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Passenger extends Person {
    private String passportNumber;
    private String visaEntryRequirements;

    private final List<Booking> bookings = new ArrayList<>();

    public Passenger() {
        super();
    }

    public Passenger(String id, String name, String email, String password, String phone,
                     String passportNumber, String visaEntryRequirements) {
        super(id, name, email, password, phone);
        this.passportNumber = passportNumber;
        this.visaEntryRequirements = visaEntryRequirements;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate date) {
        System.out.println("Searching flights from " + source + " to " + destination + " on " + date);
        return flight.FlightRegistry.getInstance().findFlights(source, destination, date);
    }

    public Booking bookFlight(Flight flight, Seat seat, Payment payment) {
        System.out.println("Booking ticket for passenger: " + getName());
        if (flight == null || seat == null) return null;

        boolean paid = (payment == null) || payment.processPayment();
        if (!paid) {
            System.err.println("Payment failed for passenger: " + getName());
            return null;
        }

        boolean seatBooked = seat.book();
        if (!seatBooked) {
            System.err.println("Failed to reserve seat " + seat.getSeatNumber());
            return null;
        }

        Booking booking = new Booking(UUID.randomUUID().toString(), this, flight, seat);
        booking.confirm();
        bookings.add(booking);
        return booking;
    }

    // Backwards-compatible alias
    public Booking bookTicket(Flight flight, Seat seat, Payment payment) {
        return bookFlight(flight, seat, payment);
    }

    public boolean cancelBooking(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                b.cancel();
                return true;
            }
        }
        System.err.println("No booking found with id: " + bookingId);
        return false;
    }

    public List<Booking> viewBookingHistory() {
        System.out.println("Retrieving booking history for passenger: " + getName());
        return new ArrayList<>(bookings);
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getVisaEntryRequirements() {
        return visaEntryRequirements;
    }

    public void setVisaEntryRequirements(String visaEntryRequirements) {
        this.visaEntryRequirements = visaEntryRequirements;
    }

    @Override
    public String serializeData() {
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"passportNumber\":\"%s\",\"visaEntryRequirements\":\"%s\"}",
                getId(), getName(), getEmail(), getPhone(), passportNumber, visaEntryRequirements);
    }
}
