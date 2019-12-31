import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;

public class SymmetricKeySharerClient {

    private static String startSymShareCommand = "startSymSharer";

    public static boolean clientSideProtocol(Socket socket){
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            pw.println(startSymShareCommand);

            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
