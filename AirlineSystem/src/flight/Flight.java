package flight;

import seat.Seat;
import seat.TravelClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Flight {

    private String flightId;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double basePrice;
    private String status;

    // Not shown as fields in the Flight box itself, but the "operated by"
    // and "origin/dest" associations in the diagram need somewhere to live.
    private final Airline airline;
    private final Airport origin;
    private final Airport destination;

    // Backs getAvailableSeats()/addSeat()/removeSeat() - the "composed of"
    // relationship to Seat implies Flight owns a seat collection.
    private final List<Seat> seats = new ArrayList<>();

    public Flight(String flightId, String flightNumber, LocalDateTime departureTime,
                  LocalDateTime arrivalTime, double basePrice, Airline airline,
                  Airport origin, Airport destination) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.basePrice = basePrice;
        this.status = "SCHEDULED";
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;

        if (airline != null) {
            airline.addFlight(this);
        }
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Seat> getAvailableSeatsByClass(TravelClass travelClass) {
        return seats.stream()
                .filter(Seat::isAvailable)
                .filter(s -> s.getTravelClass() == travelClass)
                .collect(Collectors.toList());
    }

    public double getOccupancyRate() {
        if (seats.isEmpty()) {
            return 0.0;
        }
        long booked = seats.stream().filter(s -> !s.isAvailable()).count();
        return (double) booked / seats.size();
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public boolean removeSeat(String seatId) {
        return seats.removeIf(s -> s.getSeatId().equals(seatId));
    }

    public Duration getFlightDuration() {
        return Duration.between(departureTime, arrivalTime);
    }

    public boolean isFullyBooked() {
        return !seats.isEmpty() && seats.stream().noneMatch(Seat::isAvailable);
    }

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
    }

    public String getFlightId() {
        return flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Airline getAirline() {
        return airline;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Airport getDestination() {
        return destination;
    }
}
