package systems;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * AchievementSystem - Hệ thống thành tựu và xếp hạng với thông báo tích hợp
 * Tự động hiển thị notification khi mở khóa thành tựu hoặc rank up
 */
public class AchievementSystem {

    // Rank System
    private final IntegerProperty currentRankIndex;
    private final StringProperty currentRankName;
    private final StringProperty currentRankIcon;
    private final IntegerProperty pointsToNextRank;

    // Achievement System
    private final List<Achievement> achievements;
    private final List<AchievementListener> listeners;
    private final List<RankUpListener> rankListeners;

    // Notification System (Tích hợp)
    private VBox notificationBox;
    private StackPane gameContainer;
    private final Queue<Runnable> notificationQueue = new LinkedList<>();
    private boolean isShowingNotification = false;

    private static final double NOTIFICATION_WIDTH = 300;
    private static final double NOTIFICATION_HEIGHT = 70;  // Giảm từ 80 xuống 70
    private static final double MARGIN_TOP = 80;
    private static final double MARGIN_RIGHT = 20;
    private static final int MAX_CONCURRENT_NOTIFICATIONS = 3;  // Tối đa 3 notification cùng lúc

    // Danh sách các rank theo thứ tự
    private static final Rank[] RANKS = {
            new Rank("Bronze", "🥉", 0),
            new Rank("Silver", "🥈", 2),
            new Rank("Gold", "🥇", 4),
            new Rank("Platinum", "💎", 6),
            new Rank("Diamond", "💠", 400),
            new Rank("Master", "⭐", 500),
            new Rank("Grandmaster", "👑", 600),
            new Rank("Challenger", "🔥", 700)
    };

    /**
     * Constructor - Khởi tạo hệ thống thành tựu và rank
     */
    public AchievementSystem() {
        this.achievements = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.rankListeners = new ArrayList<>();

        // Khởi tạo rank properties
        this.currentRankIndex = new SimpleIntegerProperty(0);
        this.currentRankName = new SimpleStringProperty(RANKS[0].name);
        this.currentRankIcon = new SimpleStringProperty(RANKS[0].icon);
        this.pointsToNextRank = new SimpleIntegerProperty(RANKS[1].minPoints);

        initializeAchievements();
    }

    /**
     *Khởi tạo notification system
     * GỌI METHOD NÀY TỪNG LẦN DUY NHẤT TRONG Game.java sau khi tạo StackPane
     * @param container StackPane chứa game (root của Scene)
     */
    public void initNotificationSystem(StackPane container) {
        this.gameContainer = container;

        // Tạo VBox để chứa các notification - xếp từ trên xuống
        notificationBox = new VBox(10);  // Gap 10px giữa các notification
        notificationBox.setAlignment(Pos.TOP_RIGHT);  // Căn trên-phải
        notificationBox.setPadding(new Insets(MARGIN_TOP, MARGIN_RIGHT, 0, 0));
        notificationBox.setMouseTransparent(true);
        notificationBox.setPickOnBounds(false);

        // Thêm CSS transition để các notification tự động di chuyển mượt mà
        notificationBox.setStyle("-fx-effect: null;");

        // Thêm vào container
        container.getChildren().add(notificationBox);
        StackPane.setAlignment(notificationBox, Pos.TOP_RIGHT);
    }

