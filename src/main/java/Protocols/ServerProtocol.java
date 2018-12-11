package Protocols;

public interface ServerProtocol {
    Protocol.ClientToServerType interpretClientMessage(String message);
    String getPreparedMessage();
    String createMessageToClient(Protocol.ServerToClientType type, String Message);
}
