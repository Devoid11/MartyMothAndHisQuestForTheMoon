package pkgTwoDGame;

public class Camera {

    private float x, y;

    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick(Moth marty) {
        if (-y > (marty.getY() - 2 * Java2DGame.SCREENHEIGHT / 5) - 50) {
            y += 3;
        } else if (-y < (marty.getY() - 3 * Java2DGame.SCREENHEIGHT / 5) + 50) {
            y -= 3;
        }
    }

    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }
    public void setY(float newY){
        y = newY;
    }

    public void reset(int newX, int newY) {
        x = newX;
        y = newY;
    }

}
