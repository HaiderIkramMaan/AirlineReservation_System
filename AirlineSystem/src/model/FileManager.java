package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {
    private static final FileManager INSTANCE = new FileManager();

    private String filePath;

    private FileManager() {
        this.filePath = "./data/";
    }

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    public static FileManager getInstance() {
        return INSTANCE;
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

    public boolean writeToData(String filename, String content) {
        return writeToFile(filename, content, ".dat");
    }

    public String readFromData(String filename) {
        String dataFile = normalizeFileName(filename, ".dat");
        String legacyFile = normalizeFileName(filename, ".json");

        File datFile = new File(filePath, dataFile);
        if (datFile.exists()) {
            return readFromFile(dataFile);
        }

        File jsonFile = new File(filePath, legacyFile);
        if (jsonFile.exists()) {
            return readFromFile(legacyFile);
        }

        return "";
    }

    public boolean writeToFile(String filename, String content, String extension) {
        String normalized = normalizeFileName(filename, extension);
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, normalized);
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(content);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            return false;
        }
    }

    public boolean writeToJson(String filename, String content) {
        return writeToFile(filename, content, ".json");
    }

    public String readFromJson(String filename) {
        String jsonFile = normalizeFileName(filename, ".json");
        File file = new File(filePath, jsonFile);
        if (file.exists()) {
            return readFromFile(jsonFile);
        }
        return readFromData(filename);
    }

    private String normalizeFileName(String filename, String extension) {
        if (filename == null || filename.trim().isEmpty()) {
            return extension.startsWith(".") ? "data" + extension : "data";
        }
        String trimmed = filename.trim();
        if (trimmed.toLowerCase().endsWith(extension.toLowerCase())) {
            return trimmed;
        }
        return trimmed + extension;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Loads lines from a file in the configured filePath directory; returns null if missing
    public java.util.List<String> loadFromFile(String filename) {
        try {
            java.nio.file.Path p = java.nio.file.Paths.get(filePath, filename);
            if (!java.nio.file.Files.exists(p)) return null;
            return java.nio.file.Files.readAllLines(p);
        } catch (java.io.IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            return null;
        }
    }
}
