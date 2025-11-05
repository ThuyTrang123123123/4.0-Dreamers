package engine;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    private Image image;
    private Rectangle2D[] frames;
    private int currentFrame = 0;
    private double scale = 1.0;

    private int frameDelay = 2;
    private int frameCounter = 0;

    public Sprite(Image image, Rectangle2D[] clips) {
        this.image = image;
        this.frames = clips;
    }
    public void tick() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            currentFrame = (currentFrame + 1) % frames.length;
            frameCounter = 0;
        }
    }
    public void render(GraphicsContext gc, double x, double y) {
        Rectangle2D clip = frames[currentFrame];
        double drawWidth = clip.getWidth() * scale;
        double drawHeight = clip.getHeight() * scale;
        double drawX = x - drawWidth / 2;
        double drawY = y - drawHeight / 2;

        gc.drawImage(
                image,
                clip.getMinX(), clip.getMinY(), clip.getWidth(), clip.getHeight(), // source
                drawX, drawY, drawWidth, drawHeight                                // destination
        );
    }
    public void setFrame(int index) {
        if (index >= 0 && index < frames.length) {
            currentFrame = index;
            frameCounter = 0;
        }
    }
    public void setFrameDelay(int delay) {
        this.frameDelay = Math.max(1, delay);
    }

    public void setScale(double scale) {
        this.scale = Math.max(0.1, scale);
    }
    public Rectangle2D getCurrentFrame() {
        return frames[currentFrame];
    }
    public int getFrameCount() {
        return frames.length;
    }
}