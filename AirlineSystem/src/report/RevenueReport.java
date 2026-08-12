package report;

// TODO: adjust to match your teammates' actual package names
import person.Admin;
import booking.Booking;
import booking.BookingStatus;
import model.FileManager;

import java.io.File;
import java.util.ArrayList;
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
        List<Booking> sourceBookings = bookingList;
        if (sourceBookings == null) {
            sourceBookings = loadBookingsFromFile();
        }
        if (sourceBookings == null) {
            return;
        }
        for (Booking booking : sourceBookings) {
            // Only count bookings that actually completed
            if (booking.getStatus() != null && "CONFIRMED".equals(booking.getStatus().toString())) {
                totalRevenue += booking.getTotalPrice();
            }
        }
        bookingList = sourceBookings;
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

    private List<Booking> loadBookingsFromFile() {
        FileManager fileManager = FileManager.getInstance();
        fileManager.setFilePath("data");
        String text = fileManager.readFromFile("bookings.txt");
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        List<Booking> parsedBookings = new ArrayList<>();
        String[] lines = text.split(System.lineSeparator());
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\|");
            if (parts.length < 5) {
                continue;
            }

            try {
                double amount = Double.parseDouble(parts[4]);
                BookingStatus status = BookingStatus.CONFIRMED;
                if (parts.length >= 6) {
                    try {
                        status = BookingStatus.valueOf(parts[5].trim().toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                        status = BookingStatus.CONFIRMED;
                    }
                }
                parsedBookings.add(new FileBackedBooking(amount, status));
            } catch (NumberFormatException ignored) {
            }
        }
        return parsedBookings;
    }

    private static final class FileBackedBooking extends Booking {
        private final double storedTotal;

        private FileBackedBooking(double storedTotal, BookingStatus status) {
            super("FILE", null, null, null);
            this.storedTotal = storedTotal;
            updateStatus(status == null ? BookingStatus.CONFIRMED : status);
        }

        @Override
        public double getTotalPrice() {
            return storedTotal;
        }
    }
}
