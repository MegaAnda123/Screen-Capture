import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Class for holding data from a single connected client or a signed up user.
 *
 * @author Andre
 * @version 0.1
 */
public class ServerClient {
    private String name;
    private String password;
    private Socket socket;
    private boolean available = true;
    private String SymKey;
    private PublicKey publicKey;
    private PrivateKey privateKey;


    public ServerClient() {
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean getAvailable() {
        return available;
    }
}
