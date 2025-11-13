package entity;
/**
 * Definira mogućnost treniranja.
 * Klase koje implementiraju ovo sučelje moraju definirati
 * kako se trenira objekt (npr. trener ili igrač).
 */
public interface Trainable {
    /**
     * Pokreće proces treniranja.
     * Konkretna implementacija ovisi o tome ako je objekt Coach ili Player
     */
    void train();
}
