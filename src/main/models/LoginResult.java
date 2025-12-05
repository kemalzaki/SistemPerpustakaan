package main.models;

public class LoginResult {
    private String role; // "ADMIN" or "MEMBER"
    private String memberId; // null for admin

    public LoginResult() {}

    public LoginResult(String role, String memberId) {
        this.role = role;
        this.memberId = memberId;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
}
