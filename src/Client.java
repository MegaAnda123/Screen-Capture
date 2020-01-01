import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;

/**
 * Class for the client backend of the program.
 *
 * @author Andre
 * @version 0.1
 */
public class Client {
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        new Client();
    }

    private View view;
    private Capture capture = new Capture();
    private boolean readInData = true;
    private ClientSocket socket;

    /**
     * Client initializer. Connects the client to the server and
     * starts the threads for listening to the server.
     * @throws IOException TODO
     * @throws AWTException TODO
     * @throws InterruptedException TODO
     */
    Client() throws IOException, AWTException, InterruptedException {
        try {
            socket = new ClientSocket("192.168.1.118");
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread socketInThread = new Thread(new ReadSocketInThread());
        socketInThread.start();

        while(true) {
            File image = Capture.makeJPG(Capture.captureScreenFrame());
            String string = Serializer.ObjectToString(new Serializer.Data(image));
            outData("image " + string);
            Thread.sleep(1000/144);
        }
    }

    /**
     * Method for displaying a serialized image.
     * @param string The serialized image.
     */
    private void displayImage(String string) {
        try {
            Serializer.Data image = (Serializer.Data) Serializer.ObjectFromString(string);
            view.drawFrame(ImageIO.read(image.getFile()));
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for processing data form the server and executing the given command.
     * @param string The string given by the server.
     */
    private void processInData(String string) {
        String[] temp = string.split(" ");
        String command = temp[0];
        String message;
        try {
            message = string.substring((temp[0].length() + 1));
        } catch (StringIndexOutOfBoundsException e) {
            message = "";
        }

        switch (command) {
            case "return":
                System.out.println(message);
                break;
            case "error":
                System.out.println("Error: " + message);
                break;
            case "image":
                displayImage(message);
                break;
            default:
                System.out.println("Unknown error");
        }
    }

    /**
     * Method for writing data to the server.
     * @param string The string that will be sent to the server.
     */
    private void outData(String string) {
        socket.writeSocket(string+"\n");
    }

    class ReadSocketInThread implements Runnable {

        @Override
        public void run() {
            while(readInData) {
                try {
                    processInData(socket.readSocket());
                } catch (IOException ignored) {}
            }
        }
    }
}