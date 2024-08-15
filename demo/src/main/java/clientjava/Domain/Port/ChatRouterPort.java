package clientjava.Domain.Port;

import clientjava.Infrastructure.Model.Transaction;

public interface ChatRouterPort {
    public Boolean Init();
    public void Close();
    public void Write(Transaction data);
    public void Route(byte[] data);
    public void RouteWelcome(Object data);
    public void RouteConnect(Object data);
    public void RouteDisconnect(Object data);
    public void RouteMessage(Object data);
}
