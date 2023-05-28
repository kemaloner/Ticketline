## Ticketline 3.1 - System für Ticket-Verkauf für Veranstaltungen (Kino,Theater, Konzerte)

Funktionalität:
• Ticket-Verkauf
• Prämien-System
• Verkauf von Merchandising-Artikeln
• Auswertungen

### Building Ticketline

1. Install:
    * Oracle JDK 10
2. Navigate to the ticketline code directory
3. Build the server as well as the client with the following commands

#### Server + Client:
```
./mvnw clean verify
```
#### Server:
```
./mvnw -pl=server -am clean verify
```
#### Client
```
./mvnw -pl=client -am clean verify
```

### Running Ticketline

1. Navigate to the ticketline code directory
2. Run the server as well as the client with with the following commands

#### Server:
```
./mvnw -pl=server -am spring-boot:run
```

To generate some testData add the generateData profile when running the server
```
./mvnw -pl=server -am spring-boot:run -Dspring-boot.run.profiles=generateData
```

#### Client
```
./mvnw -pl=client -am spring-boot:run
```

### Login

You can login to the client using one of the following credentials:

* System Administrator
  * Username: admin
  * Password: password
* User
  * Username: user
  * Password: password