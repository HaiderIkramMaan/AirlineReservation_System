package report;

// TODO: adjust to match your teammates' actual package names
import person.Admin;
import flight.Flight;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Report listing scheduled flights, ordered by departure time.
 */
public class FlightScheduleReport extends Report {

    private List<Flight> flightList;

    public FlightScheduleReport(Admin generatedBy, List<Flight> flightList) {
        super(generatedBy);
        this.flightList = flightList;
    }

    @Override
    public void generate() {
        if (flightList == null) {
            return;
        }
        // Sort in place by departure time so the exported schedule reads chronologically
        flightList.sort(Comparator.comparing(Flight::getDepartureTime));
    }

    @Override
    public File export(String format) {
        StringBuilder content = new StringBuilder();
        content.append("Flight Schedule Report\n");
        content.append("Report ID: ").append(getReportId()).append("\n");
        content.append("Generated: ").append(getGeneratedDate()).append("\n\n");

        if (flightList != null) {
            for (Flight flight : flightList) {
                content.append(flight.getFlightNumber())
                        .append(" | Departs: ").append(flight.getDepartureTime())
                        .append(" | Arrives: ").append(flight.getArrivalTime())
                        .append("\n");
            }
        }

        return writeReportFile(content.toString(), format);
    }

    public List<Flight> getFlightList() {
        return flightList == null ? Collections.emptyList() : flightList;
    }
}
