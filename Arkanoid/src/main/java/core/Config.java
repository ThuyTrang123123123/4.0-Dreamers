package core;
/**
 * Lớp Config (Cấu hình)
 * Chứa các hằng số tĩnh (static final) định nghĩa kích thước màn hình,
 * bố cục cấp độ, kích thước đối tượng và tốc độ mặc định trong trò chơi.
 */
public class Config {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    public static final int BRICK_ROWS = 2;
    public static final int BRICK_COLS = 5;

    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 15;

    public static final double BALL_RADIUS = 10;
    public static final double BALL_SPEED = 350;

    public static final String DEFAULT_MUSIC = "TRUYỆN AUDIO.mp3";

    public static final double BONUSCOIN_RADIUS = 15;
    public static final double BONUSCOIN_SPEED = 100;

    public static final double POWERUP_WIDTH = 24;
    public static final double POWERUP_HEIGHT = 24;

}