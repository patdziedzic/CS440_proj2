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
    public boolean isVisited;

    //IDENTITY
    public boolean isBot;
    public boolean isButton;

    //FLAMMABILITY
    private boolean onFire;
    public double flammability; //flammability of the cell based on neighbors
    public int k; //number of fire neighbors

    /**
     * Cell Constructor given row and col
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.numOpenNeighbors = 0;
        this.isOpen = false;
        this.neighbors = new LinkedList<>();
        this.onFire = false;
        this.k = 0;
        this.flammability = 1-Math.pow((1-Main.q),k);
        this.isBot = false;
        this.isButton = false;
        this.isVisited = false;
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
            this.isVisited = cell.isVisited;
            this.isBot = cell.isBot;
            this.isButton = cell.isButton;
            this.onFire = cell.getOnFire();
            this.flammability = cell.flammability;
            this.k = cell.k;
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

    /**
     * Increment k (the number of fire neighbors) by 1 and adjust flammability
     */
    public void incK() {
        k++;
        flammability = 1 - Math.pow((1 - Main.q), k);
    }

    public boolean getOnFire() {
        return onFire;
    }

    /**
     * If set cell on fire to true, update the neighbors automatically
     * @param value the
     */
    public void setOnFire(boolean value) {
        onFire = value;
        if (value) {
            if(up != null)
                up.incK();
            if(down != null)
                down.incK();
            if(left != null)
                left.incK();
            if(right != null)
                right.incK();
        }
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
