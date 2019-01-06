package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * This class is used to communicate between server-client interface and GUI
 * Actions taken by any controller which change server-client state will be
 * noted here. So it goes the other way
 */
public class Model {
    private final ObservableList<Point2D> points;

    /**
     * Current state of a server. True, if there's at least one running server
     * TODO few servers case
     */
    private static boolean serverRunning = false;

    public ObservableList<Point2D> getPoints() {
        return points;
    }

    public Model()
    {
        this.points = FXCollections.observableArrayList();
    }

    public void clearPointList() {
        points.removeAll();
    }

    public static boolean isServerRunning() {
        return serverRunning;
    }

    public static void setServerRunning(boolean serverRunning) {
        Model.serverRunning = serverRunning;
    }

     public static void popupWarning(String warningMessage) {
       Alert alert = new Alert(Alert.AlertType.WARNING);
       alert.setTitle("Connection");
       alert.setHeaderText("Result:");
       alert.setContentText(warningMessage);

       alert.showAndWait();
    }
}
