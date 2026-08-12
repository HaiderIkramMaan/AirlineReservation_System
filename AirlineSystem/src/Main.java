import flight.Airline;
import flight.Airport;
import flight.Flight;
import flight.FlightRegistry;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import person.Admin;
import person.Passenger;
import seat.BusinessSeat;
import seat.EconomySeat;
import seat.FirstClassSeat;
import ui.LoginView;

import java.time.LocalDateTime;

public class Main extends Application {
    public static Passenger samplePassenger;
    public static Admin sampleAdmin;

    public static void main(String[] args) {
        initializeSampleData();
        launch(args);
    }

    private static void initializeSampleData() {
        samplePassenger = new Passenger(
                "P101",
                "John Doe",
                "john.doe@example.com",
                "securePass123",
                "+1234567890",
                "A98765432",
                "Visa Not Required"
        );

        sampleAdmin = new Admin(
                "A501",
                "Alice Smith",
                "alice.admin@airline.com",
                "adminPass456",
                "+0987654321",
                "EMP-001",
                "Flight Operations"
        );

        Airline airline = new Airline("AL1", "Acme Air", "AC");
        Airport jfk = new Airport("AP1", "John F Kennedy", "New York", "NYC");
        Airport lhr = new Airport("AP2", "Heathrow", "London", "LON");
        Airport dub = new Airport("AP3", "Dublin Airport", "Dublin", "DUB");

        Flight flight1 = new Flight(
                "F100",
                "AC100",
                LocalDateTime.now().plusDays(1).withHour(9).withMinute(30),
                LocalDateTime.now().plusDays(1).withHour(16).withMinute(45),
                500.0,
                airline,
                jfk,
                lhr
        );
        for (int r = 1; r <= 8; r++) {
            flight1.addSeat(new EconomySeat("E-" + r, "E" + r, r, 1));
        }
        for (int r = 1; r <= 4; r++) {
            flight1.addSeat(new BusinessSeat("B-" + r, "B" + r, r, 2));
        }
        flight1.addSeat(new FirstClassSeat("F-1", "F1", 1, 3));

        Flight flight2 = new Flight(
                "F200",
                "AC200",
                LocalDateTime.now().plusDays(2).withHour(12).withMinute(15),
                LocalDateTime.now().plusDays(2).withHour(18).withMinute(20),
                420.0,
                airline,
                dub,
                lhr
        );
        for (int r = 1; r <= 6; r++) {
            flight2.addSeat(new EconomySeat("E2-" + r, "E2" + r, r, 1));
        }
        flight2.addSeat(new BusinessSeat("B2-1", "B2-1", 1, 2));
        flight2.addSeat(new FirstClassSeat("F2-1", "F2-1", 1, 3));

        FlightRegistry.getInstance().registerFlight(flight1);
        FlightRegistry.getInstance().registerFlight(flight2);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Airline Reservation System");
        primaryStage.setScene(new Scene(new LoginView(primaryStage), 480, 260));
        primaryStage.show();
    }
}
