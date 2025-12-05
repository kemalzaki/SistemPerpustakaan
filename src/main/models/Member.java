package main.models;

public class Member {
    private String id;
    private String name;
    private String salt; // hex
    private String passwordHash; // hex

    public Member() {}

    // without password
    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // with password data
    public Member(String id, String name, String salt, String passwordHash) {
        this.id = id;
        this.name = name;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Override
    public String toString() {
        return String.format("%s - %s", id, name);
    }
}
