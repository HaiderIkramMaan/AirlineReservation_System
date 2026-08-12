package person;

import flight.Airline;
import flight.Airport;
import flight.Flight;
import flight.FlightRegistry;
import report.Report;
import report.RevenueReport;
import report.OccupancyReport;
import report.FlightScheduleReport;

import java.time.LocalDateTime;

public class Admin extends Person {
    private String employeeId;
    private String department;

    public Admin() {
        super();
    }

    public Admin(String id, String name, String email, String password, String phone,
                 String employeeId, String department) {
        super(id, name, email, password, phone);
        this.employeeId = employeeId;
        this.department = department;
    }

    public boolean addFlight(Flight flight) {
        if (flight == null) {
            return false;
        }
        System.out.println("Admin " + getName() + " adding flight " + flight.getFlightNumber() + ".");
        FlightRegistry.getInstance().registerFlight(flight);
        return true;
    }

    public boolean addFlight(String flightId, String flightNumber, LocalDateTime departureTime,
                             LocalDateTime arrivalTime, double basePrice,
                             Airline airline, Airport origin, Airport destination) {
        if (flightId == null || flightNumber == null || departureTime == null || arrivalTime == null) {
            return false;
        }
        Flight flight = new Flight(flightId, flightNumber, departureTime, arrivalTime, basePrice, airline, origin, destination);
        return addFlight(flight);
    }

    public boolean updateFlight(String flightId) {
        System.out.println("Admin " + getName() + " updating flight: " + flightId);
        return true;
    }

    public boolean removeFlight(String flightId) {
        System.out.println("Admin " + getName() + " removing flight: " + flightId);
        for (Flight flight : FlightRegistry.getInstance().getAllFlights()) {
            if (flight.getFlightId().equalsIgnoreCase(flightId) || flight.getFlightNumber().equalsIgnoreCase(flightId)) {
                return FlightRegistry.getInstance().removeFlight(flight);
            }
        }
        return false;
    }

    public Report generateReport(String type) {
        System.out.println("Generating report of type: " + type);
        switch (type) {
            case "Revenue":
                return new RevenueReport(this, null);
            case "Occupancy":
                return new OccupancyReport(this, null);
            case "FlightSchedule":
                return new FlightScheduleReport(this, null);
            default:
                return null;
        }
    }

    public void managePassengerRecords(String passengerId, String action) {
        System.out.println("Admin " + getName() + " performed action '" + action + "' on passenger ID: " + passengerId);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String serializeData() {
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"employeeId\":\"%s\",\"department\":\"%s\"}",
                getId(), getName(), getEmail(), getPhone(), employeeId, department);
    }
}
