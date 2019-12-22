import java.io.IOException;
import java.net.SocketException;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSockets sockets = new ServerSockets(42069);
        sockets.acceptConnection();
        while (true) {
            try {
                String string = sockets.readSocket(sockets.clientSocket);
                System.out.println(string);
                sockets.writeSocket(sockets.clientSocket,string);

            } catch (SocketException e) {}
        }
    }
}
