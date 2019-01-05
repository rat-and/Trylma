package GUI;

import Controllers.*;
import Logic.Board;
import Logic.GameSettings;
import Other.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {

    private static Board board;
    private static Area area;

    public static Stage primaryStage;
    private static BorderPane rootLayout;
    private AnchorPane menuLayout;
    private static Model model;
    private static Canvas canvas;
    private static Client client;

    public static void main(String[] args) {
        launch(args);
    }

     public void newGame() {
        initBoard();
    }

    private void initBoard() {
        board = new Board(GameSettings.BOARD_RADIUS, GameSettings.PLAYERS);
    }

    private void initScreen() {
        area = new Area(this);
    }

    private void initClient() {
        client = new Client();
    }

    public static Board getBoard() {
        return board;
    }

    public static Client getClient() {
        return client;
    }


    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initScreen();
        initBoard();
        initClient();

        this.model = new Model();
        this.canvas = new Canvas(GameSettings.SCREEN_SIZE, GameSettings.SCREEN_SIZE);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Trylma");

//        initRootLayout();
        initMenuLayout();
        client.connectToServer("LOCALHOST",9009);//TODO: ADDRESS AND PORT OF SERVER
//        overView();


    }

    public void initMenuLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/menu_red.fxml"));
            menuLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(menuLayout);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            MenuOptionController menuController = loader.getController();
            menuController.setArea(area);
            menuController.setCanvas(canvas);
            menuController.setModel(model);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/board.fxml"));
            rootLayout = (BorderPane) loader.load();

            /** Show the scene containing the root layout*/
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
//            primaryStage.setHeight(GameSettings.SCREEN_SIZE);
//            primaryStage.setWidth(GameSettings.SCREEN_SIZE);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Platform.exit();
                    System.exit(0);
                }
            });


            /** Give the controller access to the main app */
            Controller rootController = loader.getController();
            rootController.initModel(model);
            rootController.setArea(area);
            rootController.setCanvas(canvas);

            rootLayout.setOnMouseClicked(rootController.mouseClicked);

            rootLayout.getChildren().add(canvas);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void overView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/overview.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            Observer owController = loader.getController();
            owController.setCanvas(canvas);
            owController.setArea(area);

            rootLayout.setCenter(overview);

            owController.initModel(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

