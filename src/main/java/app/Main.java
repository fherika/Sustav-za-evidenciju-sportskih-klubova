package app;

import entity.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JsonUtil;
import util.LogList;
import util.XmlLog;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Upravlja sportskim klubovima, omogucuje unos klubova, igrača i trenera, te nudi prikaz različitih statistika
 */
public class Main {

    static Logger log= LoggerFactory.getLogger(Main.class);

    /**
     * Čita string od korisnika i provjerava da nije prazan.
     *
     * @param sc Scanner za unos
     * @param prompt Poruka koju ispisuje prije unosa
     * @return Uneseni string (ne prazan)
     * @throws InvalidInputException Ako je uneseni string prazan
     */
    public static String readNonEmptyString(Scanner sc, String prompt) {
        System.out.print(prompt);
        String input = sc.nextLine();
        if (input.trim().isEmpty()) {
            throw new InvalidInputException("Input cannot be empty!");
        }
        return input;
    }
    /**
     * Provjerava postoji li već klub s istim imenom u zadanom nizu klubova.
     *
     * @param clubs Niz klubova u kojem se traži duplikat
     * @param clubName Ime kluba kojeg se provjerava
     * @param currentIndex Trenutni indeks do kojeg se provjerava (ne ukljucuje buduce unose)
     * @throws DuplicateClubException Ako klub već postoji
     */
    public static void checkDuplicateClub(List<Club> clubs, String clubName, int currentIndex) throws DuplicateClubException {
        for (int i = 0; i < currentIndex; i++) {
            if (clubs.get(i) != null && clubs.get(i).getName().equalsIgnoreCase(clubName)) {
                throw new DuplicateClubException("Club with name '" + clubName + "' already exists!");
            }
        }
    }
    /**
     * Provjerava je li dostignut maksimalni broj slobodnih agenata u nizu.
     *
     * @param freeAgents Niz slobodnih agenata
     * @param count Trenutni broj slobodnih agenata
     * @throws FreeAgentLimitException Ako je dostignut ili prekoračen maksimalni broj slobodnih agenata
     */
    public static void checkFreeAgentLimit(Person[] freeAgents, int count) throws FreeAgentLimitException {
        if (count >= freeAgents.length) {
            throw new FreeAgentLimitException("Cannot add more free agents! Maximum capacity reached: " + freeAgents.length);
        }
    }

    /**
     * Izvodi trening nad svim elementima iz zadane kolekcije.
     *
     * Metoda prihvaća kolekciju objekata koji su istovremeno tipa {@link Person}
     * i implementiraju sučelje {@link Trainable}. Za svaki element u kolekciji
     * poziva se njegova metoda {@code train()}.
     *
     * @param <T>      tip elemenata u kolekciji mora nasljeđivati {@link Person}
     *                 i implementirati {@link Trainable}
     *
     */
    public static <T extends Person & Trainable> void trainAll(Collection<T> people) {
        for (T p : people) {
            p.train();
        }
    }

    /**
     * Ispisuje statistiku svih igrača iz klubova koji igraju određeni sport.
     *
     * @param clubs Kolekcija klubova (ili njihovih podtipova) koje će se pregledati
     * @param sport Sport po kojem se filtriraju klubovi
     */
    public static void printPlayerStatsBySport(Collection<? extends Club> clubs, Sport sport) {
        clubs.stream()
                .filter(c -> c.getSport() == sport)
                .forEach(c -> {
                    System.out.println("Club: " + c.getName());
                    c.players.forEach(p -> System.out.println(" - " + p.showStats()));
                });
    }

    /**
     * Dodaje osobu u kolekciju koja može primiti tip Person ili nadtipove.
     *
     * @param people Kolekcija koja može primiti Person ili nadtipove
     * @param p Osoba koja se dodaje (Player ili Coach)
     */
    public static void addToCollection(Collection<? super Person> people, Person p) {
        people.add(p);
    }

