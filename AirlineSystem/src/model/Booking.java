package model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Booking{

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private String bookingId;
    private LocalDateTime bookingDate;
    private String status;
    private Passenger passenger;
    private Flight flight;
    private Seat seat;


    Booking(String bookingId, Passenger passenger, Flight flight, Seat seat) {
        this.bookingId = bookingId;
        this.bookingDate = LocalDateTime.now();
        this.status = STATUS_PENDING;
        this.passenger = passenger;
        this.flight = flight;
        this.seat = seat;
    }

    /**
     * Marks this booking CONFIRMED. The seat is already reserved at booking
     * time (see Passenger.bookTicket()) to prevent a second passenger from
     * grabbing it while this one is mid-payment - confirmBooking() just
     * finalizes the status once payment succeeds.
     *
     * TODO (integration point for Person B): this is where
     * InvalidBookingException / SeatUnavailableException should be thrown
     * instead of the placeholder IllegalStateException below, and where
     * NotificationService.notifyBookingConfirmation() should be called once
     * those classes exist. Left as plain exceptions for now so this compiles
     * independently of Person B's package.
     */
    public void confirmBooking() {
        if (!STATUS_PENDING.equals(status)) {
            throw new IllegalStateException(
                    "Booking " + bookingId + " cannot be confirmed from status " + status);
            // TODO: replace with InvalidBookingException once available.
        }
        this.status = STATUS_CONFIRMED;
        // TODO: NotificationService.getInstance().notifyBookingConfirmation(this);
    }

    /**
     * Cancels the booking and releases the seat back to the pool.
     *
     * TODO (Person B integration): call
     * NotificationService.notifyCancellation() here once that class exists.
     */
    public void cancel() {
        if (STATUS_CANCELLED.equals(status)) {
            throw new IllegalStateException("Booking " + bookingId + " is already cancelled");
        }
        if (seat != null && !seat.isAvailable()) {
            seat.release();
        }
        this.status = STATUS_CANCELLED;
        // TODO: NotificationService.getInstance().notifyCancellation(this);
    }

    /**
     * Generates a Ticket for this booking. Only valid once the booking is
     * CONFIRMED (i.e. after payment has gone through).
     */
    public Ticket generateTicket() {
        if (!STATUS_CONFIRMED.equals(status)) {
            throw new IllegalStateException(
                    "Cannot generate a ticket for a booking that is not CONFIRMED (current status: "
                            + status + ")");
        }
        String ticketId = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String eTicketNo = "E" + bookingId;
        return new Ticket(ticketId, eTicketNo, LocalDate.now(), this);
    }

    public String getBookingId() {
        return bookingId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
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
}


