import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Class for Probabilistic Bots (3 and 4)
 */
public class ProbabilisticBots extends Main {
    //private static PriorityQueue<Cell> pqProb = new PriorityQueue<>(new PQComparator());
    //^ priority queue of cells to compare P(L) for each

    /**
     * Run an experiment for Bot 3
     * Update numActions taken (moves + sensing) to plug the leak
     */
    public static void runBot3() {
        //initialize the bot
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the leak
        randIndex = Main.rand(0, openCells.size()-1);
        Cell leak = openCells.get(randIndex);
        leak.isLeak = true;

        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }
        bot.noLeak = true;

        //set initial probabilities and get the cell with the max
        int size = openCells.size();
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

            //System.out.println(numActions);

            //move the bot one step toward cell with highest P(L)
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
            Bfs.updateDistances(bot);

            if (bot.isLeak) return;

            bot.noLeak = true;
            bot.setProbLeak(0);
            updateProb_Step(bot);

            //Sense Action
            probSenseAction(bot, leak);
        }
    }

    /**
     * Run an experiment for Bot 4
     * Update numActions taken (moves + sensing) to plug the leak
     */
    public static void runBot4() {
        //initialize the bot
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize the leak
        randIndex = Main.rand(0, openCells.size()-1);
        Cell leak = openCells.get(randIndex);
        leak.isLeak = true;

        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }
        bot.noLeak = true;

        //set initial probabilities and get the cell with the max
        int size = openCells.size();
        double maxProb = 1/(double)size;
        for (Cell cell : openCells) {
            cell.setProbLeak(maxProb);
        }
        Cell maxProbCell = bot.neighbors.peek(); //randomly get a neighbor to go to

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

            //System.out.println(numActions);

            //move the bot one step toward cell with highest P(L)
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
            Bfs.updateDistances(bot);

            if (bot.isLeak) return;

            bot.noLeak = true;
            bot.setProbLeak(0);
            updateProb_Step(bot);

            //Sense Action (looping now for bot 4)
            int numNonLeak = 0;
            for (Cell cell: openCells)
                if (cell.noLeak) numNonLeak++;

            double percentChecked = numNonLeak/(double)openCells.size(); //high = majority has been checked
            //0.25 < p < 0.75 --> looping when 25 to 75 percent is checked

            if (percentChecked > 0.2 && percentChecked < 0.5) {
                for (int i = 0; i < 3; i++)
                    probSenseAction(bot, leak);
            }
            else {
                probSenseAction(bot, leak);
            }
        }
    }

    /**
     * Update P(L) for each cell after taking a step
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