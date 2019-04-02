package CanbanBoard;

public enum Priority {
    LOW{
        @Override
        public String toString() {
            return "Low";
        }
    },
    MODERATE{
        @Override
        public String toString() {
            return "Moderate";
        }
    },
    HIGH{
        @Override
        public String toString() {
            return "High";
        }
    },
    EXTREME{
        @Override
        public String toString() {
            return "Extreme";
        }
    }
}
