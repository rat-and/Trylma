public class TestMain {
    private static Client client;


    public static void main(String[] args) {

        System.out.println("Start maina");
        client = new Client(null);
        try {
            client.connectToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
