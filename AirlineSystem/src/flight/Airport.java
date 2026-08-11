package flight;

public class Airport {

    private String airportId;
    private String name;
    private String city;
    private String iataCode;

    public Airport(String airportId, String name, String city, String iataCode) {
        this.airportId = airportId;
        this.name = name;
        this.city = city;
        this.iataCode = iataCode;
    }

    public String getAirportId() {
        return airportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    @Override
    public String toString() {
        return name + " (" + iataCode + "), " + city;
    }
}
