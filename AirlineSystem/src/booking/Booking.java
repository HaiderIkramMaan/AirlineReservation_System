package booking;

import flight.Flight;
import person.Passenger;
import seat.Seat;

import java.time.LocalDateTime;

public class Booking {

    private String bookingId;
    private LocalDateTime bookingDate;
    private BookingStatus status;

    // Not listed as fields in the Booking box, but the "makes"/"composed of"/
    // "references" associations to Passenger, Flight, and Seat need to be
    // backed by something - this is how getTotalPrice() and cancel() work.
    private final Passenger passenger;
    private final Flight flight;
    private final Seat seat;
    private String persistedFlightId;
    private String persistedSeatId;
    private double persistedTotalPrice;


    public Booking(String bookingId, Passenger passenger, Flight flight, Seat seat) {
        this.bookingId = bookingId;
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.passenger = passenger;
        this.flight = flight;
        this.seat = seat;
        this.persistedFlightId = flight != null ? flight.getFlightId() : "";
        this.persistedSeatId = seat != null ? seat.getSeatId() : "";
        this.persistedTotalPrice = (flight != null && seat != null) ? seat.getPrice(flight.getBasePrice()) : 0.0;
    }

    public static Booking fromStoredRecord(String bookingId, Passenger passenger, String flightId, String seatId, double totalPrice) {
        return fromStoredRecord(bookingId, passenger, flightId, seatId, totalPrice, BookingStatus.CONFIRMED);
    }

    public static Booking fromStoredRecord(String bookingId, Passenger passenger, String flightId, String seatId, double totalPrice, BookingStatus status) {
        Booking booking = new Booking(bookingId, passenger, null, null);
        booking.persistedFlightId = flightId == null ? "" : flightId;
        booking.persistedSeatId = seatId == null ? "" : seatId;
        booking.persistedTotalPrice = totalPrice;
        booking.updateStatus(status == null ? BookingStatus.CONFIRMED : status);
        return booking;
    }


    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException(
                    "Booking " + bookingId + " cannot be confirmed from status " + status);
        }
        status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled");
        }
        if (seat != null && !seat.isAvailable()) {
            seat.release();
        }
        status = BookingStatus.CANCELLED;
    }

    public double getTotalPrice() {
        if (seat != null && flight != null) {
            return seat.getPrice(flight.getBasePrice());
        }
        return persistedTotalPrice;
    }

    public String getBookingId() {
        return bookingId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void updateStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public Seat getSeat() {
        return seat;
    }

    public String getFlightReference() {
        if (flight != null && flight.getFlightNumber() != null) {
            return flight.getFlightNumber();
        }
        return persistedFlightId;
    }

    public String getSeatReference() {
        if (seat != null && seat.getSeatId() != null) {
            return seat.getSeatId();
        }
        return persistedSeatId;
    }
}
