# ACME AIR Microservice

## Assumptions Made
- Searching of flights is not extended for different class types such as Economy, Premium or Business
- There are no provisions for adding extra stops
- All flights searched have a departure and arrival date as search query parameters alongside origin and destination.


## API(s) implemented
- Search Flights (/acme-air/flights/search)
  - Flights can be searched using Origin, Destination and Departure Date
  - Return Date is Optional
- Search Flights By Id (/acme-air/flights/{flightId})

## API(s) Not implemented
- Create and save passenger flight booking
- Update Passenger details on an existing booking
- Cancel an existing passenger's flight booking


## Commands
### Run Application
Using Gradle: In your terminal, go to the project directory root folder and run command 
```bash
./gradlew bootRun
```
Use postman script to test endpoints

### Run Tests
Using Gradle: In your terminal, go to the project directory root folder and run command
```bash
./gradlew build
```
A index.html file can be found under build>>reports>>tests

## Postman Collection
Postman script [AcmeAir.postman_collection.json](src/main/resources/AcmeAir.postman_collection.json) can be found under the resources folder and used to test the 2 APIs