import java.util.HashSet;
import java.util.List;

public class GameMap {
    public HashSet<Entity> walls = new HashSet<>();
    public HashSet<Entity> foods = new HashSet<>();
    public HashSet<Ghost> ghosts = new HashSet<>();
    public Pacman pacman;

    private final int tileSize;
    private final String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };


    public GameMap(int tileSize, TextureManager tm) {
        this.tileSize = tileSize;
        load(tm);
    }

    private void load(TextureManager tm) {
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[r].length(); c++) {
                char ch = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                switch (ch) {
                    case 'X':
                        walls.add(new Entity(tm.getWallTex(), x, y, tileSize, tileSize, tileSize));
                        break;
                    case 'b':
                        ghosts.add(new Ghost(tm.getBlueGhostTex(), x, y, tileSize));
                        break;
                    case 'o':
                        ghosts.add(new Ghost(tm.getOrangeGhostTex(), x, y, tileSize));
                        break;
                    case 'p':
                        ghosts.add(new Ghost(tm.getPinkGhostTex(), x, y, tileSize));
                        break;
                    case 'r':
                        ghosts.add(new Ghost(tm.getRedGhostTex(), x, y, tileSize));
                        break;
                    case 'P':
                        pacman = new Pacman(
                                tm.getPacRightTex(), tm.getPacUpTex(), tm.getPacDownTex(), tm.getPacLeftTex(),
                                x, y, tileSize
                        );
                        break;
                    case ' ':
                        // food is a tiny quad; you can use wall texture or a dedicated dot texture
                        foods.add(new Entity(tm.getWallTex(), x + 14, y + 14, 4, 4, tileSize));
                        break;
                    default:
                        // 'O' skip tiles do nothing
                }
            }
        }
        for (Ghost ghost : ghosts) {
            ghost.randomizeDirection(); // ensures all ghosts start moving
        }

    }
    public List<Entity> getWalls() {
        return (List<Entity>) walls;
    }


}
