import com.sun.opengl.util.GLUT;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.awt.Font;



public class GameRenderer implements GLEventListener, ActionListener {
    private final int rows = 21, cols = 19, tileSize = 32;
    private final int boardWidth = cols * tileSize;
    private final int boardHeight = rows * tileSize;

    private TextureManager tm;
    private GameMap map;
    private GLU glu = new GLU();
    private javax.swing.Timer gameLoop;
    private boolean gameOver = false;
    private int score = 0;
    private final char[] directions = {'U','D','L','R'};
    private final Random random = new Random();
    private int lives = 3;
    private GLUT glut = new GLUT();
    private MusicPlayer music;
    private Entity speakerIcon;




    public GameRenderer(TextureManager tm) {
        this.tm = tm;
        gameLoop = new javax.swing.Timer(50, this); // 20 FPS
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);

        // Setup orthographic projection: origin top-left, y down
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, boardWidth, boardHeight, 0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        tm.init(gl);
        map = new GameMap(tileSize, tm);
        // Randomize ghost initial directions
        for (Ghost g : map.ghosts) {
            g.updateDirection(directions[random.nextInt(4)]);
        }

        gameLoop.start();
        speakerIcon = new Entity(
                tm.getSpeakerTex(), boardWidth - 40, 10, 25, 25, tileSize
        );
        music = new MusicPlayer();
        music.playBackground(); // loop background music
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        // Draw walls
        for (Entity w : map.walls) drawEntity(gl, w);

        // Draw foods (small quads)
        for (Entity f : map.foods) drawEntity(gl, f);

        // Draw ghosts
        for (Ghost g : map.ghosts) drawEntity(gl, g);

        // Draw pacman
        if (map.pacman != null) drawEntity(gl, map.pacman);
        //draw heart
        drawHUD(gl);
        drawEntity(gl, speakerIcon);


    }

    //draw heart and score
    private void drawHUD(GL gl) {
        int x = 10;
        int y = boardHeight - 30; // top-left
        for (int i = 0; i < lives; i++) {
            gl.glBindTexture(GL.GL_TEXTURE_2D, tm.getHeartTex());
            drawQuad(gl, x, y, 25, 25);
            x += 30; // spacing
            gl.glDisable(GL.GL_TEXTURE_2D); // disable textures for text


            gl.glRasterPos2i(10, 20);       // position near top-left
            String scoreText = "Score: " + score;
            for (char c : scoreText.toCharArray()) {
                glut.glutBitmapCharacter(GLUT.BITMAP_HELVETICA_18, c);
            }
            gl.glEnable(GL.GL_TEXTURE_2D);
        }
    }
    private void drawQuad(GL gl, int x, int y, int w, int h) {
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0, 0); gl.glVertex2f(x, y);
        gl.glTexCoord2f(1, 0); gl.glVertex2f(x + w, y);
        gl.glTexCoord2f(1, 1); gl.glVertex2f(x + w, y + h);
        gl.glTexCoord2f(0, 1); gl.glVertex2f(x, y + h);
        gl.glEnd();
    }

    //handle mouse click for mute and unmute
    public void onMouseClick(int mouseX, int mouseY) {
        // Check if click is inside speaker icon bounds
        if (mouseX >= speakerIcon.x && mouseX <= speakerIcon.x + speakerIcon.width &&
                mouseY >= speakerIcon.y && mouseY <= speakerIcon.y + speakerIcon.height) {

            toggleMute(); // flip mute state
        }
    }
    //toggleMute
    public void toggleMute() {
        if (music != null) {
            boolean current = music.isMuted();
            music.setMuted(!current);
            System.out.println("Sound " + (current ? "unmuted" : "muted"));
        }
    }

    private void drawEntity(GL gl, Entity e) {
        gl.glBindTexture(GL.GL_TEXTURE_2D, e.textureId);
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0f, 0f); gl.glVertex2f(e.x,           e.y);
        gl.glTexCoord2f(1f, 0f); gl.glVertex2f(e.x + e.width, e.y);
        gl.glTexCoord2f(1f, 1f); gl.glVertex2f(e.x + e.width, e.y + e.height);
        gl.glTexCoord2f(0f, 1f); gl.glVertex2f(e.x,           e.y + e.height);
        gl.glEnd();
    }

    private boolean collision(Entity a, Entity b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    private void move() {
        if (gameOver || map.pacman == null) return;
        // Pacman movement and wall collisions
        map.pacman.x += map.pacman.velocityX;
        map.pacman.y += map.pacman.velocityY;
        for (Entity wall : map.walls) {
            if (collision(map.pacman, wall)) {
                map.pacman.x -= map.pacman.velocityX;
                map.pacman.y -= map.pacman.velocityY;
                break;
            }
        }

        // Ghosts: independent random wandering with periodic turns
        for (Ghost g : map.ghosts) {
            // Move
            g.x += g.velocityX;
            g.y += g.velocityY;

            boolean hitWall = false;
            for (Entity wall : map.walls) {
                if (collision(g, wall) || g.x <= 0 || g.y <= 0 ||
                        g.x + g.width >= boardWidth || g.y + g.height >= boardHeight) {
                    // Undo and choose a new direction
                    g.x -= g.velocityX;
                    g.y -= g.velocityY;
                    hitWall = true;
                    break;
                }
            }

            if (hitWall) {
                g.randomizeDirection();
            } else {
                g.tickTurnTimer();
                g.maybeTurnAtIntersection(tileSize);
            }

            // Collision with Pacman
            if (collision(g, map.pacman)) {
                lives--;
                music.playGhost();
                if (lives <= 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }
        }

        // Food
        Entity eaten = null;
        for (Entity f : map.foods) {
            if (collision(map.pacman, f)) {
                eaten = f;
                score += 10;
                music.playFruit();
                break;
            }
        }
        if (eaten != null) map.foods.remove(eaten);

        // New level
        if (map.foods.isEmpty()) {
            map = new GameMap(tileSize, tm);
            resetPositions();
        }
        if (gameOver) {
            music.stopBackground();
        }

    }




    private void resetPositions() {
        map.pacman.reset();
        map.pacman.velocityX = 0;
        map.pacman.velocityY = 0;
        for (Ghost g : map.ghosts) {
            g.reset();
            g.randomizeDirection();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        GL gl = drawable.getGL();
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, boardWidth, boardHeight, 0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    }

    // Expose simple controls to update pacman direction
    public void onDirection(char dir) {
        if (map != null && map.pacman != null) {
            map.pacman.updateDirection(dir);
        }
    }

    public boolean isGameOver() { return gameOver; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
}
