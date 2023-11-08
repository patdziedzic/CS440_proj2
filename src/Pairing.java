public class Pairing {
    public Cell c1;
    public Cell c2;
    public double probLeakPair;

    public Pairing(Cell c1, Cell c2, double probLeakPair) {
        this.c1 = c1;
        this.c2 = c2;
        this.probLeakPair = probLeakPair;
    }

    public Pairing(Cell c1, Cell c2) {
        this.c1 = c1;
        this.c2 = c2;
        probLeakPair = 0.0;
    }

    /**
     * Two Pairings are equal if their cells have the same row and col
     * @param obj Pairing object
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pairing){
            Pairing pair = (Pairing) obj;
            if (this.c1.equals(pair.c1) || this.c1.equals(pair.c2))
                return this.c2.equals(pair.c1) || this.c2.equals(pair.c2);
        }
        return false;
    }

    /**
     * Implement own hashcode for HashMap.
     */
    @Override
    public int hashCode() {
        return (c1.getRow() * Ship.D + c1.getCol()) * Ship.D +
                (c2.getRow() * Ship.D + c2.getCol());
    }
}
