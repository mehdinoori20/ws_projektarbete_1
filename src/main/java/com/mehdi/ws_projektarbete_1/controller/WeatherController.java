package com.mehdi.ws_projektarbete_1.controller;

import com.mehdi.ws_projektarbete_1.exception.CityNotFoundException;
import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // HTML-sida för att visa väder
    @GetMapping
    public String showWeatherPage(@RequestParam(defaultValue = "Stockholm") String city, Model model) {
        // Validera att användaren har angett en giltig stad
        if (city == null || city.trim().isEmpty()) {
            model.addAttribute("error", "Vänligen ange en stad.");
            return "weather"; // Återvänd till väder-sidan med felmeddelande
        }

        try {
            // Hämta aktuellt väder och lägg till modellen
            Weather weather = weatherService.fetchCurrentWeather(city);
            model.addAttribute("weather", weather);
            return "weather"; // Renderar weather.html
        } catch (CityNotFoundException e) {
            // Felhantering om staden inte finns
            model.addAttribute("error", "Staden kunde inte hittas. Kontrollera att du angivit rätt namn.");
            return "weather"; // Visa väder-sidan även vid fel
        } catch (Exception e) {
            // Allmän felhantering om något annat går fel
            model.addAttribute("error", "Kunde inte hämta väderdata: " + e.getMessage());
            return "weather"; // Visa väder-sidan även vid fel
        }
    }

    // API: Hämta aktuellt väder som JSON
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Weather> getCurrentWeather(@RequestParam String city) {
        // Validering => att användaren har angett en giltig stad
        if (city == null || city.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Om stad är tom eller ogiltig
        }

        try {
            Weather weather = weatherService.fetchCurrentWeather(city);
            return ResponseEntity.ok(weather);
        } catch (CityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Staden kunde inte hittas
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Allmän felhantering
        }
    }

    // API: Skapa väderdata
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {
        try {
            Weather savedWeather = weatherService.saveWeather(weather);
            return ResponseEntity.ok(savedWeather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Om något går fel vid skapandet
        }
    }

    // API: Uppdatera väderdata
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Weather> updateWeather(@PathVariable Long id, @RequestBody Weather weather) {
        try {
            Weather updatedWeather = weatherService.updateWeather(id, weather);
            return ResponseEntity.ok(updatedWeather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Om något går fel vid uppdateringen
        }
    }

    // API: Radera väderdata
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteWeather(@PathVariable Long id) {
        try {
            weatherService.deleteWeather(id);
            return ResponseEntity.noContent().build(); // Radering lyckades
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Om väderdata inte hittas för radering
        }
    }

    // API: Hämta historiskt väder som JSON
    @GetMapping("/api/history/{city}/{date}")
    @ResponseBody
    public ResponseEntity<Optional<Weather>> getHistoricalWeather(@PathVariable String city, @PathVariable String date) {
        // Validera att användaren har angett en giltig stad
        if (city == null || city.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Om stad är tom eller ogiltig
        }

        try {
            Optional<Weather> weather = weatherService.getHistoricalWeather(city, date);
            return ResponseEntity.ok(weather);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Allmän felhantering vid historisk data
        }
    }
}
