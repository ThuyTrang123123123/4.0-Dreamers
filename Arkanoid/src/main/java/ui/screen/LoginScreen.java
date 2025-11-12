package ui.screen;

import core.Config;
import data.AccountManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ui.theme.Colors;
import ui.theme.Fonts;
import ui.widgets.ButtonUI;
import java.io.File;
import ui.screen.MainMenu;

public class LoginScreen {
    private final Stage stage;
    private final AccountManager accountManager;
    private final Runnable onLoginSuccess;

    public LoginScreen(Stage stage, AccountManager accountManager, Runnable onLoginSuccess) {
        this.stage = stage;
        this.accountManager = accountManager;
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene create() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #FFF8F6;");

        Label title = new Label("LOGIN / REGISTER");
        title.setFont(Fonts.main(36));
        title.setTextFill(Colors.PRIMARY);

        // Form
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label message = new Label("");
        message.setTextFill(Color.RED);

        ButtonUI loginBtn = new ButtonUI("Login");
        ButtonUI registerBtn = new ButtonUI("Register");

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("Please fill in all fields!");
                return;
            }
            if (accountManager.login(user, pass)) {
                AccountManager.setLoggedInUser(user);
                // === THÊM VÀO ĐÂY ===
                // Vô hiệu hóa (null) các instance game cũ
                // để MainMenu buộc phải tạo game MỚI cho user này
                MainMenu.playGame = null;
                MainMenu.practiceGame = null;
                onLoginSuccess.run();
            } else {
                message.setText("Invalid username or password!");
            }
        });

        registerBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("Please fill in all fields!");
                return;
            }

            if (accountManager.register(user, pass)) {
                message.setText("Registered successfully! Logging in...");
                AccountManager.setLoggedInUser(user);
                MainMenu.playGame = null;
                MainMenu.practiceGame = null;
// === THÊM LOGIC XÓA PROGRESS CŨ TẠI ĐÂY === // XÓA BẮT ĐẦU TỪ ĐÂY
                try {
                    // "progress_play" là key trong Game.java
                    // JsonStorage sẽ biến nó thành "progress_play.json"
                    File oldProgress = new File("src/main/resources/data/progress_play.json");
                    if (oldProgress.exists()) {
                        if (oldProgress.delete()) {
                            System.out.println("New account: Deleted old progress file.");
                        } else {
                            System.err.println("Could not delete progress file.");
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Error deleting old progress file: " + ex.getMessage());
                }
                // === KẾT THÚC LOGIC XÓA === // XÓA ĐẾN HẾT ĐÂY
                onLoginSuccess.run(); // Chuyển đến MainMenu
            } else {
                message.setText("Username already exists!");
            }
        });

        root.getChildren().addAll(
                title,
                usernameField,
                passwordField,
                loginBtn,
                registerBtn,
                message
        );

        return new Scene(root, Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
    }
}