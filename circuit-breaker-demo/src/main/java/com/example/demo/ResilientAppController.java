package com.example.demo;

//import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/")
public class ResilientAppController {
    @Autowired
    private ExternalAPICaller externalAPICaller;

    @GetMapping("/circuit-breaker")
    public String circuitBreakerApi() {
        return externalAPICaller.callApi();
    }

//    @GetMapping("/time-limiter")
//    @TimeLimiter(name = "timeLimiterApi")
//    public CompletableFuture<String> timeLimiterApi() {
//        return CompletableFuture.supplyAsync(externalAPICaller::callApi);
//    }
}
