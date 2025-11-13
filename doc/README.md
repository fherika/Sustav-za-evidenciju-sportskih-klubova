# Sports Club Manager 🏀⚽

Ovaj program omogućuje upravljanje sportskim klubovima, igračima i trenerima putem konzolnog izbornika.

## 📋 Funkcionalnosti
- Pretraga klubova po imenu
- Pretraga igrača po imenu
- Pretraga klubova po sportu
- Prikaz najmlađeg igrača
- Prikaz najiskusnijeg trenera
- Ispis svih klubova
- Pregled odabranog kluba i obavljanje treninga
- Dodavanje i pregled nagrada (*medalje i pehari*)
- Dodavanje i pregled slobodnih igrača i trenera (*free agents*)


## ▶️ Pokretanje i koristenje programa
- Pokreni `Main` klasu. 
- Nakon pokretanja, program će odmah tražiti da unesete osnovne podatke za 5 klubova (ime, sport, godinu osnivanja, trenera i pet igrača) -> ovaj korak se moze preskočiti kopiranjem sadržaja datoteke `presetzaunos.txt`, koja se nalazi u mapi `doc`, te lijepljenjem tog sadržaja u konzolu. 
- Nakon unosa svih klubova, program automatski ispisuje pregled svih klubova i zatim prikazuje glavni izbornik. 
- Nakon početnog unosa, prikazuje se meni s opcijama, a željena opcija se odabire na način da se s tipkovnice upiše broj koji stoji ispred željene opcije:

=== MENU ===
1. Search club by name
2. Search player by name
3. Search clubs by sport
4. Show youngest player
5. Show most experienced coach
6. Show all clubs
7. Show all awards for all clubs
8. Add free agent
9. View free agents
10. Show youngest free agent (player)
11. Show most experienced coach among free agents
12. Exit 

Choice:

1. Search club by name

   - Unesite ime kluba (točno onako kako ste ga unijeli na početku)
   - Ako postoji ispisat će se informacije o pronađenom klubu
   - Ako ne postoji ispisat će se “Club not found!”
2. Search player by name
   - Unesite ime i prezime igrača (npr. “Luka Modrić”)
   - Program pretražuje sve klubove i ispisuje klub i poziciju igrača ako ga pronađe
3. Search clubs by sport
   - Unesite vrstu sporta (npr. “Rukomet”, “Nogomet”)
   - Program ispisuje sve klubove koji igraju taj sport
4. Show youngest player
   - Pronalazi najmlađeg igrača iz svih klubova i ispisuje njegovo ime, prezime, godine i klub u kojem igra
5. Show most experienced coach
   - Pronalazi trenera s najviše godina iskustva među svim klubovima
6. Show all clubs
   - Ispisuje sve klubove s rednim brojevima.
   - Korisnik može odabrati klub po broju i zatim dobiti podizbornik za taj klub:
   1. Start training session?
      - simulira trening (poziva train() metodu trenera i igrača).
   2. Add awards?
      - omogućuje dodavanje medalja i pehara
      - Medalje: Gold, Silver, Bronze (biraju se brojevima 1–3)
      - Pehari: Cup ili League (biraju se brojevima 1–2)
      - Nakon dodavanja, zapis o nagradi se sprema za taj klub, ali se i sprema u record, gdje se drži evidencija svih osvojenih nagrada za sve klubove
   3. Show awards?
      - ispisuje sve nagrade koje je klub osvojio
   4. Go back to main menu?
      - povratak u glavni izbornik
7. Show all awards for all clubs
   - Ispisuje zapis svih dodijeljenih nagrada za sve ekipe
8. Add free agent
   - Dodaje osobu bez kluba (free agent)
   - Može biti:
     - Player → unose se ime, prezime, godine i pozicija 
     - Coach → unose se ime, prezime, godine i iskustvo
9. View free agents
   - Ispisuje sve trenutačno unesene free agente (igrače i trenere)
10. Show youngest free agent (player)
    - Pronalazi najmlađeg igrača među igračima bez kluba
11. Show most experienced coach among free agents
    - Pronalazi trenera s najviše iskustva među free agentima
12. Exit
    - Zatvara program

## ⚙️ Tehnologije
- **Java 25**
- Objektno orijentirano programiranje (nasljeđivanje, apstraktne klase, polimorfizam)
- Korištenje `record` tipova
- `sealed` sučelja za nagrade
- `Builder pattern`
- Rukovanje korisničkim unosom pomoću `Scanner`

## 🏗️ Struktura koda
- `Person` – apstraktna klasa (nadklasa za `Player` i `Coach`)
- `Player` i `Coach` – podklase s dodatnim atributima
- `Club` – klasa koja sadrži igrače, trenera i sport
- `Award` – sealed interface za medalje i pehare
- `AwardRecord` – record koji pohranjuje zapise o svim ostvarenim nagradama svih ekipa

## 👨‍💻 Autor
Franjo Kranjčec  
Tehničko veleučilište u Zagrebu – kolegij Programiranje u jeziku Java
