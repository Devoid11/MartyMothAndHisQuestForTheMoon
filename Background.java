/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgTwoDGame;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author EKUStudent
 */
public class Background {

    private Image root;
    private Image trunk;
    private Image treeTop;
    private Image moon;
    private Image curImage;
    protected int x;
    protected int y;
    private int imageCounter = 0;
    private int approximateImageCount;
    private Image[][] images;
    private int height;

    public Background(int x, int y, String imageType) {
        this.y = y;
        this.x = x;
        loadImage();
        assignImage(imageType);
    }

    private void loadImage() {
        ImageIcon rootII = new ImageIcon("src/resources/TreeImages/Tree0.png");
        root = rootII.getImage();

        trunk = getTrunkImage();

        ImageIcon treeTopII = new ImageIcon("src/resources/TreeImages/Tree4.png");
        treeTop = treeTopII.getImage();

        ImageIcon moonII = new ImageIcon("src/resources/TreeImages/Tree5.png");
        moon = moonII.getImage();
    }

    protected void assignImage(String imageType) {
        if (imageType.equals("root")) {
            curImage = root;
            height = 750;
        } else if (imageType.equals("trunk")) {
            curImage = getTrunkImage();
            height = 550;
        } else if (imageType.equals("treeTop")) {
            curImage = treeTop;
            height = 550;
        } else if (imageType.equals("moon")) {
            curImage = moon;
            height = 1500;
        }
    }

    public Image getTrunkImage() {
        String path = "src/resources/TreeImages/Tree";
        int rand = (int) (Math.random() * 3);

        switch (rand) {
            case 0:
                path += "1.png";
                break;
            case 1:
                path += "2.png";
                break;
            default:
                path += "3.png";
                break;
        }
        ImageIcon trunkII = new ImageIcon(path);
        return trunkII.getImage();
    }

    public void generateNext(Background lastBg) {
        if (!Board.isEndGame) {
            Board.bgImages.add(new Background(0, y - 550, "trunk"));
        }
    }

    public void tick() {
        if (y > Board.darkness.deathZone.y) {
            Board.bgImages.remove(this);
        }
    }

    public Image getRootImage() {
        return root;
    }

    public Image getTreeTop() {
        return treeTop;
    }

    public Image getMoon() {
        return moon;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Image getCurImg() {
        return curImage;
    }

    public int getHeight() {
        return height;
    }

}
