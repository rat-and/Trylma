import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *  To ma rysować tylko
 * */
public class Area extends Pane {
//    private static final Color TRANSPARENT_GRAY = new Color(128, 128, 128, 100);
    private static final long SERIAL_VERSION = 1L;
    private Main mainStage; //TODO: Move to server
    private ArrayList<Point<HexCell<Piece>>> possibleMoves; //TODO: Move to server
    private int currentPlayerIndex; //TODO: Move to server
    private int winPlayerIndex; //TODO: Move to server
    private HitTestAdapter htAdaper;

    public Area(Main mainStage) {
        this.mainStage = mainStage;
        currentPlayerIndex = 0;
        winPlayerIndex = -1;
        possibleMoves = new ArrayList<Point<HexCell<Piece>>>();
        htAdaper = new HitTestAdapter();
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, htAdaper.mouseCicked);
    }

    public void draw(GraphicsContext graphicsContext) {
        /** Draws Win Message */
        if (winPlayerIndex >= 0) {
            graphicsContext.strokeText("The " + GameSettings.PLAYER_NAMES[winPlayerIndex] + " player won!", GameSettings.SCREEN_SIZE / 20, GameSettings.SCREEN_SIZE / 20);

            for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {

                /**draw if there's place*/
                if (p.getKey().getKey() != null) {
                    if (p.getKey().getKey().getPlayer().equals(GameSettings.PLAYERS[winPlayerIndex])) {
                        graphicsContext.setFill(p.getKey().getKey().getColor().brighter());

                    } else {
                        graphicsContext.setFill(p.getKey().getKey().getColor());
                    }
                    //tutaj chyba zle
                    graphicsContext.fillOval(p.getEllipse().getCenterX() - .5*GameSettings.PIECE_DIAMETER, p.getEllipse().getCenterY() - .5*GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER);
                    graphicsContext.setFill(Color.BLACK);
//                    graphicsContext.fillOval(p.getEllipse().getCenterX(), p.getEllipse().getCenterY(), 2 * Main.PIECE_DIAMETER, 2 * Main.PIECE_DIAMETER);
                }
                /** draws move-assistant */
                else if (GameSettings.MOVE_ASSISTANCE && this.isPossibleMove(p)) {
                    graphicsContext.setFill(Color.gray(0.5, 0.5));
                    graphicsContext.fillOval(p.getEllipse().getCenterX() - .5*GameSettings.PIECE_DIAMETER, p.getEllipse().getCenterY() - .5*GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER);
//                    graphicsContext.setFill(Color.BLACK);
//                    graphicsContext.fillOval(p.getEllipse().getCenterX(), p.getEllipse().getCenterY(), 2 * Main.PIECE_DIAMETER, 2 * Main.PIECE_DIAMETER);
                }
                /** if there*s no piece */
                else {
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillOval(p.getEllipse().getCenterX() - .5*GameSettings.PIECE_DIAMETER, p.getEllipse().getCenterY() - .5*GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER);
                }

            }
        }
        /** no-win sequence */
        else {
           graphicsContext.strokeText("It's the " + GameSettings.PLAYER_NAMES[currentPlayerIndex] + " player's turn", GameSettings.SCREEN_SIZE / 20, GameSettings.SCREEN_SIZE / 20);
            for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {

                /** draw if there's place */
                if (p.getKey().getKey() != null) {
                    graphicsContext.setFill(p.getKey().getKey().getColor());
                    graphicsContext.fillOval(p.getEllipse().getCenterX() - .5*GameSettings.PIECE_DIAMETER, p.getEllipse().getCenterY() - .5*GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER);
//                    graphicsContext.setFill(Color.BLACK);
//                    graphicsContext.fillOval(p.getEllipse().getCenterX(), p.getEllipse().getCenterY(), 2 * Main.PIECE_DIAMETER, 2 * Main.PIECE_DIAMETER);
                }
                /** move assistant mechanism */
                else if (GameSettings.MOVE_ASSISTANCE && isPossibleMove(p)) {
                    graphicsContext.setFill(Color.gray(0.5, 0.5));
                    graphicsContext.fillOval(p.getEllipse().getCenterX() - .5*GameSettings.PIECE_DIAMETER, p.getEllipse().getCenterY() - .5*GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER);
//                    graphicsContext.setFill(Color.BLACK);
//                    graphicsContext.fillOval(p.getEllipse().getCenterX(), p.getEllipse().getCenterY(), 2 * Main.PIECE_DIAMETER, 2 * Main.PIECE_DIAMETER);
                }
                /** if no piece and it's not possilbe move */
                else {
                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillOval(p.getEllipse().getCenterX() - .5*GameSettings.PIECE_DIAMETER, p.getEllipse().getCenterY() - .5*GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER, 2 * GameSettings.PIECE_DIAMETER);
                }
            }
        }
    }

    private boolean isPossibleMove(Point<HexCell<Piece>> p) {
        for(Point<HexCell<Piece>> poss : possibleMoves) {
            if(p.equals(poss)) return true;
        }
        return false;
    }

    public void paintComponent(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, graphicsContext.getCanvas().getWidth(), graphicsContext.getCanvas().getHeight());
        draw(graphicsContext);
    }

    //TODO: What with this? // Should be ok
    private void updatePossibleMoves(HexCell<Piece> src) {
        possibleMoves.clear();
        if (src == null || src.getKey() == null) {
            return;
        }
        for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {
            if (p.getKey() != null && mainStage.getBoard().isValidMove(src, p.getKey())) {
                possibleMoves.add(p);
            }
        }
    }

    // DO hexcella jest wstawiony Piece
    private boolean isPlayer(Point<HexCell<Piece>> p) {
        if(p == null || p.getKey().getKey() == null) return false;
        return (p.getKey().getKey().getPlayer().equals(GameSettings.PLAYERS[currentPlayerIndex]));
    }


    private void unhighlight() {
        for(Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {
            if (p.getKey().getKey() != null) p.getKey().getKey().highlight(false);
            possibleMoves.clear();
        }
    }
    // ?
    private void nextPlayer() {
        if(currentPlayerIndex == GameSettings.PLAYERS.length - 1)
            currentPlayerIndex = 0;
        else
            currentPlayerIndex++;
    }

    private void runComputerPlayer() {
        Point<HexCell<Piece>> cornerPiece = findCornerPiece(currentPlayerIndex).pointConversion();
        Point<HexCell<Piece>> destPiece = mainStage.getBoard().getNearestOpenPoint(cornerPiece);

        Point<HexCell<Piece>> bestPiece = null;
        Point<HexCell<Piece>> bestMove = null;
        double bestScore = Math.pow(GameSettings.BOARD_RADIUS - 1, 4);

        /** For each of the current player's pieces... */
        for(Point<HexCell<Piece>> p : mainStage.getBoard().getPlayerPoints(currentPlayerIndex)) {
            /** For each possible move from the current piece... */
            updatePossibleMoves(p.getKey());
            for(Point<HexCell<Piece>> poss : possibleMoves) {
                /** Record the move if it is better than the best previous one */
                if(calculateScore(p.getKey(), poss.getKey(), destPiece.getKey()) < bestScore) {
                    bestPiece = p;
                    bestMove = poss;
                    bestScore = calculateScore(p.getKey(), poss.getKey(), destPiece.getKey());
                }
            }
        }

        if(bestPiece != null && bestMove != null)
            mainStage.getBoard().move(bestPiece.getKey(), bestMove.getKey());
        nextPlayer();
    }

    /**
     * Helper Method for AI. Finds the nearest open location from the farthest winLoc
     * @param playerIndex computer player running
     * @return nearest open location from farthest winLoc
     */
    private HexCell<Piece> findCornerPiece(int playerIndex) {
        ArrayList<HexCell<Piece>> winLocs = mainStage.getBoard().getWinLocs(currentPlayerIndex);
        HexCell<Piece> cornerPiece = null;
        for(HexCell<Piece> n : winLocs) {
            if(Math.abs(n.getX()) >= (GameSettings.BOARD_RADIUS-1)*2 ||
                    Math.abs(n.getY()) >= (GameSettings.BOARD_RADIUS-1)*2 ||
                    Math.abs(n.getZ()) >= (GameSettings.BOARD_RADIUS-1)*2) {
                cornerPiece = n;
            }
        }
        return cornerPiece;
    }

    /**
     * Helper Method for AI. Calculates the score for a possible move. A score is
     * the total (mean-square) distance of each piece from the open location closest
     * to the corner winLoc.
     * @param src possible source node
     * @param dst possible destination node
     * @param goal open location closest to the corner winLoc
     * @return score
     */
    private double calculateScore(HexCell<Piece> src, HexCell<Piece> dst, HexCell<Piece> goal) {
        double score = 0;
        mainStage.getBoard().move(src, dst);

        for(Point<HexCell<Piece>> p : mainStage.getBoard().getPlayerPoints(currentPlayerIndex)) {
            score += Math.pow(mainStage.getBoard().getDistance(p.getKey(), goal), 2);
        }

        mainStage.getBoard().move(dst, src);
        return Math.sqrt(score);
    }

    /**
     * Runs the Win Sequence after a player won the game.
     */
    private void runWinSequence(int playerIndex) {
        winPlayerIndex = playerIndex;
    }


/*--------------------------------------------Mouse Event Handling----------------------------------------------------*/
    class HitTestAdapter implements Runnable {
        private Thread runner;
        private boolean isRunning;
        private HexCell<Piece> toMove;

        public HitTestAdapter() {
            isRunning = true;
            runner = new Thread(this);
            runner.start();
        }

        public void stop() { isRunning = false; }
        public void destroy() { isRunning = false; }

        EventHandler<MouseEvent> mouseCicked = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                /**reset possible moves */
                updatePossibleMoves(null);
                /** For all positions on the board*/
                for (Point<HexCell<Piece>> p : mainStage.getBoard().getPoints()) {//TODO: Should this be here?
                    /** If there is a piece at this position and it's the player's piece ... */
                    if(p.getKey().getKey() != null && isPlayer(p)) {
                        /** Highlight it if the user clicked on it */
                        p.getKey().getKey().highlight(p.getEllipse().contains(event.getX(), event.getY()));
                        /** If the user clicked on it, update possible moves and update toMove field */
                        if(p.getEllipse().contains(event.getX(), event.getY())) {
                            toMove = p.getKey();
                            updatePossibleMoves(toMove);
                        }
                    }
                    /** If there is a piece here and it's NOT the player's piece ... */
                    else if (p.getKey().getKey() != null) {
                        /** If the user clicked on it ... */
                        if (p.getEllipse().contains(event.getX(), event.getY())) {
                            /** reset move-assistance */
                            toMove = null;
                            unhighlight();
                        }
                    } else {
                        /** Move the toMove piece here if possible and reset move-assistance */
                        if(toMove != null && p.getEllipse().contains(event.getX(), event.getY())) {
                            if(mainStage.getBoard().isValidMove(toMove, p.getKey())) {//TODO: this is crucial
                                mainStage.getBoard().move(toMove, p.getKey());
                                mainStage.getClient().moveCommand(toMove.toString() + " " + p.getKey().toString());
                                /** Test for winner and run win sequence */
                                if(mainStage.getBoard().won() >= 0)//TODO: Win condition
                                    runWinSequence(mainStage.getBoard().won());
                                /** Move to next player and run Computer Player */
                                nextPlayer();//TODO: nextPLayer
                                while(currentPlayerIndex >= GameSettings.NUM_HUMAN_PLAYERS) {
                                    runComputerPlayer();
                                    if(mainStage.getBoard().won() >= 0) {
                                        runWinSequence(mainStage.getBoard().won());
                                        break;
                                    }
                                }
                            }
                            toMove = null;
                            unhighlight();
                        }
                    }
                }
            }
        };

        //TODO: This shouldn`t be here
        @Override
        public void run() {
            while(isRunning) {

                /** If a player won, wait 5 seconds, then create a new game */
                if(winPlayerIndex >= 0) {
//                    repaint();

                    try {
                        Thread.sleep(5000);
                    } catch(InterruptedException e) {
                        Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, e);
                    }

                    winPlayerIndex = -1;
                    mainStage.newGame();//wtf
                }

                /** Repaint */
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) {
                    Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, e);
                }
//                repaint();
            }
        }
    }
}




