import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final ChatServer server;
    private PrintWriter writer;
    private String userName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            printUsers();

            userName = reader.readLine();
            server.addUserName(userName);
            server.broadcast(userName + " has joined the chat.", this);

            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                String serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
            }
        } catch (IOException ex) {
            System.out.println("Error in ClientHandler: " + ex.getMessage());
        } finally {
            server.removeUser(userName, this);
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void printUsers() {
    }
    
    public void sendMessage(String message) {
        writer.println(message);
    }
}