import java.io.*;
import java.net.*;

public class Server {
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
            System.out.println("Siri Server (Project1) running on port " + port);
            System.out.println("Waiting for ONE client...");

            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                System.out.println("Client connected: " + clientSocket.getInetAddress());

                String encryptedQuestion;
                while ((encryptedQuestion = in.readLine()) != null) {
                    System.out.println("\n[RECV ENCRYPTED] " + encryptedQuestion);

                    String decryptedQuestion = Vigenere.decrypt(encryptedQuestion);
                    System.out.println("[RECV DECRYPTED] " + decryptedQuestion);

                    String replyPlain = siriReply(decryptedQuestion);
                    String replyEncrypted = Vigenere.encrypt(replyPlain);

                    System.out.println("[SEND PLAIN]     " + replyPlain);
                    System.out.println("[SEND ENCRYPTED] " + replyEncrypted);

                    out.println(replyEncrypted);

                    if (decryptedQuestion.trim().equalsIgnoreCase("bye") ||
                        decryptedQuestion.trim().equalsIgnoreCase("quit")) {
                        break;
                    }
                }

                System.out.println("\nClient disconnected. Server ending (Project1).");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
