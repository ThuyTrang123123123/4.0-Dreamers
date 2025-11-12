package engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Sprite - Quản lý hoạt ảnh theo sprite sheet
 * Dùng cho các hiệu ứng như Explosion, Animation, v.v.
 */
public class Sprite {
    private Image image;
    private Rectangle2D[] frames;
    private int currentFrame = 0;
    private int frameDelay = 5;  // số lần update trước khi chuyển frame
    private int frameCounter = 0;
    private double scale = 0.25;
    public Sprite(Image image, int columns, int rows) {
        this.image = image;

        int frameWidth = (int) (image.getWidth() / columns);
        int frameHeight = (int) (image.getHeight() / rows);

        frames = new Rectangle2D[columns * rows];
        int index = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                frames[index++] = new Rectangle2D(
                        x * frameWidth, y * frameHeight, frameWidth, frameHeight);
            }
        }
    }
    public void update() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            currentFrame++;
            if (currentFrame >= frames.length) {
                currentFrame = frames.length - 1; // đứng yên ở frame cuối
            }
        }
    }
    public void render(GraphicsContext gc, double x, double y) {
        Rectangle2D frame = frames[currentFrame];
        double w = frame.getWidth() * scale;
        double h = frame.getHeight() * scale;

        gc.drawImage(image,
                frame.getMinX(), frame.getMinY(), frame.getWidth(), frame.getHeight(),
                x - w / 2, y - h / 2, w, h);
    }

    public boolean isLastFrame() {
        return currentFrame == frames.length - 1;
    }

    public void setFrameDelay(int delay) {
        this.frameDelay = delay;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void reset() {
        currentFrame = 0;
        frameCounter = 0;
    }
}
