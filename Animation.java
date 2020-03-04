/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgTwoDGame;

import javax.swing.ImageIcon;

/**
 *
 * @author chase_dawson7
 */
public class Animation {

    ImageIcon[] frames;
    private ImageIcon curImage;
    private int imageIndex = 0;
    private boolean reverse = false;
    protected double count = 0.0;
    protected final double countIncrement;
    protected final double ABITRARYCONSTANT = 40;

    public Animation(ImageIcon[] images, int animationSpeed) {
        frames = images;
        countIncrement = animationSpeed;
    }

    public ImageIcon next() {
        try {
            curImage = frames[imageIndex];
        } catch (Exception e) {
            System.out.println(e);
        }
        updateIndex();
        return curImage;

    }

    private void updateIndex() {

        if (imageIndex == 0) {
            reverse = false;
        }
        if (imageIndex == frames.length - 1) {
            reverse = true;
        }

        count += countIncrement;
        if (count >= ABITRARYCONSTANT) {
            count = 0;
            if (!reverse) {
                imageIndex += 1;
            } else {
                imageIndex -= 1;
            }
        }
    }

}
