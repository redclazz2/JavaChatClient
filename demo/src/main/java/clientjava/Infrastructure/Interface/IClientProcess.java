package clientjava.Infrastructure.Interface;

public interface IClientProcess {

    public Boolean Init();

    public void Close();

    public void Write(byte[] data);
}
