import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server for a network multi-player game.  Modified and
 *
 *  PROTOCOL:
 *  Client -> Server           Server -> Client
 *  ----------------           ----------------
 *  MOVE <src> <dst> (x,y,z)   WELCOME PLAYER<int>  (player number)
 *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
 *                             MESSAGE <text>
 *
 *
 * A second change is that it allows an unlimited number of pairs of
 * players to play.
 */
public class Server {

    /**
     * Runs the application. Pairs up clients that connect.
     */
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(9898,0, InetAddress.getByName("localhost"));
        System.out.println("Server is Running");
        try {
            while (true) {
                //TODO: Normal Server start
                // 1. Enough Players
                // 2. Players cicle
                // 3. Run players
                Game game = new Game();
                Game.Player a = game.new Player(listener.accept(),0,null);
                a.start();
                /*
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept());
                Game.Player playerO = game.new Player(listener.accept());
                game.currentPlayer = playerX;
                playerX.start();
                playerO.start();
                */
            }
        } finally {
            listener.close();
        }
    }
}

class Game {

    private Board board;

    Game(){  //                             TODO: Here we will have defind amount of players
        board = new Board(GameSettings.BOARD_RADIUS, GameSettings. PLAYERS);
    }
    /**
     * The current player.
     */
    Player currentPlayer;
    ArrayList<Player> players;


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
     * The class for the helper threads in this multithreaded server
     * application.  A Player is identified by a character mark
     * which is either 'X' or 'O'.  For communication with the
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
                output.println("WELCOME PLAYER:" + order);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public int getOrder(){
            return order;
        }


        /**
         * We sent updated info from board to other clients
         */
        public void otherPlayerMoved(HexCell<Piece>src, HexCell<Piece> dst) {
            // TODO: Sent packet here
            output.println("PLAYER_MOVED: " + src + dst);
        }

        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                // Tell the first player that it is her turn.
                if (order == 0) {
                    output.println("MESSAGE Your move");
                }

                output.println("EXIT");
                /*
                // Repeatedly get commands from the client and process them.
                while (true) {

                    String command = input.readLine();
                    if (command.startsWith("MOVE")) {
                        int location = Integer.parseInt(command.substring(5));//TODO: Conversion to board location
                        HexCell<Piece>src = new HexCell<Piece>(null,0,0,0);
                        HexCell<Piece> dst = new HexCell<Piece>(null,0,0,0);
                        if (legalMove(src, dst, this)) {
                            output.println("VALID_MOVE");
                            output.println("");
                        } else {
                            output.println("MESSAGE ?");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
                */
            } catch (Exception e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }

}
