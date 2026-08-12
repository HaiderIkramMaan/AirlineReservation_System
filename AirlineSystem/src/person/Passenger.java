package person;

import booking.Booking;
import booking.BookingStatus;
import flight.Flight;
import flight.FlightRegistry;
import model.FileManager;
import payment.Payment;
import seat.Seat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        fm.appendToFile(
                "bookings.txt",
                booking.getBookingId() + "|" + getId() + "|" + flight.getFlightId() + "|" + seat.getSeatId() + "|" + booking.getTotalPrice() + "|" + booking.getStatus()
        );
        return booking;
    }

    // Backwards-compatible alias
    public Booking bookTicket(Flight flight, Seat seat, Payment payment) {
        return bookFlight(flight, seat, payment);
    }

    public boolean cancelBooking(String bookingId) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                if (b.getStatus() == BookingStatus.CANCELLED) {
                    return false;
                }
                if (b.getFlight() != null && b.getSeat() != null) {
                    b.cancel();
                } else {
                    releaseSeatFromReferences(b.getFlightReference(), b.getSeatReference());
                    b.updateStatus(BookingStatus.CANCELLED);
                }
                persistBookingStatus(bookingId, BookingStatus.CANCELLED);
                return true;
            }
        }
        System.err.println("No booking found with id: " + bookingId);
        return false;
    }

    public List<Booking> viewBookingHistory() {
        System.out.println("Retrieving booking history for passenger: " + getName());
        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        File bookingFile = new File("data/bookings.txt");
        if (bookingFile.exists()) {
            String text = fm.readFromFile("bookings.txt");
            String[] lines = text.split(System.lineSeparator());
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && getId().equals(parts[1])) {
                    String bookingId = parts[0];
                    if (containsBooking(bookingId)) {
                        continue;
                    }
                    String flightId = parts[2];
                    String seatId = parts[3];
                    double totalPrice;
                    try {
                        totalPrice = Double.parseDouble(parts[4]);
                    } catch (NumberFormatException ex) {
                        totalPrice = 0.0;
                    }
                    BookingStatus status = BookingStatus.CONFIRMED;
                    if (parts.length >= 6) {
                        try {
                            status = BookingStatus.valueOf(parts[5].trim().toUpperCase());
                        } catch (IllegalArgumentException ignored) {
                            status = BookingStatus.CONFIRMED;
                        }
                    }
                    bookings.add(Booking.fromStoredRecord(bookingId, this, flightId, seatId, totalPrice, status));
                }
            }
        }
        return new ArrayList<>(bookings);
    }

    private void releaseSeatFromReferences(String flightId, String seatId) {
        if (flightId == null || seatId == null || flightId.isBlank() || seatId.isBlank()) {
            return;
        }
        for (Flight flight : FlightRegistry.getInstance().getAllFlights()) {
            if (!flightId.equalsIgnoreCase(flight.getFlightId()) && !flightId.equalsIgnoreCase(flight.getFlightNumber())) {
                continue;
            }
            for (Seat seat : flight.getSeats()) {
                if (seatId.equalsIgnoreCase(seat.getSeatId())) {
                    seat.release();
                    return;
                }
            }
        }
    }

    private void persistBookingStatus(String bookingId, BookingStatus newStatus) {
        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        String text = fm.readFromFile("bookings.txt");
        if (text == null || text.isBlank()) {
            return;
        }

        String[] lines = text.split(System.lineSeparator());
        StringBuilder updated = new StringBuilder();
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length >= 5 && bookingId.equals(parts[0])) {
                String statusText = newStatus.toString();
                String rewritten = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|" + parts[4] + "|" + statusText;
                updated.append(rewritten).append(System.lineSeparator());
            } else {
                updated.append(line).append(System.lineSeparator());
            }
        }
        Path bookingPath = Path.of("data", "bookings.txt");
        try {
            Files.writeString(bookingPath, updated.toString());
        } catch (IOException ex) {
            System.err.println("Failed to persist booking status: " + ex.getMessage());
        }
    }

    private boolean containsBooking(String bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return true;
            }
        }
        return false;
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
