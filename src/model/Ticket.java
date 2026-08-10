package model;
import java.time.LocalDate;

public class Ticket {

    private String ticketId;
    private String eTicketNo;
    private LocalDate issueDate;


    private final Booking booking;

    public Ticket(String ticketId, String eTicketNo, LocalDate issueDate, Booking booking) {
        this.ticketId = ticketId;
        this.eTicketNo = eTicketNo;
        this.issueDate = issueDate;
        this.booking = booking;
    }

    public void printTicket() {
        System.out.println("========================================");
        System.out.println(" E-TICKET");
        System.out.println("========================================");
        System.out.println("Ticket ID:     " + ticketId);
        System.out.println("E-Ticket No:   " + eTicketNo);
        System.out.println("Issue Date:    " + issueDate);
        if (booking != null) {
            System.out.println("Passenger:     " + booking.getPassenger().getName());
            System.out.println("Flight:        " + booking.getFlight().getFlightNo());
            System.out.println("Seat:          " + booking.getSeat().getSeatNo()
                    + " (" + booking.getSeat().getSeatClass() + ")");
        }
        System.out.println("========================================");
    }

    public String getTicketId() {
        return ticketId;
    }

    public String geteTicketNo() {
        return eTicketNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Booking getBooking() {
        return booking;
    }
}

