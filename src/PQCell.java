public class PQCell implements Comparable<PQCell> {
    public Cell cell;
    public int heuristic; //number of wins after running simulations

    public PQCell(Cell cell, int heuristic) {
        this.cell = cell;
        this.heuristic = heuristic;
    }

    @Override
    public int compareTo(PQCell pqCell) {
        return Double.compare(this.heuristic, pqCell.heuristic);
    }
}
