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
            if (!listener.isClosed())
                listener.close();
        }catch(Exception e){
            sh.setLogMessage("WARNING2: " + e.getMessage() + "\n close failure...",this);
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
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //
                }

                while(game.hasRunningSockets()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
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

class Game {

    private Board board;

    Game(){  //                             TODO: Here we will have defind amount of players
        board = new Board(GameSettings.BOARD_RADIUS, GameSettings.PLAYERS);
        protocol = new StandardServerProtocol();
        players = new ArrayList<>();
    }

    public boolean hasRunningSockets(){
        if(players.size() >= 1)
            return true;

        return false;
    }

    public void setPlayersArray(ArrayList<Player>al){
        players = al;
    }

    /**
     * The current player.
     */
    Player currentPlayer;
    ArrayList<Player> players;
    ServerProtocol protocol;

    /**
     * Called by the player threads when a player tries to make a
     * move.  This method checks to see if the move is legal: that
     * is, the player requesting the move must be the current player
     * and the square in which she is trying to move must not already
     * be occupied.  If the move is legal the game state is updated
     */
    public synchronized boolean legalMove(HexCell<Piece>src, HexCell<Piece> dst, Player player) {
        if (player == currentPlayer ) {
            if(board.move(src, dst)) {
                /*
                if(board.won() != -1)
                {
                    for (Player p : players) {
                        if(p != currentPlayer)
                            p.otherPlayerWon();
                    }
                }
                */
                for (Player p : players) {
                    if(p != currentPlayer)
                      p.otherPlayerMoved(src, dst);
                }
                currentPlayer = currentPlayer.next;
                return true;
            }
        }
        return false;
    }

    /**
     * Thread for communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.
     */
    class Player extends Thread {
        Player next;
        int order;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, int order, Player next) {
            this.next = next;
            this.socket = socket;
            this.order = order;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.WELCOME, Integer.toString(order)));
                //output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {

                System.out.println("Player died: " + e);
                players.remove(this);
            }
        }

        public int getOrder(){
            return order;
        }


        /**
         * We sent updated info from board to other clients
         */
        public void otherPlayerMoved(HexCell<Piece>src, HexCell<Piece> dst) {
            output.println(protocol.createMessageToClient(Protocol.ServerToClientType.PLAYER_MOVED, src.toString() + dst.toString()));
        }

        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "All players connected"));

                // Tell the first player that it is her turn.
                if (order == 0) {
                    output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Your Move"));
                }

               // output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "End"));

                // Repeatedly get commands from the client and process them.
                boolean on = true;
                while (on) {
                    String command = input.readLine();
                    switch(protocol.interpretClientMessage(command)){
                        case EXIT:

                            output.println("EXIT");
                                on = false;
                                break;
                        case MOVE:

                                String prep = protocol.getPreparedMessage();

                                try {
                                    HexCell<Piece> src = new HexCell<Piece>(prep);
                                    prep = prep.substring(prep.indexOf(')') + 1 );
                                    HexCell<Piece> dst = new HexCell<Piece>(prep);

                                    if (legalMove(src, dst, this)) {
                                        output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Valid move"));
                                    } else {
                                        output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Invalid move"));
                                    }
                                }
                                catch (IllegalArgumentException e){
                                    output.println(protocol.createMessageToClient(Protocol.ServerToClientType.MESSAGE, "Can`t convert src && dst"));
                                }
                            break;

                            default:
                                break;
                    }
                }

            } catch (Exception e) {
                players.remove(this);
                e.printStackTrace();
            } finally {
                try {socket.close();} catch (IOException e) { players.remove(this);}
            }
        }
    }

}
