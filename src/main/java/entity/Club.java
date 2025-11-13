package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Predstavlja sportski klub sa svim relevantnim informacijama.
 */
public class Club {
    private String name;
    private Sport sport;
    public Integer yearOfFoundation;
    public Coach coach;
    public List<Player> players;
    private List<Award> awards;

    /**
     * Konstruktor za stvaranje novog kluba.
     *
     * @param name ime kluba
     * @param sport sport kojim se klub bavi
     * @param yearOfFoundation godina osnivanja kluba
     * @param coach trener kluba
     * @param players niz igrača koji pripadaju klubu
     */
    public Club(String name, Sport sport, Integer yearOfFoundation, Coach coach,  List<Player> players) {
        this.name = name;
        this.sport = sport;
        this.yearOfFoundation = yearOfFoundation;
        this.coach = coach;
        this.players = new ArrayList<>(players);
        this.players.sort(Comparator.comparingInt(Player::getAge));
        this.awards = new ArrayList<>();
    }
    /**
     * Dodaje nagradu klubu.
     *
     * @param award nagrada koju treba dodati
     */
    public void addAward(Award award) {
       awards.add(award);
       System.out.println("Added: "+award);
    }
    /**
     * Ispisuje sve nagrade kluba.
     */
    public void listAwards() {
        if (awards.isEmpty()) {
            System.out.println("This club has no awards.");
            return;
        }
        System.out.println("Awards for " + name + ":");
        for (Award award : awards) {
            System.out.println("- " + award.displayAward());
        }
    }
    /**
     * Dohvaća ime kluba.
     *
     * @return ime kluba
     */
    public String getName() {
        return name;
    }

    /**
     * Postavlja ime kluba.
     *
     * @param name novo ime kluba
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dohvaća sport kojim se klub bavi.
     *
     * @return sport kluba
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * Postavlja sport kojim se klub bavi.
     *
     * @param sport novi sport kluba
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }


    /**
     * Vraća string reprezentaciju kluba koja uključuje ime, sport, godinu osnivanja,
     * trenera i popis igrača.
     *
     * @return string reprezentacija kluba
     */
    @Override
    public String toString() {
        String roster ="";
        for(Player player : players) {
            roster+=player.showStats();
            roster+="\n";
        }
        return "\nClub name: " + name + "\nSport: " + sport + "\nYear of Foundation: " + yearOfFoundation+"\nCoach: " + coach.showStats()+"\nRoster:\n" + roster;
    }
}
