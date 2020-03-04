package pkgTwoDGame;

import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

/**
 *
 * @Author Jacob Williams and Chase Dawson
 */
public class Moth {

    // Force of gravity on math
    private int GRAVITY = 4;
    // Directional velocities
    private int dx;
    private int dy = GRAVITY;
    // Walking speed and jumping speed
    int SPEED = 3;
    final int JUMPINGSPEED = 80;
    // Collision Checking
    private int distToCollRight = SPEED + 1, distToCollLeft = SPEED + 1;
    private int distToCollUp = SPEED + 1, distToCollDown = SPEED + 1;
    /*Variables for remembering the direction the moth is flying in
        useful for preventing a key being held downand the moth not moving
        Example: Hold right, press left, release left, should still be moving right*/
    private boolean movingLeft;
    private boolean movingRight;
    private boolean movingUp;
    private boolean movingDown;
    private int numOfJumps;
    // Size and position info
    final private int WIDTH = 37; //Width of unflapped
    final private int HEIGHT = 33; //Height of unflapped
    private int x = Board.STARTINGX;
    private int y = Board.STARTINGY - HEIGHT;
    // "Soft Hitbox" for detecting nearby platforms, so that not collision doesn't have to be checked with every platform every tick
    public Rectangle softHitbox = new Rectangle(x - 2 * SPEED, y - 2 * SPEED - GRAVITY, WIDTH + 4 * SPEED, HEIGHT + 4 * SPEED + GRAVITY);
    protected Rectangle hardHitbox = new Rectangle(x, y, WIDTH, HEIGHT);
    //Needed images for Moth
    private Image curImg;
    private Image preFlapLeft;
    private Image preFlapRight;
    private Image postFlapLeft;
    private Image postFlapRight;
    // private Image back;
    private Image darkCurImg;
    private Image darkPreFlapLeft;
    private Image darkPreFlapRight;
    private Image darkPostFlapLeft;
    private Image darkPostFlapRight;
    //private Image darkBack;
    private float darkInfluence;

    private boolean isWinning = false;

    private boolean isTouchingPlatform;

    public Moth() {
        loadImages();
    }

    // Initialize Image variables to images from resource folder
    private void loadImages() {
        ImageIcon preFlapLeftii = new ImageIcon("src/resources/MothImages/MothPreFlapLeftV1.png");
        preFlapLeft = preFlapLeftii.getImage();

        ImageIcon preFlapRightii = new ImageIcon("src/resources/MothImages/MothPreFlapRightV1.png");
        preFlapRight = preFlapRightii.getImage();

        ImageIcon postFlapLeftii = new ImageIcon("src/resources/MothImages/MothFlappedLeftV1.png");
        postFlapLeft = postFlapLeftii.getImage();

        ImageIcon postFlapRightii = new ImageIcon("src/resources/MothImages/MothFlappedRightV1.png");
        postFlapRight = postFlapRightii.getImage();

        //ImageIcon backii = new ImageIcon("src/resources/MothImages/MothBackV1.png");
        ImageIcon darkPreFlapLeftii = new ImageIcon("src/resources/DarkMothImages/DarkMothPreFlapLeftV1.png");
        darkPreFlapLeft = darkPreFlapLeftii.getImage();

        ImageIcon darkPreFlapRightii = new ImageIcon("src/resources/DarkMothImages/DarkMothPreFlapRightV1.png");
        darkPreFlapRight = darkPreFlapRightii.getImage();

        ImageIcon darkPostFlapLeftii = new ImageIcon("src/resources/DarkMothImages/DarkMothFlappedLeftV1.png");
        darkPostFlapLeft = darkPostFlapLeftii.getImage();

        ImageIcon darkPostFlapRightii = new ImageIcon("src/resources/DarkMothImages/DarkMothFlappedRightV1.png");
        darkPostFlapRight = darkPostFlapRightii.getImage();

//        ImageIcon darkBackii = new ImageIcon("src/resources/DarkMothImages/DarkMothBackV1.png");
//       // darkBack = backii.getImage();
        curImg = preFlapRight;
        darkCurImg = darkPreFlapRight;
    }

