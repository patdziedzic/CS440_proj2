import java.util.*;

public class Main {
    private static final int numTests = 50;
    public static int k; //size of detection square - (2k+1) x (2k+1), k >= 1
    //^ for a 50x50 ship, 1 <= k <= 24 because max square can be 49x49
    public static double alpha; //accuracy of probabilistic sensor (smaller = more accurate), 0 < alpha < 1
    public static Integer numActions;


    //SHIP
    protected static Cell[][] ship;
    protected static ArrayList<Cell> openCells = new ArrayList<>();


    /**
     * Get random int between min to max
     * @return randomly generated int
     */
    public static int rand(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Get Euclidean distance between 2 Cells
     */
    public static double distFormula(Cell a, Cell b) {
        return Math.sqrt(Math.pow(b.getRow()-a.getRow(), 2) + Math.pow(b.getCol()-a.getCol(), 2));
    }

    /**
     * Move the bot along the given path and increment numActions for each step
     * @return the cell the bot ends up in
     */
    public static Cell moveBot(Cell bot, LinkedList<Cell> path) {
        while (!path.isEmpty()) {
            Cell neighbor = path.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
        }
        return bot;
    }


    /**
     * Run tests on the given bot number
     * @param bot the bot number
     */
    private static void runTests(int bot) {
        LinkedList<Integer> testResults = new LinkedList<>();
        for (int test = 1; test <= numTests; test++) {
            ship = Ship.makeShip();
            openCells = new ArrayList<>();
            for (int i = 0; i < Ship.D; i++){
                for (int j = 0; j < Ship.D; j++){
                    if (ship[i][j].isOpen)
                        openCells.add(ship[i][j]);
                }
            }
            numActions = 0;
            switch (bot) {
                case 1 -> DeterministicBots.runBot1();
                case 2 -> DeterministicBots.runBot2();
                case 3 -> ProbabilisticBots.runBot3();
                case 4 -> ProbabilisticBots.runBot4();
                case 5 -> MultipleLeaksBots.runBot5();
                case 6 -> MultipleLeaksBots.runBot6();
                case 7 -> MultipleLeaksBots.runBot7();
                case 8 -> MultipleLeaksBots.runBot8();
                default -> numActions = 0;
            }

            //Ship.printShip(ship);

            if (numActions == null) //if null, forget this test
                test--;
            else
                testResults.add(numActions);
        }

        int totalActions = 0;
        for (Integer result : testResults) {
            if (result != null)
                totalActions += result;
        }
        double avg = totalActions / (double) numTests;

        switch (bot) {
            case 1, 2, 5, 6 ->
                    System.out.println("Avg Actions Taken for k = " + k + " is " + avg);
            case 3, 4, 7, 8, 9, 45 ->
                    System.out.println("Avg Actions Taken for alpha = " + alpha + " is " + avg);
            default -> System.out.println("Bot number out of range.");
        }

    }

    /**
     * Main driver method to run the tests for each bot
     */
    public static void main(String[] args) {
//        k = 1;
//
//        ship = Ship.makeShip();
//        Cell[][] copyShip = Ship.copyShip(ship);
//        openCells = new ArrayList<>();
//        for (int i = 0; i < Ship.D; i++){
//            for (int j = 0; j < Ship.D; j++){
//                if (ship[i][j].isOpen)
//                    openCells.add(ship[i][j]);
//            }
//        }
//
//        numActions = 0;
//        DeterministicBots.runBot1();
//        System.out.println(numActions);
//
//        numActions = 0;
//        ship = copyShip;
//        openCells = new ArrayList<>();
//        for (int i = 0; i < Ship.D; i++){
//            for (int j = 0; j < Ship.D; j++){
//                if (ship[i][j].isOpen)
//                    openCells.add(ship[i][j]);
//            }
//        }
//        DeterministicBots.runBot2();
//        System.out.println(numActions);





/*
        //PART 1 - DETERMINISTIC LEAK DETECTORS
        //Bot 1
        System.out.println("Bot 1");
        k = 1; runTests(1);
        k = 5; runTests(1);
        k = 10; runTests(1);
        k = 15; runTests(1);
        k = 24; runTests(1);
        System.out.println();


        //Bot 2
        System.out.println("Bot 2");
        k = 1; runTests(2);
        k = 5; runTests(2);
        k = 10; runTests(2);
        k = 15; runTests(2);
        k = 20; runTests(2);
        k = 24; runTests(2);
        System.out.println();
*/

        //PART 2 - PROBABILISTIC LEAK DETECTORS
        //Bot 3
        System.out.println("Bot 3");
        alpha = 0.2; runTests(3);
        alpha = 0.25; runTests(3);
        alpha = 0.3; runTests(3);
        alpha = 0.35; runTests(3);
        alpha = 0.4; runTests(3);
        alpha = 0.6; runTests(3);
        alpha = 0.8; runTests(3);
        alpha = 1; runTests(3);
        System.out.println();

        //Bot 4
        System.out.println("Bot 4");
        alpha = 0.2; runTests(4);
        alpha = 0.25; runTests(4);
        alpha = 0.3; runTests(4);
        alpha = 0.35; runTests(4);
        alpha = 0.4; runTests(4);
        alpha = 0.6; runTests(4);
        alpha = 0.8; runTests(4);
        alpha = 1; runTests(4);
        System.out.println();

/*
        //PART 3 - MULTIPLE LEAKS
        //Bot 5
        System.out.println("Bot 5");
        k = 1; runTests(5);
        k = 5; runTests(5);
        k = 10; runTests(5);
        k = 15; runTests(5);
        k = 24; runTests(5);
        System.out.println();

        //Bot 6
        System.out.println("Bot 6");
        k = 1; runTests(6);
        k = 5; runTests(6);
        k = 10; runTests(6);
        k = 15; runTests(6);
        k = 24; runTests(6);
        System.out.println();
*/
/*
        //Bot 7
        System.out.println("Bot 7");
        alpha = 0.25; runTests(7);
        alpha = 0.5; runTests(7);
        alpha = 0.75; runTests(7);
        System.out.println();
*//*
        //Bot 8
        System.out.println("Bot 8");
        alpha = 0.25; runTests(8);
        alpha = 0.5; runTests(8);
        alpha = 0.75; runTests(8);
        System.out.println();
*/
        //Bot 9
    }
}