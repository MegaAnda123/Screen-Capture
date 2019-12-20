import java.awt.*;
import java.awt.image.BufferedImage;

public class Capture {

    static int fps=60;

    public static void main(String[] args) throws AWTException, InterruptedException {
        BufferedImage capture;
        View view = new View();
        while (true) {
            capture = captureScreenFrame();
            view.drawFrame(capture);
            Thread.sleep(1000/fps);
        }
    }

    private static BufferedImage captureScreenFrame() throws AWTException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = new Robot().createScreenCapture(screenRect);
        return capture;
    }
}
