import java.util.ArrayList;

public class Ship {
    //the ship layout - 2D array of Cell objects
    public static final int D = 100;
    public static Cell[][] ship;
    public static Cell initial;


    /**
     * Make the ship, including opening dead ends
     */
    public static Cell[][] makeShip() {
        //ship is a D x D 2D array of Cell objects
        ship = new Cell[D][D];
        for (int r = 0; r < D; r++) {
            for (int c = 0; c < D; c++) {
                ship[r][c] = new Cell(r, c);
            }
        }

        //open the initial cell
        int row = Main.rand(0, D-1);
        int col = Main.rand(0, D-1);
        initial = openCell(ship[row][col]);

        openBlockedCandidates();
        openDeadEnds();

        for (int r = 0; r < D; r++) {
            for (int c = 0; c < D; c++) {
                ship[r][c].setNeighbors(ship);
            }
        }
        
        return ship;
    }

    /**
     * Randomly open blocked candidates to make the ship
     */
    private static void openBlockedCandidates() {
        //populating blocked candidates
        ArrayList<Cell> blockedCandidates = new ArrayList<>(); //blocked cells with one open neighbor
        for (int r = 0; r < D; r++) {
            for (int c = 0; c < D; c++) {
                addBlockedCandidate(ship[r][c], blockedCandidates);
            }
        }

        //opening blocked candidates
        while (!blockedCandidates.isEmpty()) {
            int randomIndex = Main.rand(0, blockedCandidates.size() - 1);
            Cell randomBlocked = blockedCandidates.get(randomIndex);
            openCell(randomBlocked);
            updateBlockedArrayList(blockedCandidates);

            //Check neighbors - if one is now a candidate, then add it
            //up
            try {
                addBlockedCandidate(ship[randomBlocked.getRow() - 1][randomBlocked.getCol()], blockedCandidates);
            } catch (ArrayIndexOutOfBoundsException ignore) { }
            //down
            try {
                addBlockedCandidate(ship[randomBlocked.getRow() + 1][randomBlocked.getCol()], blockedCandidates);
            } catch (ArrayIndexOutOfBoundsException ignore) { }
            //left
            try {
                addBlockedCandidate(ship[randomBlocked.getRow()][randomBlocked.getCol() - 1], blockedCandidates);
            } catch (ArrayIndexOutOfBoundsException ignore) { }
            //right
            try {
                addBlockedCandidate(ship[randomBlocked.getRow()][randomBlocked.getCol() + 1], blockedCandidates);
            } catch (ArrayIndexOutOfBoundsException ignore) { }

            updateBlockedArrayList(blockedCandidates);
        }
    }

    /**
     * Add a blocked candidate to the list of blocked candidates
     */
    private static void addBlockedCandidate(Cell cell, ArrayList<Cell> a) {
        if (!cell.isOpen && cell.numOpenNeighbors == 1) {
            a.add(cell);
        }
    }

    /**
     * Randomly open about 50% of the dead ends
     */
    private static void openDeadEnds() {
        ArrayList<Cell> deadEnds = new ArrayList<>();
        for (int r = 0; r < D; r++) {
            for (int c = 0; c < D; c++) {
                if (ship[r][c].isOpen && ship[r][c].numOpenNeighbors == 1) {
                    deadEnds.add(ship[r][c]);
                }
            }
        }

        int threshold = deadEnds.size() / 2;

        //opening dead ends
        while (deadEnds.size() > threshold) {
            int randomIndex = Main.rand(0, deadEnds.size()-1);
            Cell randomDeadEnd = deadEnds.get(randomIndex);
            ArrayList<Cell> blockedNeighbors = new ArrayList<>();
            try {
                if(!ship[randomDeadEnd.getRow() - 1][randomDeadEnd.getCol()].isOpen)
                    blockedNeighbors.add(ship[randomDeadEnd.getRow() - 1][randomDeadEnd.getCol()]);
            } catch (ArrayIndexOutOfBoundsException ignore) { }
            try {
                if(!ship[randomDeadEnd.getRow() + 1][randomDeadEnd.getCol()].isOpen)
                    blockedNeighbors.add(ship[randomDeadEnd.getRow() + 1][randomDeadEnd.getCol()]);
            } catch (ArrayIndexOutOfBoundsException ignore) { }
            try {
                if(!ship[randomDeadEnd.getRow()][randomDeadEnd.getCol()-1].isOpen)
                    blockedNeighbors.add(ship[randomDeadEnd.getRow()][randomDeadEnd.getCol()-1]);
            } catch (ArrayIndexOutOfBoundsException ignore) { }
            try {
                if(!ship[randomDeadEnd.getRow()][randomDeadEnd.getCol()+1].isOpen)
                    blockedNeighbors.add(ship[randomDeadEnd.getRow()][randomDeadEnd.getCol()+1]);
            } catch (ArrayIndexOutOfBoundsException ignore) { }

            randomIndex = Main.rand(0, blockedNeighbors.size()-1);
            if(!blockedNeighbors.isEmpty()) {
                Cell randomNeighbor = blockedNeighbors.get(randomIndex);
                openCell(randomNeighbor);
            }

            updateDeadEndsArrayList(deadEnds);
        }
    }

    /**
     * Updates blockedCandidates
     */
    private static void updateBlockedArrayList(ArrayList<Cell> a) {
        for (int i = 0; i < a.size(); i++) {
            Cell c = a.get(i);
            if (c.isOpen || c.numOpenNeighbors > 1)
                a.remove(c);
        }
    }

    /**
     * Updates deadEnds
     */
    private static void updateDeadEndsArrayList(ArrayList<Cell> a) {
        for (int i = 0; i < a.size(); i++) {
            Cell c = a.get(i);
            if (c.numOpenNeighbors > 1)
                a.remove(c);
        }
    }

    /**
     * Opens a cell
     */
    private static Cell openCell(Cell cell) {
        //open the given cell
        cell.isOpen = true;

        //Set neighbors' numOpenNeighbors
        //up
        changeNumOpenNeighbors(cell.getRow() - 1, cell.getCol());
        //down
        changeNumOpenNeighbors(cell.getRow() + 1, cell.getCol());
        //left
        changeNumOpenNeighbors(cell.getRow(), cell.getCol() - 1);
        //right
        changeNumOpenNeighbors(cell.getRow(), cell.getCol() + 1);

        return cell;
    }

    /**
     * Increment the number of open neighbors for the newly opened cell
     */
    private static void changeNumOpenNeighbors(int row, int col) {
        try {
            ship[row][col].incNumOpenNeighbors();
        } catch (ArrayIndexOutOfBoundsException ignore) { }
    }
}
