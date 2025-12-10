import javax.swing.*;
import javax.media.opengl.*;
import com.sun.opengl.util.Animator;

public class Main {
    public static void main(String[] args) {
        // JOGL setup
        GLCapabilities caps = new GLCapabilities();
        GLCanvas canvas = new GLCanvas(caps);

        TextureManager tm = new TextureManager();
        GameRenderer renderer = new GameRenderer(tm);
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(new InputController(renderer));

        // JFrame
        int tileSize = 32, cols = 19, rows = 21;
        int boardWidth = cols * tileSize, boardHeight = rows * tileSize;
        JFrame frame = new JFrame("Pac Man");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(canvas);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Animator to drive the display() loop
        Animator animator = new Animator(canvas);
        animator.start();

        // Ensure canvas gets focus for keyboard input
        canvas.requestFocus();
    }
}
