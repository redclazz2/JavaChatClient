package clientjava.Domain.Port;

import clientjava.Domain.Entity.ChatMessage;

public interface ChatControllerPort {

    public void HandleWelcome(String[] usernames);

    public void HandleConnection(String username);

    public void HandleDisconnection(String username);

    public void HandleChatMessage(ChatMessage message);
}
