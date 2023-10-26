import java.util.*;

public class Main {
    private static final int numTests = 500;
    public static int k; //size of detection square - (2k+1) x (2k+1), k >= 1
    //^ for a 50x50 ship, 1 <= k <= 24 because max square can be 49x49
    public static double alpha; //accuracy of probabilistic sensor (smaller = more accurate), 0 < alpha < 1
    public static Integer numActions;


    //SHIP
    private static Cell[][] ship;
    private static ArrayList<Cell> openCells = new ArrayList<>();


    /**
     * Get random int between min to max
     * @return randomly generated int
     */
    public static int rand(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    // ******************** DETERMINISTIC LEAK DETECTORS ********************

    /**
     * Run an experiment for Bot 1
     * Update numActions taken (moves + sensing) to plug the leak
     */
    private static void runBot1() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;
        LinkedList<Cell> detSquare = getDetectionSquare(bot);

        //initialize the leak
        Cell leak = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak == null) {
            randIndex = rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);

            boolean found = false;
            for (Cell cell : detSquare) {
                if (cell.equals(tempLeak)) {
                    found = true;
                    break;
                }
            }
            if (!found) leak = tempLeak;
        }
        leak.isLeak = true;

        bot.noLeak = true;
        for (Cell cell : detSquare) cell.noLeak = true;

        //Ship.printShip(ship);

        while (!bot.isLeak) {
            //BFS Shortest Path from bot -> nearest potential leak
            LinkedList<Cell> shortestPath = Bfs.detSP_BFS(bot);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //move the bot to the nearest potential leak
            bot = moveBot(bot, shortestPath);
            if (bot.isLeak) return;
            else bot.noLeak = true;

            //Sense Action
            detSenseAction(bot);
        }
    }

    /**
     * Move the bot along the given path and increment numActions for each step
     * @return the cell the bot ends up in
     */
    private static Cell moveBot(Cell bot, LinkedList<Cell> path) {
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
     * Deterministic Sense Action (update cells accordingly)
     * @return true if leak was detected
     */
    private static boolean detSenseAction(Cell bot) {
        LinkedList<Cell> detSquare = getDetectionSquare(bot);
        numActions++;
        if (!leakInSquare(detSquare)) {
            //set noLeak = true for everything in the square
            for (Cell cell : detSquare) cell.noLeak = true;
            return false;
        }
        else {
            //leak detected --> set noLeak = true for everything outside the square
            for (int r = 0; r < Ship.D; r++) {
                for (int c = 0; c < Ship.D; c++) {
                    Cell curr = ship[r][c];
                    if (!detSquare.contains(curr)) curr.noLeak = true;
                }
            }
            return true;
        }
    }

    /**
     * Get all cells in the detection square
     * @return linked list of cells in the square
     */
    private static LinkedList<Cell> getDetectionSquare(Cell bot) {
        LinkedList<Cell> detSquare = new LinkedList<>();

        int bRow = bot.getRow();
        int bCol = bot.getCol();
        for (int r = bRow - k; r <= bRow + k; r++) {
            for (int c = bCol - k; c <= bCol + k; c++) {
                try {
                    Cell curr = ship[r][c];
                    if (curr != null && curr.isOpen)
                        detSquare.add(curr);
                }
                catch (ArrayIndexOutOfBoundsException ignore){}
            }
        }
        return detSquare;
    }

    /**
     * Given a detection square, check if a leak was detected
     * @return true if a leak was detected
     */
    private static boolean leakInSquare(LinkedList<Cell> detSquare) {
        for (Cell cell : detSquare) {
            if (cell.isLeak) return true;
        }
        return false;
    }

    /**
     * Run an experiment for Bot 2
     */
    private static void runBot2() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;
        LinkedList<Cell> detSquare = getDetectionSquare(bot);

        //initialize the leak
        Cell leak = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak == null) {
            randIndex = rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);

            boolean found = false;
            for (Cell cell : detSquare) {
                if (cell.equals(tempLeak)) {
                    found = true;
                    break;
                }
            }
            if (!found) leak = tempLeak;
        }
        leak.isLeak = true;

        bot.noLeak = true;
        for (Cell cell : detSquare) cell.noLeak = true;

        //Ship.printShip(ship);

        //Check the 4 Quadrants
        if (checkQuadrant(bot, 0, 0)) {
            if (bot.isLeak) return;
            else bot.noLeak = true;
        }
        else if (checkQuadrant(bot, 0, Ship.D - 1)) {
            if (bot.isLeak) return;
            else bot.noLeak = true;
        }
        else if (checkQuadrant(bot, Ship.D - 1, 0)) {
            if (bot.isLeak) return;
            else bot.noLeak = true;
        }
        else if (checkQuadrant(bot, Ship.D - 1, Ship.D - 1)) {
            if (bot.isLeak) return;
            else bot.noLeak = true;
        }

        while (!bot.isLeak) {
            //BFS Shortest Path from bot -> nearest potential leak
            LinkedList<Cell> shortestPath = Bfs.detSP_BFS(bot);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //move the bot to the nearest potential leak
            bot = moveBot(bot, shortestPath);
            if (bot.isLeak) return;
            else bot.noLeak = true;

            //Sense Action
            detSenseAction(bot);
        }
    }

    /**
     * Go to the corner of the quadrant and perform a sense action
     * @return true if the leak was detected
     */
    private static boolean checkQuadrant(Cell bot, int row, int col) {
        Cell nearestOpen = null;
        Cell cell = ship[row][col];
        if (cell.isOpen) nearestOpen = cell;
        else nearestOpen = Bfs.nearestOpen_BFS(cell);

        LinkedList<Cell> shortestPath = Bfs.SP_BFS(bot, nearestOpen);
        if (shortestPath == null) {
            numActions = null;
            return false;
        }
        shortestPath.removeFirst();

        //move the bot to nearestOpen
        bot = moveBot(bot, shortestPath);
        if (bot.isLeak) return true;
        else bot.noLeak = true;

        //Sense Action
        return detSenseAction(bot);
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
                case 1 -> runBot1();
                //case 2 -> result = runBot2(ship);
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
        double avg = totalActions * 100.0 / numTests;

        switch (bot) {
            case 1, 2, 5, 6 ->
                    System.out.println("Avg Actions Taken for k = " + k + " is " + avg);
            case 3, 4, 7, 8, 9 ->
                    System.out.println("Avg Actions Taken for alpha = " + alpha + " is " + avg);
            default -> System.out.println("Bot number out of range.");
        }

    }

    /**
     * Main driver method to run the tests for each bot
     */
    public static void main(String[] args) {
        //PART 1 - DETERMINISTIC LEAK DETECTORS
        //Bot 1
        System.out.println("Bot 1");
        //k = 1; runTests(1);
        //k = 5; runTests(1);
        k = 10; runTests(1);
        //k = 15; runTests(1);
        //k = 24; runTests(1);
        System.out.println();

        //Bot 2
        System.out.println("Bot 2");
        k = 10; runTests(1);
        System.out.println();


        //PART 2 - PROBABILISTIC LEAK DETECTORS
        //Bot 3


        //Bot 4



        //PART 3 - MULTIPLE LEAKS
        //Bot 5


        //Bot 6


        //Bot 7


        //Bot 8


        //Bot 9
    }
}