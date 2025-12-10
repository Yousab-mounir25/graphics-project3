import com.sun.opengl.util.GLUT;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import java.io.IOException;

public class TextureManager {
    private final String assetsFolder = "assets";
    private final String[] textureNames = {
            "wall.png", "blueGhost.png", "orangeGhost.png", "pinkGhost.png", "redGhost.png",
            "pacmanUp.png", "pacmanDown.png", "pacmanLeft.png", "pacmanRight.png"
    };

    public int[] textureIDs;            // GL texture handles
    public TextureReader.Texture[] textures; // raw texture data

    public void init(GL gl) {
        textures = new TextureReader.Texture[textureNames.length];
        textureIDs = new int[textureNames.length];
        gl.glGenTextures(textureNames.length, textureIDs, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                textures[i] = TextureReader.readTexture(assetsFolder + "/" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textureIDs[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        textures[i].getWidth(), textures[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        textures[i].getPixels()
                );

                // Texture parameters for smoothing
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);

            } catch (IOException e) {
                System.err.println("Failed to load texture: " + textureNames[i]);
                e.printStackTrace();
            }
        }
    }

    public int getWallTex()       { return textureIDs[0]; }
    public int getBlueGhostTex()  { return textureIDs[1]; }
    public int getOrangeGhostTex(){ return textureIDs[2]; }
    public int getPinkGhostTex()  { return textureIDs[3]; }
    public int getRedGhostTex()   { return textureIDs[4]; }
    public int getPacUpTex()      { return textureIDs[5]; }
    public int getPacDownTex()    { return textureIDs[6]; }
    public int getPacLeftTex()    { return textureIDs[7]; }
    public int getPacRightTex()   { return textureIDs[8]; }
}
