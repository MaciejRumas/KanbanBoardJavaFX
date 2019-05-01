package CanbanBoard;


import java.time.LocalDate;

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
    EXTREME {
        @Override
        public String toString() {
            return "Extreme";
        }
    };

    public static Priority fromString(String val){
        Priority priority;
        switch (val){
            case "High":
                priority = HIGH;
                break;
            case "Moderate":
                priority = MODERATE;
                break;
            case "Low":
                priority = LOW;
                break;
            case "Extreme":
                priority = EXTREME;
                break;
                default:
                    priority = null;
        }
        return priority;
    }

}
