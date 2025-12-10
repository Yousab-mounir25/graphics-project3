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
    }
}
/// //Ghost class


 class Ghost extends Entity {
    private final Random random = new Random();
    private final char[] dirs = {'U','D','L','R'};

    public Ghost(int textureId, int x, int y, int tileSize) {
        super(textureId, x, y, tileSize, tileSize, tileSize);
        randomizeDirection(); // ensures velocity is set
    }

    public void randomizeDirection() {
        char dir = dirs[random.nextInt(dirs.length)];
        updateDirection(dir);
    }

    @Override
    public void reset() {
        super.reset();
        randomizeDirection(); // reset with new direction
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



