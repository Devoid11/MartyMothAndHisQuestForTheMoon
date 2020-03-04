/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgTwoDGame;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import static pkgTwoDGame.Java2DGame.SCREENHEIGHT;
import static pkgTwoDGame.Java2DGame.SCREENWIDTH;

public class Board extends JPanel implements ActionListener {

    public static Camera cam = new Camera(0, 0);
    private static Timer timer;
    static protected Moth marty;
    private final int DELAY = 15;
    protected static final int STARTINGX = 100;
    protected static final int STARTINGY = 300;
    protected static Bird bird;
    protected static Darkness darkness;
    protected Background bg;

    public static Rectangle moonBox = new Rectangle(-10, -10, 0, 0);

//    protected static Rectangle deathZone = new Rectangle(0, 680, SCREENWIDTH, 100);
    protected static Rectangle darkZone = new Rectangle(0, 280, SCREENWIDTH, 500); //The rectangele that corrupts marty on intersection
    protected float alpha = 0f; //Alpha value of marty, used for "dark moth transtion"
    public static ArrayList<Platform> platforms = new ArrayList<>(); //Array list of all currently placed platforms
    public static ArrayList<Background> bgImages = new ArrayList<>();
    public static ArrayList<Bird> birds = new ArrayList<>(); //Array list to hold all the birds

    public static boolean isPaused; //Checks if game is currently set to paused

    public static boolean gameIsOver = false; // Tracks whether or not the game has ended.

    protected static boolean isWinning = false; //checks if player has passed last platform

    public static boolean isEndGame = false;

    public static boolean isPregame = true;

    public static boolean isMooned = false;

    public static int totalOfPlatforms = 0; //Counter for total platforms gerneated to watch game progression

    private int randNum;//for random number generation
    private int gameLength = 30; //max amount of platforms the player must climb to win

    private int camCap = gameLength * 1000; // Sets cap at a large number so as not to accidently trigger a boolean

    public static boolean cheatMode = false;

    protected Graphics2D g2d;

    public Board() {

        initBoard();
    }

    protected void initBoard() {

        setSize(SCREENWIDTH, SCREENHEIGHT);
        addKeyListener(new TAdapter());
        Color color = new Color(0x5C2C07);
        setBackground(color);
        setFocusable(true);
        timer = new Timer(DELAY, this);
        startGame();

    }

    protected static void startGame() {
        //resest values to default
        isEndGame = false;
        if (bgImages.size() != 0) {
            bgImages.clear();
        }
        bgImages.add(new Background(0, 0, "root"));
        isWinning = false;
        gameIsOver = false;
        isPaused = true;
        cam.reset(0, 0);
        marty = null;
        marty = new Moth();
        timer.start();
        if (!platforms.isEmpty()) {
            platforms.clear();
            totalOfPlatforms = 0;
        }
        if (!birds.isEmpty()) {
            birds.clear();
        }
        generateStartingPlatform();
        initThreats();
        for (int i = 0; i < 5; i++) {
            Platform.generateNext(platforms.get(platforms.size() - 1));
            totalOfPlatforms++;
        }
    }

    protected static void gameOver() {
        //otherstuff
        cam.setY(0);
        gameIsOver = true;
        timer.stop();
        // startGame();
    }

