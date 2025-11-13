package entity;

/**
 * Checked iznimka koja se baca kada se pokuša dodati slobodnog agenta, a dostignut je maksimalni broj dopuštenih free agenat-a.
 */
public class FreeAgentLimitException extends Exception {
    /**
     * Konstruktor za stvaranje nove FreeAgentLimitException s određenom porukom.
     *
     * @param message opis greške koji se prikazuje
     */
    public FreeAgentLimitException(String message) {
        super(message);
    }
}
