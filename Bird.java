/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgTwoDGame;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Jacob
 */
public class Bird extends Threat {


    final Rectangle hitBox;

    public Bird(int x, int y, Rectangle shape, int speed, String BASEPATH, int animationSpeed) {
        super(x, y, shape, speed, 8, BASEPATH, animationSpeed);
        super.assignFrames(NUMFRAMES, BASEPATH);
        w = (int) shape.getWidth();
        h = (int) shape.getHeight();
        curFrame = new ImageIcon("src/Resources/BirdImages/brb0.png");
        threatCount++;
        hitBox = shape;
        
    }

    public void generateNext(Bird lastBird) {
        Random rand = new Random();
        // X value of the next platform
        int xNew = (lastBird.getX() + rand.nextInt(300) - 150 - w / 2);

        if (xNew < 0) { // If the new X is off the left side of the screen
            xNew += -xNew + rand.nextInt(80); // Add the distance it is off the screen, then add a random value
        } else if (w + xNew >= Java2DGame.SCREENWIDTH - 7) { // If the right side of the platform is off the right side of the screen
            xNew -= (xNew + w - Java2DGame.SCREENWIDTH + 6) + rand.nextInt(80); // subtract the distance it is off the screen plus a random value

        }

        // Determine the new Y coordinate for the platform
        int yNew = lastBird.getY() - 120 - rand.nextInt(10);
        Rectangle newRectangle = new Rectangle(xNew, yNew, w, h);

        Board.birds.add(new Bird(xNew, yNew, newRectangle, speed, BASEPATH, 1));    }

      @Override
    public Image getImage() {
        return super.getImage();
    }
    
    @Override
    public void move() {
        x += speed;
        hitBox.setLocation(x, y+5);
        //If the bird leaves screen, delete it
        if (x < 0 - w || x > Java2DGame.SCREENWIDTH+w) {
            delete();
        }

    }

    public void delete() {
        Board.birds.remove(this);
    }

    @Override
    public void interactWithMoth() {
        if (Board.marty.hardHitbox.intersects(hitBox)) {
            Board.marty.kill();
        }
    }

}
