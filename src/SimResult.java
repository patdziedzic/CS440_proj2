public class SimResult implements Comparable<SimResult>{
    public Cell cell;
    public int result;

    public SimResult(Cell cell, int result) {
        this.cell = cell;
        this.result = result;
    }

    @Override
    public int compareTo(SimResult simResult) {
        return Integer.compare(this.result, simResult.result);
    }
}
