import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        new Client();
    }

    View view = new View();
    Capture capture = new Capture();

    private boolean readInData = true;
    private ClientSocket socket;

    Client() throws IOException, AWTException, InterruptedException {
        try {
            socket = new ClientSocket("192.168.1.118");
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread socketInThread = new Thread(new ReadSocketInThread());
        socketInThread.start();

        /**
        Scanner reader = new Scanner(System.in);
        while (true) {
            socket.writeSocket(reader.nextLine());
        }
         */

        while(true) {
            File image = capture.makeJPG(capture.captureScreenFrame());
            String string = Serializer.ObjectToString(new Serializer.Data(image));
            outData("image " + string);
            System.out.println("Data should be sent");
            Thread.sleep(1000/10);
        }
    }


    void displayImage(String string) {
        try {
            Serializer.Data image = (Serializer.Data) Serializer.ObjectFromString(string);
            view.drawFrame(ImageIO.read(image.getFile()));
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

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
                System.out.println("YES");
                displayImage(message);
                break;
            default:
                System.out.println("Unknown error");
        }
    }

    void outData(String string) {
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