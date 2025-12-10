package entity;

import java.io.Serializable;

/**
 * Predstavlja trenera.
 */
public class Coach extends Person implements Trainable, Serializable {
    private String type = "Coach";
    private Integer experience;

    public Coach(){}
    /**
     * Konstruktor za stvaranje novog trenera.
     *
     * @param name ime trenera
     * @param surname prezime trenera
     * @param experience broj godina iskustva u trenerskom radu
     * @param age dob trenera
     */
    public Coach(String name, String surname, Integer experience, Integer age) {
        super(name, surname, age);
        this.experience = experience;
    }

    /**
     * Prikazuje osnovne informacije o treneru uključujući ime, prezime, dob i iskustvo.
     *
     * @return string s informacijama o treneru
     */
    @Override
    public String showStats(){
        return name+ " " + surname + ", "+ age+" years old, has been coach for " + experience + " years";
    }

    /**
     * Provodi trening koji trener izvodi.
     */
    @Override
    public void train(){
        System.out.println(name + " " + surname + " is conducting training!\n");
    }

    /**
     * Dohvaća dob trenera.
     *
     * @return dob trenera
     */
    @Override
    public Integer getAge(){
        return age;
    }
    public void setAge(Integer age){
        this.age = age;
    }

    /**
     * Dohvaća ime trenera.
     *
     * @return ime trenera
     */
    public String getName() {
        return name;
    }

    /**
     * Postavlja ime trenera.
     *
     * @param name ime trenera
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dohvaća broj godina iskustva trenera.
     *
     * @return broj godina iskustva
     */
    public Integer getExperience() {
        return experience;
    }
    public void setExperience(Integer experience) {
        this.experience = experience;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getType() {
        return type;
    }
}
