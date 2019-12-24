import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Capture {

    private static final int fps=144;

    public static void main(String[] args) throws AWTException, InterruptedException, IOException, ClassNotFoundException {
        BufferedImage capture;
        View view = new View();
        Client client = new Client();

        while (true) {
            capture = captureScreenFrame();

            //TODO serialize image

            view.drawFrame(capture);

            //client.outData("");



            Thread.sleep(1000/fps);

        }
    }

    private static BufferedImage captureScreenFrame() throws AWTException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return new Robot().createScreenCapture(screenRect);
    }
}
