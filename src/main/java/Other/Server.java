package Other;

import Protocols.Protocol;
import Protocols.ServerProtocol;
import Protocols.StandardServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import Logic.*;

/**
 * A server for a network multi-player game.  Modified and
 *
 *  PROTOCOL:
 *  Other.Client -> Other.Server           Other.Server -> Other.Client
 *  ----------------           ----------------
 *  MOVE <src> <dst> (x,y,z)   WELCOME PLAYER<int>  (player number)
 *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
 *                             MESSAGE <text>
 *
 *
 * A second change is that it allows an unlimited number of pairs of
 * players to play.
 */
public class Server extends Thread {

    private ServerCreator sh;
    private Game game;
    private int port;
    private String ip;
    private int players;
    private ServerSocket listener;

    Server(ServerCreator handler) {
        sh = handler;
    }

    public ServerSocket getServerSocket() {
        return listener;
    }

    public void close(){
        if(listener == null)
            return;
        try {
            endGame();

            if (!listener.isClosed())
                listener.close();
        }catch(Exception e){
            sh.setLogMessage("WARNING2: " + e.getMessage() + "\n close failure...",this);
        }
    }

    public void endGame(){
        if(game != null) {
            game.stopGame();
        }
    }

    /**
     * Runs the application. Pairs up clients that connect.
     */

    /**
     * @param port    {4000-20000}
     * @param ip      {x.x.x.x}
     * @param players {1,2,3,6}
     */
    public void setNewGame(int port, String ip, int players) {
        this.port = port;
        this.ip = ip;
        this.players = players;
    }

    public void run() {
        try {
            listener = new ServerSocket(port, 0, InetAddress.getByName(ip));

            sh.setLogMessage("Server is waiting for all players\n\t to join\n",this);
            try {
                GameSettings.NUM_HUMAN_PLAYERS = players;
                ArrayList<Game.Player> game_players = new ArrayList<>();
                game = new Game();

                for (int i = 0; i < players; ++i) {
                    Game.Player a = game.new Player(listener.accept(), 0, null);
                    game_players.add(a);
                    sh.increment();
                }
                game.setPlayersArray(game_players);

                for (Game.Player p : game_players) {
                    p.start();
                }



                sh.setLogMessage("Server is running",this);
                sh.setState(ServerState.RUNNING);

                while(game.hasRunningSockets()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //
                    }
                }

            } finally {
                listener.close();
                sh.setLogMessage("Server closed action",this);
            }
        } catch(IOException e1) {
            sh.setLogMessage("ERROR1: " + e1.getMessage() + "\n IO exception", this);
        }


    }
}

