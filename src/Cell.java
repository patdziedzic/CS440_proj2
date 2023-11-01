import java.util.LinkedList;

public class Cell {
    //COORDINATES
    private int row;
    private int col;

    //OPENNESS
    public int numOpenNeighbors; //increment for each new open neighbor
    public boolean isOpen;

    //REACHING NEIGHBORS OF CELLS
    public Cell up;
    public Cell down;
    public Cell left;
    public Cell right;
    public LinkedList<Cell> neighbors;


    //IDENTITY
    public boolean isBot;
    public boolean isLeak;
    public boolean noLeak; //true if there is definitely no leak, false if there might be a leak
    public int distFromBot;

    //PROBABILITIES
    private double probLeak; //P(Leak) which is updated to P(L|B) or P(L|~B) each sense action
    private double beepProb; //P(Beep in cell i | Leak in cell j)


    /**
     * Cell Constructor given row and col
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.numOpenNeighbors = 0;
        this.isOpen = false;
        this.neighbors = new LinkedList<>();
        this.isBot = false;
        this.isLeak = false;
        this.noLeak = false;
        this.probLeak = 0.0;
        this.beepProb = 0.0;
    }

    /**
     * Copy constructor using the given Cell
     */
    public Cell(Cell cell) {
        if (cell != null) {
            this.row = cell.getRow();
            this.col = cell.getCol();
            this.numOpenNeighbors = cell.numOpenNeighbors;
            this.isOpen = cell.isOpen;
            this.up = null;
            this.down = null;
            this.left = null;
            this.right = null;
            this.neighbors = new LinkedList<>();
            this.isBot = cell.isBot;
            this.isLeak = cell.isLeak;
            this.noLeak = cell.noLeak;
            this.probLeak = cell.probLeak;
            this.beepProb = cell.beepProb;
        }
    }

    public void incNumOpenNeighbors() {
        this.numOpenNeighbors++;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Set the neighbors of this cell and add them to the list of neighbors
     */
    public void setNeighbors(Cell[][] ship) {
        try {
            up = ship[row-1][col];
            if (up != null) neighbors.add(up);
        } catch (ArrayIndexOutOfBoundsException ignore){}
        try {
            down = ship[row+1][col];
            if (down != null) neighbors.add(down);
        } catch (ArrayIndexOutOfBoundsException ignore){}
        try {
            left = ship[row][col-1];
            if (left != null) neighbors.add(left);
        } catch (ArrayIndexOutOfBoundsException ignore){}
        try {
            right = ship[row][col+1];
            if (right != null) neighbors.add(right);
        } catch (ArrayIndexOutOfBoundsException ignore){}
    }

    public double getProbLeak() {
        return probLeak;
    }

    public void setProbLeak(double probLeak) {
        this.probLeak = probLeak;
    }

    public double getBeepProb() {
        return beepProb;
    }

    //P(B|L) = e ^ [ -alpha * (d(i,j) - 1) ]
    public void setBeepProb(Cell leak) {
        this.beepProb = 1 / Math.exp(Main.alpha * (leak.distFromBot - 1));
    }




    /**
     * Two Cells are equal if they have the same row and col
     * @param obj Cell object
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell){
            Cell c = (Cell) obj;
            return c.row == this.row && c.col == this.col;
        }
        return false;
    }
}
