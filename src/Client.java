import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        ClientSocket socket = new ClientSocket("192.168.1.118");
        Scanner reader = new Scanner(System.in);
        while (true) {
            socket.writeSocket(reader.nextLine());
            System.out.println(socket.readSocket());
        }
    }

}
