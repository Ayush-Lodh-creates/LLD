package dating_app;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatRoom {
    private final String id = UUID.randomUUID().toString();
    private final List<String> participantIds;
    private final List<Message> messages = new ArrayList<>();

    public ChatRoom(String firstUserId, String secondUserId) {
        participantIds = List.of(firstUserId, secondUserId);
    }

    public String getId() { return id; }
    public List<String> getParticipantIds() { return participantIds; }
    public List<Message> getMessages() { return List.copyOf(messages); }

    public void addMessage(String senderId, String content) {
        if (!participantIds.contains(senderId)) throw new IllegalArgumentException("Sender is not a participant");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("Message cannot be empty");
        messages.add(new Message(senderId, content));
        for (String participantId : participantIds) {
            if (!participantId.equals(senderId)) {
                NotificationService.getInstance().notifyUser(participantId, "New message from " + senderId);
            }
        }
    }

    public void displayChat() {
        for (Message message : messages) {
            System.out.println(message.getSenderId() + ": " + message.getContent());
        }
    }
}
