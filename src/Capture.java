import java.awt.*;
import java.awt.image.BufferedImage;

public class Capture {

    private static final int fps=144;

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
        return new Robot().createScreenCapture(screenRect);
    }
}
