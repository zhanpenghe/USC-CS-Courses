public class Point {
    short i;
    short j;

    public Point(short i, short j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public int hashCode() {
        int result = (int) i;
        result = 31 * result + (int) j;
        return result;
    }
}
