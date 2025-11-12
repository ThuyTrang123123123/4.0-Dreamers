// data/PlayerAccount.java
package data;

import java.util.Objects;

public class PlayerAccount {
    private String username;
    private String passwordHash; // Lưu hash, không lưu mật khẩu gốc

    public PlayerAccount() {}

    public PlayerAccount(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerAccount)) return false;
        PlayerAccount that = (PlayerAccount) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}