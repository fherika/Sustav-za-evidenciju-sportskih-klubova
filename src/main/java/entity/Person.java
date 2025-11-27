package entity;
/**
 * Predstavlja osobu.
 * Sadrži osnovne podatke kao što su ime, prezime i dob.
 * Sve konkretne osobe moraju implementirati metode za prikaz statistika i dob.
 */
public abstract class Person implements Trainable{
    protected String name;
    protected String surname;
    protected Integer age;
    /**
     * Konstruktor za kreiranje osobe.
     *
     * @param name Ime osobe
     * @param surname Prezime osobe
     * @param age Dob osobe
     */
    public Person(String name, String surname, Integer age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }
    /**
     * Apstraktna metoda koja vraća statistike osobe u obliku Stringa.
     *
     * @return Statistike osobe
     */
    public abstract String showStats();
    /**
     * Apstraktna metoda koja vraća dob osobe.
     *
     * @return Dob osobe
     */
    public abstract Integer getAge();
}