    /**
     * Khởi tạo danh sách các thành tựu có sẵn
     */
    private void initializeAchievements() {
        // ===== Thành tựu về gạch =====
        achievements.add(new Achievement(
                "first_brick",
                "Khởi đầu",
                "Phá viên gạch đầu tiên",
                "🧱"
        ));

        achievements.add(new Achievement(
                "brick_10",
                "Người phá vỡ",
                "Phá 10 viên gạch",
                "💥"
        ));

        achievements.add(new Achievement(
                "brick_50",
                "Chuyên gia phá gạch",
                "Phá 50 viên gạch",
                "⚡"
        ));

        achievements.add(new Achievement(
                "brick_100",
                "Hủy diệt",
                "Phá 100 viên gạch",
                "💣"
        ));

        // ===== Thành tựu về rank =====
        achievements.add(new Achievement(
                "rank_bronze",
                "Chiến binh Đồng",
                "Đạt rank Đồng",
                "🥉"
        ));

        achievements.add(new Achievement(
                "rank_silver",
                "Chiến binh Bạc",
                "Đạt rank Bạc",
                "🥈"
        ));

        achievements.add(new Achievement(
                "rank_gold",
                "Chiến binh Vàng",
                "Đạt rank Vàng",
                "🥇"
        ));

        achievements.add(new Achievement(
                "rank_platinum",
                "Chiến binh Bạch Kim",
                "Đạt rank Bạch Kim",
                "💎"
        ));

        achievements.add(new Achievement(
                "rank_diamond",
                "Chiến binh Kim Cương",
                "Đạt rank Kim Cương",
                "💠"
        ));

        achievements.add(new Achievement(
                "rank_master",
                "Chiến binh Tinh Anh",
                "Đạt rank Tinh Anh",
                "⭐"
        ));

        achievements.add(new Achievement(
                "rank_grandmaster",
                "Cao Thủ Vô Đối",
                "Đạt rank Cao Thủ",
                "👑"
        ));

        achievements.add(new Achievement(
                "rank_challenger",
                "Thách Đấu Tối Thượng",
                "Đạt rank Thách Đấu",
                "🔥"
        ));

        // ===== Thành tựu về level =====
        achievements.add(new Achievement(
                "level_1",
                "Hoàn thành nhiệm vụ",
                "Hoàn thành Level 1",
                "🎯"
        ));

        achievements.add(new Achievement(
                "level_5",
                "Kiên trì",
                "Đạt Level 5",
                "🏆"
        ));

        achievements.add(new Achievement(
                "level_10",
                "Bất khả chiến bại",
                "Đạt Level 10",
                "👑"
        ));

        // ===== Thành tựu về mạng sống =====
        achievements.add(new Achievement(
                "lives_5",
                "Sống còn",
                "Có 5 mạng cùng lúc",
                "❤️"
        ));

        achievements.add(new Achievement(
                "lives_10",
                "Bất tử",
                "Có 10 mạng cùng lúc",
                "💖"
        ));

        // ===== Thành tựu đặc biệt =====
        achievements.add(new Achievement(
                "flawless",
                "Hoàn hảo",
                "Hoàn thành 1 level không mất mạng",
                "💎"
        ));

        achievements.add(new Achievement(
                "speedrun",
                "Tốc độ",
                "Hoàn thành level trong 30 giây",
                "⚡"
        ));
    }

    /**
     * Cập nhật rank dựa trên điểm số
     */
    public void updateRank(int score) {
        int oldRankIndex = currentRankIndex.get();
        int newRankIndex = calculateRankIndex(score);

        // Nếu lên rank mới
        if (newRankIndex > oldRankIndex) {
            currentRankIndex.set(newRankIndex);
            currentRankName.set(RANKS[newRankIndex].name);
            currentRankIcon.set(RANKS[newRankIndex].icon);

            // Mở khóa thành tựu rank tương ứng
            unlockRankAchievement(newRankIndex);

            // Hiển thị notification rank up
            showRankUpNotification(RANKS[oldRankIndex], RANKS[newRankIndex], score);

            // Thông báo qua listener
            notifyRankUp(RANKS[oldRankIndex], RANKS[newRankIndex], score);
        }

        // Cập nhật điểm cần để lên rank tiếp theo
        if (newRankIndex < RANKS.length - 1) {
            pointsToNextRank.set(RANKS[newRankIndex + 1].minPoints - score);
        } else {
            pointsToNextRank.set(0);
        }
    }

