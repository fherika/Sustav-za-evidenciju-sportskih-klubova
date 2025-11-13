package app;

import entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    private static final Integer NUMBER_OF_CLUBS = 5;
    private static final Integer NUMBER_OF_PLAYERS_IN_EACH_CLUB = 5;

    static void main(String[] args) {

        log.info("Program pokrenut!");
        Scanner sc = new Scanner(System.in);

        /*AwardRecord[] records = new AwardRecord[500];
        Integer awardRecordCount = 0;*/
        List<AwardRecord> records = new ArrayList<>();

        /*Person[] freeAgents = new Person[100];
        Integer freeAgentCount = 0;*/
        List<Person> freeAgents = new ArrayList<>();


        //Club[] clubs = new Club[NUMBER_OF_CLUBS];
        List<Club> clubs = new ArrayList<>();
        Set<String> clubNames = new HashSet<>();
        Map<String, Club> clubsMap = new HashMap<>();

        for (Integer i = 0; i < NUMBER_OF_CLUBS; i++) {
            String clubName = "";
            Sport clubSport;
            Integer clubYearOfFoundation;
            String clubCoachName="";
            String clubCoachSurname="";
            Integer clubCoachYearsOfExpirience;
            Integer clubCoachAge;
            try {
                clubName = readNonEmptyString(sc, "Enter club name: ");
                //checkDuplicateClub(clubs, clubName, i);
                if (!clubNames.add(clubName)) {
                    System.out.println("Club with this name already exists!");
                    i--;
                    continue;
                }
                log.trace("Unos kluba pocinje: " + clubName);
                System.out.print("Enter club sport: ");
                String inputSport = sc.nextLine();
                try {
                    clubSport = Sport.fromString(inputSport);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid sport! Try again.");
                    i--;
                    continue;
                }

                System.out.print("Enter clubs year of foundation: ");
                clubYearOfFoundation = sc.nextInt();
                sc.nextLine();
                clubCoachName = readNonEmptyString(sc, "Enter clubs coaches name: ");
                clubCoachSurname = readNonEmptyString(sc, "Enter clubs coaches surname: ");
                System.out.print("Enter clubs coaches years of expirience: ");
                clubCoachYearsOfExpirience = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter clubs coaches age: ");
                clubCoachAge = sc.nextInt();
                sc.nextLine();
            }
            catch (InvalidInputException e) {
                log.warn("Problem pri unosu: " + e.getMessage());
                System.out.println(e.getMessage());
                i--;
                continue;
            }
            catch (InputMismatchException e) {
                log.warn("Korisnik nije unio broj, a trebao je!", e);
                System.out.println("Invalid input, must be a number!");
                i--;
                clubNames.remove(clubName);
                sc.nextLine();
                continue;
            }
            Coach clubCoach = new Coach(clubCoachName, clubCoachSurname, clubCoachYearsOfExpirience, clubCoachAge);

            //Player[] clubPlayers = new Player[NUMBER_OF_PLAYERS_IN_EACH_CLUB];
            List<Player> clubPlayers= new ArrayList<>();
            for (Integer j = 0; j < NUMBER_OF_PLAYERS_IN_EACH_CLUB; j++) {
                String playerName="";
                String playerSurname="";
                Integer playerAge;
                String playerPosition="";
                try{
                    playerName = readNonEmptyString(sc, "Enter player name: ");
                    playerSurname = readNonEmptyString(sc, "Enter player surname: ");
                    log.trace("Unos igraca pocinje: " + playerName + " " + playerSurname);
                    System.out.print("Enter player age: ");
                    playerAge = sc.nextInt();
                    sc.nextLine();
                    playerPosition = readNonEmptyString(sc, "Enter player position: ");
                }
                catch (InvalidInputException e) {
                    log.warn("Problem pri unosu: " + e.getMessage());
                    System.out.println(e.getMessage());
                    j--;
                    continue;
                }
                catch (InputMismatchException e) {
                    log.warn("Korisnik nije unio broj, a trebao je!");
                    System.out.println("Invalid input, must be a number!");
                    j--;
                    sc.nextLine();
                    continue;
                }
                Player newPlayer = new Player.PlayerBuilder().setName(playerName).setSurname(playerSurname).setAge(playerAge).setPosition(playerPosition).build();
                clubPlayers.add(newPlayer);
                log.info("Dodan igrac: " + playerName + " " + playerSurname + " u klub: " + clubName);
            }

            Club newClub = new Club(clubName, clubSport, clubYearOfFoundation, clubCoach, clubPlayers);
            clubs.add(newClub);
            clubsMap.put(newClub.getName(), newClub);
            log.info("Dodani klub: " + clubName + ", sport: " + clubSport);
        }
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
            System.out.println("13. Exit");
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
                    Club c = clubsMap.get(searchName);
                    if (c != null) {
                        System.out.println("Club found: " + c.getName());
                        log.info("Klub pronaden: " + c.getName());
                    } else {
                        System.out.println("Club not found!");
                        log.warn("Klub nije pronaden: " + searchName);
                    }

                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 2 -> {
                    System.out.print("Enter player name to search: ");
                    String searchName = sc.nextLine();
                    log.debug("Pretraga igraca po imenu: " + searchName);
                    boolean found = false;
                    for (Integer i = 0; i < NUMBER_OF_CLUBS; i++) {
                        for (Integer j = 0; j < NUMBER_OF_PLAYERS_IN_EACH_CLUB; j++) {
                            String playerName = clubs.get(i).players.get(j).getName() + " " + clubs.get(i).players.get(j).getSurname();
                            if (searchName.equals(playerName)) {
                                System.out.println("Player found: " + playerName + " - " + clubs.get(i).getName() + " - " + clubs.get(i).players.get(j).getPosition());
                                log.info("Igrac pronaden: " + playerName);
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        System.out.println("Player not found!");
                        log.warn("Igrac nije pronaden: " + searchName);
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 3 -> {
                    System.out.print("Enter sport to search: ");
                    String searchSport = sc.nextLine();
                    Integer numberOfFoundClubs = 0;

                    boolean found = false;
                    for (Integer i = 0; i < NUMBER_OF_CLUBS; i++) {
                        if (searchSport.equals(clubs.get(i).getSport())) {
                            System.out.println("Club " + (++numberOfFoundClubs) + ". " + clubs.get(i).getName());
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("Clubs not found!");
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 4 -> {
                    Player youngest = null;
                    String youngestClubName = "";
                    for (Integer i = 0; i < NUMBER_OF_CLUBS; i++) {
                        for (Integer j = 0; j < NUMBER_OF_PLAYERS_IN_EACH_CLUB; j++) {
                            if (youngest == null || clubs.get(i).players.get(j).getAge() < youngest.getAge()) {
                                youngest = clubs.get(i).players.get(j);
                                youngestClubName = clubs.get(i).getName();
                            }
                        }
                    }
                    System.out.println("Youngest player: " + youngest.showStats() + " from " + youngestClubName);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 5 -> {
                    Coach mostExperienced = clubs.getFirst().coach;
                    String mostExperiencedClubName = "";
                    for (Integer i = 0; i < NUMBER_OF_CLUBS; i++) {
                        if (mostExperienced.getExperience() < clubs.get(i).coach.getExperience()) {
                            mostExperienced = clubs.get(i).coach;
                            mostExperiencedClubName = clubs.get(i).getName();
                        }
                    }
                    System.out.println(mostExperienced.showStats() + ", curently coaching " + mostExperiencedClubName);
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
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
                            clubs.get(clubChoice - 1).coach.train();
                            for (Integer i = 0; i < clubs.get(clubChoice - 1).players.size(); i++) {
                                clubs.get(clubChoice - 1).players.get(i).train();
                            }
                            System.out.println("\nTraining completed for " + clubs.get(clubChoice - 1).getName());
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
                                String today = java.time.LocalDate.now().toString();
                                records.add(new AwardRecord(clubs.get(clubChoice - 1).getName(), award.displayAward(), today));
                            }
                        }
                        case 3 -> {
                            clubs.get(clubChoice - 1).listAwards();
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
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 9 -> {
                    System.out.println("=== All free agents ===");
                    if (freeAgents.size() == 0) {
                        System.out.println("No free agents yet!");
                        break;
                    }
                    for(Person p: freeAgents) {
                        if(p instanceof Player) {
                            System.out.println("Player: "+ p.showStats());
                        } else if(p instanceof Coach) {
                            System.out.println("Coach: "+ p.showStats());
                        }
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 10 -> {
                    Person youngest = null;
                    Integer freeAgentPlayerCount = 0;
                    for (int i = 0; i < freeAgents.size(); i++) {
                        if (freeAgents.get(i) instanceof Player) {
                            freeAgentPlayerCount++;
                        }
                    }
                    if (freeAgentPlayerCount == 0) {
                        System.out.println("No free agents yet!");
                        break;
                    }
                    for (int i = 0; i < freeAgents.size(); i++) {
                        if (freeAgents.get(i) instanceof Player) {
                            youngest = freeAgents.get(i);
                            break;
                        }
                    }

                    for (int i = 0; i < freeAgents.size(); i++) {
                        if (freeAgents.get(i) instanceof Player) {
                            if (freeAgents.get(i).getAge() < youngest.getAge()) {
                                youngest = freeAgents.get(i);
                            }
                        }
                    }

                    System.out.println("Youngest free agent: " + youngest.showStats());
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                }
                case 11 -> {
                    Integer freeAgentCoachCount = 0;
                    for (int i = 0; i < freeAgents.size(); i++) {
                        if (freeAgents.get(i) instanceof Coach) {
                            freeAgentCoachCount++;
                        }
                    }
                    if (freeAgentCoachCount == 0) {
                        System.out.println("No coaches free agents yet!");
                        System.out.println("Press enter to continue...");
                        sc.nextLine();
                        break;
                    }

                    Coach mostExperiencedCoach = new Coach("", "", 0, 0);
                    for (int i = 0; i < freeAgents.size(); i++) {
                        if (freeAgents.get(i) instanceof Coach) {
                            if (((Coach) freeAgents.get(i)).getExperience() > mostExperiencedCoach.getExperience()) {
                                mostExperiencedCoach = (Coach) freeAgents.get(i);
                            }
                        }
                    }

                    System.out.println("Most experienced free agent coach: " + mostExperiencedCoach.showStats());
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
                            }

                            case 2 -> { // Stream gatherer
                                System.out.println("\nAll players younger than 25 (all clubs):");
                                List<Player> youngPlayers = clubs.stream()
                                        .flatMap(club -> club.players.stream())
                                        .filter(p -> p.getAge() < 25)
                                        .toList();
                                youngPlayers.forEach(p -> System.out.println(" - " + p.showStats()));
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
                            }
                            case 5 -> extraOptions = false;
                            default -> System.out.println("Invalid choice.");
                        }
                    }
                }
                case 13 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 13);
        log.info("Program zavrsio!");
    }
}