/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package clientjava.Infrastructure.Interface;

/**
 *
 * @author sebastian
 */
public interface IClientSocket {
    public Boolean Connect();
    public void Write(byte[] data);
    public void Read();
    public Boolean Close();
}
