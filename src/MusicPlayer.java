import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class MusicPlayer {
    private Clip backgroundClip, fruitClip, ghostClip;
    private boolean muted = false;

    public MusicPlayer() {
        backgroundClip = loadClip("sounds/17. Game Play.wav");
        fruitClip = loadClip("sounds/11. PAC-MAN - Eating The Fruit.wav");
        ghostClip = loadClip("sounds/13. PAC-MAN - Eating The Ghost.wav");
    }

    public void setMuted(boolean mute) {
        muted = mute;
        if (muted) stopBackground();
        else playBackground();
    }

    public boolean isMuted() {
        return muted;
    }

    public void playBackground() {
        if (!muted && backgroundClip != null) {
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        }
    }

    public void stopBackground() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    public void playFruit() {
        if (!muted && fruitClip != null) {
            fruitClip.setFramePosition(0);
            fruitClip.start();
        }
    }

    public void playGhost() {
        if (!muted && ghostClip != null) {
            ghostClip.setFramePosition(0);
            ghostClip.start();
        }
    }

    private Clip loadClip(String path) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
