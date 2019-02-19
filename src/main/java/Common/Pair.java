package Common;

public class Pair<F, S> {
    private F first;
    private S second;

    public Pair(F f, S s) {
        first = f;
        second = s;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
}
