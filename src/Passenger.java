import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Passenger extends Person {
    private String passportNumber;
    private String visaEntryRequirements;

    public Passenger() {
        super();
    }

    public Passenger(String id, String name, String email, String password, String phone,
                     String passportNumber, String visaEntryRequirements) {
        super(id, name, email, password, phone);
        this.passportNumber = passportNumber;
        this.visaEntryRequirements = visaEntryRequirements;
    }

    public List<Object> searchFlights(String source, String destination, LocalDate date) {
        System.out.println("Searching flights from " + source + " to " + destination + " on " + date);
        return new ArrayList<>();
    }

    public Object bookTicket(Object flight, Object seat, Object payment) {
        System.out.println("Booking ticket for passenger: " + getName());
        return null;
    }

    public boolean cancelBooking(String bookingId) {
        System.out.println("Cancelling booking " + bookingId + " for passenger: " + getName());
        return true;
    }

    public List<Object> viewBookingHistory() {
        System.out.println("Retrieving booking history for passenger: " + getName());
        return new ArrayList<>();
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
