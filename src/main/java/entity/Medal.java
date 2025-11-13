package entity;

/**
 * Predstavlja medalju kao nagradu.
 * Klasa je finalna.
 */
public final class Medal implements Award {
    private final String type;

    /**
     * Privatni konstruktor za stvaranje medalje određenog tipa.
     *
     * @param type tip medalje (npr. "Gold medal")
     */
    private Medal(String type) {
        this.type = type;
    }
    /**
     * Stvara zlatnu medalju.
     *
     * @return nova instanca zlatne medalje
     */
    public static Medal gold() {
        return new Medal("Gold medal");
    }
    /**
     * Stvara srebrnu medalju.
     *
     * @return nova instanca srebrne medalje
     */
    public static Medal silver() {
        return new Medal("Silver medal");
    }
    /**
     * Stvara brončanu medalju.
     *
     * @return nova instanca brončane medalje
     */
    public static Medal bronze() {
        return new Medal("Bronze medal");
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
