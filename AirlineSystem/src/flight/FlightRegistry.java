package flight;

import model.FileManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightRegistry {
    private static final FlightRegistry INSTANCE = new FlightRegistry();
    private final List<Flight> flights = new ArrayList<>();

    private FlightRegistry() {
        loadFromFile();
    }

    public static FlightRegistry getInstance() {
        return INSTANCE;
    }

    public void registerFlight(Flight flight) {
        if (flight == null) return;
        flight.ensureDefaultSeatScheme();
        synchronized (flights) {
            if (flight.getFlightId() != null) {
                flights.removeIf(f -> f.getFlightId() != null && f.getFlightId().equalsIgnoreCase(flight.getFlightId()));
            }
            flights.add(flight);
        }
        saveToFile();
    }

    public boolean addFlight(Flight flight) {
        if (flight == null) return false;
        registerFlight(flight);
        return true;
    }

    public boolean removeFlight(Flight flight) {
        boolean removed = flights.remove(flight);
        if (removed) saveToFile();
        return removed;
    }

    public List<Flight> getAllFlights() {
        synchronized (flights) {
            return new ArrayList<>(flights);
        }
    }

    public List<Flight> findFlights(String source, String destination, LocalDate date) {
        String s = source == null ? "" : source.trim();
        String d = destination == null ? "" : destination.trim();

        return flights.stream().filter(f -> {
            boolean originMatches = s.isEmpty() || f.getOrigin() == null ||
                    s.equalsIgnoreCase(f.getOrigin().getIataCode()) ||
                    s.equalsIgnoreCase(f.getOrigin().getCity());
            boolean destMatches = d.isEmpty() || f.getDestination() == null ||
                    d.equalsIgnoreCase(f.getDestination().getIataCode()) ||
                    d.equalsIgnoreCase(f.getDestination().getCity());
            if (!originMatches || !destMatches) return false;
            if (date == null) return true;
            if (f.getDepartureTime() == null) return true;
            return f.getDepartureTime().toLocalDate().isEqual(date);
        }).sorted((a, b) -> a.getDepartureTime().compareTo(b.getDepartureTime())).collect(Collectors.toList());
    }

    public void saveToFile() {
        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        StringBuilder content = new StringBuilder();
        for (Flight f : flights) {
            content.append(f.getFlightId()).append("|")
                    .append(f.getFlightNumber()).append("|")
                    .append(f.getDepartureTime()).append("|")
                    .append(f.getArrivalTime()).append("|")
                    .append(f.getBasePrice()).append("|")
                    .append(f.getAirline().getAirlineId()).append("|")
                    .append(f.getAirline().getName()).append("|")
                    .append(f.getAirline().getCode()).append("|")
                    .append(f.getOrigin().getAirportId()).append("|")
                    .append(f.getOrigin().getName()).append("|")
                    .append(f.getOrigin().getCity()).append("|")
                    .append(f.getOrigin().getIataCode()).append("|")
                    .append(f.getDestination().getAirportId()).append("|")
                    .append(f.getDestination().getName()).append("|")
                    .append(f.getDestination().getCity()).append("|")
                    .append(f.getDestination().getIataCode()).append(System.lineSeparator());
        }
        fm.writeToData("flights", content.toString());
    }

    public void loadFromFile() {
        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        String text = fm.readFromData("flights");
        if (text == null || text.isBlank()) return;
        if (!new java.io.File("data", "flights.dat").exists()) {
            fm.writeToData("flights", text);
        }
        String[] lines = text.split(System.lineSeparator());
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            String[] parts = line.split("\\|");
            if (parts.length < 16) continue;
            try {
                Airline airline = new Airline(parts[5], parts[6], parts[7]);
                Airport origin = new Airport(parts[8], parts[9], parts[10], parts[11]);
                Airport destination = new Airport(parts[12], parts[13], parts[14], parts[15]);
                Flight flight = new Flight(
                        parts[0],
                        parts[1],
                        LocalDateTime.parse(parts[2]),
                        LocalDateTime.parse(parts[3]),
                        Double.parseDouble(parts[4]),
                        airline,
                        origin,
                        destination
                );
                flight.ensureDefaultSeatScheme();
                flights.add(flight);
            } catch (Exception ignore) {
            }
        }
    }
}
