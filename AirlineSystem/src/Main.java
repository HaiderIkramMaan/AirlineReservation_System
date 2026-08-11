import java.time.LocalDate;
import person.Passenger;
import person.Admin;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Airline Reservation System - Category 1 Test Harness ===");

        // 1. Instantiate Passenger
        Passenger passenger = new Passenger(
                "P101",
                "John Doe",
                "john.doe@example.com",
                "securePass123",
                "+1234567890",
                "A98765432",
                "Visa Not Required"
        );

        // 2. Instantiate Admin
        Admin admin = new Admin(
                "A501",
                "Alice Smith",
                "alice.admin@airline.com",
                "adminPass456",
                "+0987654321",
                "EMP-001",
                "Flight Operations"
        );

        // 3. Test Login and Password Verification
        System.out.println("\n--- Testing Authentication ---");
        passenger.login();
        System.out.println("Passenger password verification (valid): " + passenger.verifyPassword("securePass123"));
        System.out.println("Passenger password verification (invalid): " + passenger.verifyPassword("wrongPass"));

        admin.login();
        System.out.println("Admin password verification (valid): " + admin.verifyPassword("adminPass456"));

        // 4. Test Notification Module
        System.out.println("\n--- Testing Notifications ---");
        Notification confirmationNotif = new Notification(
                "N001",
                passenger,
                "Your booking for Flight AR-102 has been confirmed.",
                NotificationType.BOOKING_CONFIRMATION
        );
        confirmationNotif.send();

        Notification updateNotif = new Notification(
                "N002",
                passenger,
                "Flight AR-102 departure time updated to 14:30.",
                NotificationType.FLIGHT_UPDATE
        );
        updateNotif.send();

        // 5. Test Data Serialization
        System.out.println("\n--- Testing Serialization ---");
        System.out.println("Passenger Serialized JSON: " + passenger.serializeData());
        System.out.println("Admin Serialized JSON: " + admin.serializeData());

        // 6. Test Passenger Action Stubs
        System.out.println("\n--- Testing Action Stubs ---");
        passenger.searchFlights("NYC", "LON", LocalDate.now());
        admin.managePassengerRecords(passenger.getId(), "VERIFIED");

        System.out.println("\n=== Category 1 Core System Initialized Successfully ===");
    }
}
