import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

/**
 * Class for running the server and managing clients.
 *
 * @author Andre
 * @version 0.1
 */
class Server {
    public static void main(String[] args) throws IOException {
        new Server();
    }

    private boolean readInData = true;
    private boolean acceptNewClients = true;
    private boolean quit = false;
    private ServerSockets sockets;
    private ArrayList<ServerClient> clients = new ArrayList<>();

    /**
     * Initializing the server, opening server socket and starts threads for listening to connections and starts command line.
     * @throws IOException if an I/O error occurs when waiting for a connection.
     */
    Server() throws IOException {
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

    /**
     * Method checks if given string is a supported command and executes it.
     * @param string The command given.
     */
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
                try {
                    readInData = setBoolean(options);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "acceptNew":
                try {
                    acceptNewClients = setBoolean(options);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "help":
                //TODO show supported commands
                break;
            default:
                System.out.println("Command " + "\"" + string + "\"" + " not supported");
                break;
        }
    }

    /**
     * Method for processing string received from a client and execute what the string says.
     * @param client Client the data is from.
     * @param string The string from the client.
     */
    private void processInData(ServerClient client, String string) {
        String[] temp = splitText(string);
        String command = temp[0];
        String message = temp[1];

        switch(command) {
            case "return":
                outData(client,"return " + message);
                break;
            case "error":
                System.out.println("Client sent a error message?");
                break;
            case "image":
                outData(client,"image " + message);
                break;
            default:
                outData(client,"error Command error");
        }

    }

    /**
     * Method for writing data out to a given client.
     * @param client The client the method will write a string to.
     * @param string The string the method will write.
     */
    private void outData(ServerClient client, String string) {
        try {
            sockets.writeSocket(client.getSocket(),string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for turning user input strings to boolean conditions.
     * The method can interpret true, false, f, t, in lower and upper case. And 0 and 1.
     * If the method does not find a true or false case in the given string it will
     * throw a IllegalArgumentException.
     * @param string The string the method will interpret.
     * @return boolean condition the method found in the given string.
     */
    private boolean setBoolean(String string) {
        if(string.equalsIgnoreCase("true") || string.equals("1") || string.equalsIgnoreCase("t")) {
            return true;
        }
        if(string.equalsIgnoreCase("false") || string.equals("0") || string.equalsIgnoreCase("f")) {
            return false;
        }
        throw new IllegalArgumentException("No accepted condition given. Supported: true,false,t,f,0,1");
    }

    /**
     * Method for splitting text in two pieces using " " as regex.
     * @param string The string to be split.
     * @return The two split strings.
     */
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

    /**
     * Method adds new client object to the "clients" array containing client info.
     * (Method only adds socket to the ServerClient object.)
     * @param socket Socket for the new client.
     */
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
                        try {
                            processInData(client, sockets.readSocket(client.getSocket()));
                        } catch (SocketException ignored) {}
                    }
                    Thread.sleep(10);
                } catch (IOException | ConcurrentModificationException | InterruptedException ignored) {}
            }
        }
    }
}
