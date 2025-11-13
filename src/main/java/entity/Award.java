package entity;

/**
 * Reprezentira nagradu koja se može dodijeliti klubu.
 *
 */
public sealed interface Award permits Medal, Trophy{
    /**
     * Vraća opis nagrade koji se prikazuje korisniku.
     *
     * @return String koji opisuje nagradu
     */
    public String displayAward();
    /**
     * Vraća tekstualni prikaz nagrade.
     *
     * @return String koji predstavlja nagradu
     */
    public String toString();
}
