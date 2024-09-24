package com.mehdi.ws_projektarbete_1.controller;


import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


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
    @PutMapping("/{id}")
    public ResponseEntity<Weather> updateWeather(@PathVariable Long id, @RequestBody Weather weather) {
        try {
            Weather updatedWeather = weatherService.updateWeather(id, weather);
            return ResponseEntity.ok(updatedWeather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeather(@PathVariable Long id) {
        try {
            weatherService.deleteWeather(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history/{city}/{date}")
    public ResponseEntity<Optional<Weather>> getHistoricalWeather(@PathVariable String city, @PathVariable String date) {
        try {
            Optional<Weather> weather = weatherService.getHistoricalWeather(city, date);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}

