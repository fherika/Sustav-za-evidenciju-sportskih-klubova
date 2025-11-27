package entity;

public enum Sport {
    FOOTBALL("Football"),
    BASKETBALL("Basketball"),
    HANDBALL("Handball");

    private final String displayName;

    private Sport(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Sport fromString(String input) {
        for (Sport s : Sport.values()) {
            if (s.displayName.equalsIgnoreCase(input.trim())) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid sport: " + input);
    }
}
