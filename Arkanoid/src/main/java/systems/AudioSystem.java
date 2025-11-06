package systems;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.Objects;

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
    private String currentMusic = null;
    private String selectedMusic = null;

    private AudioSystem() {
    }

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
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
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

    public void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    public void resumeMusic() {
        if (musicPlayer != null && enabled) {
            musicPlayer.play();
        }
    }

    public void playBrickHit() {
        playEffectOneShot("break.wav");   // <- chỉ 1 nhạc hiệu ứng
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stopMusic();
        }
    }

    private void playEffectOneShot(String fileName) {
        try {
            URL url = Objects.requireNonNull(
                    getClass().getResource("/sounds/" + fileName),
                    "Không tìm thấy hiệu ứng: " + fileName
            );
            MediaPlayer fx = new MediaPlayer(new Media(url.toString()));
            fx.setOnEndOfMedia(fx::dispose);
            fx.play();
        } catch (Exception ex) {
            System.err.println("⚠️ Lỗi phát hiệu ứng " + fileName + ": " + ex.getMessage());
        }
    }

    public void toggleMusic() {
        if (musicPlayer != null && musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }

    public boolean isPlaying() {
        return musicPlayer != null && musicPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void dispose() {
        stopMusic();
        instance = null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getCurrentMusic() {
        return currentMusic;
    }

    public void setSelectedMusic(String fileName) { this.selectedMusic = fileName; }
    public String getSelectedMusic() { return selectedMusic; }

    public String getSelectedMusicOrDefault(String def) {
        return (selectedMusic == null || selectedMusic.isEmpty()) ? def : selectedMusic;
    }

    public void playIfChanged(String fileName) {
        if (!Objects.equals(currentMusic, fileName)) {
            playBackgroundMusic(fileName);
        }
    }
}