package clientjava.Infrastructure.Controller;

import clientjava.Domain.Entity.ChatMessage;
import clientjava.Domain.Port.ChatControllerPort;
import clientjava.Domain.Port.ChatDataPort;

public class ChatController implements ChatControllerPort {
    ChatDataPort chatData;

    public ChatController(ChatDataPort chatData){
        this.chatData = chatData;
    }

    @Override
    public void HandleWelcome(String[] usernames) {
        this.chatData.AddUsernames(usernames);
        this.chatData.AddMessage(new ChatMessage("[SYSTEM]","Welcome to the chatroom"));
    }

    @Override
    public void HandleConnection(String username) {
        this.chatData.AddUsername(username);
        this.chatData.AddMessage(new ChatMessage("[SYSTEM]","User: " + username + " joined the chatroom"));
    }

    @Override
    public void HandleDisconnection(String username) {
        this.chatData.RemoveUsername(username);
    }

    @Override
    public void HandleChatMessage(ChatMessage message) {
        this.chatData.AddMessage(message);
    }
}
