package report;

// TODO: adjust to match your teammates' actual package names
import models.Admin;
import models.Booking;

import java.io.File;
import java.util.List;

/**
 * Report summarizing total revenue across a set of bookings.
 */
public class RevenueReport extends Report {

    private double totalRevenue;
    private List<Booking> bookingList;

    public RevenueReport(Admin generatedBy, List<Booking> bookingList) {
        super(generatedBy);
        this.bookingList = bookingList;
    }

    @Override
    public void generate() {
        totalRevenue = 0.0;
        if (bookingList == null) {
            return;
        }
        for (Booking booking : bookingList) {
            // Only count bookings that actually completed
            if (booking.getStatus() != null && "CONFIRMED".equals(booking.getStatus().toString())) {
                totalRevenue += booking.getTotalPrice();
            }
        }
    }

    @Override
    public File export(String format) {
        StringBuilder content = new StringBuilder();
        content.append("Revenue Report\n");
        content.append("Report ID: ").append(getReportId()).append("\n");
        content.append("Generated: ").append(getGeneratedDate()).append("\n");
        content.append("Total Bookings Counted: ").append(bookingList == null ? 0 : bookingList.size()).append("\n");
        content.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");

        return writeReportFile(content.toString(), format);
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
