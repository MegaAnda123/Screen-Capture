import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

class View {
    private JFrame frame = buildFrame();
    GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice device = graphics.getDefaultScreenDevice();

    void drawFrame(BufferedImage image) {
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0,frame.getWidth(),frame.getHeight(),null);
            }
        };


        frame.getContentPane().removeAll();
        frame.add(pane);
        frame.setVisible(true);

    }

    private static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setVisible(true);
        return frame;
    }

    void setFullScreen() {
        frame.setResizable(false);
        device.setFullScreenWindow(frame);
    }
}