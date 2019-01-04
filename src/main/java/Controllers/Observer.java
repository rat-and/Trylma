package Controllers;

import GUI.Area;
import GUI.Model;
import GUI.Point2D;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.net.URL;
import java.util.ResourceBundle;

public class Observer {

    private static GraphicsContext gc;
    private static Model model;
    private static Area area;

    public Observer() {
    }

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resourceBundle;

    @FXML
    private static Canvas canvas;

    @FXML
    private void initialize() {
    }

    public void initModel(Model model) {
        this.model = model;
        this.model.getPoints().addListener((ListChangeListener.Change<? extends Point2D> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Point2D p : change.getAddedSubList()) {
                        System.out.println("I saw a change");
                        reDraw();
                    }
                }
            }
        } );
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @FXML
    public static void reDraw() {
        area.reDraw();
    }

    @FXML
    public static void drawNewBoard() {
        Observer.gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        Observer.model.clearPointList();
        area.drawLatticing(gc);
    }
}