    // Movement handler of the Moth
    public void move() {
        if (Board.cheatMode) {
            numOfJumps = 10000;
        }

        // Check collision with platform or obstacles prior to move
        checkCollision();
        if (dy < GRAVITY) { // If moving upward
            if (distToCollUp >= JUMPINGSPEED) { // If top of the Moth is further from the above platform than moving again would put it
                y += dy;
                dy += GRAVITY;
            } else {
                y -= distToCollUp;
                dy += GRAVITY;
            }

        } else { // Else Moving downward
            if (distToCollDown >= SPEED) { // If bottom of the Moth is further from the above platform than moving again would put it
                y += dy;
            } else {

                y += distToCollDown;
                if (!isWinning) {
                    numOfJumps = 2;
                }
            }
            if (distToCollUp < 0) {
                y -= distToCollUp;
            }
        }

        if (dx > 0) { // Moving right
            if (distToCollRight >= SPEED) { // If right of the Moth is further from the above platform than moving again would put it
                x += dx;
            } else {
                x += distToCollRight;
            }
        } else if (dx < 0) { // Moving left
            if (distToCollLeft >= SPEED) { // If left of the Moth is further from the above platform than moving again would put it
                x += dx;
            } else {
                x -= distToCollLeft;
            }
        }
        //Update hit boxes
        softHitbox.setLocation(x - 2 * SPEED, y - 2 * SPEED - GRAVITY);
        hardHitbox.setLocation(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {

        return y;
    }

    public int getDy() {
        return dy;
    }

    public Image getImage() {
        return curImg;
    }

    public Image getDarkImage() {
        return darkCurImg;
    }

    public float getDarkInfluence() {
        return Math.max(0, Math.min(1, darkInfluence));
    }
    
    public void setGravity(int grav){
        GRAVITY = grav;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -SPEED;
            movingLeft = true;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = SPEED;
            movingRight = true;
        }

        if (key == KeyEvent.VK_UP) {

            if (numOfJumps > 0) {
                dy = -JUMPINGSPEED;
                numOfJumps--;
                movingUp = true;
            }
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = SPEED + GRAVITY;
            movingDown = true;
        }
        pickImages();
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && movingRight == false) {
            dx = 0;
            movingLeft = false;
        } else if (key == KeyEvent.VK_LEFT && movingRight == true) {
            dx = SPEED;
            movingLeft = false;
        }

        if (key == KeyEvent.VK_RIGHT && movingLeft == false) {
            dx = 0;
            movingRight = false;
        } else if (key == KeyEvent.VK_RIGHT && movingLeft == true) {
            dx = -SPEED;
            movingRight = false;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = GRAVITY;
        }
        pickImages();
    }

    public void pickImages() {
        if (dx < 0) {
            if (curImg != postFlapLeft) {
                curImg = preFlapLeft;
                darkCurImg = darkPreFlapLeft;
            }
        } else if (dx > 0) {
            if (curImg != postFlapRight) {
                curImg = preFlapRight;
                darkCurImg = darkPreFlapRight;
            }
        }
        if (dy > 0) {
            if (distToCollDown != 0) {
                if (curImg == preFlapRight) {
                    curImg = postFlapRight;
                    darkCurImg = darkPostFlapRight;
                } else if (curImg == preFlapLeft) {
                    curImg = postFlapLeft;
                    darkCurImg = darkPostFlapLeft;
                }
            } else {
                if (curImg == postFlapRight) {
                    curImg = preFlapRight;
                    darkCurImg = darkPreFlapRight;
                } else if (curImg == postFlapLeft) {
                    curImg = preFlapLeft;
                    darkCurImg = darkPreFlapLeft;
                }
            }
        } else if (dy < 0) {
            if (curImg == preFlapLeft /*|| curImg == back*/) {
                curImg = postFlapLeft;
                darkCurImg = darkPostFlapLeft;
            } else if (curImg == postFlapLeft) {
                curImg = preFlapLeft;
                darkCurImg = darkPreFlapLeft;
            } else if (/*curImg == back || */curImg == postFlapRight) {
                curImg = preFlapRight;
                darkCurImg = darkPreFlapRight;
            } else if (curImg == preFlapRight) {
                curImg = postFlapRight;
                darkCurImg = darkPostFlapRight;
            }
        }
    }

