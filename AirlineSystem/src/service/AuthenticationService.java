package service;

import model.FileManager;
import person.Admin;
import person.Passenger;
import person.Person;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationService {
    private static final AuthenticationService INSTANCE = new AuthenticationService();
    private final Map<String, Person> registeredUsers = new ConcurrentHashMap<>();
    private final Map<String, Person> activeSessions = new ConcurrentHashMap<>();

    private AuthenticationService() {
        loadUsersFromFile();
    }

    public static AuthenticationService getInstance() {
        return INSTANCE;
    }

    public void registerUser(Person person) {
        if (person == null || person.getId() == null) {
            throw new IllegalArgumentException("Person and Person id must not be null");
        }
        if (person.getEmail() != null) {
            for (Person existing : registeredUsers.values()) {
                if (person.getEmail().equalsIgnoreCase(existing.getEmail())) {
                    return;
                }
            }
        }
        registeredUsers.put(person.getId(), person);
        saveUsersToFile();
    }

    public boolean authenticate(String id, String password) {
        if (id == null || password == null) return false;
        Person person = registeredUsers.get(id);
        if (person == null || !person.checkPassword(password)) return false;
        activeSessions.put(id, person);
        return true;
    }

    public Person authenticateByEmail(String email, String password) {
        if (email == null || password == null) return null;
        for (Person p : registeredUsers.values()) {
            if (email.equalsIgnoreCase(p.getEmail()) && p.checkPassword(password)) {
                activeSessions.put(p.getId(), p);
                return p;
            }
        }
        return null;
    }

    public void loginUser(Person person) {
        if (person != null && person.getId() != null) {
            activeSessions.put(person.getId(), person);
        }
    }

    public void logoutUser(String id) {
        activeSessions.remove(id);
    }

    public boolean isLoggedIn(String id) {
        return activeSessions.containsKey(id);
    }

    public Person getActiveUser(String id) {
        return activeSessions.get(id);
    }

    public List<Person> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers.values());
    }

    public void saveUsersToFile() {
        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        StringBuilder content = new StringBuilder();
        for (Person p : registeredUsers.values()) {
            if (p instanceof Admin) {
                Admin a = (Admin) p;
                content.append("ADMIN|")
                        .append(a.getId()).append("|")
                        .append(a.getName()).append("|")
                        .append(a.getEmail()).append("|")
                        .append(a.getPassword()).append("|")
                        .append(a.getPhone()).append("|")
                        .append(a.getEmployeeId()).append("|")
                        .append(a.getDepartment()).append(System.lineSeparator());
            } else if (p instanceof Passenger) {
                Passenger pa = (Passenger) p;
                content.append("PASSENGER|")
                        .append(pa.getId()).append("|")
                        .append(pa.getName()).append("|")
                        .append(pa.getEmail()).append("|")
                        .append(pa.getPassword()).append("|")
                        .append(pa.getPhone()).append("|")
                        .append(pa.getPassportNumber()).append("|")
                        .append(pa.getVisaEntryRequirements()).append(System.lineSeparator());
            }
        }
        fm.writeToData("users", content.toString());
    }

    public void loadUsersFromFile() {
        FileManager fm = FileManager.getInstance();
        fm.setFilePath("data");
        File datFile = new File("data/users.dat");
        if (!datFile.exists()) {
            File legacyJsonFile = new File("data/users.json");
            if (!legacyJsonFile.exists()) return;
        }
        String text = fm.readFromData("users");
        if (text == null || text.isBlank()) return;
        if (!datFile.exists()) {
            fm.writeToData("users", text);
        }
        String[] lines = text.split(System.lineSeparator());
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            String[] parts = line.split("\\|");
            if (parts.length < 7) continue;
            if ("ADMIN".equals(parts[0])) {
                Admin admin = new Admin(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts.length > 7 ? parts[7] : "");
                registeredUsers.put(admin.getId(), admin);
            } else if ("PASSENGER".equals(parts[0])) {
                Passenger passenger = new Passenger(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts.length > 7 ? parts[7] : "");
                registeredUsers.put(passenger.getId(), passenger);
            }
        }
    }
}
