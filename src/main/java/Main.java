import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class Main extends Application {

    public static final int SCREEN_SIZE = 700;
    public static final int BOARD_RADIUS = 5;
    public static final int HEX_DIAMETER = (SCREEN_SIZE*2/3)/(BOARD_RADIUS + 2*(BOARD_RADIUS-1));
    public static final int PIECE_DIAMETER = HEX_DIAMETER*4/5;
    public static final double Y_OFFSET = ((double) HEX_DIAMETER/2)*(2-Math.sqrt(3));
    public static final double VISUAL_OFFSET = ((double) HEX_DIAMETER/2) * Math.sqrt(2)/9;

    public static final boolean MOVE_ASSISTANCE = true;
    public static final int NUM_HUMAN_PLAYERS = 1;
    public static final Color[] PLAYERS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PINK, Color.GRAY};
    public static final String[] PLAYER_NAMES = {"Red", "Blue", "Green", "Yellow", "Pink", "Gray"};

    private Board board;
    private Area area;

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
    }

     public void newGame() {
        initBoard();
    }

    private void initBoard() {
        board = new Board(BOARD_RADIUS, PLAYERS);
    }

    private void initScreen() {
        area = new Area(this);
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        Group root = new Group();
        primaryStage.setTitle("Trylma");

        root.getChildren().add(area);

        Canvas mainCanvas = new Canvas(SCREEN_SIZE - 5, SCREEN_SIZE - 5);
        GraphicsContext graphicsContext = mainCanvas.getGraphicsContext2D();
        root.getChildren().add(mainCanvas);

        area.paintComponent(graphicsContext);

        Scene scene = new Scene(root, SCREEN_SIZE, SCREEN_SIZE);

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

