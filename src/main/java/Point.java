import javafx.scene.shape.Ellipse;

public class Point<K> {
    private double px, py;
    private K key;

    public Point(K key, double px, double py) {
        this.key = key;
        this.px = px;
        this.py = py;
    }

    public double getPx() {
        return px;
    }

    public double getPy() {
        return py;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public boolean equals(Point<K> p) {
        return (px == p.getPx() && py == p.getPy());
    }

    public Ellipse getEllipse() {
        return new Ellipse(px * GameSettings.HEX_DIAMETER + GameSettings.SCREEN_SIZE / 2,
                py * (GameSettings.HEX_DIAMETER - (GameSettings.Y_OFFSET - GameSettings.VISUAL_OFFSET)) + GameSettings.SCREEN_SIZE / 2,
                GameSettings.PIECE_DIAMETER, GameSettings.PIECE_DIAMETER);
    }

    public String toString() {
        return ("(" + px + "," + py + ")");
    }
}
