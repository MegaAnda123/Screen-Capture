import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Capture {

    private static final int fps=144;

    public static void main(String[] args) throws AWTException, InterruptedException, IOException, ClassNotFoundException {
        BufferedImage capture;
        View view = new View();
        //Client client = new Client();

        while (true) {
            capture = captureScreenFrame();

            String serializedObject = Serializer.ObjectToString(new Serializer.Data(makeJPG(capture)));
            Serializer.Data image = (Serializer.Data) Serializer.ObjectFromString(serializedObject);


            view.drawFrame(ImageIO.read(image.getFile()));

            //client.outData("");



            Thread.sleep(1000/fps);

        }
    }


    /**
     *
     * @param image
     * @return
     * @throws IOException
     */
    //TODO make method write to memory, not a temp file on disk. (If possible).
    static File makeJPG(BufferedImage image) throws IOException {
        File out = new File("temp\\temp.jpg");
        ImageIO.write(image, "jpg", out);
        return out;
    }

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

    static BufferedImage captureScreenFrame() throws AWTException {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return new Robot().createScreenCapture(screenRect);
    }
}
