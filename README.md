# kts-nvt-backend

## Pokretanje

### Baza
Potrebno je instalirati PostgreSQL bazu podataka. Bazu mozete preuzeti [ovdje](https://www.postgresql.org/download/). Prilikom instalacije je pozeljno podesiti username na `postgres` i password na `root`. U PGAdmin-u kreirati bazu sa nazivom `kts-nvt
`.
Ukoliko pokrecete testove, pozeljno je instalirati i H2 bazu. Bazu mozete preuzeti [ovdje](http://www.h2database.com/html/download.html).

Ukoliko vam se kredencijali za pristup bazi razlikuju od gore navedenih mozete ih promijeniti u projektu tako sto cete azurirati `src/main/resources/application.properties` fajl. Postavite `spring.datasource.username` i `spring.datasource.password` na one koje ste izabrali prilikom instalacije.

### Testovi

Testove mozete pokrenuti iz IntelliJ IDE-a desnim klikom na `src/test/java` i odabirom `Run all tests` opcije ili komandom `mvn test` iz korijenskog repozitorijuma. Imajte u vidu da se pokrecu i e2e testovi tako da je potrebno pokrenuti i backend (e2e profil) i frontend prije pokretanja ove komande.

### Pokretanje backend-a

Iz IntelliJ IDE-a pokrenuti klikom na dugme `Run` ili iz terminala komandom `mvn spring-boot:run` iz korijenskog repozitorijuma (onaj repozitorijum gdje se nalazi pom.xml).
