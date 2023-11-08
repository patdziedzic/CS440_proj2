import java.util.LinkedList;

/**
 * Class for Multiple Leaks Bots (5-9)
 */
public class MultipleLeaksBots extends DeterministicBots {

    /*
     * Run an experiment on Bot 5
     */
    public static void runBot5() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;
        LinkedList<Cell> detSquare = getDetectionSquare(bot);

        //initialize leak1
        Cell leak1 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak1 == null) {
            randIndex = rand(0, openCells.size() - 1);
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
        
        //initialize leak2
        Cell leak2 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak2 == null) {
            randIndex = rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);
            if (!tempLeak.isLeak) {
                boolean found = false;
                for (Cell cell : detSquare) {
                    if (cell.equals(tempLeak)) {
                        found = true;
                        break;
                    }
                }
                if (!found) leak2 = tempLeak;
            }
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
     * Run an experiment for Bot 6
     */
    public static void runBot6() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;
        LinkedList<Cell> detSquare = getDetectionSquare(bot);

        //initialize leak1
        Cell leak1 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak1 == null) {
            randIndex = rand(0, openCells.size() - 1);
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
        
        //initialize leak2
        Cell leak2 = null;
        //the leak must initially be placed in a random open cell outside of the detection square
        while (leak2 == null) {
            randIndex = rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);
            if (!tempLeak.isLeak) {
                boolean found = false;
                for (Cell cell : detSquare) {
                    if (cell.equals(tempLeak)) {
                        found = true;
                        break;
                    }
                }
                if (!found) leak2 = tempLeak;
            }
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





    /**
     * Run an experiment for Bot 7
     * Modeled after Bot 3; admittedly incorrect probabilities
     */
    public static void runBot7() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize leak1
        randIndex = rand(0, openCells.size()-1);
        Cell leak1 = openCells.get(randIndex);
        leak1.isLeak = true;
        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }

        //initialize leak2
        Cell leak2 = null;
        while (leak2 == null) {
            randIndex = rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);
            if (!tempLeak.isLeak) leak2 = tempLeak;
        }
        leak2.isLeak = true;
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

        while (!bot.isLeak && (leak1.isLeak || leak2.isLeak)) {
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

            //if bot reached one of the leaks and both have not been plugged
            if (bot.isLeak && leak1.isLeak && leak2.isLeak) {
                bot.isLeak = false;
                bot.noLeak = true;
            }
            //else if bot reached one of the leaks and the other has been plugged
            else if (bot.isLeak && (leak1.isLeak ^ leak2.isLeak)) return;
            else bot.noLeak = true;

            bot.setProbLeak(0);
            updateProb_Step_Bot7(bot);

            //Sense Action
            if (leak1.isLeak && leak2.isLeak)
                probSenseAction_Bot7(bot, leak1);
            else
                probSenseAction_Bot7(bot, leak2);
        }
    }

    /**
     * Update P(L) for each cell after taking a step
     */
    private static void updateProb_Step_Bot7(Cell bot) {
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
    private static void updateProb_Sense_Bot7(double probB) {
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
    private static void probSenseAction_Bot7(Cell bot, Cell leak) {
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
            updateProb_Sense_Bot7(bot.getBeepProb());
        }
        else {
            updateProb_Sense_Bot7(1 - bot.getBeepProb());
        }
    }


    /**
     * Run an experiment for Bot 8
     * Modeled after Bot 7 but adjusted probability calculations
     */
    public static void runBot8() {
        //initialize the bot
        int randIndex = rand(0, openCells.size()-1);
        Cell bot = openCells.get(randIndex);
        bot.isBot = true;

        //initialize leak1
        randIndex = rand(0, openCells.size()-1);
        Cell leak1 = openCells.get(randIndex);
        leak1.isLeak = true;
        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }

        //initialize leak2
        Cell leak2 = null;
        while (leak2 == null) {
            randIndex = rand(0, openCells.size() - 1);
            Cell tempLeak = openCells.get(randIndex);
            if (!tempLeak.isLeak) leak2 = tempLeak;
        }
        leak2.isLeak = true;
        //if the leak spawns on the bot, ignore test case
        if (bot.isLeak) {
            numActions = null;
            return;
        }

        bot.noLeak = true;

        //set initial probabilities and get the cell with the max
        int size = openCells.size();
        double maxProbLeakPair = Math.pow(1/(double)size, 2);
        //^ initially, P(L in j, L in k) = P(L in j)*P(L in k) = 1/numOpenCells squared.

        LinkedList<Pairing> pairings = new LinkedList<>();
        for (int i = 0; i < openCells.size(); i++) {
            Cell c1 = openCells.get(i);
            for (int j = i + 1; j < openCells.size(); j++) {
                Cell c2 = openCells.get(j);
                pairings.add(new Pairing(c1, c2, maxProbLeakPair));
            }
        }

        //what about setting it tho? iterate through LL every time I guess? *************** LEFT OFF HERE
        bot.setProbLeak(0);
        Cell maxProbCell;

        //Ship.printShip(ship);

        while (!bot.isLeak && (leak1.isLeak || leak2.isLeak)) {
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

            //if bot reached one of the leaks and both have not been plugged
            if (bot.isLeak && leak1.isLeak && leak2.isLeak) {
                bot.isLeak = false;
                bot.noLeak = true;
            }
            //else if bot reached one of the leaks and the other has been plugged
            else if (bot.isLeak && (leak1.isLeak ^ leak2.isLeak)) return;
            else bot.noLeak = true;

            bot.setProbLeak(0);
            updateProb_Step_Bot8(bot);

            //Sense Action
            if (leak1.isLeak && leak2.isLeak)
                probSenseAction_Bot8(bot, leak1);
            else
                probSenseAction_Bot8(bot, leak2);
        }
    }

    /**
     * Update P(L) for each cell after taking a step
     */
    private static void updateProb_Step_Bot8(Cell bot) {
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
    private static void updateProb_Sense_Bot8(double probB) {
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
    private static void probSenseAction_Bot8(Cell bot, Cell leak) {
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
            updateProb_Sense_Bot8(bot.getBeepProb());
        }
        else {
            updateProb_Sense_Bot8(1 - bot.getBeepProb());
        }
    }
}