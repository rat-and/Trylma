package Other;

import Protocols.ClientProtocol;
import Protocols.Protocol;
import Protocols.StandardClientProtocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String serverAddress;
    private int port;
    private Main mainStage;

    private ClientProtocol protocol;
    private ServerListener listener;

    Client(Main mainStage){
        this.mainStage = mainStage;
        //serverAddress = "192.168.1.13";
        serverAddress = "LOCALHOST";
        port = 9898;
    }

    public void connectToServer()throws Exception{
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        protocol = new StandardClientProtocol();

        listener = new ServerListener(in,this);
        listener.start();

       // out.println(protocol.createMessageToServer(Protocol.ClientToServerType.MOVE, "(3,0,-3)_(2,0,-2)"));
        //out.println(protocol.createMessageToServer(Protocol.ClientToServerType.EXIT," "));
    }

    /**
     * NA TEN MOMENT, polaczy sie z serwerem, dostanie od niego wiadomosc EXIT i skonczy dzialanie
    */
    public void receive(String str)throws Exception{
        switch (protocol.interpretServerMessage(str)){

            case WELCOME:
                System.out.println(str);
                // TODO: Napis przywitania
                break;
            case PLAYER_MOVED:
                String prep = protocol.getPreparedMessage();
                HexCell<Piece> src = new HexCell<Piece>(prep);
                prep = prep.substring(prep.indexOf(')') + 1);
                HexCell<Piece> dst = new HexCell<Piece>(prep);

                System.out.println(str);

                mainStage.getBoard().move(src, dst);
                break;
            case MESSAGE:
                System.out.println(str);
                // TODO: Wiadomosc np. Twoja tura
                break;
                default:
                    break;
        }
        //if(str.contains("End")){
        //    System.out.println("END RECEIVED:" + str);
        //   listener.stopListener();
        //}
    }

    public void critical_error(){
        out.println(protocol.createMessageToServer(Protocol.ClientToServerType.EXIT," "));
        listener.stopListener();
    }

    public void moveCommand(String str){
        out.println(protocol.createMessageToServer(Protocol.ClientToServerType.MOVE, str));
    }
}
