package clientjava.Infrastructure.Implementation;

import java.io.IOException;

import clientjava.Domain.Port.ChatRouterPort;
import clientjava.Helper.Logger.Logger;
import clientjava.Helper.Logger.LoggerLevel;
import clientjava.Infrastructure.Interface.IClientProcess;

public class ClientProcess implements IClientProcess {

    private final String name = "ClientProcess";
    private final int port;
    private ClientSocket socket;
    private final ChatRouterPort router;
    public boolean connected = false;

    public ClientProcess(int port, ChatRouterPort router) {
        this.port = port;
        this.router = router;
    }

    @Override
    public Boolean Init() {
        try {
            socket = new ClientSocket(
                    port,
                    "192.168.1.145",
                    router
            );

            return Connect();
        } catch (IOException e) {
            Logger.Log(LoggerLevel.Error, name, "Failed to initialize ClientSocket: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void Close() {
        if (socket != null && socket.Close()) {
            connected = false;
            Logger.Log(LoggerLevel.Info, name, "Closed client socket");
        } else if (connected) {
            Logger.Log(LoggerLevel.Warning, name, "Failed to safely close socket");
        }
    }

    private boolean Connect() {
        if (!connected) {
            if (socket != null && socket.Connect()) {
                connected = true;
                Logger.Log(LoggerLevel.Info, name, "Connected to server");

                Thread newThread = new Thread(socket::Read);
                newThread.start();

                Logger.Log(LoggerLevel.Info, name, "Read thread started");

                return true;
            } else {
                Logger.Log(LoggerLevel.Error, name, "Failed connection to server");
                return false;
            }
        } else {
            Logger.Log(LoggerLevel.Error, name, "Socket already connected");
            return false;
        }
    }

    @Override
    public void Write(byte[] data) {
        if (connected && socket != null) {
            socket.Write(data);
        } else {
            Logger.Log(LoggerLevel.Error, name, "Socket not connected.");
        }
    }
}
