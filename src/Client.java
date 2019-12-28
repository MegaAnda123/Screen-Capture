import java.io.IOException;
import java.net.SocketException;

public class Client {

    public static void main(String[] args) throws IOException {
        new Client();
        new Client();
    }

    private boolean readInData = true;
    private ClientSocket socket;

    Client() throws IOException {
        try {
            socket = new ClientSocket("192.168.1.118");
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread socketInThread = new Thread(new ReadSocketInThread());
        socketInThread.start();
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

    void outData(String string) {
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