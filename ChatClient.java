import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public void startClient() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to the chat server");
            
            new Thread(() -> readMessages(socket)).start();
            writeMessages(socket);

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    private void readMessages(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
        }
    }

    private void writeMessages(Socket socket) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter your username: ");
            String userName = scanner.nextLine();
            writer.println(userName);

            String clientMessage;
            do {
                clientMessage = scanner.nextLine();
                writer.println(clientMessage);
            } while (!clientMessage.equalsIgnoreCase("bye"));

        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ChatClient().startClient();
    }
}