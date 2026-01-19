import java.util.*;

public class SiriBrain {
    public String answer(String msg) {
        String m = msg.toLowerCase().trim();

        if (m.contains("hello") || m.contains("hi")) return "Hello! How can I help you?";
        if (m.contains("time")) return "I can't see your clock, but check the system time on your computer.";
        if (m.contains("weather")) return "I can't access live weather, but you can check a weather app.";
        if (m.equals("bye") || m.equals("quit")) return "Goodbye!";

        return "You said: " + msg;
    }
}
