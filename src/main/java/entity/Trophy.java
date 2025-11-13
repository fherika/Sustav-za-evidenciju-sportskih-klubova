package entity;
/**
 * Predstavlja trofej kao nagradu.
 */
public final class Trophy implements Award {
    private final String type;
    /**
     * Privatni konstruktor za stvaranje trofeja određenog tipa.
     *
     * @param type tip trofeja (npr. "Cup trophy")
     */
    private Trophy(String type) {
        this.type = type;
    }
    /**
     * Stvara trofej kupa.
     *
     * @return nova instanca trofeja kupa
     */
    public static Trophy cup() {
        return new Trophy("Cup trophy");
    }
    /**
     * Stvara trofej lige.
     *
     * @return nova instanca trofeja lige
     */
    public static Trophy league() {
        return new Trophy("League trophy");
    }
    @Override
    public String displayAward() {
        return "Award: "+type;
    }

    @Override
    public String toString(){
        return type;
    }
}
