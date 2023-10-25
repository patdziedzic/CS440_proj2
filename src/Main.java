import java.util.*;

public class Main {
    public static double q;
    private static final int numTests = 400;
    private static ArrayList<Cell> openCells = new ArrayList<>();


    /**
     * Get random int between min to max
     * @return randomly generated int
     */
    public static int rand(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    /**
     * Check the distance from bot to button and fire to button.
     * @return true if the bot is closer to the button than the fire
     */
    private static boolean checkDistBotVsFire(Cell bot, Cell fire, Cell button, Cell[][] ship) {
        //BFS Shortest Path from bot -> button
        LinkedList<Cell> shortestPath_Bot = Bfs.shortestPathBFS(bot, button, ship);
        //BFS Shortest Path from fire -> button
        LinkedList<Cell> shortestPath_Fire = Bfs.shortestPathBFS(fire, button, ship);
        try {
            return shortestPath_Bot.size() <= shortestPath_Fire.size();
        }
        catch (NullPointerException e) { return false; }
    }

    /**
     * Try to ignite the given neighbor of a fire cell
     */
    private static void tryFireNeighbor(Cell neighbor, LinkedList<Cell> fireCells) {
        if (neighbor != null && Math.random() <= neighbor.flammability && neighbor.isOpen && !neighbor.getOnFire()) {
            neighbor.setOnFire(true);
            fireCells.add(neighbor);
        }
    }


    /**
     * Run an experiment for Bot 1
     * @return true if the bot made it to the button
     */
    private static Boolean runBot1(Cell[][] ship) {
        //initialize the bot
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = Main.rand(0, openCells.size()-1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = Main.rand(0, openCells.size()-1);
        Cell initialFire = openCells.get(randIndex);
        initialFire.setOnFire(true); //setting on fire automatically updates neighbors
        fireCells.add(initialFire);

        if (bot.getOnFire() || button.getOnFire())
            return null;

        if(checkDistBotVsFire(bot, initialFire, button, ship))
            return null;

        //BFS Shortest Path from bot -> button
        LinkedList<Cell> shortestPath = Bfs.shortestPathBFS(bot, button, ship);
        if (shortestPath == null)
            return null;

        shortestPath.removeFirst();

        while (!shortestPath.isEmpty()) {
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
     * Run an experiment for Bot 2
     * @return true if the bot made it to the button
     */
    private static Boolean runBot2(Cell[][] ship) {
        //initialize the bot
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = Main.rand(0, openCells.size()-1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = Main.rand(0, openCells.size()-1);
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
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = Main.rand(0, openCells.size()-1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = Main.rand(0, openCells.size()-1);
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
        Cell[][] ship = copyShip(ogShip);
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
        int randIndex = Main.rand(0, openCells.size() - 1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the button
        randIndex = Main.rand(0, openCells.size() - 1);
        Cell button = openCells.get(randIndex);
        button.isButton = true;

        if (bot.isButton)
            return null;

        //initialize the fire
        LinkedList<Cell> fireCells = new LinkedList<>();
        randIndex = Main.rand(0, openCells.size() - 1);
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
     * Perform a deep copy of the given ship
     */
    private static Cell[][] copyShip(Cell[][] ship) {
        Cell[][] newShip = new Cell[Ship.D][Ship.D];
        for (int i = 0; i < ship.length; i++){
            for (int j = 0; j < ship[0].length; j++){
                newShip[i][j] = new Cell(ship[i][j]);
            }
        }
        for (int r = 0; r < Ship.D; r++) {
            for (int c = 0; c < Ship.D; c++) {
                newShip[r][c].setNeighbors(newShip);
            }
        }
        return newShip;
    }

    /**
     * Print the ship
     */
    private static void printShip(Cell[][] ship, Cell bot, Cell button, Cell fire) {
        System.out.println("x| 0 1 2 3 4 5 6 7 8 9");
        System.out.println(" ---------------------");
        for (int r = 0; r < Ship.D; r++) {
            System.out.print(r + "| ");
            for (int c = 0; c < Ship.D; c++) {
                if (ship[r][c].isOpen) {
                    if (ship[r][c].isBot)
                        System.out.print('s');
                    else if (ship[r][c].isButton)
                        System.out.print('g');
                    else if (ship[r][c].getOnFire())
                        System.out.print('f');
                    else
                        System.out.print(1);
                }
                else
                    System.out.print(0);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }


    /**
     * Run tests on the given bot number
     * @param bot the bot number
     */
    private static void runTests(int bot) {
        LinkedList<Boolean> testResults = new LinkedList<>();
        for (int test = 1; test <= numTests; test++) {
            Cell[][] tempShip = Ship.makeShip();
            openCells = new ArrayList<>();
            for (int i = 0; i < tempShip.length; i++){
                for (int j = 0; j < tempShip[0].length; j++){
                    if (tempShip[i][j].isOpen)
                        openCells.add(tempShip[i][j]);
                }
            }
            Boolean result = false;
            if (bot == 1) {
                result = runBot1(tempShip);
            }
            else if (bot == 2) {
                result = runBot2(tempShip);
            }
            else if (bot == 3) {
                result = runBot3(tempShip);
            }
            else if (bot == 4) {
                result = runBot4(tempShip);
            }

            if (result == null) //if null, forget this test (bot just got lucky or unlucky)
                test--;
            else
                testResults.add(result);
        }
        if (bot == 4) System.out.println();
        int totalWins = 0;
        for (Boolean result : testResults) {
            if (result)
                totalWins++;
        }

        double avg = totalWins * 100.0 / numTests;
        System.out.println("Avg Success Rate for q = " + q + " is " + avg);
    }

    /**
     * Main driver method to run the tests for each bot
     */
    public static void main(String[] args) {
        //BOT 1
        System.out.println("Bot 1");
        q = 0.1; runTests(1);
        q = 0.25; runTests(1);
        q = 0.50; runTests(1);
        q = 0.75; runTests(1);
        q = 0.9; runTests(1);
        System.out.println();

        //BOT 2
        System.out.println("Bot 2");
        q = 0.1; runTests(2);
        q = 0.25; runTests(2);
        q = 0.50; runTests(2);
        q = 0.75; runTests(2);
        q = 0.9; runTests(2);
        System.out.println();

        //BOT 3
        System.out.println("Bot 3");
        q = 0.1; runTests(3);
        q = 0.25; runTests(3);
        q = 0.50; runTests(3);
        q = 0.75; runTests(3);
        q = 0.9; runTests(3);
        System.out.println();

        //BOT 4
        System.out.println("Bot 4");
        q = 0.1; runTests(4);
        q = 0.25; runTests(4);
        q = 0.50; runTests(4);
        q = 0.75; runTests(4);
        q = 0.9; runTests(4);
        System.out.println();
    }
}