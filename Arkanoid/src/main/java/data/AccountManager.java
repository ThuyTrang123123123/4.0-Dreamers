// data/AccountManager.java
package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private static final String ACCOUNTS_FILE = "src/main/resources/data/accounts.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private List<PlayerAccount> accounts = new ArrayList<>();

    public AccountManager() {
        loadAccounts();
    }

    private void loadAccounts() {
        try {
            File file = new File(ACCOUNTS_FILE);
            if (file.exists()) {
                accounts = mapper.readValue(file, new TypeReference<List<PlayerAccount>>() {});
            }
        } catch (IOException e) {
            System.err.println("Không thể tải tài khoản: " + e.getMessage());
            accounts = new ArrayList<>();
        }
    }

    private void saveAccounts() {
        try {
            Files.createDirectories(Paths.get("src/main/resources/data"));
            mapper.writeValue(new File(ACCOUNTS_FILE), accounts);
        } catch (IOException e) {
            System.err.println("Lỗi lưu tài khoản: " + e.getMessage());
        }
    }

    // Mã hóa mật khẩu đơn giản (SHA-256)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password; // fallback
        }
    }

    public boolean register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return false;
        if (accounts.stream().anyMatch(a -> a.getUsername().equalsIgnoreCase(username))) {
            return false; // Đã tồn tại
        }
        accounts.add(new PlayerAccount(username, hashPassword(password)));
        saveAccounts();
        return true;
    }

    public boolean login(String username, String password) {
        String hash = hashPassword(password);
        return accounts.stream()
                .anyMatch(a -> a.getUsername().equalsIgnoreCase(username) && a.getPasswordHash().equals(hash));
    }

    public String getCurrentPlayer() {
        return accounts.stream()
                .filter(a -> a.getUsername().equalsIgnoreCase(getLoggedInUser()))
                .findFirst()
                .map(PlayerAccount::getUsername)
                .orElse("Guest");
    }

    // Lưu tạm tên người đăng nhập
    private static String loggedInUser = null;
    public static void setLoggedInUser(String username) { loggedInUser = username; }
    public static String getLoggedInUser() { return loggedInUser; }
    public static void logout() { loggedInUser = null; }
}