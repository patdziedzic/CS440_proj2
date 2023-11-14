import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * Class for Probabilistic Bots (3 and 4)
 */
public class ProbabilisticBots extends Main {
    private static final int numSimBeeps = 10000;


    //private static PriorityQueue<Cell> pqProb = new PriorityQueue<>(new PQComparator());
    //^ priority queue of cells to compare P(L) for each

    /**
     * Run an experiment for Bot 3
     * Update numActions taken (moves + sensing) to plug the leak
     */
    public static void runBot3() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the leak
        randIndex = rand(0, openCells.size()-1);
        Cell leak = openCells.get(randIndex);
        leak.isLeak = true;

        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }
        bot.noLeak = true;

        //set initial probabilities and get the cell with the max
        int size = openCells.size() - 1; //(-1) to account for the bot initially not being the leak
        double maxProb = 1/(double)size;
        for (Cell cell : openCells) {
            cell.setProbLeak(maxProb);
        }
        bot.setProbLeak(0);
        Cell maxProbCell;

        //Ship.printShip(ship);

        while (!bot.isLeak) {
            //BFS Shortest Path from bot -> cell with highest P(L)
            maxProb = 0.0;
            maxProbCell = null;
            for (Cell cell : openCells) {
                if (maxProb < cell.getProbLeak()) {
                    maxProb = cell.getProbLeak();
                    maxProbCell = cell;
                }
            }
            LinkedList<Cell> shortestPath = Bfs.SP_BFS(bot, maxProbCell);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //move the bot one step toward cell with highest P(L)
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
            Bfs.updateDistances(bot);

            if (bot.isLeak) return;

            bot.noLeak = true;
            updateProb_Step(bot);

            //Sense Action
            if (bot.equals(maxProbCell))
                probSenseAction(bot, leak);
        }
    }

    /**
     * Run an experiment for Bot 4
     * Update numActions taken (moves + sensing) to plug the leak
     */
    /*public static void runBot4() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the leak
        randIndex = rand(0, openCells.size()-1);
        Cell leak = openCells.get(randIndex);
        leak.isLeak = true;

        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }
        bot.noLeak = true;

        //set initial probabilities and get the cell with the max
        int size = openCells.size() - 1; //(-1) to account for the bot initially not being the leak
        double maxProb = 1/(double)size;
        for (Cell cell : openCells) {
            cell.setProbLeak(maxProb);
        }
        bot.setProbLeak(0);
        Cell maxProbCell;

        //Ship.printShip(ship);

        while (!bot.isLeak) {
            PriorityQueue<SimResult> simResults = new PriorityQueue<>();

            maxProb = 0.0;
            maxProbCell = null;
            for (Cell cell : openCells) {
                if (maxProb < cell.getProbLeak()) {
                    maxProb = cell.getProbLeak();
                    maxProbCell = cell;
                }
            }

            maxProb = 0.0;
            Cell maxProbCell_2 = null;
            for (Cell cell : openCells) {
                if (maxProb < cell.getProbLeak() && !cell.equals(maxProbCell)) {
                    maxProb = cell.getProbLeak();
                    maxProbCell_2 = cell;
                }
            }

            maxProb = 0.0;
            Cell maxProbCell_3 = null;
            for (Cell cell : openCells) {
                if (maxProb < cell.getProbLeak() && !cell.equals(maxProbCell) && !cell.equals(maxProbCell_2)) {
                    maxProb = cell.getProbLeak();
                    maxProbCell_3 = cell;
                }
            }

            if (maxProbCell != null)
                simResults.add(new SimResult(maxProbCell, runSimulations_Bot45(maxProbCell, leak)));
            if (maxProbCell_2 != null)
                simResults.add(new SimResult(maxProbCell_2, runSimulations_Bot45(maxProbCell_2, leak)));
            if (maxProbCell_3 != null)
                simResults.add(new SimResult(maxProbCell_3, runSimulations_Bot45(maxProbCell_3, leak)));

            Cell next = simResults.peek().cell;

            //BFS Shortest Path from bot -> cell with highest P(L)
            LinkedList<Cell> shortestPath = Bfs.SP_BFS(bot, next);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //System.out.println(numActions);

            //move the bot to next
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
            Bfs.updateDistances(bot);

            if (bot.isLeak) return;

            bot.noLeak = true;
            updateProb_Step(bot);

            //Sense Action
            if (bot.equals(next))
                probSenseAction(bot, leak);
        }
    }
    */
    public static void runBot4() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the leak
        randIndex = rand(0, openCells.size()-1);
        Cell leak = openCells.get(randIndex);
        leak.isLeak = true;

        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }
        bot.noLeak = true;

        //set initial probabilities and get the cell with the max
        int size = openCells.size() - 1; //(-1) to account for the bot initially not being the leak
        double maxProb = 1/(double)size;
        for (Cell cell : openCells) {
            cell.setProbLeak(maxProb);
        }
        bot.setProbLeak(0);
        Cell maxProbCell;

        boolean performSense = true;
        while (!bot.isLeak) {
            //BFS Shortest Path from bot -> cell with highest P(L)
            maxProb = 0.0;
            maxProbCell = null;
            for (Cell cell : openCells) {
                if (maxProb < cell.getProbLeak()) {
                    maxProb = cell.getProbLeak();
                    maxProbCell = cell;
                }
            }
            LinkedList<Cell> shortestPath = Bfs.SP_BFS(bot, maxProbCell);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //move the bot one step toward cell with highest P(L)
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
            Bfs.updateDistances(bot);

            if (bot.isLeak) return;

            bot.noLeak = true;
            updateProb_Step(bot);

            //Sense Action
            if (bot.equals(maxProbCell)) {
                if (performSense) probSenseAction(bot, leak);
                performSense = !performSense;
            }
        }
    }

    /**
     * Run simulations for Bot 45 --> pretend to go to the given maxProbCell and beep a given number of times
     * @return number of beeps out of numSimBeeps
     */
    private static int runSimulations_Bot45(Cell maxProbCell, Cell leak) {
        if (maxProbCell.isLeak) return numSimBeeps;

        //copy ship
        Cell[][] copyShip = Ship.copyShip(Main.ship);
        Cell copyMaxProbCell = copyShip[maxProbCell.getRow()][maxProbCell.getCol()];
        Cell copyLeak = copyShip[leak.getRow()][leak.getCol()];

        Bfs.updateDistances(copyMaxProbCell);
        copyMaxProbCell.setBeepProb(copyLeak);

        int numBeeps = 0;
        for (int i = 0; i < numSimBeeps; i++)
            if (Math.random() <= copyMaxProbCell.getBeepProb())
                numBeeps++;

        return numBeeps;
    }




    /**
     * Update P(L) for each cell after taking a step and set P(L) = 0 for bot
     */
    private static void updateProb_Step(Cell bot) {
        //update P(L) for each cell j to be P(leak in cell j | leak is not in bot)
        // --> conditional probability formula
        // --> marginalization
        // --> conditional factoring
        // --> = P(leak in cell j) / P(leak is not in bot)
        double denominator = 1 - bot.getProbLeak();

        //calculate and store the new values
        double[][] newValues = new double[Ship.D][Ship.D];
        for (Cell cell : openCells) {
            newValues[cell.getRow()][cell.getCol()] = cell.getProbLeak() / denominator;
        }

        //copy over the new values to the cells
        for (Cell cell : openCells) {
            cell.setProbLeak(newValues[cell.getRow()][cell.getCol()]);
        }

        bot.setProbLeak(0);
    }

    /**
     * Update P(L) for each cell after a sense action
     */
    private static void updateProb_Sense(double probB) {
        //calculate the denominator
        double denominator = 0.0;
        for (Cell cell : openCells) {
            denominator += cell.getProbLeak() * probB;
        }

        //calculate and store the new values
        double[][] newValues = new double[Ship.D][Ship.D];
        for (Cell cell : openCells) {
            newValues[cell.getRow()][cell.getCol()] = (cell.getProbLeak() * probB) / denominator;
        }

        //copy over the new values to the cells
        for (Cell cell : openCells) {
            cell.setProbLeak(newValues[cell.getRow()][cell.getCol()]);
        }
    }

    /**
     * Sense the leak and update based on whether a beep was heard
     */
    private static void probSenseAction(Cell bot, Cell leak) {
        //sense action
        bot.setBeepProb(leak);
        boolean beep = Math.random() <= bot.getBeepProb(); //true if beep occurred
        numActions++;

        //update P(L) for each cell j to be P(leak in cell j | B/~B in bot cell)
        // --> conditional probability formula
        // --> marginalization
        // --> conditional factoring
        // --> = P(L in j)*P(B/~B in bot | L in leak cell) / summation{P(L in j)*P(B/~B in bot | L in leak cell)}
        if (beep) {
            updateProb_Sense(bot.getBeepProb());
        }
        else {
            updateProb_Sense(1 - bot.getBeepProb());
        }
    }
}