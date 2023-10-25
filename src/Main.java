import java.util.*;

public class Main {
    private static final int numTests = 1;
    public static int k; //size of detection square - (2k+1) x (2k+1), k >= 1
    //^ for a 50x50 ship, 1 <= k <= 24 because max square can be 49x49
    public static double alpha; //accuracy of probabilistic sensor (smaller = more accurate), 0 < alpha < 1


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
     * @return number of actions taken (moves + sensing) to plug the leak
     */
    private static Integer runBot1() {
        Integer numActions = 0;

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

        while (!bot.isLeak) {
            //BFS Shortest Path from bot -> nearest potential leak
            LinkedList<Cell> shortestPath = Bfs.detSP_BFS(bot);
            if (shortestPath == null) return null;
            shortestPath.removeFirst();

            //move the bot to the nearest potential leak
            while (shortestPath != null) {
                Cell neighbor = shortestPath.removeFirst();
                bot.isBot = false;
                neighbor.isBot = true;
                bot = neighbor;
                numActions++;
            }
            if (bot.isLeak) return numActions;
            else bot.noLeak = true;

            //Sense Action
            detSquare = getDetectionSquare(bot);
            if (!leakInSquare(detSquare)) {
                for (Cell cell : detSquare) cell.noLeak = true;
            }
            else {
                //leak detected
                for (int r = 0; r < Ship.D; r++) {
                    for (int c = 0; c < Ship.D; c++) {
                        Cell curr = ship[r][c];
                        if (!detSquare.contains(curr)) curr.noLeak = true;
                    }
                }
            }

            // THIS IS WHERE I STOPPED CODING *******************************************

        }
        return numActions;
    }

