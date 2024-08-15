package clientjava.Domain.Port;

import clientjava.Domain.Entity.ChatMessage;

public interface ChatDataPort {
    public void AddUsername(String username);
    public void AddUsernames(String[] usernames);
    public void RemoveUsername(String username);
    public void AddMessage(ChatMessage message);
}
