import javax.crypto.SecretKey;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

class ClientSocket {

    private Socket socket;
    private InputStream inStream;
    private PrintWriter pr;
    private BufferedReader bf;
    private SecretKey key;


    /**
     * Constructor try to connect to the given ip and port. Throws exception if connecting fails.
     * @param ip Ip to connect to with optional port at end separated with ":" (default port 42069 will be used if no port number is given).
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    ClientSocket(String ip) throws IOException {
        if(ip.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String IP;
        String PORT;
        if (ip.contains(":")) {
            String[] chunks = ip.split(":");
            IP = chunks[0];
            PORT = chunks[1];
        } else {
            IP = ip;
            PORT = "42069";
        }
        try {
            socket = new Socket(IP, Integer.parseInt(PORT));

            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
            inStream = socket.getInputStream();
            pr = new PrintWriter(outStream);
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            bf = new BufferedReader(in);
            System.out.println("Connected successfully to " + IP);
        } catch (ConnectException | UnknownHostException e) {
            throw new SocketException("Failed to connect");
        }
    }

    /**
     * Reads new data from socket.
     * @return Returns the new data as a string.
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    String readSocket() throws IOException {
        StringBuilder string = new StringBuilder();
        while (inStream.available() != 0) {
            string.append(bf.readLine());
        }
        return string.toString();
    }

    /**
     * Writes data to socket.
     * @param string What the method will write to the socket.
     */
    void writeSocket(String string) {
        pr.println(string);
        pr.flush();
    }

    void closeSocket() throws IOException {
        socket.close();
    }
}
