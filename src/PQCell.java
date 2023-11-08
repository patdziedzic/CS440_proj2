public class PQCell implements Comparable<PQCell> {
    public Cell cell;
    public int dist;

    public PQCell(Cell cell, int dist) {
        this.cell = cell;
        this.dist = dist;
    }

    @Override
    public int compareTo(PQCell pqCell) {
        return Double.compare(this.dist, pqCell.dist);
    }
}