    /**
     * Tính chỉ số rank dựa trên điểm
     */
    private int calculateRankIndex(int score) {
        for (int i = RANKS.length - 1; i >= 0; i--) {
            if (score >= RANKS[i].minPoints) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Mở khóa thành tựu rank tương ứng
     */
    private void unlockRankAchievement(int rankIndex) {
        String[] rankAchievements = {
                "rank_bronze", "rank_silver", "rank_gold", "rank_platinum",
                "rank_diamond", "rank_master", "rank_grandmaster", "rank_challenger"
        };

        if (rankIndex < rankAchievements.length) {
            unlockAchievement(rankAchievements[rankIndex]);
        }
    }

    /**
     * Kiểm tra và mở khóa thành tựu dựa trên thống kê game
     */
    public void checkAchievements(ScoringSystem scoring, int currentLevel) {
        int bricksDestroyed = scoring.getBricksDestroyed();
        int score = scoring.getScore();
        int lives = scoring.getLives();

        // Kiểm tra rank TRƯỚC để rank up hiển thị trước
        // (vì rank quan trọng hơn)
        updateRank(score);

        // Kiểm tra thành tựu gạch
        checkAndUnlock("first_brick", bricksDestroyed >= 1);
        checkAndUnlock("brick_10", bricksDestroyed >= 10);
        checkAndUnlock("brick_50", bricksDestroyed >= 50);
        checkAndUnlock("brick_100", bricksDestroyed >= 100);

        // Kiểm tra thành tựu level
        checkAndUnlock("level_1", currentLevel >= 2);
        checkAndUnlock("level_5", currentLevel >= 6);
        checkAndUnlock("level_10", currentLevel >= 11);

        // Kiểm tra thành tựu mạng
        checkAndUnlock("lives_5", lives >= 5);
        checkAndUnlock("lives_10", lives >= 10);
    }

    /**
     * Kiểm tra và mở khóa thành tựu nếu điều kiện thỏa mãn
     */
    private void checkAndUnlock(String id, boolean condition) {
        Achievement achievement = getAchievementById(id);
        if (achievement != null && !achievement.isUnlocked() && condition) {
            achievement.unlock();

            //  Hiển thị notification thành tựu NGAY LẬP TỨC
            // Sử dụng javafx.application.Platform.runLater để đảm bảo chạy trên UI thread
            javafx.application.Platform.runLater(() -> {
                showAchievementNotification(achievement);
            });

            // Thông báo qua listener
            notifyAchievementUnlocked(achievement);
        }
    }

    /**
     * Mở khóa thành tựu thủ công
     */
    public void unlockAchievement(String id) {
        Achievement achievement = getAchievementById(id);
        if (achievement != null && !achievement.isUnlocked()) {
            achievement.unlock();

            //  Hiển thị notification
            showAchievementNotification(achievement);

            notifyAchievementUnlocked(achievement);
        }
    }

    /**
     * Lấy thành tựu theo ID
     */
    public Achievement getAchievementById(String id) {
        return achievements.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ===== NOTIFICATION SYSTEM (TÍCH HỢP) =====

    /**
     * Hiển thị notification thành tựu
     */
    private void showAchievementNotification(Achievement achievement) {
        if (notificationBox == null) return; // Chưa init notification system

        showNotification(
                achievement.getIcon() + " THÀNH TỰU MỚI!",
                achievement.getName(),
                achievement.getDescription(),
                Color.web("#FFD700"),  // Vàng gold
                Color.web("#FF8C00")   // Cam đậm
        );
    }

    /**
     * Hiển thị notification rank up
     */
    private void showRankUpNotification(Rank oldRank, Rank newRank, int score) {
        if (notificationBox == null) return;

        showNotification(
                "🎖️ RANK UP!",
                oldRank.getIcon() + " " + oldRank.getName() + " → " + newRank.getIcon() + " " + newRank.getName(),
                "Điểm hiện tại: " + score,
                Color.web("#9370DB"),  // Tím medium
                Color.web("#8B008B")   // Tím đậm
        );
    }

    /**
     * Hiển thị notification tùy chỉnh
     */

    private void showNotification(String title, String subtitle, String description,
                                  Color primaryColor, Color secondaryColor) {
        //  Nếu đã có quá nhiều notification, xóa cái cũ nhất (ở trên cùng)
        if (notificationBox != null && notificationBox.getChildren().size() >= MAX_CONCURRENT_NOTIFICATIONS) {
            var oldest = notificationBox.getChildren().get(0);

            // Fade out notification cũ nhất
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), oldest);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> notificationBox.getChildren().remove(oldest));
            fadeOut.play();
        }



        // Hiển thị notification mới
        createAndShowNotification(title, subtitle, description, primaryColor, secondaryColor);
    }
    /**
     * Xử lý hàng đợi notification (DEPRECATED - không dùng nữa)
     */
    @Deprecated
    private void processQueue() {
        if (notificationQueue.isEmpty()) {
            isShowingNotification = false;
            return;
        }

        isShowingNotification = true;
        Runnable next = notificationQueue.poll();
        if (next != null) {
            next.run();
        }
    }

