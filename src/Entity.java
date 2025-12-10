public class Entity {
    public int x, y, width, height;
    public int startX, startY;
    public char direction = 'U'; // U D L R
    public int velocityX = 0, velocityY = 0;
    public int tileSize;
    public int textureId; // OpenGL texture handle

    public Entity(int textureId, int x, int y, int width, int height, int tileSize) {
        this.textureId = textureId;
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.startX = x; this.startY = y;
        this.tileSize = tileSize;
    }

    public void updateVelocity() {
        switch (direction) {
            case 'U': velocityX = 0;          velocityY = -tileSize/4; break;
            case 'D': velocityX = 0;          velocityY =  tileSize/4; break;
            case 'L': velocityX = -tileSize/4; velocityY = 0;           break;
            case 'R': velocityX =  tileSize/4; velocityY = 0;           break;
        }
    }

    public void updateDirection(char dir) {
        direction = dir;
        updateVelocity();
    }

    public void reset() {
        x = startX; y = startY;
    }
}
