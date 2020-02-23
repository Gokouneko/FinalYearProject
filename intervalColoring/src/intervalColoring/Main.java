package intervalColoring;

public class Main {

    public static void main(String[] args) {

        Algorithm algorithm = new Algorithm();
        algorithm.onlineAlgorithm();
        Results results = new Results();
        results.calSkylineCost();
    }

}
