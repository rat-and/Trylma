import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.Random;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
/**
 * Client
 *
 *  PROTOCOL:
 *  Client -> Server           Server -> Client
 *  ----------------           ----------------
 *  MOVE <src> <dst> (x,y,z)   WELCOME PLAYER<int>  (player number)
 *  EXIT                       PLAYER_MOVED: <src> <dst> (x,y,z)
 *                             MESSAGE <text>
 *
 * A second change is that it allows an unlimited number of pairs of
 * players to play.
 */
public class Main extends Application {

    private Board board;
    private Area area;
    private Client client;

    public static void main(String[] args) {
        launch(args);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Main main = new Main();
            }
        };
    }

    public Main() {
        initScreen();
        initBoard();
        initConnection();
    }

     public void newGame() {
        initBoard();
    }

    private void initBoard() {
        board = new Board(GameSettings.BOARD_RADIUS,GameSettings. PLAYERS);
    }

    private void initScreen() {
        area = new Area(this);
    }

    private void initConnection() {
        client = new Client(this);
    }

    public Board getBoard() {
        return board;
    }

    public Client getClient (){
        return client;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));

        //client.connectToServer(); TODO: uncomment in future

        Group root = new Group();
        primaryStage.setTitle("Trylma");

        root.getChildren().add(area);

        Canvas mainCanvas = new Canvas(GameSettings.SCREEN_SIZE - 5, GameSettings.SCREEN_SIZE - 5);
        GraphicsContext graphicsContext = mainCanvas.getGraphicsContext2D();
        root.getChildren().add(mainCanvas);

        area.paintComponent(graphicsContext);

        Scene scene = new Scene(root, GameSettings.SCREEN_SIZE, GameSettings.SCREEN_SIZE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

/*
   @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graphics in JavaFX");
        Group root = new Group();
        Canvas canvas = new Canvas(650, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        draw2DShapes(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    private void draw2DShapes(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        Random random = new Random(System.currentTimeMillis());

        gc.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.9));
        gc.translate(width / 2, height / 2);

        for (int i = 0; i < 60; i++) {
            gc.rotate(6.0);
            gc.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.9));
            gc.fillOval(10, 60, 30, 30);
            gc.strokeOval(60, 60, 30, 30);
            gc.setFill(Color.rgb(random.nextInt(255), random.nextInt(255),random.nextInt(255), 0.9));
            gc.fillRoundRect(110, 60, 30, 30, 10, 10);
            gc.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0.9));
            gc.fillPolygon(
                    new double[] { 105, 117, 159, 123, 133, 105, 77, 87,51, 93 },
                    new double[] { 150, 186, 186, 204, 246, 222, 246,204, 186, 186 },
                    10);
        }
    }
*/
}

