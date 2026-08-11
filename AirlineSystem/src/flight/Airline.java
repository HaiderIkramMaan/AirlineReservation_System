package flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Airline {

    private String airlineId;
    private String name;
    private String code;

    // Not shown as a field in the diagram, but getFlights() needs a backing
    // list from somewhere. Flight registers itself here in its constructor
    // (same self-registration pattern used for Person/AuthenticationService).
    private final List<Flight> flights = new ArrayList<>();

    public Airline(String airlineId, String name, String code) {
        this.airlineId = airlineId;
        this.name = name;
        this.code = code;
    }

    public List<Flight> getFlights() {
        return Collections.unmodifiableList(flights);
    }

    /**
     * Package-visible hook Flight calls in its constructor to register
     * itself with its operating airline.
     */
    void addFlight(Flight flight) {
        flights.add(flight);
    }

    void removeFlight(Flight flight) {
        flights.remove(flight);
    }

    public String getAirlineId() {
        return airlineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
