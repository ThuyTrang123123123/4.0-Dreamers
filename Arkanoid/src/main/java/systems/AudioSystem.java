package systems;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * AudioSystem - Hệ thống phát nhạc nền đơn giản
 * Chỉ chạy nhạc background xuyên suốt
 */
public class AudioSystem {

    // ===== Singleton Instance =====
    private static AudioSystem instance;

    // ===== Media Player =====
    private MediaPlayer musicPlayer;

    // ===== Settings =====
    private double volume = 0.3;  // Âm lượng (0.0 - 1.0)
    private boolean enabled = true;

    /**
     * Private constructor (Singleton)
     */
    private AudioSystem() {
    }

    /**
     * Lấy instance duy nhất
     */
    public static AudioSystem getInstance() {
        if (instance == null) {
            instance = new AudioSystem();
        }
        return instance;
    }

    /**
     * Phát nhạc nền (looping vô hạn)
     * @param fileName Tên file trong thư mục resources/sounds/ (ví dụ: "background.mp3")
     */
    public void playBackgroundMusic(String fileName) {
        if (!enabled) return;

        // Dừng nhạc cũ nếu đang phát
        stopMusic();

        try {
            // Tìm file trong resources/sounds/
            URL musicUrl = getClass().getResource("/sounds/" + fileName);

            if (musicUrl == null) {
                System.err.println("⚠️ Không tìm thấy file nhạc: " + fileName);
                System.err.println("   Đặt file vào: src/main/resources/sounds/" + fileName);
                return;
            }

            Media music = new Media(musicUrl.toString());
            musicPlayer = new MediaPlayer(music);
            musicPlayer.setVolume(volume);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);  // Lặp vô hạn
            musicPlayer.play();

            System.out.println("🎵 Đang phát nhạc: " + fileName);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi phát nhạc: " + e.getMessage());
        }
    }

    /**
     * Dừng nhạc nền
     */
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }
    }

    /**
     * Tạm dừng nhạc nền
     */
    public void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    /**
     * Tiếp tục phát nhạc
     */
    public void resumeMusic() {
        if (musicPlayer != null && enabled) {
            musicPlayer.play();
        }
    }

    /**
     * Đặt âm lượng (0.0 - 1.0)
     */
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        if (musicPlayer != null) {
            musicPlayer.setVolume(this.volume);
        }
    }

    /**
     * Bật/tắt nhạc
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stopMusic();
        }
    }

    /**
     * Toggle nhạc (bật/tắt)
     */
    public void toggleMusic() {
        if (musicPlayer != null && musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }

    /**
     * Kiểm tra nhạc có đang phát không
     */
    public boolean isPlaying() {
        return musicPlayer != null && musicPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Cleanup khi thoát game
     */
    public void dispose() {
        stopMusic();
        instance = null;
    }
}