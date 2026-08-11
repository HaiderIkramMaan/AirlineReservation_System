package service;

import model.Flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FlightRegistry {

    private static final FlightRegistry INSTANCE = new FlightRegistry();

    private final Map<String, Flight> flights = new ConcurrentHashMap<>();

    private FlightRegistry() {
    }

    public static FlightRegistry getInstance() {
        return INSTANCE;
    }

    public void register(Flight flight) {
        flights.put(flight.getFlightId(), flight);
    }

    public void remove(String flightId) {
        flights.remove(flightId);
    }

    public Flight get(String flightId) {
        return flights.get(flightId);
    }

    public List<Flight> getAllFlights() {
        return Collections.unmodifiableList(new ArrayList<>(flights.values()));
    }
}
