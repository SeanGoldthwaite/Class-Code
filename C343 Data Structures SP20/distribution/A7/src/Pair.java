public class Pair<A,B> {
    private A a;
    private B b;

    Pair (A a, B b) { this.a = a; this.b = b; }

    A getFirst() { return a; }
    B getSecond() { return b; }
}
