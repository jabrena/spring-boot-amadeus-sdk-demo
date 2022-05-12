package com.example.demo;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.DiseaseAreaReport;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

@Configuration
class AmadeusConfig {

    public static Logger logger = LoggerFactory.getLogger(AmadeusConfig.class);

    final String AMADEUS_CLIENT_ID;
    final String AMADEUS_CLIENT_SECRET;
    final Amadeus amadeus;

    public AmadeusConfig() {
        this.AMADEUS_CLIENT_ID =
            (Objects.isNull(System.getenv("AMADEUS_CLIENT_ID")))
                ? "PENDING_AMADEUS_CLIENT_ID"
                : System.getenv("AMADEUS_CLIENT_ID");
        this.AMADEUS_CLIENT_SECRET =
            (Objects.isNull(System.getenv("AMADEUS_CLIENT_SECRET")))
                ? "PENDING_AMADEUS_CLIENT_ID"
                : System.getenv("AMADEUS_CLIENT_SECRET");
        this.amadeus = Amadeus.builder(AMADEUS_CLIENT_ID, AMADEUS_CLIENT_SECRET).build();
    }

    @Bean
    public Amadeus getAmadeusBean() {
        return Amadeus.builder(AMADEUS_CLIENT_ID, AMADEUS_CLIENT_SECRET).setLogLevel("debug").build();
    }

    @PostConstruct
    public void init() {
        logger.info("Loading environment variables:");
        logger.info("AMADEUS_CLIENT_ID: {}", AMADEUS_CLIENT_ID);
        logger.info("AMADEUS_CLIENT_SECRET: {}", AMADEUS_CLIENT_SECRET);
    }
}

@RestController
class WebController {

    public static Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private Amadeus amadeus;

    @GetMapping("/api/v1/covid-restrictions")
    public ResponseEntity<String> getCOVIDRestrictions(@RequestParam(name = "country-code") String countryCode) {
        try {
            DiseaseAreaReport diseaseAreaReport = amadeus.dutyOfCare.diseases.covid19AreaReport.get(
                Params.with("countryCode", countryCode)
            );
            return ResponseEntity.ok().body(diseaseAreaReport.toString());
        } catch (ResponseException e) {
            logger.error(e.getMessage());
            if (e.getMessage().contains("[401]")) {
                return ResponseEntity.internalServerError().body("Something goes wrong internally");
            } else if (e.getMessage().contains("[400]")) {
                return ResponseEntity.badRequest().body("Review parameters in the request");
            }
            return ResponseEntity.internalServerError().body("Something goes wrong");
        }
    }
}
