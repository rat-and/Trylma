package Controllers;

import GUI.Main;
import GUI.Model;
import Other.ServerCreator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class MenuOptionController {
    private String nick;
    private static GraphicsContext graphicsContext;
    private Model model;
    private int currentPort;
    private Main mainApp;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField playerNick;

    @FXML
    ChoiceBox availableGames;

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void setCurrentPort(int currentPort) {
        this.currentPort = currentPort;
    }

    public int getCurrentPort() {
        return currentPort;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void updateAvaibleGames() {
        availableGames.setItems(model.getRunningPorts());
    }

    @FXML
    protected void launchBoard(ActionEvent event) {
        if (Model.isServerRunning()) {
//            availableGames.getSelectionModel().selectedItemProperty().addListener(
//                    (ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> setCurrentPort(newValue)
//            );

//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        mainApp = new Main();
//                        mainApp.start(new Stage());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });


            Main.setModel(model);
            Main.setCanvas(canvas);

            setCurrentPort((Integer) availableGames.getValue());

            setNick(playerNick.getText());
            Main.connectClient(getCurrentPort());
            Main.initRootLayout();
            System.out.println(nick);
        } else {
            Model.popupWarning("Couldn't find a server running");
        }
    }

    @FXML
    protected void setUpServer() {
        try {
            ServerCreator.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
   }
