import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 12345;
    private final Set<ClientHandler> clientHandlers = new HashSet<>();
    private final Set<String> userNames = new HashSet<>();

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server is running on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                ClientHandler newClient = new ClientHandler(socket, this);
                clientHandlers.add(newClient);
                newClient.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer().startServer();
    }

    public void broadcast(String message, ClientHandler excludeUser) {
        for (ClientHandler aClient : clientHandlers) {
            if (aClient != excludeUser) {
                aClient.sendMessage(message);
            }
        }
    }

    public void addUserName(String userName) {
        userNames.add(userName);
    }
    
    public void removeUser(String userName, ClientHandler clientHandler) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            clientHandlers.remove(clientHandler);
            System.out.println("The user " + userName + " disconnected");
            broadcast(userName + " has left the chat.", null);
        }
    }
}