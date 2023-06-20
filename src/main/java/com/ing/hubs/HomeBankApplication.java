package com.ing.hubs;

import com.ing.hubs.models.ExchangeRate;
import com.ing.hubs.repositories.ExchangeRateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class HomeBankApplication implements CommandLineRunner {
    private final ExchangeRateRepository exchangeRateRepository;

    public HomeBankApplication(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(HomeBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Populating exchange rates...");
        ExchangeRate exchangeRate = new ExchangeRate(1,"ron","eur",0.2);
        ExchangeRate exchangeRate1 = new ExchangeRate(2,"eur","ron",4.96);
        ExchangeRate exchangeRate2 = new ExchangeRate(3,"eur","eur",1);
        ExchangeRate exchangeRate3 = new ExchangeRate(4,"ron","ron",1);
        ExchangeRate exchangeRate4 = new ExchangeRate(5,"ron","gbp",0.17);
        ExchangeRate exchangeRate5 = new ExchangeRate(6,"gbp","ron",5.81);
        ExchangeRate exchangeRate6 = new ExchangeRate(7,"gbp","gbp",1);

        exchangeRateRepository.save(exchangeRate);
        exchangeRateRepository.save(exchangeRate1);
        exchangeRateRepository.save(exchangeRate2);
        exchangeRateRepository.save(exchangeRate3);
        exchangeRateRepository.save(exchangeRate4);
        exchangeRateRepository.save(exchangeRate5);
        exchangeRateRepository.save(exchangeRate6);

    }
}
