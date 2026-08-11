package person;

public abstract class Person implements DataSerializer<Person> {
    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;

    public Person() {}

    public Person(String id, String name, String email, String password, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public boolean login() {
        System.out.println("User " + name + " (" + email + ") logged in successfully.");
        return true;
    }

    public void logout() {
        System.out.println("User " + name + " logged out.");
    }

    public void updateProfile(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        System.out.println("Profile updated for user: " + id);
    }

    public boolean checkPassword(String password) {
        return this.password != null && this.password.equals(password);
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String serializeData() {
        return String.format("{\"id\":\"%s\",\"name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\"}",
                id, name, email, phone);
    }

    @Override
    public Person deserializeData(String data) {
        // Concrete subclasses implement specific deserialization
        return this;
    }
}
