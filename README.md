# Spring Boot + Amadeus Java SDK Demo

## Motivation

Sometimes, I need to travel in Europe or cross the Atlantic ocean but
after 2 years of COVID, before travelling I need to review the country restrictions
defined for travellers.

## Value

This example show how to see COVID restrictions in a particular [country](https://www.iso.org/iso-3166-country-codes.html).

## How to use it?

Register in [Amadeus for Developers](https://developers.amadeus.com) to get your `AMADEUS_CLIENT_ID` & `AMADEUS_CLIENT_SECRET`

Export the values:

```
export AMADEUS_CLIENT_ID=YOUR_CLIENT_ID
export AMADEUS_CLIENT_SECRET=YOUR_CLIENT_SECRET
```

## How to run in local?

```
mvn spring-boot:run
mvn clean package
java -jar ./target/spring-boot-amadeus-sdk-demo-0.1.0-SNAPSHOT.jar
```

## How to run as a Docker Container?

```
mvn spring-boot:build-image
docker run \
    -e AMADEUS_CLIENT_ID=$AMADEUS_CLIENT_ID \
    -e AMADEUS_CLIENT_SECRET=$AMADEUS_CLIENT_SECRET \
    -t docker.io/library/spring-boot-amadeus-sdk-demo:0.1.0-SNAPSHOT
```

## How to test the example?

```
curl 'http://localhost:8080/api/v1/covid-restrictions?country-code=ES'
```

## Others commands

```
mvn prettier:write
mvn versions:display-dependency-updates
```
