package com.example.demo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ExternalAPICaller {

    private int counter = 0;

    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "fallback")
    public String callApi() {
        var response = "{\n" +
                "    \"Status\": \"SUCCESS\",\n" +
                "    \"WaitTimeInSeconds\": %d"+
                "}";
        Random random = new Random();
        int randomValue = random.nextInt(5) + 1;

        try
        {
            if (counter >= 0 && counter < 20){
                randomValue = 3;
            } else if(counter >= 20 && counter < 30){
                randomValue = 1;
            } else {
                randomValue = 1;
            }
//            randomValue = counter % 6;
//            randomValue = 3;
            TimeUnit.SECONDS.sleep(3);
//            TimeUnit.SECONDS.sleep(1);
            counter++;
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return String.format(response, randomValue);
    }

    public String fallback(Exception e) throws TimeoutException {
        e.printStackTrace();
        try
        {
            TimeUnit.SECONDS.sleep(1);
        } catch (final InterruptedException e2) {
            Thread.currentThread().interrupt();
        }

        throw new TimeoutException();
    }
}
