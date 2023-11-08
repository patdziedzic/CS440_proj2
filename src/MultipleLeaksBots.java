import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Class for Multiple Leaks Bots (5-9)
 */
public class MultipleLeaksBots extends Main {

    /*
     * Run an experiment on bot 5
     */
    public static void runBot5() {
        //initialize the bot
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;
        LinkedList<Cell> detSquare = getDetectionSquare(bot);

        //initialize the leak1
        Cell leak1 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak1 == null) {
            randIndex = Main.rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);

            boolean found = false;
            for (Cell cell : detSquare) {
                if (cell.equals(tempLeak)) {
                    found = true;
                    break;
                }
            }
            if (!found) leak1 = tempLeak;
        }
        leak1.isLeak = true;
        
        //initialization of leak2
        Cell leak2 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak2 == null) {
            randIndex = Main.rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);

            boolean found = false;
            for (Cell cell : detSquare) {
                if (cell.equals(tempLeak)) {
                    found = true;
                    break;
                }
            }
            if (!found) leak2 = tempLeak;
        }
        leak2.isLeak = true;

        bot.noLeak = true;
        for (Cell cell : detSquare) cell.noLeak = true;

        //Ship.printShip(ship);

        while (!bot.isLeak && (leak1.isLeak || leak2.isLeak)) {
            //BFS Shortest Path from bot -> nearest potential leak
            LinkedList<Cell> shortestPath = Bfs.detSP_BFS(bot);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //System.out.println(numActions);

            //move the bot to the nearest potential leak
            bot = moveBot(bot, shortestPath);
            //if bot reached one of the leaks and both have not been plugged
            if (bot.isLeak && leak1.isLeak && leak2.isLeak) {
                bot.isLeak = false;
                bot.noLeak = true;
            }
            //else if bot reached one of the leaks and the other has been plugged
            else if (bot.isLeak && (leak1.isLeak ^ leak2.isLeak)) return;
            else bot.noLeak = true;

            //Sense Action
            detSenseAction(bot);
        }
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
     * Run an experiment for Bot 6
     * Must make changes to the leaks (framework in bot 5)
     */
    public static void runBot6() {
        //initialize the bot
        int randIndex = Main.rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;
        LinkedList<Cell> detSquare = getDetectionSquare(bot);

        //initialize the leak1
        Cell leak1 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak1 == null) {
            randIndex = Main.rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);

            boolean found = false;
            for (Cell cell : detSquare) {
                if (cell.equals(tempLeak)) {
                    found = true;
                    break;
                }
            }
            if (!found) leak1 = tempLeak;
        }
        leak1.isLeak = true;
        
        //initialization of leak2
        Cell leak2 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak2 == null) {
            randIndex = Main.rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);

            boolean found = false;
            for (Cell cell : detSquare) {
                if (cell.equals(tempLeak)) {
                    found = true;
                    break;
                }
            }
            if (!found) leak2 = tempLeak;
        }
        leak2.isLeak = true;

        bot.noLeak = true;
        for (Cell cell : detSquare) cell.noLeak = true;

        //Ship.printShip(ship);

        while (!bot.isLeak && (leak1.isLeak || leak2.isLeak)) {
            //BFS Shortest Path from bot -> nearest potential leak
            LinkedList<Cell> shortestPath = Bfs.detSP_BFS(bot);
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //System.out.println(numActions);

            //move the bot one step to the nearest potential leak
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;

            //if bot reached one of the leaks and both have not been plugged
            if (bot.isLeak && leak1.isLeak && leak2.isLeak) {
                bot.isLeak = false;
                bot.noLeak = true;
            }
            //else if bot reached one of the leaks and the other has been plugged
            else if (bot.isLeak && (leak1.isLeak ^ leak2.isLeak)) return;
            else bot.noLeak = true;

            //Sense Action
            detSenseAction(bot);
        }
    }
}