    public void checkCollision() {
        //Check intersections
        Platform curP;
        int i = 0;
        isTouchingPlatform = false;
        for (i = 0; i < Board.platforms.size(); i++) {
            curP = Board.platforms.get(i);
            if (this.softHitbox.intersects(curP.getRect())) { // If platform is within soft hitbox
                /*Check collision from left and right if y values are in correcct range, if not within correct y values
                  i.e. above or below platform, set those walues to their default values*/
                if (x >= curP.getX() + curP.getWidth() && (y < curP.getY() + curP.getHeight() || y + HEIGHT > curP.getY())) {
                    distToCollLeft = x - (curP.getX() + curP.getWidth());
                } else if (x >= curP.getX() + curP.getWidth() && !(y < curP.getY() + curP.getHeight() || y + HEIGHT > curP.getY())) {
                    distToCollLeft = SPEED + 1;
                } else if (x + WIDTH <= curP.getX() && (y < curP.getY() + curP.getHeight() || y + HEIGHT > curP.getY())) {
                    distToCollRight = curP.getX() - (x + WIDTH);
                } else if (x <= curP.getX() && !(y < curP.getY() + curP.getHeight() && y + HEIGHT > curP.getY())) {
                    distToCollRight = SPEED + 1;
                }

                /*Check collision of up and down if x values are in correcct range, if not within correct x values
                  i.e. to the right or left of the platform, set those walues to their default values*/
                if (y <= curP.getY() && (x < curP.getX() + curP.getWidth() && x + WIDTH > curP.getX())) {
                    distToCollDown = curP.getY() - (y + HEIGHT) + GRAVITY;
                } else if (y <= curP.getY() && !(x < curP.getX() + curP.getWidth() && x + WIDTH > curP.getX())) {
                    distToCollDown = SPEED + 1;
                } else if (y >= curP.getY() + curP.getHeight() && (x < curP.getX() + curP.getWidth() && x + WIDTH > curP.getX())) {
                    distToCollUp = y - (curP.getY() + curP.getHeight() + curP.getDy());

                } else if (y >= curP.getY() + curP.getHeight() && !(x < curP.getX() + curP.getWidth() && x + WIDTH > curP.getX())) {
                    distToCollUp = SPEED + 1;
                }
                isTouchingPlatform = true;
            }
        }
        if (!isTouchingPlatform) { // No platforms are within the softhitBox
            //Reset distances to default values
            distToCollRight = SPEED + 1;
            distToCollLeft = SPEED + 1;
            distToCollUp = SPEED + 1;
            distToCollDown = SPEED + 1;
        }

        if (softHitbox.getX() <= 0) {
            distToCollLeft = x;
        }

//        if (softHitbox.intersects(Board.darkness.deathZone)) {
//            distToCollDown = (int) Board.darkness.deathZone.getY() - (y + HEIGHT) + GRAVITY;
//        }
        if (softHitbox.getX() + softHitbox.getWidth() >= Java2DGame.SCREENWIDTH - 7) {
            distToCollRight = (Java2DGame.SCREENWIDTH - 7) - (x + WIDTH);
        }

        if (softHitbox.intersects(Board.moonBox)) {
            Rectangle curR = Board.moonBox;
            if (x >= curR.getX() + curR.getWidth() && (y < curR.getY() + curR.getHeight() || y + HEIGHT > curR.getY())) {
                distToCollLeft = (int) (x - (curR.getX() + curR.getWidth()));
            } else if (x >= curR.getX() + curR.getWidth() && !(y < curR.getY() + curR.getHeight() || y + HEIGHT > curR.getY())) {
                distToCollLeft = SPEED + 1;
            } else if (x + WIDTH <= curR.getX() && (y < curR.getY() + curR.getHeight() || y + HEIGHT > curR.getY())) {
                distToCollRight = (int) (curR.getX() - (x + WIDTH));
            } else if (x <= curR.getX() && !(y < curR.getY() + curR.getHeight() && y + HEIGHT > curR.getY())) {
                distToCollRight = SPEED + 1;
            }

            /*Check collision of up and down if x values are in correcct range, if not within correct x values
                  i.e. to the right or left of the platform, set those walues to their default values*/
            if (y <= curR.getY() && (x < curR.getX() + curR.getWidth() && x + WIDTH > curR.getX())) {
                distToCollDown = (int) (curR.getY() - (y + HEIGHT) + GRAVITY);
            } else if (y <= curR.getY() && !(x < curR.getX() + curR.getWidth() && x + WIDTH > curR.getX())) {
                distToCollDown = SPEED + 1;
            } else if (y >= curR.getY() + curR.getHeight() && (x < curR.getX() + curR.getWidth() && x + WIDTH > curR.getX())) {
                distToCollUp = (int) (y - (curR.getHeight() + curR.getY()));

            } else if (y >= curR.getY() + curR.getHeight() && !(x < curR.getX() + curR.getWidth() && x + WIDTH > curR.getX())) {
                distToCollUp = SPEED + 1;
            }
            isTouchingPlatform = true;
        }
    }

    public void kill() {
        die();
        Board.gameOver();
    }

    public void darken(double darkIncrement) {
        darkInfluence = (float) Math.max(0, darkInfluence + darkIncrement / 400);
    }

    public void die() {
        GRAVITY = 0;
        dy = 0;
        numOfJumps = 0;
        SPEED = 0;
        Board.cam.setY(0);
    }

    public void win() {
        isWinning = true;
        numOfJumps = 10000;
        darkInfluence = 0;
    }

}
