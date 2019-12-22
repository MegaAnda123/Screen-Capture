import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerSockets {
    ServerSocket serverSocket;
    Socket clientSocket;

    public ServerSockets(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void acceptConnection() throws IOException {
        clientSocket = serverSocket.accept();
    }

    public String readSocket(Socket socket) throws IOException {
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        BufferedReader bf = new BufferedReader(in);
        StringBuilder string = new StringBuilder();
        if(socket.getInputStream().available() != 0) {
            while (socket.getInputStream().available() != 0) {
                string.append(bf.readLine());
            }
        } else {
            throw new SocketException("Socket contains no new data");
        }
        return string.toString();
    }

    public void writeSocket(Socket socket, String string) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(string);
        printWriter.flush();
    }
}
