package Protocols;


public interface ClientProtocol {
    Protocol.ServerToClientType interpretServerMessage(String message);
    String getPreparedMessage();
    String createMessageToServer(Protocol.ClientToServerType type, String Message);
}
