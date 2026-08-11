package service;

import airline.reservation.model.Person;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AuthenticationService {

    private static final AuthenticationService INSTANCE = new AuthenticationService();

    // All known users, keyed by id. Populated whenever a Person is constructed.
    private final Map<String, Person> registeredUsers = new ConcurrentHashMap<>();

    // Currently logged-in users, keyed by id.
    private final Map<String, Person> activeSessions = new ConcurrentHashMap<>();

    private AuthenticationService() {
    }

    public static AuthenticationService getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a Person so they can later be authenticated. Called
     * automatically from Person's constructor - not normally called directly.
     */
    public void registerUser(Person person) {
        if (person == null || person.getId() == null) {
            throw new IllegalArgumentException("Person and Person id must not be null");
        }
        registeredUsers.put(person.getId(), person);
    }

    /**
     * Checks id/password against the registered user and, if valid,
     * starts a session for that user.
     *
     * @return true if credentials were valid and a session was started
     */
    public boolean authenticate(String id, String password) {
        if (id == null || password == null) {
            return false;
        }
        Person person = registeredUsers.get(id);
        if (person == null) {
            return false;
        }
        if (!person.checkPassword(password)) {
            return false;
        }
        activeSessions.put(id, person);
        return true;
    }

    /**
     * Ends the session for the given user id. Safe to call even if the
     * user has no active session.
     */
    public void logoutUser(String id) {
        activeSessions.remove(id);
    }

    public boolean isLoggedIn(String id) {
        return activeSessions.containsKey(id);
    }

    public Person getActiveUser(String id) {
        return activeSessions.get(id);
    }
}
