package com.mehdi.ws_projektarbete_1.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
//   TODO: Hasha lösenord ist spring security
// - Om användaren anger felaktig lösenord då ska synas röd text som varnar för fel username/lösenord
// - Ist man matar in "?city=Uppsala" i webben när man är inloggad för att se uppsalas väder ist skapa en sökruta
// - Om man stavar en stad eller ett land fel då kommer upp ett meddelande att tex rätta till ....
// - Ändra fr kelvin till celsius
// - Skapa enhetstest för WeatherController klass
// - CityNotFoundException
// - Skapa WeatherResponse och metod samt getter och setter mm i WeatherService klassen för att mappa JSON-svaret från API:et till Java-objekt
// - Passande css till inloggning, utloggningssida
// -Application properties (syns lös?)


@Configuration
public class  SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // (Om du vill testa utan CSRF)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/styles.css").permitAll()  // Tillåt login och styling
                        .anyRequest().authenticated()  // Skydda allt annat
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Använd egen login-sida
                        .defaultSuccessUrl("/v1/weather", true)  // Vid lyckad inloggning
                        .failureUrl("/login?error=true")  // Om inloggningen misslyckas, omdirigera till login med fel
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Standard logout
                        .logoutSuccessUrl("/login")// Tillbaka till login-sidan efter logout
                        .permitAll()
                );

        return http.build();
    }
    // Detta kod hashar lösenordet fr spring security
    // Kristoffer om du avkommenterar 2 @Bean fr rad 44 till 58 så kommer koden ovanför köras alltså spring security. jag skapade koden på så sätt så attbåda har jag tillgängligt.

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123")) // Hashar lösenordet
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Använder BCrypt för säker hashning
    }


}