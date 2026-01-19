import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SiriServerOneClient {
    private static final AtomicInteger CLIENT_ID = new AtomicInteger(1);

    private static String siriReply(String msg) {
        String m = msg.toLowerCase().trim();
        if (m.contains("hello") || m.contains("hi")) return "Hello! How can I help you?";
        if (m.contains("time")) return "I can't see your clock, but check the system time on your computer.";
        if (m.contains("weather")) return "I can't access live weather, but you can check a weather app.";
        if (m.equals("bye") || m.equals("quit")) return "Goodbye!";
        return "You said: " + msg;
    }

    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Siri Server (Project2) running on port " + port);
            System.out.println("Waiting for MULTIPLE clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int id = CLIENT_ID.getAndIncrement();
                new Thread(new ClientHandler(clientSocket, id)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final int id;

        ClientHandler(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("Client #" + id + " connected: " + socket.getInetAddress());

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String encryptedQuestion;
                while ((encryptedQuestion = in.readLine()) != null) {
                    System.out.println("\n[#"+id+" RECV ENCRYPTED] " + encryptedQuestion);

                    String decryptedQuestion = VigenereCipher.decrypt(encryptedQuestion);
                    System.out.println("[#"+id+" RECV DECRYPTED] " + decryptedQuestion);

                    String replyPlain = siriReply(decryptedQuestion);
                    String replyEncrypted = VigenereCipher.encrypt(replyPlain);

                    System.out.println("[#"+id+" SEND PLAIN]     " + replyPlain);
                    System.out.println("[#"+id+" SEND ENCRYPTED] " + replyEncrypted);

                    out.println(replyEncrypted);

                    if (decryptedQuestion.trim().equalsIgnoreCase("bye") ||
                        decryptedQuestion.trim().equalsIgnoreCase("quit")) {
                        break;
                    }
                }

            } catch (IOException e) {
                System.out.println("Client #" + id + " error: " + e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException ignored) {}
                System.out.println("Client #" + id + " disconnected.");
            }
        }
    }
}
