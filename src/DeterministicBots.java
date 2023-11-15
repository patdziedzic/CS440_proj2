import java.util.LinkedList;

/**
 * Class for Deterministic Bots (1 and 2)
 */
public class DeterministicBots extends Main {

    /**
     * Run an experiment for Bot 1
     * Update numActions taken (moves + sensing) to plug the leak
     */
    public static void runBot1() {
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
     * Run an experiment for Bot 2
     */
    public static void runBot2() {
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
            if (shortestPath == null) {
                numActions = null;
                return;
            }
            shortestPath.removeFirst();

            //move the bot one step to the nearest potential leak
            Cell neighbor = shortestPath.removeFirst();
            bot.isBot = false;
            neighbor.isBot = true;
            bot = neighbor;
            numActions++;
            if (bot.isLeak) return;
            else bot.noLeak = true;

            //Sense Action
            detSenseAction(bot);
        }
    }



    /**
     * Deterministic Sense Action (update cells accordingly)
     * @return true if leak was detected
     */
    public static boolean detSenseAction(Cell bot) {
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
    public static LinkedList<Cell> getDetectionSquare(Cell bot) {
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
    public static boolean leakInSquare(LinkedList<Cell> detSquare) {
        for (Cell cell : detSquare) {
            if (cell.isLeak) return true;
        }
        return false;
    }

}