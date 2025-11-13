package entity;

/**
 * Checked iznimka koja se baca kada se pokuša dodati klub s imenom koje već postoji u sustavu.
 */
public class DuplicateClubException extends Exception {
    /**
     * Konstruktor za stvaranje nove DuplicateClubException s određenom porukom.
     *
     * @param message opis greške koji se prikazuje
     */
    public DuplicateClubException(String message) {
        super(message);
    }
}
