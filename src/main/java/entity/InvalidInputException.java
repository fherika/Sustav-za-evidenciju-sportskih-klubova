package entity;

/**
 * Unchecked iznimka koja se baca kada korisnik unese neispravan ili prazni podatak.
 */
public class InvalidInputException extends RuntimeException{
    /**
     * Konstruktor za stvaranje nove InvalidInputException s određenom porukom.
     *
     * @param message opis greške koji se prikazuje
     */
    public InvalidInputException(String message){
        super(message);
    }
}