    /**
     * Tạo và hiển thị notification với animation
     */
    private void createAndShowNotification(String title, String subtitle, String description,
                                           Color primaryColor, Color secondaryColor) {
        // Tạo notification card
        HBox card = new HBox(15);
        card.setPrefSize(NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
        card.setMaxSize(NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10, 15, 10, 15));

        // Gradient background
        String gradient = String.format(
                "-fx-background-color: linear-gradient(to right, %s, %s); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-color: rgba(255,255,255,0.3); " +
                        "-fx-border-width: 2;",
                toRgbString(primaryColor),
                toRgbString(secondaryColor)
        );
        card.setStyle(gradient);

        // Drop shadow
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(15);
        shadow.setOffsetY(5);
        card.setEffect(shadow);

        // Nội dung text
        VBox textBox = new VBox(2);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Text titleText = new Text(title);
        titleText.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleText.setFill(Color.WHITE);

        Text subtitleText = new Text(subtitle);
        subtitleText.setFont(Font.font("System", FontWeight.BOLD, 18));
        subtitleText.setFill(Color.WHITE);

        Text descText = new Text(description);
        descText.setFont(Font.font("System", 12));
        descText.setFill(Color.web("#FFFFE0"));

        textBox.getChildren().addAll(titleText, subtitleText, descText);
        card.getChildren().add(textBox);

        // Animation
        card.setTranslateX(400);
        card.setOpacity(0);

        notificationBox.getChildren().add(card);

        // Slide in
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), card);
        slideIn.setFromX(400);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), card);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ParallelTransition showAnimation = new ParallelTransition(slideIn, fadeIn);

        // Pause 3 giây
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        // Slide out
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(400), card);
        slideOut.setToX(400);
        slideOut.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), card);  // Nhanh hơn: 400ms → 300ms
        fadeOut.setToValue(0);

        ParallelTransition hideAnimation = new ParallelTransition(slideOut, fadeOut);

        hideAnimation.setOnFinished(e -> {
            notificationBox.getChildren().remove(card);
            processQueue();
        });

        // Chạy sequence
        SequentialTransition sequence = new SequentialTransition(
                showAnimation,
                pause,
                hideAnimation
        );
        sequence.play();
    }

    /**
     * Chuyển Color sang rgb string cho CSS
     */
    private String toRgbString(Color color) {
        return String.format("rgba(%d, %d, %d, %.2f)",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255),
                color.getOpacity()
        );
    }

    /**
     * Xóa tất cả notification
     */
    public void clearNotifications() {
        if (notificationBox != null) {
            notificationBox.getChildren().clear();
        }
        notificationQueue.clear();
        isShowingNotification = false;
    }

    // ===== LISTENERS =====

    public void addListener(AchievementListener listener) {
        listeners.add(listener);
    }

    public void addRankListener(RankUpListener listener) {
        rankListeners.add(listener);
    }

    private void notifyAchievementUnlocked(Achievement achievement) {
        for (AchievementListener listener : listeners) {
            listener.onAchievementUnlocked(achievement);
        }
    }

    private void notifyRankUp(Rank oldRank, Rank newRank, int score) {
        for (RankUpListener listener : rankListeners) {
            listener.onRankUp(oldRank, newRank, score);
        }
    }

    /**
     * Reset tất cả
     */
    public void resetAll() {
        achievements.forEach(Achievement::reset);
        currentRankIndex.set(0);
        currentRankName.set(RANKS[0].name);
        currentRankIcon.set(RANKS[0].icon);
        pointsToNextRank.set(RANKS[1].minPoints);
        clearNotifications();
    }

    // ===== GETTERS =====

    public List<Achievement> getAllAchievements() { return new ArrayList<>(achievements); }
    public List<Achievement> getUnlockedAchievements() {
        return achievements.stream().filter(Achievement::isUnlocked).toList();
    }
    public int getUnlockedCount() {
        return (int) achievements.stream().filter(Achievement::isUnlocked).count();
    }
    public int getTotalCount() { return achievements.size(); }
    public double getCompletionPercentage() {
        if (achievements.isEmpty()) return 0.0;
        return (getUnlockedCount() * 100.0) / getTotalCount();
    }

    public int getCurrentRankIndex() { return currentRankIndex.get(); }
    public String getCurrentRankName() { return currentRankName.get(); }
    public String getCurrentRankIcon() { return currentRankIcon.get(); }
    public int getPointsToNextRank() { return pointsToNextRank.get(); }
    public Rank getCurrentRank() { return RANKS[currentRankIndex.get()]; }
    public Rank getNextRank() {
        int next = currentRankIndex.get() + 1;
        return next < RANKS.length ? RANKS[next] : RANKS[RANKS.length - 1];
    }

    public IntegerProperty currentRankIndexProperty() { return currentRankIndex; }
    public StringProperty currentRankNameProperty() { return currentRankName; }
    public StringProperty currentRankIconProperty() { return currentRankIcon; }
    public IntegerProperty pointsToNextRankProperty() { return pointsToNextRank; }

    // ===== INNER CLASSES =====

    public static class Rank {
        private final String name;
        private final String icon;
        private final int minPoints;

        public Rank(String name, String icon, int minPoints) {
            this.name = name;
            this.icon = icon;
            this.minPoints = minPoints;
        }

        public String getName() { return name; }
        public String getIcon() { return icon; }
        public int getMinPoints() { return minPoints; }

        @Override
        public String toString() {
            return icon + " " + name + " (" + minPoints + "+ điểm)";
        }
    }

    public static class Achievement {
        private final String id;
        private final String name;
        private final String description;
        private final String icon;
        private final BooleanProperty unlocked;

        public Achievement(String id, String name, String description, String icon) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.icon = icon;
            this.unlocked = new SimpleBooleanProperty(false);
        }

        public void unlock() { unlocked.set(true); }
        public void reset() { unlocked.set(false); }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getIcon() { return icon; }
        public boolean isUnlocked() { return unlocked.get(); }
        public BooleanProperty unlockedProperty() { return unlocked; }

        @Override
        public String toString() {
            return String.format("%s %s - %s %s",
                    icon, name, description, isUnlocked() ? "✅" : "🔒");
        }
    }

    public interface AchievementListener {
        void onAchievementUnlocked(Achievement achievement);
    }

    public interface RankUpListener {
        void onRankUp(Rank oldRank, Rank newRank, int currentScore);
    }
}