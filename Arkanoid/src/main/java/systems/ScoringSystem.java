package systems;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ScoringSystem {
    // Sử dụng IntegerProperty thay vì int để tận dụng data binding
    private final IntegerProperty score;
    private final IntegerProperty lives;

    public ScoringSystem() {
        this.score = new SimpleIntegerProperty(0); // Điểm ban đầu
        this.lives = new SimpleIntegerProperty(3); // Mạng sống ban đầu
    }

    // Cung cấp các "Property" này cho giao diện
    public IntegerProperty scoreProperty() { return score; }
    public IntegerProperty livesProperty() { return lives; }

    // Các hàm để module khác thay đổi dữ liệu
    public void addScore(int points) { this.score.set(this.score.get() + points); }
    public void loseLife() { this.lives.set(this.lives.get() - 1); }
}