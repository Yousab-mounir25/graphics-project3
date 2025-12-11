import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputController extends KeyAdapter {
    private final GameRenderer renderer;

    public InputController(GameRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            renderer.onDirection('U');
        } else if (key == KeyEvent.VK_DOWN) {
            renderer.onDirection('D');
        } else if (key == KeyEvent.VK_LEFT) {
            renderer.onDirection('L');
        } else if (key == KeyEvent.VK_RIGHT) {
            renderer.onDirection('R');
        }
        if (key == KeyEvent.VK_M) { // M for mute
            renderer.toggleMute();
        }

    }

}
