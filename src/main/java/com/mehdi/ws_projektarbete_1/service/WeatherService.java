package com.mehdi.ws_projektarbete_1.service;

import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class WeatherService {

    private final WebClient webClient;
    private final WeatherRepository weatherRepository;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public WeatherService(WebClient.Builder webClientBuilder, WeatherRepository weatherRepository) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.weatherRepository = weatherRepository;
    }
    public Mono<Weather> fetchCurrentWeather(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric") // Optional => "metric" for Celsius
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    // Här bör du bearbeta JSON-svaret
                    // Detaljerad parsing och mapping beroende på API-svaret
                    Weather weather = new Weather();
                    weather.setCityName(city);
                    weather.setTemperature(15.0); // Exempelvärde, ersätt med faktisk data
                    weather.setHumidity(80); // Exempelvärde, ersätt med faktisk data
                    weather.setWeatherDescription("Clear sky"); // Exempelvärde, ersätt med faktisk data
                    weather.setTimestamp(LocalDateTime.now());
                    return weatherRepository.save(weather);
                });
    }


    }








