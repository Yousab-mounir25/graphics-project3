import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputController extends KeyAdapter {
    private final GameRenderer renderer;

    public InputController(GameRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    renderer.onDirection('U'); break;
            case KeyEvent.VK_DOWN:  renderer.onDirection('D'); break;
            case KeyEvent.VK_LEFT:  renderer.onDirection('L'); break;
            case KeyEvent.VK_RIGHT: renderer.onDirection('R'); break;
        }
    }
}
