package entity;
/**
 * Predstavlja igrača.
 * Nasljeđuje apstraktnu klasu Person i implementira sučelje Trainable.
 * Koristi Builder pattern za kreiranje objekata.
 */
public class Player extends Person implements Trainable{
    private String position;
    /**
     * Privatni konstruktor koji se koristi u Builderu.
     *
     * @param builder PlayerBuilder objekt koji sadrži podatke za inicijalizaciju
     */
    private Player(PlayerBuilder builder) {
        super(builder.name, builder.surname, builder.age);
        this.position = builder.position;
    }
    /**
     * Vraća statistike igrača u obliku stringa.
     *
     * @return Statistike igrača
     */
    @Override
    public String showStats(){
        return name+ " " + surname + ", " + age + " years old, " + position;
    }
    /**
     * Simulira trening igrača.
     */
    @Override
    public void train(){
        System.out.println(name + " " + surname + " is training hard!");
    }

    /** Getteri i setteri za ime, prezime, dob i poziciju */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    @Override
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }


    /**
     * Builder klasa za kreiranje Player objekata.
     */
    public static class PlayerBuilder {
        private String name;
        private String surname;
        private Integer age;
        private String position;

        public PlayerBuilder setName(String name) {
            this.name = name;
            return this;
        }
        public PlayerBuilder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public PlayerBuilder setAge(Integer age) {
            this.age = age;
            return this;
        }

        public PlayerBuilder setPosition(String position) {
            this.position = position;
            return this;
        }

        /**
         * Kreira i vraća Player objekt koristeći podatke iz Buildera.
         *
         * @return Novi Player objekt
         */
        public Player build() {
            return new Player(this);
        }
    }
}
