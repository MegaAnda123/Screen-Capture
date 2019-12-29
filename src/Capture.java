import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Class for handling screen capture and image conversion.
 *
 * @author Andre
 * @version 0.1
 */
public class Capture {

    /**
     * Method for converting and compressing a buffered image to a JPG file.
     * @param image The buffered image the method will convert.
     * @return Returns a compressed JPG file of the buffered image.
     * @throws IOException TODO
     */
    //TODO make method write to memory, not a temp file on disk. (If possible).
    static File makeJPG(BufferedImage image) throws IOException {
        File out = new File("temp\\temp.jpg");
        ImageIO.write(image, "jpg", out);
        return out;
    }

    //TODO Pending deletion
    JPanel makeImage(BufferedImage image) {
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0,1920,1080,null);
            }
        };
        return pane;
    }

    /**
     * Method for capturing a frame of the primary screen on the system.
     * @return Returns the frame of the screen as buffered image.
     * @throws AWTException TODO
     */
    static BufferedImage captureScreenFrame() throws AWTException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return new Robot().createScreenCapture(screenRect);
    }
}
