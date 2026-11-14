package dating_app;

import java.time.LocalDateTime;

public class Message {
    private final String senderId;
    private final String content;
    private final LocalDateTime time;

    public Message(String senderId, String content) {
        this.senderId = senderId;
        this.content = content;
        this.time = LocalDateTime.now();
    }

    public String getSenderId() { return senderId; }
    public String getContent() { return content; }
    public LocalDateTime getTime() { return time; }
}
