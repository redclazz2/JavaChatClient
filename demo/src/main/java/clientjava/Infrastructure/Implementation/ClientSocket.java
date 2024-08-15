package clientjava.Infrastructure.Implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import clientjava.Domain.Port.ChatRouterPort;
import clientjava.Helper.Logger.Logger;
import clientjava.Helper.Logger.LoggerLevel;
import clientjava.Infrastructure.Interface.IClientSocket;

public class ClientSocket implements IClientSocket {

    private final Socket socket;
    private final InetSocketAddress ipEndPoint;
    private final ChatRouterPort router;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Boolean running = false;

    public ClientSocket(int port, String url, ChatRouterPort router) throws IOException {
        this.router = router;
        InetAddress localIpAddress = InetAddress.getByName(url);
        ipEndPoint = new InetSocketAddress(localIpAddress, port);
        socket = new Socket();
    }

    @Override
    public Boolean Close() {
        try {
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Boolean Connect() {
        try {
            socket.connect(ipEndPoint);
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
            this.running = true;
            return true;
        } catch (IOException e) {
            Logger.Log(LoggerLevel.Error, "ClientSocket", "Unable to connect to the server");
            return false;
        }
    }

    @Override
    public void Read() {
        while (running) {
            try {
                byte[] lengthBuffer = new byte[4];
                inputStream.read(lengthBuffer);

                int messageLength = ByteBuffer.wrap(lengthBuffer).getInt();
                if (messageLength > 0) {
                    byte[] buffer = new byte[messageLength];
                    inputStream.read(buffer, 0, messageLength);

                    router.Route(buffer);
                } else {
                    running = false;
                    Logger.Log(LoggerLevel.Warning, "ClientSocket", "Forcefully disconnected");
                }
            } catch (IOException e) {
                running = false;
                Logger.Log(LoggerLevel.Error, "ClientSocket", "Disconnected from server");
                break;
            }
        }

        Logger.Log(LoggerLevel.Info, "ClientSocket", "Read thread closed");
    }

    @Override
    public void Write(byte[] data) {
        try {
            byte[] lengthBytes = ByteBuffer.allocate(4).putInt(data.length).array();

            outputStream.write(lengthBytes);
            outputStream.flush();

            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            Logger.Log(LoggerLevel.Error, "ClientSocket", "Error while writing data: " + e.getMessage());
        }
    }
}
