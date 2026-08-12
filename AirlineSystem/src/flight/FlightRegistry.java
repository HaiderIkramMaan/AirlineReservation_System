package flight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple singleton registry for flights. Supports registration and basic
 * searching by origin/destination strings (matches IATA code or city).
 */
public class FlightRegistry {
    private static final FlightRegistry INSTANCE = new FlightRegistry();

    private final List<Flight> flights = new ArrayList<>();

    private FlightRegistry() {}

    public static FlightRegistry getInstance() {
        return INSTANCE;
    }

    public void registerFlight(Flight flight) {
        if (flight == null) return;
        synchronized (flights) {
            if (!flights.contains(flight)) flights.add(flight);
        }
    }

    public boolean removeFlight(Flight flight) {
        return flights.remove(flight);
    }

    public List<Flight> getAllFlights() {
        synchronized (flights) {
            return new ArrayList<>(flights);
        }
    }

    /**
     * Finds flights whose origin or destination matches the provided source/destination
     * by IATA code or city name (case-insensitive). If date is non-null, this filters
     * flights that depart on the same date (local date).
     */
    public List<Flight> findFlights(String source, String destination, LocalDate date) {
        if (source == null || destination == null) return new ArrayList<>();
        String s = source.trim().toLowerCase();
        String d = destination.trim().toLowerCase();

        return flights.stream().filter(f -> {
            boolean originMatches = f.getOrigin() != null && (
                    s.equalsIgnoreCase(f.getOrigin().getIataCode()) || s.equalsIgnoreCase(f.getOrigin().getCity())
            );
            boolean destMatches = f.getDestination() != null && (
                    d.equalsIgnoreCase(f.getDestination().getIataCode()) || d.equalsIgnoreCase(f.getDestination().getCity())
            );
            if (!originMatches || !destMatches) return false;
            if (date == null) return true;
            if (f.getDepartureTime() == null) return true;
            return f.getDepartureTime().toLocalDate().isEqual(date);
        }).collect(Collectors.toList());
    }
}
