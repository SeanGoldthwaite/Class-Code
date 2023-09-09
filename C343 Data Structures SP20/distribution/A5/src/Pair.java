class Pair<A,B> {
  private A fst;
  private B snd;

  Pair (A fst, B snd) {
      this.fst = fst;
      this.snd = snd;
  }

  A getFst () { return fst; }
  B getSnd () { return snd; }

  public boolean equals (Object o) {
      if (o instanceof Pair) {
        Pair<A,B> other = (Pair) o;
        return fst.equals(other.fst) && snd.equals(other.snd);
      }
      return false;
  }

  public int hashCode () {
      return fst.hashCode() + snd.hashCode();
  }

  public String toString () {
      return String.format("[%s,%s]", fst.toString(), snd.toString());

  }
}

