import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connected to Siri Server (Project1) at " + host + ":" + port);

            while (true) {
                System.out.print("\nYou: ");
                String plainQuestion = sc.nextLine();

                String encryptedQuestion = Vigenere.encrypt(plainQuestion);
                System.out.println("[SEND ENCRYPTED] " + encryptedQuestion);

                out.println(encryptedQuestion);

                String encryptedAnswer = in.readLine();
                if (encryptedAnswer == null) {
                    System.out.println("Server closed connection.");
                    break;
                }

                System.out.println("[RECV ENCRYPTED] " + encryptedAnswer);

                String decryptedAnswer = Vigenere.decrypt(encryptedAnswer);
                System.out.println("[RECV DECRYPTED] " + decryptedAnswer);

                if (plainQuestion.trim().equalsIgnoreCase("bye") ||
                    plainQuestion.trim().equalsIgnoreCase("quit")) {
                    break;
                }
            }

            System.out.println("\nClient exiting.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