    /*
    The image seems to want to keep up with the camera for some reason. Otherwise it would stay put at the given value;
     */
    protected static void win() {
        marty.win();
        isWinning = true;
        platforms.add(new Platform(0, platforms.get(platforms.size() - 1).getY(), SCREENWIDTH, 10));
        if (!birds.isEmpty()) {
            birds.clear();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void doDrawing(Graphics g) {
        System.out.println(g);
        g2d = (Graphics2D) g;
        Graphics2D g2dAlpha = (Graphics2D) g;
        g2d.translate(cam.getX(), cam.getY());

        for (int i = 0; i <= bgImages.size() - 1; i++) {
            bg = bgImages.get(i);
            g2d.drawImage(bg.getCurImg(), bg.x, bg.y, this);
        }
        Color leaf = Color.decode("#005500");
        g.setColor(leaf);
        //Draw the platforms
        if (platforms.size() > 0) {
            Platform p;
            for (int i = 0; i < platforms.size(); i++) {
                p = platforms.get(i);
                if (isWinning) { //end of game
                    if (platforms.size() - 1 != i) { //If the current platform is not the last "hidden" platform
                        g2d.fill(p.getRect()); //color it
                    }
                } else { // not end of game
                    g2d.fill(p.getRect());//color all platforms
                }
            }
        }

        //Draw the moth
        g2d.drawImage(marty.getImage(), marty.getX(), marty.getY(), this);
        g.setColor(Color.BLACK);
        //draw bird threats
        for (int i = 0; i < birds.size(); i++) {
            g2d.drawImage(birds.get(i).getImage(), birds.get(i).getX(), birds.get(i).getY(), this);
        }
        g2d.drawImage(darkness.getImage(), 0, darkness.y, this);

        g.setColor(Color.BLACK);
        g2d.fill(darkness.theVoid);

        //Draw the darkness on the moth
        alpha = marty.getDarkInfluence();
        g2dAlpha.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2dAlpha.drawImage(marty.getDarkImage(), marty.getX(), marty.getY(), this);

        if (gameIsOver) {
            g2dAlpha.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.drawImage(new ImageIcon("src/Resources/DeathScreenImages/deathSplashScreen.png").getImage(), 0, 0, this);
        }
        if (isPregame) {
            g2dAlpha.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.drawImage(new ImageIcon("src/Resources/StartTextImage/startText.png").getImage(), 0, 0, this);
        }
        if (isPaused && !isPregame && !gameIsOver) {
            g2dAlpha.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.drawImage(new ImageIcon("src/Resources/PauseText/pauseText.png").getImage(), 0, marty.getY() - 300, this);
        }
        if (isMooned) {
            g2dAlpha.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            g2d.drawImage(new ImageIcon("src/Resources/winText/winText.png").getImage(), 0, bgImages.get(bgImages.size()-1).getY(), this);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e);
        step();
    }

    private void step() {
        if (!isPaused) {
            handleMarty();
            if (cam.getY() < camCap) {
                cam.tick(marty);
            }
            if (isEndGame && marty.getY() < bgImages.get(bgImages.size() - 1).getY() + 200 && !isMooned) {
                isMooned = true;
                platforms.add(new Platform(0, bgImages.get(bgImages.size() - 1).getY(), SCREENWIDTH, 1));
                platforms.add(new Platform(0, bgImages.get(bgImages.size() - 1).getY() + 233, SCREENWIDTH, 1));
            }
            if (!isWinning) {
                handleThreats();
                handlePlatforms();
                handleBg();
            }
            repaint();
        }
        randNum = (int) (Math.random() * (500) + 1);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            marty.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            marty.keyPressed(e);
            if (KeyEvent.VK_SPACE == e.getKeyCode()) {
                if (!isPaused) {
                    timer.stop(); // Pauses Game
                    isPaused = true;
                    repaint();
                } else {
                    timer.start();
                    isPregame = false;
                    isPaused = false;
                }
            } else if (KeyEvent.VK_X == e.getKeyCode()) {
                startGame();
                isPregame = true;
                repaint();
            } else if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
                Java2DGame.ex.dispose();
                marty.kill();
            }
        }
    }

    private static void generateStartingPlatform() {

        platforms.add(new Platform(STARTINGX, STARTINGY, 100, 50));

    }

    private static void initThreats() {
        darkness = new Darkness(0, 280, darkZone, 1, "src/resources/DarknessImages/dark", 1);
        birds.add(new Bird(400, 10, new Rectangle(400, 15, 80, 70), -2, "src/resources/BirdImages/brb", 30));
    }

    private void handlePlatforms() {
        if (platforms.size() > 0) {
            for (int i = 0; i < platforms.size(); i++) {
                platforms.get(i).tick();
            }
            if (platforms.size() < 50 && totalOfPlatforms < gameLength) {
                Platform.generateNext(platforms.get(platforms.size() - 1));
                totalOfPlatforms++;
            }
        }
    }

    private void handleThreats() {
        darkness.tick();
        for (int i = 0; i < birds.size(); i++) {
            birds.get(i).tick();
        }
        if (randNum == 100) {
            birds.add(new Bird(SCREENWIDTH, marty.getY() - 150, new Rectangle(500, marty.getY() - 110, 80, 70), -2, "src/resources/BirdImages/brb", 30));
        }

    }

    private void handleMarty() {
        marty.move();
        if (alpha >= 1) {
            marty.kill();
        }
        if (totalOfPlatforms == gameLength && marty.getY() < platforms.get(platforms.size() - 1).getY() && !isWinning) { //Marty has gone above the last platform
            win();
        }
    }

    private void handleBg() {
        for (int i = 0; i <= bgImages.size() - 1; i++) {
            bgImages.get(i).tick();
        }
        if (bgImages.size() < 60 && !isEndGame) {
            bg = bgImages.get(bgImages.size() - 1);
            bg.generateNext(bg);
        }
        if (totalOfPlatforms == gameLength && !isEndGame) { //The last platform has been placed
            bgImages.add(new Background(0, platforms.get(platforms.size() - 1).getY() - 550, "treeTop"));
            bgImages.add(new Background(0, bgImages.get(bgImages.size() - 1).getY() - 1500, "moon"));
            camCap = (-1 * platforms.get(platforms.size() - 1).getY()) + 2039;
            isEndGame = true;
        }
    }

}
