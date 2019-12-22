import java.net.Socket;

public class ServerClient {
    private final String name;
    private final String password;
    private Socket socket;
    private boolean available = true;

    public ServerClient(String name,String password) {
        this.name = name;
        this.password = password;
    }
}
