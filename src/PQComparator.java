import java.util.Comparator;

public class PQComparator implements Comparator<Cell> {
    @Override
    public int compare(Cell c1, Cell c2) {
        return Double.compare(c1.getProbLeak(), c2.getProbLeak());
    }
}
