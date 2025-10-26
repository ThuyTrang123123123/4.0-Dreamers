package ui.screen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import systems.ScoringSystem;
import ui.theme.Colors;
import ui.theme.Fonts;
import ui.widgets.ButtonUI;

public class InGame {
    // ===== UI Components =====
    private Label scoreLabel;                    // Hiển thị điểm số
    private Label livesLabel;                    // Hiển thị mạng sống
    private Label bricksLabel;                   // Hiển thị số gạch đã phá

    // ===== Data =====
    private final ScoringSystem scoring;         // Hệ thống quản lý điểm (được truyền từ Game)

    public InGame(ScoringSystem scoring) {
        this.scoring = scoring;
    }

    public Scene create(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #" + colorToHex(Colors.BACKGROUND) + ";");

        // === Canvas gameplay ===
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, 800, 600); // tạm vẽ nền gameplay

        // === HUD (score, lives, bricks, pause button) ===
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Fonts.main(18));
        scoreLabel.setTextFill(Colors.TEXT);

        livesLabel = new Label("Lives: 0");
        livesLabel.setFont(Fonts.main(18));
        livesLabel.setTextFill(Colors.TEXT);

        bricksLabel = new Label("Bricks: 0");
        bricksLabel.setFont(Fonts.main(18));
        bricksLabel.setTextFill(Colors.TEXT);

        ButtonUI pauseBtn = new ButtonUI("Pause");
        pauseBtn.setOnAction(e -> stage.setScene(new MainMenu().create(stage)));

        HBox hudBar = new HBox(30, scoreLabel, livesLabel, bricksLabel, pauseBtn);
        hudBar.setAlignment(Pos.CENTER);
        hudBar.setStyle("-fx-padding: 10; -fx-background-color: #" + colorToHex(Colors.SECONDARY) + ";");

        // === Binding dữ liệu với ScoringSystem ===
        bindHUDWithScoring();

        // === Đặt layout ===
        root.setTop(hudBar);
        root.setCenter(canvas);

        return new Scene(root, 800, 600);
    }

    public HBox createHUD() {
        // Khởi tạo các label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Fonts.main(18));
        scoreLabel.setTextFill(Colors.TEXT);

        livesLabel = new Label("Lives: 0");
        livesLabel.setFont(Fonts.main(18));
        livesLabel.setTextFill(Colors.TEXT);

        bricksLabel = new Label("Bricks: 0");
        bricksLabel.setFont(Fonts.main(18));
        bricksLabel.setTextFill(Colors.TEXT);

        // Tạo thanh HUD
        HBox hudBar = new HBox(40, scoreLabel, livesLabel, bricksLabel);
        hudBar.setAlignment(Pos.TOP_LEFT);
        hudBar.setPadding(new Insets(10));
        hudBar.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 8;");
        hudBar.setMouseTransparent(true); // không chặn input cho gameplay

        // Binding dữ liệu với ScoringSystem
        bindHUDWithScoring();

        return hudBar;
    }

    /**
     * Binding HUD với ScoringSystem để tự động cập nhật
     * Khi scoring thay đổi → Label tự động cập nhật, không cần gọi updateHUD()
     */
    private void bindHUDWithScoring() {
        // Bind điểm số
        scoreLabel.textProperty().bind(
                scoring.scoreProperty().asString("Score: %d")
        );

        // Bind mạng sống
        livesLabel.textProperty().bind(
                scoring.livesProperty().asString("Lives: %d")
        );

        // Bind số gạch đã phá
        bricksLabel.textProperty().bind(
                scoring.bricksDestroyedProperty().asString("Bricks: %d")
        );
    }

    @Deprecated
    public void updateHUD(int newScore, int newLives) {
        // Method này không còn cần thiết nếu dùng binding
        // Nhưng giữ lại để code cũ không bị lỗi
        System.out.println("⚠️ updateHUD() deprecated - HUD tự động cập nhật qua binding!");
    }

    private String colorToHex(javafx.scene.paint.Color color) {
        return color.toString().substring(2, 8);
    }
}