package warriorgame;

public class Start {

    public static void main(String[] args) {
        String inputFile = args[0];
        boolean gameVisualization = false;
        if (args[1].equals("1")) {
            gameVisualization = true;
        }

        Game game = new Game(inputFile, gameVisualization);
        game.runGame();
    }
}
