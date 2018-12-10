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

    private ServerListener listener;

    Client(Main mainStage){
        this.mainStage = mainStage;
        serverAddress = "192.168.1.13";
        port = 9898;
    }

    public void connectToServer()throws Exception{
        socket = new Socket(serverAddress, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        listener = new ServerListener(in,this);
        listener.run();
    }
    /**
     * NA TEN MOMENT, polaczy sie z serwerem, dostanie od niego wiadomosc EXIT i skonczy dzialanie
    */
    public void receive(String str)throws Exception{
        System.out.println(str);
        if(str.contains("EXIT")){
            listener.stopListener();
        }
        //TODO: handle server listener
    }

    public void criticalerror(){

    }

    public void moveCommand(String str){
        out.println(str);
    }
}
