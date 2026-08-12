package report;

import person.Admin;
import flight.Flight;

import java.io.File;
/**
 * Report showing how full a specific flight is.
 */
public class OccupancyReport extends Report {

    private Flight targetFlight;
    private double occupancyRate;

    public OccupancyReport(Admin generatedBy, Flight targetFlight) {
        super(generatedBy);
        this.targetFlight = targetFlight;
    }

    @Override
    public void generate() {
        if (targetFlight == null) {
            occupancyRate = 0.0;
            return;
        }
        // Assumes Flight exposes getOccupancyRate() as shown in the UML
        occupancyRate = targetFlight.getOccupancyRate();
    }

    @Override
    public File export(String format) {
        StringBuilder content = new StringBuilder();
        content.append("Occupancy Report\n");
        content.append("Report ID: ").append(getReportId()).append("\n");
        content.append("Generated: ").append(getGeneratedDate()).append("\n");
        if (targetFlight != null) {
            content.append("Flight: ").append(targetFlight.getFlightNumber()).append("\n");
        }
        content.append("Occupancy Rate: ").append(String.format("%.1f", occupancyRate * 100)).append("%\n");

        return writeReportFile(content.toString(), format);
    }

    public double getOccupancyRate() {
        return occupancyRate;
    }
}
