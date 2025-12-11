import java.util.List;
import java.util.Random;
import java.util.List;
import java.util.Set;

public class Pacman extends Entity {
    private int texUp, texDown, texLeft, texRight;

    public Pacman(int texRight, int texUp, int texDown, int texLeft,
                  int x, int y, int tileSize) {
        super(texRight, x, y, tileSize, tileSize, tileSize);
        this.texUp = texUp; this.texDown = texDown;
        this.texLeft = texLeft; this.texRight = texRight;
    }

    @Override
    public void updateDirection(char dir) {
        this.direction = dir;
        updateVelocity();
        super.updateDirection(dir);
        switch (dir) {
            case 'U': textureId = texUp;    break;
            case 'D': textureId = texDown;  break;
            case 'L': textureId = texLeft;  break;
            case 'R': textureId = texRight; break;
        }
//        System.out.println("Pacman texture: " + textureId);

    }
}



/// //Ghost class

     class Ghost extends Entity {
    private final Random random;
    private final char[] dirs = {'U','D','L','R'};

    // Per-ghost timing and speed desync
    private int framesSinceTurn = 0;
    private int nextTurnAt;           // randomized interval per ghost
    private final float speedMul;     // small per-ghost speed variation

    public Ghost(int textureId, int x, int y, int tileSize) {
        super(textureId, x, y, tileSize, tileSize, tileSize);
        // Unique RNG per ghost to avoid identical sequences
        this.random = new Random(System.nanoTime() ^ (x * 31L + y * 17L));
        // 0.85x–1.15x speed variation to break sync visually
        this.speedMul = 0.85f + random.nextFloat() * 0.30f;

        randomizeDirection();
        scheduleNextTurn();
    }

    /** Pick a random direction and update velocity. */
    public void randomizeDirection() {
        char dir = dirs[random.nextInt(dirs.length)];
        updateDirection(dir);
    }

    /** Called from move() each frame to advance timing. */
    public void tickTurnTimer() {
        framesSinceTurn++;
        if (framesSinceTurn >= nextTurnAt) {
            randomizeDirection();
            scheduleNextTurn();
            framesSinceTurn = 0;
        }
    }

    /** Occasionally turn at intersections for more variety. */
    public void maybeTurnAtIntersection(int tileSize) {
        if ((x % tileSize == 0) && (y % tileSize == 0)) {
            // Small chance to turn when aligned to grid
            if (random.nextFloat() < 0.10f) { // 10% at intersections
                randomizeDirection();
                scheduleNextTurn();
                framesSinceTurn = 0;
            }
        }
    }

    /** Direction change must re-apply velocity with per-ghost speedMul. */
    @Override
    public void updateDirection(char dir) {
        this.direction = dir;
        updateVelocity(); // ensures velocity uses speedMul below
    }

    /** Use per-ghost speed multiplier to desync movement. */
    @Override
    public void updateVelocity() {
        int base = tileSize / 4; // match your engine’s speed unit
        float v = base * speedMul;

        switch (direction) {
            case 'U':
                velocityX = 0;
                velocityY = (int) -v;
                break;
            case 'D':
                velocityX = 0;
                velocityY = (int) v;
                break;
            case 'L':
                velocityX = (int) -v;
                velocityY = 0;
                break;
            case 'R':
                velocityX = (int) v;
                velocityY = 0;
                break;
            default:
                velocityX = 0;
                velocityY = 0;
        }
    }

    @Override
    public void reset() {
        super.reset();
        framesSinceTurn = 0;
        randomizeDirection();
        scheduleNextTurn();
    }

    // ---------------- helpers ----------------

    private void scheduleNextTurn() {
        // Different interval per ghost (40–120 frames) + jitter
        nextTurnAt = 40 + random.nextInt(81); // [40..120]
    }
}









//     public void randomizeDirection() {
//         char dir = dirs[random.nextInt(dirs.length)];
//         updateDirection(dir);
//     }




//class Ghost extends Entity {
//    private final Random random = new Random();
//    private final char[] dirs = {'U','D','L','R'};
//
//    public Ghost(int textureId, int x, int y, int tileSize) {
//        super(textureId, x, y, tileSize, tileSize, tileSize);
//        randomizeDirection(); // each ghost starts with its own random direction
//    }
//
//    /** Pick a random direction and update velocity */
//    public void randomizeDirection() {
//        char dir = dirs[random.nextInt(dirs.length)];
//        updateDirection(dir);
//    }
//
//    /** Update direction and velocity */
//    @Override
//    public void updateDirection(char dir) {
//        this.direction = dir;
//        updateVelocity();
//    }
//
//    /** Reset ghost to starting position with a new random direction */
//    @Override
//    public void reset() {
//        super.reset();
//        randomizeDirection();
//    }
//}