    /**
     * Deterministic Sense Action - Get all cells in the detection square
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
     * @return true if the bot made it to the button
     */
    private static Boolean runBot2(Cell[][] ship) {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = rand(0, openCells.size()-1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = rand(0, openCells.size()-1);
        Cell initialFire = openCells.get(randIndex);
        initialFire.setOnFire(true); //setting on fire automatically updates neighbors
        fireCells.add(initialFire);

        if (bot.getOnFire() || button.getOnFire())
            return null;

        if(checkDistBotVsFire(bot, initialFire, button, ship))
            return null;

        while (!bot.isButton && !bot.getOnFire() && !button.getOnFire()) {
            //BFS Shortest Path from bot -> button
            LinkedList<Cell> shortestPath = Bfs.shortestPathBFS(bot, button, ship);
            if (shortestPath == null)
                return false;

            shortestPath.removeFirst();

            //move the bot
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;

            if (bot.isButton)
                return true;
            else {
                //else, potentially advance fire
                LinkedList<Cell> copyFireCells = (LinkedList<Cell>) fireCells.clone();
                while (!copyFireCells.isEmpty()) {
                    Cell fireCell = copyFireCells.removeFirst();
                    tryFireNeighbor(fireCell.up, fireCells);
                    tryFireNeighbor(fireCell.down, fireCells);
                    tryFireNeighbor(fireCell.left, fireCells);
                    tryFireNeighbor(fireCell.right, fireCells);
                }

                if (bot.getOnFire() || button.getOnFire())
                    return false;
            }
        }
        return null;
    }

    /**
     * Run an experiment for Bot 3
     * @return true if the bot made it to the button
     */
    private static Boolean runBot3(Cell[][] ship) {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = rand(0, openCells.size()-1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = rand(0, openCells.size()-1);
        Cell initialFire = openCells.get(randIndex);
        initialFire.setOnFire(true); //setting on fire automatically updates neighbors
        fireCells.add(initialFire);

        if (bot.getOnFire() || button.getOnFire())
            return null;

        if(checkDistBotVsFire(bot, initialFire, button, ship))
            return null;


        while (!bot.isButton && !bot.getOnFire() && !button.getOnFire()) {
            //BFS Shortest Path from bot -> button
            LinkedList<Cell> shortestPath;

            //Avoid fire neighbors if possible
            shortestPath = Bfs.shortestPathBFS_Bot3(bot, button, ship);
            if (shortestPath == null) {
                //if not possible, do the Bot 2 method
                shortestPath = Bfs.shortestPathBFS(bot, button, ship);
                if (shortestPath == null)
                    return false;
            }
            shortestPath.removeFirst();

            //move the bot
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;

            if (bot.isButton)
                return true;
            else {
                //else, potentially advance fire
                LinkedList<Cell> copyFireCells = (LinkedList<Cell>) fireCells.clone();
                while (!copyFireCells.isEmpty()) {
                    Cell fireCell = copyFireCells.removeFirst();
                    tryFireNeighbor(fireCell.up, fireCells);
                    tryFireNeighbor(fireCell.down, fireCells);
                    tryFireNeighbor(fireCell.left, fireCells);
                    tryFireNeighbor(fireCell.right, fireCells);
                }

                if (bot.getOnFire() || button.getOnFire())
                    return false;
            }
        }
        return null;
    }


    /**
     * Run a simulation for a neighbor of the bot in Bot 4 using Bot 1 logic
     * @return true if the neighbor made it to the button in the simulation
     */
    private static boolean runSimulation_Bot4(Cell ogBot, Cell ogButton, LinkedList<Cell> ogFireCells, Cell[][] ogShip) {
        //make copies
        Cell[][] ship = Ship.copyShip(ogShip);
        Cell bot = ship[ogBot.getRow()][ogBot.getCol()];
        Cell button = ship[ogButton.getRow()][ogButton.getCol()];
        LinkedList<Cell> fireCells = new LinkedList<>();
        for (Cell ogFireCell : ogFireCells) {
            fireCells.add(ship[ogFireCell.getRow()][ogFireCell.getCol()]);
        }

        //BFS Shortest Path from bot -> button
        LinkedList<Cell> shortestPath = Bfs.shortestPathBFS(bot, button, ship);
        if (shortestPath == null)
            return false;

        shortestPath.removeFirst();

        while (!bot.isButton && !bot.getOnFire() && !button.getOnFire()) {
            //move the bot
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;

            if (bot.isButton)
                return true;
            else {
                //else, potentially advance fire
                LinkedList<Cell> copyFireCells = (LinkedList<Cell>) fireCells.clone();
                while (!copyFireCells.isEmpty()) {
                    Cell fireCell = copyFireCells.removeFirst();
                    tryFireNeighbor(fireCell.up, fireCells);
                    tryFireNeighbor(fireCell.down, fireCells);
                    tryFireNeighbor(fireCell.left, fireCells);
                    tryFireNeighbor(fireCell.right, fireCells);
                }

                if (bot.getOnFire() || button.getOnFire())
                    return false;
            }
        }
        return false;
    }

    /**
     * Get the optimal path for the bot at the current time step.
     * The path is optimal based on the number of simulations that won for each neighbor
     * @return the optimal path linked list
     */
    private static LinkedList<Cell> getOptimalPath(Cell bot, Cell button, LinkedList<Cell> fireCells, Cell[][] ship) {
        PriorityQueue<PQCell> pq = new PriorityQueue<>();
        HashMap<Cell, Integer> distTo = new HashMap<>(); //key: curr, value: distance from bot to curr
        HashMap<Cell, Cell> parent = new HashMap<>(); //key: child, value: parent

        pq.add(new PQCell(bot, 0));
        distTo.put(bot, 0);
        parent.put(null, bot);

        while (!pq.isEmpty()) {
            Cell curr = pq.remove().cell;

            //if button is found, return
            if (curr.isButton) {
                LinkedList<Cell> optimalPath = new LinkedList<>();
                Cell ptr = button;
                while (ptr != null) {
                    optimalPath.add(ptr);
                    ptr = parent.get(ptr);
                }
                Collections.reverse(optimalPath);
                return optimalPath;
            }

            for (Cell neighbor : curr.neighbors) {
                if (neighbor != null && Bfs.shortestPathBFS(neighbor, button, ship) != null && neighbor.isOpen && !neighbor.getOnFire()) {
                    int tempDist = distTo.get(curr) + 1;
                    if (!distTo.containsKey(neighbor) || tempDist < distTo.get(neighbor)) {
                        distTo.put(neighbor, tempDist);
                        parent.put(neighbor, curr);

                        //RUN SIMULATIONS
                        int wins = 0;
                        int numSimulations = 4;
                        for (int i = 0; i < numSimulations; i ++) {
                            if (runSimulation_Bot4(neighbor, button, fireCells, ship))
                                wins++;
                        }

                        pq.add(new PQCell(neighbor, distTo.get(neighbor) + wins));
                    }
                }
            }
        }
        return null;
    }

    /**
     * Run an experiment for Bot 4
     * @return true if the bot made it to the button
     */
    private static Boolean runBot4(Cell[][] ship) {
        //initialize the bot
        int randIndex = rand(0, openCells.size() - 1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = rand(0, openCells.size() - 1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = rand(0, openCells.size() - 1);
        Cell initialFire = openCells.get(randIndex);
        initialFire.setOnFire(true); //setting on fire automatically updates neighbors
        fireCells.add(initialFire);

        if (bot.getOnFire() || button.getOnFire())
            return null;

        if(checkDistBotVsFire(bot, initialFire, button, ship))
            return null;

        while (!bot.isButton && !bot.getOnFire() && !button.getOnFire()) {
            LinkedList<Cell> optimalPath = getOptimalPath(bot, button, fireCells, ship);
            if (optimalPath == null)
                return false;

            optimalPath.removeFirst();

            //move the bot
            Cell neighbor = optimalPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;

            if (bot.isButton)
                return true;
            else {
                //else, potentially advance fire
                LinkedList<Cell> copyFireCells = (LinkedList<Cell>) fireCells.clone();
                while (!copyFireCells.isEmpty()) {
                    Cell fireCell = copyFireCells.removeFirst();
                    tryFireNeighbor(fireCell.up, fireCells);
                    tryFireNeighbor(fireCell.down, fireCells);
                    tryFireNeighbor(fireCell.left, fireCells);
                    tryFireNeighbor(fireCell.right, fireCells);
                }

                if (bot.getOnFire() || button.getOnFire())
                    return false;
            }
        }
        return null;
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
            Integer result;
            switch (bot) {
                case 1 -> result = runBot1();
                //case 2 -> result = runBot2(ship);
                default -> result = 0;
            }

            if (result == null) //if null, forget this test
                test--;
            else
                testResults.add(result);
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
        k = 10; runTests(1);
        System.out.println();

        //Bot 2



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