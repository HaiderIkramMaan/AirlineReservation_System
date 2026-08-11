package model;

import seat.Seat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Flight {

    private String flightId;
    private String flightNo;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private final List<Seat> seats;

    private final Airline airline;
    private final Airport origin;
    private final Airport destination;



    public Flight(String flightId, String flightNo, LocalDateTime departureTime,
                  LocalDateTime arrivalTime, Airline airline, Airport origin,
                  Airport destination, List<Seat> seats) {
        this.flightId = flightId;
        this.flightNo = flightNo;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.seats = new ArrayList<>(seats);

        // Self-registers with its Airline, same pattern as Person <-> AuthenticationService.
        if (airline != null) {
            airline.addFlight(this);
        }
    }


    public Flight(String flightId, String flightNo, LocalDateTime departureTime,
                  LocalDateTime arrivalTime, Airline airline, Airport origin,
                  Airport destination, int businessRows, int economyRows) {
        this(flightId, flightNo, departureTime, arrivalTime, airline, origin,
                destination, generateSeats(flightId, businessRows, economyRows));
    }

    private static List<Seat> generateSeats(String flightId, int businessRows, int economyRows) {
        List<Seat> generated = new ArrayList<>();
        char[] businessLetters = {'A', 'B', 'C', 'D'};
        char[] economyLetters = {'A', 'B', 'C', 'D', 'E', 'F'};

        for (int row = 1; row <= businessRows; row++) {
            for (char letter : businessLetters) {
                String seatNo = row + String.valueOf(letter);
                generated.add(new Seat(flightId + "-" + seatNo, seatNo, "Business"));
            }
        }
        for (int row = 1; row <= economyRows; row++) {
            for (char letter : economyLetters) {
                String seatNo = (row + businessRows) + String.valueOf(letter);
                generated.add(new Seat(flightId + "-" + seatNo, seatNo, "Economy"));
            }
        }
        return generated;
    }

    public boolean checkAvailability() {
        return seats.stream().anyMatch(Seat::isAvailable);
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.toList());
    }

    public String getFlightId() {
        return flightId;
    }

    public String getFlightNo() {
        return flightNo;
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

    public List<Seat> getSeats() {
        return Collections.unmodifiableList(seats);
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

