package entity;

/**
 * Reprezentira zapis o svim nagradama svih klubova.
 *
 * Sadrži osnovne informacije o nagradi: ime kluba, tip nagrade i datum dodjele.
 *
 * @param name  ime kluba kojem je nagrada dodijeljena
 * @param type  tip nagrade (medalja ili trofej)
 * @param date  datum kada je nagrada dodijeljena u formatu YYYY-MM-DD
 */
public record AwardRecord(String name, String type, String date) {

}
