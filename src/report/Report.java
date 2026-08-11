package report;

// TODO: Either Rahim or Haider plz work on the Admin class so we can properly test this
// placing the Admin class (per the UML, Report.generatedBy is an Admin).
import models.Admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Abstract base class for all generated reports (Revenue, Occupancy,
 * FlightSchedule, ...). Concrete subclasses implement generate() to
 * compute their own data and export(format) to write it out to a File.
 */
public abstract class Report {

    private String reportId;
    private LocalDateTime generatedDate;
    private Admin generatedBy;

    protected Report(Admin generatedBy) {
        this.reportId = UUID.randomUUID().toString();
        this.generatedDate = LocalDateTime.now();
        this.generatedBy = generatedBy;
    }

    /**
     * Computes/populates this report's data. Must be called before export().
     */
    public abstract void generate();

    /**
     * Writes this report out in the given format (e.g. "PDF", "CSV", "TXT").
     *
     * @param format the desired output format
     * @return the exported File, or null if export failed
     */
    public abstract File export(String format);

    public String getReportId() {
        return reportId;
    }

    public LocalDateTime getGeneratedDate() {
        return generatedDate;
    }

    public Admin getGeneratedBy() {
        return generatedBy;
    }

    /**
     * Shared helper for subclasses: writes the given content to a file named
     * after this report's id, timestamp and extension, inside /reports.
     * Kept protected so each concrete report can reuse it from export().
     */
    protected File writeReportFile(String content, String extension) {
        File dir = new File("reports");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = generatedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = getClass().getSimpleName() + "_" + timestamp + "." + extension.toLowerCase();
        File file = new File(dir, fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            return file;
        } catch (IOException e) {
            System.err.println("Failed to export report: " + e.getMessage());
            return null;
        }
    }
}
