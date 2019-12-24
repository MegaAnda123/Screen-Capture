import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    private boolean readInData = true;
    private ClientSocket socket;

    public static void main(String[] args) throws IOException {
        new Client();
    }

    private Client() throws IOException {
        try {
            socket = new ClientSocket("192.168.1.118");
        } catch (SocketException e) {
            e.printStackTrace();
        }


        Thread socketInThread = new Thread(new ReadSocketInThread());
        socketInThread.start();

        Scanner reader = new Scanner(System.in);
        while (true) {
            socket.writeSocket(reader.nextLine());
            System.out.println(socket.readSocket());
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
                System.out.println(message);
                break;
        }
    }

    private void outData(String string) {
        socket.writeSocket(string);
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