    public static void saveFreeAgents(List<Person> freeAgents, String path) {
        Jsonb jsonb = JsonbBuilder.create();
        try {
            String json = jsonb.toJson(freeAgents);
            try (FileWriter fw = new FileWriter(path)) {
                fw.write(json);
            }
        } catch (IOException e) {
            System.err.println("Greška pri spremanju free agenata: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void backupData(List<Club> clubs, List<Person> freeAgents, String path) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(clubs);
            out.writeObject(freeAgents);
            System.out.println("Backup saved in " + path);
        } catch (IOException e) {
            System.err.println("Greška pri spremanju backup datoteke: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void restoreData(List<Club> clubs, List<Person> freeAgents,String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            List<Club> loadedClubs = (List<Club>) ois.readObject();
            List<Person> loadedFreeAgents = (List<Person>) ois.readObject();

            clubs.clear();
            clubs.addAll(loadedClubs);

            freeAgents.clear();
            freeAgents.addAll(loadedFreeAgents);

            System.out.println("Backup loaded!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Greška pri učitavanju backup datoteke: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final Integer NUMBER_OF_CLUBS = 5;
    private static final Integer NUMBER_OF_PLAYERS_IN_EACH_CLUB = 5;

    static void main(String[] args) {

        log.info("Program pokrenut!");
        Scanner sc = new Scanner(System.in);
        LogList logList = XmlLog.loadLogs();
        /*AwardRecord[] records = new AwardRecord[500];
        Integer awardRecordCount = 0;*/
        List<AwardRecord> records = new ArrayList<>();

        /*Person[] freeAgents = new Person[100];
        Integer freeAgentCount = 0;*/
        List<Person> freeAgents = new ArrayList<>();
        try{
            String json = Files.readString(Paths.get("doc/freeagents.json"));
            Jsonb jsonb = JsonbBuilder.create();

            List<Map<String,Object>> tempList = jsonb.fromJson(json, new ArrayList<Map<String,Object>>(){}.getClass().getGenericSuperclass());

            for (Map<String,Object> map : tempList) {
                String type = (String) map.get("type");
                if ("Player".equals(type)) {
                    freeAgents.add(jsonb.fromJson(jsonb.toJson(map), Player.class));
                } else if ("Coach".equals(type)) {
                    freeAgents.add(jsonb.fromJson(jsonb.toJson(map), Coach.class));
                }
            }

        }catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
            e.printStackTrace();
        }catch (JsonbException e) {
            System.err.println("Greška pri deserijalizaciji JSON-a: " + e.getMessage());
            e.printStackTrace();
        }
        List<Club> clubs = null;
        try {
            Jsonb jsonb = JsonbBuilder.create();

            String json = Files.readString(Paths.get("doc/clubs.json"));
            clubs = jsonb.fromJson(json, new ArrayList<Club>(){}.getClass().getGenericSuperclass());


        }catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
            e.printStackTrace();
        }catch (JsonbException e) {
            System.err.println("Greška pri deserijalizaciji JSON-a: " + e.getMessage());
            e.printStackTrace();
        }
        Set<String> clubNames = new HashSet<>();
        Map<String, Club> clubsMap = new HashMap<>();

        clubs.forEach(System.out::println);
        System.out.println("Press enter to continue...");
        sc.nextLine();
        Integer choice = 0;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Search club by name");
            System.out.println("2. Search player by name");
            System.out.println("3. Search clubs by sport");
            System.out.println("4. Show youngest player");
            System.out.println("5. Show most experienced coach");
            System.out.println("6. Show all clubs");
            System.out.println("7. Show all awards for all clubs");
            System.out.println("8. Add free agent");
            System.out.println("9. View free agents");
            System.out.println("10. Show youngest free agent (player)");
            System.out.println("11. Show most experienced coach among free agents");
            System.out.println("12. Extra options for all players in all clubs.");
            System.out.println("13. Print all players in all clubs for certain sports");
            System.out.println("14. Print all people in the system");
            System.out.println("15. Make a backup");
            System.out.println("16. Reload from backup");
            System.out.println("17. Show XML log");
            System.out.println("18. Exit");
            System.out.print("Choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                log.warn("Korisnik nije pravilno odabrao opciju menia!");
                System.out.println("Invalid input, must be a number!");
                choice=-1;
            }
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter club name to search: ");
                    String searchName = sc.nextLine();
                    log.debug("Pretraga kluba po imenu: " + searchName);

                    Club foundClub = clubs.stream()
                            .filter(c -> c.getName().equalsIgnoreCase(searchName))
                            .findFirst()
                            .orElse(null);


                    if (foundClub != null) {
                        System.out.println("Club found: ");
                        System.out.println(foundClub);
                        log.info("Klub pronaden: " + foundClub.getName());
                    } else {
                        System.out.println("Club not found!");
                        log.warn("Klub nije pronaden: " + searchName);
                    }

                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Club searched: " + searchName, logList);
                }
                case 2 -> {
                    System.out.print("Enter player name to search: ");
                    String searchName = sc.nextLine().trim();
                    log.debug("Pretraga igraca po imenu: " + searchName);

                    Optional<Player> searchedPlayer= clubs.stream()
                            .flatMap(club->club.players.stream())
                            .filter(player -> (player.getName()+" "+ player.getSurname()).equals(searchName))
                            .findAny();
                    searchedPlayer.ifPresentOrElse(
                            igrac-> System.out.println("Player found: "+ igrac.showStats()),
                            () -> {
                                System.out.println("Player not found!");
                                log.warn("Igrac nije pronaden: " + searchName);
                            }
                    );

                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Player searched: " + searchName, logList);
                }
                case 3 -> {
                    System.out.print("Enter sport to search: ");
                    String searchSport = sc.nextLine();

                    Sport sportEnum;
                    try {
                        sportEnum = Sport.fromString(searchSport);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e);
                        break;
                    }
                    List<Club> foundClubs = clubs.stream()
                            .filter(club -> club.getSport() == sportEnum)
                            .toList();

                    foundClubs.forEach(System.out::println);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Sport searched: " + searchSport, logList);
                }
                case 4 -> {
                    Optional<Player> youngest = clubs.stream()
                            .flatMap(club-> club.players.stream())
                            .min(Comparator.comparing(Player::getAge));
                    youngest.ifPresent(player -> System.out.println("Youngest player: " + player.showStats()));
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Searched for the youngest player", logList);
                }
                case 5 -> {
                    Optional<Coach> mostExperienced=clubs.stream()
                                    .max(Comparator.comparing(club-> club.coach.getExperience()))
                            .map(club -> club.coach);
                    mostExperienced.ifPresent(coach -> System.out.println("Most experienced coach: " + coach.showStats()));
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Searched for the most experienced coach", logList);
                }
                case 6 -> {
                    Integer clubChoice;
                    for (Integer i = 0; i < NUMBER_OF_CLUBS; i++) {
                        System.out.println((i + 1) + ". --->" + clubs.get(i).getName());
                    }
                    System.out.print("Enter number of club to view players & and for more club actions (any other number to cancel): ");
                    try{
                        clubChoice= sc.nextInt();
                        sc.nextLine();
                    }catch(InputMismatchException e){
                        log.warn("Korisnik nije pravilno odabrao opciju kod biranja kluba!");
                        System.out.println("Invalid input, must be a number!");
                        sc.nextLine();
                        break;
                    }

                    if (clubChoice <= 0 || clubChoice > NUMBER_OF_CLUBS) {
                        break;
                    }
                    if (clubChoice > 0 && clubChoice < NUMBER_OF_CLUBS) {
                        System.out.println(clubs.get(clubChoice - 1));
                    }

                    System.out.println("=== Manage " + clubs.get(clubChoice - 1).getName() + " ===");
                    System.out.print("1. Start training session?\n");
                    System.out.print("2. Add awards?\n");
                    System.out.print("3. Show awards?\n");
                    System.out.print("4. Go back to main menu?\n");
                    Integer clubmenuChoice;
                    try{
                        clubmenuChoice = sc.nextInt();
                        sc.nextLine();
                    }catch(InputMismatchException e){
                        log.warn("Korisnik nije pravilno odabrao opciju kod biranja radnji s klubom!");
                        System.out.println("Invalid input, must be a number!");
                        sc.nextLine();
                        break;
                    }

                    switch (clubmenuChoice) {
                        case 1 -> {
                            List<Person> toTrain = new ArrayList<>();
                            toTrain.add(clubs.get(clubChoice-1).coach);
                            toTrain.addAll(clubs.get(clubChoice-1).players);
                            trainAll(toTrain);
                            System.out.println("\nTraining completed for " + clubs.get(clubChoice - 1).getName());
                            XmlLog.addLog("Started and completed training for  " + clubs.get(clubChoice - 1).getName(), logList);
                        }
                        case 2 -> {
                            System.out.println("Choose award type:");
                            System.out.println("1. Medal");
                            System.out.println("2. Trophy");
                            Integer awardType = sc.nextInt();
                            sc.nextLine();

                            Award award = null;

                            if (awardType == 1) {
                                System.out.println("Choose medal type:");
                                System.out.println("1. Gold");
                                System.out.println("2. Silver");
                                System.out.println("3. Bronze");
                                Integer medalType = sc.nextInt();
                                sc.nextLine();

                                switch (medalType) {
                                    case 1 -> award = Medal.gold();
                                    case 2 -> award = Medal.silver();
                                    case 3 -> award = Medal.bronze();
                                    default -> System.out.println("Invalid medal option!");
                                }
                            } else if (awardType == 2) {
                                System.out.println("Choose trophy type:");
                                System.out.println("1. Cup");
                                System.out.println("2. League");
                                Integer trophyType = sc.nextInt();
                                sc.nextLine();

                                switch (trophyType) {
                                    case 1 -> award = Trophy.cup();
                                    case 2 -> award = Trophy.league();
                                    default -> System.out.println("Invalid trophy option!");
                                }
                            }

                            if (award != null) {
                                clubs.get(clubChoice - 1).addAward(award);
                                String today = LocalDate.now().toString();
                                records.add(new AwardRecord(clubs.get(clubChoice - 1).getName(), award.displayAward(), today));
                            }
                            XmlLog.addLog("Added an award for " + clubs.get(clubChoice - 1).getName(), logList);
                        }
                        case 3 -> {
                            clubs.get(clubChoice - 1).listAwards();
                            XmlLog.addLog("Viewed all awards for " + clubs.get(clubChoice - 1).getName(), logList);
                        }
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 7 -> {
                    System.out.println("=== All Award Records ===");
                    for(AwardRecord record : records) {
                        System.out.println(record.name()+ " - " + record.type() + " - " + record.date());
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Viewed award record for all the clubs in the system", logList);
                }
                case 8 -> {
                        //checkFreeAgentLimit(freeAgents, freeAgentCount); -> nije vise potrebna(koriste se liste)
                        System.out.println("Add free agent:");
                        System.out.println("1. Player");
                        System.out.println("2. Coach");
                        Integer type = sc.nextInt();
                        sc.nextLine();

                        if (type == 1) {
                            System.out.print("Enter name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter surname: ");
                            String surname = sc.nextLine();
                            log.info("Dodavanje free agenta: " + name + " " + surname);
                            System.out.print("Enter age: ");
                            int age = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter position: ");
                            String position = sc.nextLine();
                            freeAgents.add(new Player.PlayerBuilder().setName(name).setSurname(surname).setAge(age).setPosition(position).build());
                        } else if (type == 2) {
                            System.out.print("Enter name: ");
                            String name = sc.nextLine();
                            System.out.print("Enter surname: ");
                            String surname = sc.nextLine();
                            log.info("Dodavanje free agenta: " + name + " " + surname);
                            System.out.print("Enter experience (years): ");
                            int experience = sc.nextInt();
                            System.out.print("Enter age: ");
                            int age = sc.nextInt();
                            sc.nextLine();
                            freeAgents.add(new Coach(name, surname, experience, age));
                        }
                    /*catch (FreeAgentLimitException e){
                        log.error("Puno je polje za popunjavanje Free Agent-a!", e);
                        System.out.println(e.getMessage());
                    }*/
                    XmlLog.addLog("Added a free agent", logList);
                    saveFreeAgents(freeAgents, "doc/freeagents.json");
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }

                case 9 -> {
                    System.out.println("=== All free agents ===");
                    if (freeAgents.isEmpty()) {
                        System.out.println("No free agents yet!");
                        break;
                    }
                    freeAgents.forEach(p -> {
                        if (p instanceof Player) {
                            System.out.println("Player: " + p.showStats());
                        } else if (p instanceof Coach) {
                            System.out.println("Coach: " + p.showStats());
                        }
                    });
                    XmlLog.addLog("Viewed all the free agents", logList);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 10 -> {
                    Optional<Player> youngestFreeAgent= freeAgents.stream()
                            .filter(p-> p instanceof Player)
                            .map(p -> (Player) p)
                            .min(Comparator.comparing(Player::getAge));

                    youngestFreeAgent.ifPresentOrElse(
                            p->System.out.println("Youngest free agent: "+ p.showStats()),
                            ()->System.out.println("No free agent players yet!")
                    );
                    XmlLog.addLog("Searched for the youngest player among free agents", logList);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 11 -> {
                    Optional<Coach> mostExCoach= freeAgents.stream()
                            .filter(p-> p instanceof Coach)
                            .map(c-> (Coach) c)
                            .max(Comparator.comparing(Coach::getExperience));

                    mostExCoach.ifPresentOrElse(
                            c->System.out.println("Most experienced free agent coach: "+ c.showStats()),
                            ()->System.out.println("No free agent coaches yet!")
                    );
                    XmlLog.addLog("Searched for the most experienced coach among free agents", logList);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 12 -> {
                    System.out.println("=== Extra options for all players in all clubs. ===");

                    boolean extraOptions = true;
                    while (extraOptions) {
                        System.out.println("\nChoose an option:");
                        System.out.println("1. All players in order added (Sequenced Collection)");
                        System.out.println("2. All players younger than 25 (Stream gatherer)");
                        System.out.println("3. Partition players by age (<25 / >=25)");
                        System.out.println("4. Group players by position");
                        System.out.println("5. Go back to main menu");
                        System.out.print("Choice: ");

                        int extraOptionsChoice;
                        try{
                            extraOptionsChoice= Integer.parseInt(sc.nextLine());
                        } catch(NumberFormatException e){
                            log.warn("Korisnik nije pravilno odabrao opciju kod biranja radnji s dodatnim opcijama!");
                            System.out.println("Invalid input, must be a number!");
                            sc.nextLine();
                            break;
                        }
                        switch (extraOptionsChoice) {
                            case 1 -> { // Sequenced Collection
                                System.out.println("\nAll players in order added (per club):");
                                for (Club c : clubs) {
                                    System.out.println("Club: " + c.getName());
                                    for (Player p : c.players) {
                                        System.out.println(" - " + p.showStats());
                                    }
                                }
                                XmlLog.addLog("Viewed all the players in the system in order added", logList);
                            }

                            case 2 -> { // Stream gatherer
                                System.out.println("\nAll players younger than 25 (all clubs):");
                                List<Player> youngPlayers = clubs.stream()
                                        .flatMap(club -> club.players.stream())
                                        .filter(p -> p.getAge() < 25)
                                        .toList();
                                youngPlayers.forEach(p -> System.out.println(" - " + p.showStats()));
                                XmlLog.addLog("Viewed all players younger then 25 in the system", logList);
                            }

                            case 3 -> {// PartitioningBy
                                System.out.println("\nPartition players by age (<25 / >=25):");
                                Map<Boolean, List<Player>> partitioned = clubs.stream()
                                        .flatMap(club -> club.players.stream())
                                        .collect(Collectors.partitioningBy(p -> p.getAge() < 25));
                                System.out.println("Younger than 25:");
                                partitioned.get(true).forEach(p -> System.out.println(" - " + p.showStats()));
                                System.out.println("25 or older:");
                                partitioned.get(false).forEach(p -> System.out.println(" - " + p.showStats()));
                                XmlLog.addLog("Partitioned players into younger or older then 25 years old", logList);
                            }
                            //dada test za git
                            case 4 -> { // GroupingBy
                                System.out.println("\nGroup players by position:");
                                Map<String, List<Player>> groupedByPosition = clubs.stream()
                                        .flatMap(club -> club.players.stream())
                                        .collect(Collectors.groupingBy(Player::getPosition));
                                groupedByPosition.forEach((position, list) -> {
                                    System.out.println("Position: " + position);
                                    list.forEach(p -> System.out.println(" - " + p.showStats()));
                                });
                                XmlLog.addLog("Viewed players by the position", logList);
                            }
                            case 5 -> extraOptions = false;
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                }
                case 13 -> {
                    System.out.print("Enter sport to display player stats: ");
                    String inputSport = sc.nextLine();
                    try {
                        Sport sportEnum = Sport.fromString(inputSport);
                        printPlayerStatsBySport(clubs, sportEnum);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid sport!");
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    XmlLog.addLog("Viewed all players in the system that are "+ inputSport +" players", logList);
                }
                case 14-> {
                    List<Object> allPeople= new ArrayList<>();

                    clubs.stream().forEach(club->{
                                club.players.forEach(player->addToCollection(allPeople,player));
                                addToCollection(allPeople, club.coach);
                            });
                    freeAgents.stream().forEach(person->addToCollection(allPeople,person));

                    System.out.println("=== All people in the system ===");
                    allPeople.stream()
                            .filter(o->o instanceof Person)
                            .map(o->(Person) o)
                            .forEach(p->System.out.println(" - " + p.showStats()));

                    XmlLog.addLog("Viewed all the people in the system", logList);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 15 -> {
                    XmlLog.addLog("Made a backup", logList);
                    backupData(clubs, freeAgents, "doc/backup.bin");
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 16 -> {
                    XmlLog.addLog("Reloaded from the backup", logList);
                    restoreData(clubs, freeAgents,"doc/backup.bin");
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 17 -> {
                    XmlLog.addLog("Viewed XML log", logList);
                    System.out.println("=== XML Log ===");
                    XmlLog.printLogsWithoutTags(logList);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 18 -> {
                    System.out.println("Exiting...");
                    XmlLog.addLog("Exited the aplication", logList);
                }
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 18);
        log.info("Program zavrsio!");
    }
}