public enum Region {
    Minima {
        @Override
        public boolean compare(int lhs, int rhs) {
            return (lhs < rhs);
        }
    }, 

    Maxima {
        @Override
        public boolean compare(int lhs, int rhs) {
            return (lhs > rhs);
        }
    };

    public abstract boolean compare(int lhs, int rhs);
}