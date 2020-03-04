package pkgTwoDGame;

import java.awt.*;
import javax.swing.*;

/*
 * Authors : Chase Dawson, Jacob Williams
 * Play as Marty Moth in his quest for the moon
 */
public class Java2DGame extends JFrame {

    // Constants for screen width and height
    protected static final int SCREENWIDTH = 500;
    public static final int SCREENHEIGHT = 750;
    public static Java2DGame ex = new Java2DGame();

    public Java2DGame() {
        initUI();
    }

    private void initUI() {
        setSize(SCREENWIDTH, SCREENHEIGHT);

        add(new Board());

        setResizable(false);
        setTitle("Marty Moth and His Quest for the Moon");
        Image icon = Toolkit.getDefaultToolkit().getImage("src/resources/MothImages/MothBackV2.png");
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        UIDefaults uiDefaults = UIManager.getDefaults();
        uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.blue));
        uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.green));
        //JFrame.setDefaultLookAndFeelDecorated(true);
        EventQueue.invokeLater(() -> {
            ex.setVisible(true);
        });
    }

}
