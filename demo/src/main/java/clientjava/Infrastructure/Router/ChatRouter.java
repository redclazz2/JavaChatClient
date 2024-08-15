package clientjava.Infrastructure.Router;

import clientjava.Domain.Entity.ChatMessage;
import clientjava.Domain.Port.ChatControllerPort;
import clientjava.Domain.Port.ChatRouterPort;
import clientjava.Helper.Formatter.Formatter;
import clientjava.Helper.Logger.Logger;
import clientjava.Helper.Logger.LoggerLevel;
import clientjava.Infrastructure.Implementation.ClientProcess;
import clientjava.Infrastructure.Interface.IClientProcess;
import clientjava.Infrastructure.Model.Transaction;

public class ChatRouter implements ChatRouterPort {

    ChatControllerPort controller;
    IClientProcess clientProcess;

    public ChatRouter(ChatControllerPort controller) {
        this.controller = controller;
        this.clientProcess = new ClientProcess(
                8059,
                this
        );
    }

    @Override
    public Boolean Init() {
        return clientProcess.Init();
    }

    @Override
    public void Close() {
        clientProcess.Close();
    }

    @Override
    public void Write(Transaction data) {
        clientProcess.Write(Formatter.Serialize(data));
    }

    @Override
    public void Route(byte[] data) {
        Logger.Log(LoggerLevel.Info, "ChatController", "Routing a request");

        Transaction transaction = Formatter.Deserialize(data, Transaction.class);
        switch (transaction.route) {
            case "Welcome":
                Logger.Log(LoggerLevel.Info, "ChatController", "Server welcomes you!");
                RouteWelcome(transaction.data);
                break;

            case "Connect":
                Logger.Log(LoggerLevel.Info, "ChatController", "A new user has connected");
                RouteConnect(transaction.data);
                break;

            case "Disconnect":
                Logger.Log(LoggerLevel.Info, "ChatController", "A user has disconnected");
                RouteDisconnect(transaction.data);
                break;

            case "Message":
                Logger.Log(LoggerLevel.Info, "ChatController", "Message recieved");
                RouteMessage(transaction.data);
                break;
        }
    }

    @Override
    public void RouteWelcome(Object data) {
        String[] usernames = Formatter.CastDataToType(data, String[].class);
        controller.HandleWelcome(usernames);
    }

    @Override
    public void RouteConnect(Object data) {
        String username = data.toString();
        controller.HandleConnection(username);
    }

    @Override
    public void RouteDisconnect(Object data) {
        String dcdUsername = data.toString();
        controller.HandleDisconnection(dcdUsername);
    }

    @Override
    public void RouteMessage(Object data) {
        ChatMessage message = Formatter.CastDataToType(data, ChatMessage.class);
        controller.HandleChatMessage(message);
    }
}
