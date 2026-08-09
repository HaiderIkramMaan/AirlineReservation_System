import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class FileManager {
    private String filePath;

    public FileManager() {
        this.filePath = "./data/";
    }

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    public boolean createFile(String filename) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            return file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFile(String filename) {
        File file = new File(filePath, filename);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public boolean appendToFile(String filename, String content) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(content + System.lineSeparator());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
            return false;
        }
    }

    public String readFromFile(String filename) {
        try {
            File file = new File(filePath, filename);
            if (!file.exists()) return "";
            return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "";
        }
    }

    public boolean writeToJson(String filename, String content) {
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(content);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing JSON: " + e.getMessage());
            return false;
        }
    }

    public String readFromJson(String filename) {
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }
        return readFromFile(filename);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
