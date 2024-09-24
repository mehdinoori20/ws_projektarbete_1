package com.mehdi.ws_projektarbete_1.controller;


import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<Weather> getCurrentWeather(@RequestParam String city) {
        try {
            Weather weather = weatherService.fetchCurrentWeather(city);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {
        try {
            Weather savedWeather = weatherService.saveWeather(weather);
            return ResponseEntity.ok(savedWeather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}

