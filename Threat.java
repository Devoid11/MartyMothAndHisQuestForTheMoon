/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgTwoDGame;

import java.awt.Image;
import java.awt.Shape;
import javax.swing.ImageIcon;

/**
 *
 * @author EKUStudent
 */
public class Threat {

    protected int x, y, w, h;
    //private int width, height;
    protected Shape shape;
    protected Animation animation;
    protected ImageIcon curFrame;
    protected int speed;
    public static int threatCount;
    //animation varibles
    protected String BASEPATH = "";
    protected int NUMFRAMES = 0;
    ImageIcon[] frames;
    protected int animationSpeed = 0;

    public Threat(int x, int y, Shape shape, int speed, int NUMFRAMES, String BASEPATH, int animationSpeed) {
        this.x = x;
        this.y = y;
        this.shape = shape;
        this.speed = speed;
        this.NUMFRAMES = NUMFRAMES;
        this.BASEPATH = BASEPATH;
        this.animationSpeed = animationSpeed;
        frames = new ImageIcon[NUMFRAMES];
    }

    protected void assignFrames(int numFrames, String basePath) {
        String path;
        for (int i = 0; i < numFrames; i++) {
            path = basePath + i + ".png";
            frames[i] = new ImageIcon(path);
        }
        animation = new Animation(frames, animationSpeed);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Shape getShape() {
        return shape;
    }

    public Image getImage() {
        return curFrame.getImage();
    }

    protected void interactWithMoth() {
    }

    protected void move() {

    }

    public void tick() {
        curFrame = animation.next();
        move();
        interactWithMoth();
    }

}
