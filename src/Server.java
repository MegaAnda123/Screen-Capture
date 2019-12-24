import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

class Server {
    private boolean readInData = true;
    private boolean acceptNewClients = true;
    private boolean quit = false;
    private ServerSockets sockets;
    private ArrayList<ServerClient> clients = new ArrayList<>();

    void start() throws IOException {
        sockets = new ServerSockets(42069);
        Thread acceptClients = new Thread(new AcceptClientsThread());
        Thread readSockets = new Thread(new ReadSocketInThread());
        acceptClients.start();
        readSockets.start();
        Scanner reader = new Scanner(System.in);
        System.out.println("SERVER STARTED");

        while (!quit) {
            runCommand(reader.nextLine());
        }
    }

    private void runCommand(String string) {
        String[] temp = splitText(string);
        String command = temp[0];
        String options = temp[1];

        switch (command) {
            case "clear":
            case "cls":
                for (int i = 0; i < 50; ++i) System.out.println();
                break;
            case "stop":
                //TODO
                break;
            case "readIn":
                if(options.equalsIgnoreCase("true")) {
                    readInData=true;
                    break;
                }
                if(options.equalsIgnoreCase("false")) {
                    readInData=false;
                    break;
                }
                System.out.println("No accepted condition given. Supported: true,false");
                break;
            case "acceptNew":
                if(options.equalsIgnoreCase("true")) {
                    acceptNewClients=true;
                    break;
                }
                if(options.equalsIgnoreCase("false")) {
                    acceptNewClients=false;
                    break;
                }
                System.out.println("No accepted condition given. Supported: true,false");
                break;
            default:
                System.out.println("Command " + "\"" + string + "\"" + " not supported");
                break;
        }
    }

    private void processInData(ServerClient client, String string) {
        String[] temp = splitText(string);
        String command = temp[0];
        String message = temp[1];

        switch(command) {
            case "return":
                outData(client,"return "+ message);
                break;
            case "error":
                System.out.println("Client sent a error message?");
                break;
            default:
                outData(client,"error Command error");
        }

    }

    private void outData(ServerClient client, String string) {
        try {
            sockets.writeSocket(client.getSocket(),string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] splitText(String string) {
        String[] temp = string.split(" ");
        String start = temp[0];
        String rest;
        try {
            rest = string.substring((temp[0].length() + 1));
        } catch (StringIndexOutOfBoundsException e) {
            rest = "";
        }
        return new String[] {start, rest};
    }

    private void addNewClient(Socket socket) {
        ServerClient temp = new ServerClient();
        temp.setSocket(socket);
        clients.add(temp);
        System.out.println("New client connected");
    }


    class AcceptClientsThread implements Runnable {

        @Override
        public void run() {
            while (acceptNewClients) {
                try {
                    addNewClient(sockets.acceptConnection());
                } catch (IOException ignore) {}
            }
        }
    }

    class ReadSocketInThread implements Runnable {

        @Override
        public void run() {
            while (readInData) {
                try {
                    for (ServerClient client : clients) {
                        processInData(client, sockets.readSocket(client.getSocket()));
                        }
                    Thread.sleep(1);
                    } catch (IOException | ConcurrentModificationException | InterruptedException ignored) {}
            }
        }
    }
}
