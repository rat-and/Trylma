package Controllers;

import GUI.Area;
import GUI.Main;
import GUI.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.ArrayList;


public class MenuOptionController {
    private String nick;
    private static GraphicsContext graphicsContext;
    private static Area area;
    private Model model;
    private ArrayList<String> gameList = new ArrayList<String>();
    private ObservableList<String> boxList = FXCollections.observableArrayList(gameList);

    @FXML
    private Canvas canvas;

    @FXML
    private ChoiceBox<String> availableGames;

    @FXML
    private TextField playerNick;

    public void setDefaultServer() {
        Main.addServer(4040);
        gameList.add("4040");
        availableGames.getItems().add("4040 - default");
    }

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
        setNick(playerNick.getText());
        Main.initClient(Integer.parseInt(playerNick.getText()));
        Main.initRootLayout();
//        Main.overView();
//        Observer.drawNewBoard();
        System.out.println(nick);
    }

    @FXML
    protected void setUpServer(ActionEvent event) {
        try {
            Main.addServer(Integer.parseInt(playerNick.getText()));
            gameList.add(playerNick.getText());
        } catch (NumberFormatException e) {
            System.err.println("Port must be an unsigned integer");
        }
        setNick(playerNick.getText());
        availableGames.setItems(boxList);
        Main.initRootLayout();
//        Main.overView();
//        Observer.drawNewBoard();
    }

    public void setNick(String nick) {
        this.nick = nick;
    }


}
