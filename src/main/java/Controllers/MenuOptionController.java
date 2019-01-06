package Controllers;

import GUI.Area;
import GUI.Main;
import GUI.Model;
import Other.ServerCreator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;


public class MenuOptionController {
    private String nick;
    private static GraphicsContext graphicsContext;
    private static Area area;
    private Model model;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField playerNick;

    public void setArea(Area area) {
        MenuOptionController.area = area;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @FXML
    protected void launchBoard(ActionEvent event) {
        if (Model.isServerRunning()) {
            setNick(playerNick.getText());
            Main.connectClient();
            Main.initRootLayout();
            Main.overView();
            Observer.drawNewBoard();
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
