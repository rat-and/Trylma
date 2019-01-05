package GUI;

import Other.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Model {
    private final ObservableList<Point2D> points;
    private static ArrayList<Client> clients;

    public ObservableList<Point2D> getPoints() {
        return points;
    }

    public static void addClientToModel(Client client) {
        clients.add(client);
    }


    public Model()
    {
        this.points = FXCollections.observableArrayList();
        clients = new ArrayList<Client>();
    }

    public void clearPointList() {
        points.removeAll();
    }

}
