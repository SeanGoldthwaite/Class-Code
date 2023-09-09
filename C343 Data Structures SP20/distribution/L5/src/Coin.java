class Coin {
    private int value;

    Coin (int value) {
        this.value = value;
    }

    int getValue () { return value; }

    public String toString () { return String.format("<%d>", value); }
}

