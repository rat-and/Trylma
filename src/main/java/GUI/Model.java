package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    private final ObservableList<Point2D> points;

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

}
