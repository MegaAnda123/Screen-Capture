import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

    private Socket socket;
    private DataOutputStream outStream;
    private InputStream inStream;
    private PrintWriter pr;
    private InputStreamReader in;
    private BufferedReader bf;


    /**
     * Constructor try to connect to the given ip and port. Throws exception if connecting fails.
     * @param ip Ip to connect to with optional port at end separated with ":" (default port 42069 will be used if no port number is given).
     * @throws IOException
     */
    public ClientSocket(String ip) throws IOException {
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

            outStream = new DataOutputStream(socket.getOutputStream());
            inStream = socket.getInputStream();
            pr = new PrintWriter(outStream);
            in = new InputStreamReader(socket.getInputStream());
            bf = new BufferedReader(in);
            System.out.println("Connected successfully to " + IP);
        } catch (ConnectException e) {
            System.out.println("Failed to connect");
            throw new IllegalArgumentException();
        } catch (UnknownHostException ignore) {
            throw new IllegalArgumentException();
        }
    }

    public String readSocket() throws IOException {
        String string = "";
        while (inStream.available() != 0) {
            string += bf.readLine();
        }
        return string;
    }

    public void writeSocket(String string) {
        pr.println(string);
        pr.flush();
    }

    public boolean IsConnected() {
        return socket.isConnected();
    }

    void closeSocket() throws IOException {
        socket.close();
    }
}
