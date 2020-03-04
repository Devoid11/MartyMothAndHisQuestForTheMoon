package pkgTwoDGame;

import java.awt.*;
import java.util.Random;

public class Platform {

    private int x;
    private int y;
    protected int dx = 0, dy = 1;
    private static int w = 100, h = 50;
    private Rectangle platform;

    public Platform(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        w = width;
        h = height;

        platform = new Rectangle(x, y, width, height);

    }

    
    // Moving functionality of platforms
    public void tick() {
        this.platform = new Rectangle(x, y, w, h);
        if(y> Java2DGame.SCREENHEIGHT){
            delete();
        }
        if(platform.intersects(Board.darkness.deathZone)){
            delete();
        }
    }

    public int getX() {return x;}

    public int getY() {return y;}

    public int getDy() {return dy;}

    public int getWidth() {return w;}

    public int getHeight() {return h;}

    public Rectangle getRect() {return platform;}

    // Generates the next platform based on the position of the last platform
    public static void generateNext(Platform lastPlatform) {
        Random rand = new Random();
        // X value of the next platform
        int xNew = ((lastPlatform.getX() + lastPlatform.getWidth() / 2) + rand.nextInt(300) - 150 - w / 2);

        if (xNew < 0) { // If the new X is off the left side of the screen
            xNew += -xNew + rand.nextInt(80); // Add the distance it is off the screen, then add a random value
        } 
        else if (xNew + w >= Java2DGame.SCREENWIDTH - 7) { // If the right side of the platform is off the right side of the screen
            xNew -= (xNew + w - Java2DGame.SCREENWIDTH +6)  + rand.nextInt(80); // subtract the distance it is off the screen plus a random value
            
        }
        
        // Determine the new Y coordinate for the platform
        int yNew = lastPlatform.getY() - 120 - rand.nextInt(10);
        Board.platforms.add(new Platform(xNew, yNew, 100, 50));
    }
    
    public void delete(){
        Board.platforms.remove(this);
        
    }
    
}