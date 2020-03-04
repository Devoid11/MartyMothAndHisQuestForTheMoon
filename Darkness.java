/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgTwoDGame;

import java.awt.*;
import javax.swing.ImageIcon;

/**
 *
 * @author EKUStudent
 */
public class Darkness extends Threat {
    protected final Rectangle darkZone;
    protected final Rectangle deathZone;
    protected final Rectangle theVoid;

    public Darkness(int x, int y, Rectangle shape, int speed, String BASEPATH, int animationSpeed) {
        super(x, y, shape, speed, 10, BASEPATH, animationSpeed);
        super.assignFrames(NUMFRAMES, BASEPATH);
        curFrame = new ImageIcon("src/resources/DarknessImages/dark0.png");
        threatCount++;
        darkZone = new Rectangle (0, y, (int) shape.getWidth(), (int) shape.getHeight() * 4 / 5);
        deathZone = new Rectangle(0, y + (int) darkZone.getHeight(), (int) shape.getWidth(), (int) shape.getHeight() * 1 / 5);
        theVoid = new Rectangle  (0, y + (int) deathZone.getHeight()+(int) darkZone.getHeight() , (int) shape.getWidth(), 10000);
    }

    @Override
    public void move() {
        //"Rubberbanding" so the threat stays imminent
        if (darkZone.getY()- Board.marty.getY() > 450) {
            y -= 105;
        }
        y -= speed;
        darkZone.setLocation(0, y);
        deathZone.setLocation(0, y + (int) darkZone.getHeight());
        theVoid.setLocation(0, y + (int) deathZone.getHeight()+(int) darkZone.getHeight());
    }

    @Override
    public Image getImage() {
        return super.getImage();
    }

    @Override
    public void interactWithMoth() {
        if (Board.marty.hardHitbox.intersects(darkZone)) {
            Board.marty.darken(1);
        } else if (Board.marty.hardHitbox.intersects(deathZone)) {
            Board.marty.kill();
        } else {
            Board.marty.darken(-0.5);
        }
    }
}
