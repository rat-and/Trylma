package Controllers;

import GUI.Area;
import GUI.Main;
import GUI.Model;
import GUI.Point2D;
import Logic.GameSettings;
import Logic.HexCell;
import Logic.Piece;
import Logic.Point;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Runnable {
    private Thread runner;
    private boolean isRunning;
    private HexCell<Piece> toMove;
    private Model model;
    private Area area;
    private Canvas canvas;

    public Controller() {
        isRunning = true;
        runner = new Thread();
        runner.start();
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void reDrawArea() {
        area.reDraw();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    @FXML
    private void initialize() {
    }

    public void initModel(Model model) {
        this.model = model;
    }

    public void clearModel() {
        model.clearPointList();
    }

    /**
     * handler that adds a point
     */
//    @FXML
//    private void addPoint(MouseEvent event) {
//        Point2D point2D = new Point2D(event.getX(), event.getY());
//        model.getPoints().add(point2D);
//        System.out.println("Point of coordinates: " + event.getX() + ", " + event.getY() + " added");
//    }
//
//    public EventHandler pointsAdder = new javafx.event.EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    addPoint(event);
//                }
//            };

    public EventHandler mouseClicked = new javafx.event.EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("Point of coordinates: " + event.getX() + ", " + event.getY() + " added");
//            area.updatePossibleMoves(null);

            for (Point<HexCell<Piece>> p : Main.getBoard().getPoints()) {

                /** If there's a player's piece at this position */
                if (p.getKey().getKey() != null && area.isPlayer(p)) {
                    /** Highlight it if the user clicked on it */
                    p.getKey().getKey().highlight(p.contains(event.getX(), event.getY()));
                    /** If the user clicked on it, update possible moves and update toMove field */
                    if (p.contains(event.getX(), event.getY())) {
                        System.out.println("Point within a piece - 1st if");
                        model.getPoints().add(new Point2D(event.getX(),  event.getY()));
                        toMove = p.getKey();
                        area.updatePossibleMoves(toMove);
                    }
                }
                else if (p.getKey().getKey() != null) {
                    /** If the user clicked on it ... */
                    if (p.contains(event.getX(), event.getY())) {
                        System.out.println("Point within a piece - 2st if");
                        model.getPoints().add(new Point2D(event.getX(),  event.getY()));

                        /** reset move-assistance */
                        toMove = null;
                        area.unhighlight();
                    }
                }

                /** If there is not a piece at this position ... */
                else {
                    /** Move the toMove piece here if possible and reset move-assistance */
                    if(toMove != null && p.contains(event.getX(), event.getY())) {
                        System.out.println("Point within a piece - 3st if");
                        model.getPoints().add(new Point2D(event.getX(),  event.getY()));

                        if(Main.getBoard().move(toMove, p.getKey())) {
                            /** Test for winner and run win sequence */
                            if(Main.getBoard().won() >= 0)
                                area.runWinSequence(Main.getBoard().won());
                            /** Move to next player and run Computer Player */
                            area.nextPlayer();
                            while(area.getCurrentPlayerIndex() >= GameSettings.NUM_HUMAN_PLAYERS) {
                                area.runComputerPlayer();
                                if(Main.getBoard().won() >= 0) {
                                    area.runWinSequence(Main.getBoard().won());
                                    break;
                                }
                            }
                        }
                        toMove = null;
                        area.unhighlight();
                    }
                }
            }
            reDrawArea();
            clearModel();
        }
    };



    @Override
    public void run() {
        while(isRunning) {

            /** If a player won, wait 5 seconds, then create a new game */
            if(area.getWinPlayerIndex() >= 0) {
//                repaint();

                try {
                    Thread.sleep(5000);
                } catch(InterruptedException e) {
                    Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, e);
                }

                area.setWinPlayerIndex(-1);
//                app.newGame();
            }

//            /** Repaint */
//            try {
//                Thread.sleep(50);
//            } catch(InterruptedException e) {
//                Logger.getLogger(Area.class.getName()).log(Level.SEVERE, null, e);
//            }
//            repaint();
        }
    }
}

