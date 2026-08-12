import flight.Airline;
import flight.Airport;
import flight.Flight;
import flight.FlightRegistry;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.FileManager;
import person.Admin;
import person.Passenger;
import seat.BusinessSeat;
import seat.EconomySeat;
import seat.FirstClassSeat;
import service.AuthenticationService;
import ui.LoginView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    public static Passenger samplePassenger;
    public static Admin sampleAdmin;

    public static void main(String[] args) {
        initializeSampleData();
        launch(args);
    }

    private static void initializeSampleData() {
        File dataDir = new File("data");
        dataDir.mkdirs();

        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");

        samplePassenger = new Passenger(
                "P101",
                "John Doe",
                "john.doe@example.com",
                "securePass123",
                "+1234567890",
                "A98765432",
                "Visa Not Required"
        );
        AuthenticationService.getInstance().registerUser(samplePassenger);

        sampleAdmin = new Admin(
                "A501",
                "Alice Smith",
                "alice.admin@airline.com",
                "adminPass456",
                "+0987654321",
                "EMP-001",
                "Flight Operations"
        );
        AuthenticationService.getInstance().registerUser(sampleAdmin);

        if (needsDemoFlightSeed()) {
            seedFlights();
        }
    }

    private static boolean needsDemoFlightSeed() {
        List<Flight> flights = FlightRegistry.getInstance().getAllFlights();
        return flights.size() < 8 || flights.stream().noneMatch(f -> !f.getAvailableSeats().isEmpty());
    }

    private static void seedFlights() {
        Airline airline = new Airline("AL1", "Acme Air", "AC");
        Airline skyAir = new Airline("AL2", "SkyJet", "SJ");
        Airline gulfAir = new Airline("AL3", "Gulf Wings", "GW");

        Airport jfk = new Airport("AP1", "John F Kennedy", "New York", "NYC");
        Airport lhr = new Airport("AP2", "Heathrow", "London", "LON");
        Airport dub = new Airport("AP3", "Dublin Airport", "Dublin", "DUB");
        Airport lax = new Airport("AP4", "Los Angeles", "Los Angeles", "LAX");
        Airport sfo = new Airport("AP5", "San Francisco", "San Francisco", "SFO");
        Airport dxb = new Airport("AP6", "Dubai International", "Dubai", "DXB");
        Airport khi = new Airport("AP7", "Jinnah International", "Karachi", "KHI");
        Airport isb = new Airport("AP8", "Islamabad International", "Islamabad", "ISB");
        Airport doha = new Airport("AP9", "Hamad International", "Doha", "DOH");
        Airport fra = new Airport("AP10", "Frankfurt Airport", "Frankfurt", "FRA");

        addMissingFlight(airline, "F100", "AC100", 500.0, jfk, lhr, 0, 9, 30, 16, 45, true);
        addMissingFlight(airline, "F200", "AC200", 420.0, dub, lhr, 1, 12, 15, 18, 20, true);
        addMissingFlight(skyAir, "F300", "SJ300", 610.0, lax, sfo, 2, 7, 0, 11, 10, false);
        addMissingFlight(gulfAir, "F400", "GW400", 780.0, jfk, dxb, 3, 18, 45, 8, 0, false);
        addMissingFlight(skyAir, "F500", "SJ500", 460.0, sfo, lhr, 4, 14, 20, 22, 35, false);
        addMissingFlight(gulfAir, "F550", "GW550", 520.0, khi, lhr, 4, 21, 15, 23, 55, false);
        addMissingFlight(airline, "F600", "AC600", 390.0, lhr, dub, 5, 10, 10, 13, 40, false);
        addMissingFlight(gulfAir, "F650", "GW650", 690.0, jfk, dxb, 5, 15, 35, 1, 10, false);
        addMissingFlight(skyAir, "F700", "SJ700", 550.0, lhr, jfk, 6, 11, 10, 17, 50, false);
        addMissingFlight(airline, "F750", "AC750", 470.0, khi, doha, 6, 9, 0, 11, 40, false);
        addMissingFlight(gulfAir, "F800", "GW800", 480.0, isb, fra, 7, 13, 10, 17, 25, false);

        Flight fullFlight = addMissingFlight(airline, "F900", "AC900", 640.0, jfk, dxb, 8, 20, 5, 10, 30, false);
        if (fullFlight != null && !fullFlight.isFullyBooked()) {
            fullFlight.getSeats().forEach(seat -> seat.book());
            fullFlight.setStatus("FULL");
        }
    }

    private static Flight addMissingFlight(Airline airline, String flightId, String flightNumber, double basePrice,
                                          Airport origin, Airport destination, int dayOffset,
                                          int departureHour, int departureMinute,
                                          int arrivalHour, int arrivalMinute, boolean markUnavailable) {
        if (flightNumberExists(flightNumber)) {
            return null;
        }
        Flight flight = addDemoFlight(airline, flightId, flightNumber, basePrice, origin, destination, dayOffset, departureHour, departureMinute, arrivalHour, arrivalMinute);
        if (markUnavailable) {
            markSeatUnavailable(flight, "E-" + flightNumber + "-1");
            markSeatUnavailable(flight, "E-" + flightNumber + "-2");
            markSeatUnavailable(flight, "B-" + flightNumber + "-1");
        }
        return flight;
    }

    private static boolean flightNumberExists(String flightNumber) {
        return FlightRegistry.getInstance().getAllFlights().stream()
                .anyMatch(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber));
    }

    private static Flight addDemoFlight(Airline airline, String flightId, String flightNumber, double basePrice,
                                        Airport origin, Airport destination, int dayOffset,
                                        int departureHour, int departureMinute,
                                        int arrivalHour, int arrivalMinute) {
        Flight flight = new Flight(
                flightId,
                flightNumber,
                LocalDateTime.now().plusDays(dayOffset).withHour(departureHour).withMinute(departureMinute),
                LocalDateTime.now().plusDays(dayOffset).withHour(arrivalHour).withMinute(arrivalMinute),
                basePrice,
                airline,
                origin,
                destination
        );

        for (int r = 1; r <= 10; r++) {
            flight.addSeat(new EconomySeat("E-" + flightNumber + "-" + r, "E" + r, r, 1));
        }
        for (int r = 1; r <= 4; r++) {
            flight.addSeat(new BusinessSeat("B-" + flightNumber + "-" + r, "B" + r, r, 2));
        }
        for (int r = 1; r <= 2; r++) {
            flight.addSeat(new FirstClassSeat("F-" + flightNumber + "-" + r, "F" + r, r, 3));
        }

        FlightRegistry.getInstance().registerFlight(flight);
        return flight;
    }

    private static void markSeatUnavailable(Flight flight, String seatId) {
        if (flight == null) return;
        for (var seat : flight.getSeats()) {
            if (seat.getSeatId().equals(seatId)) {
                seat.book();
                return;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Airline Reservation System");
        primaryStage.setScene(new Scene(new LoginView(primaryStage), 500, 280));
        primaryStage.show();
    }
}
