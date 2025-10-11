package com.esprit.artdigital_backend.config;

import com.esprit.artdigital_backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints - public
                        .requestMatchers("/api/auth/**").permitAll()

                        // Oeuvres - GET public, POST/PUT/DELETE protégé (sauf avis)
                        .requestMatchers(HttpMethod.POST, "/api/oeuvres/*/avis").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/api/oeuvres/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/oeuvres/**").hasAnyRole("ARTISTE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/oeuvres/**").hasAnyRole("ARTISTE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/oeuvres/**").hasAnyRole("ARTISTE", "ADMIN")

                        // Catégories - GET public, autres protégés
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")

                        // Bannières - GET public, autres protégés
                        .requestMatchers(HttpMethod.GET, "/api/bannieres/actives").permitAll()
                        .requestMatchers("/api/bannieres/**").hasRole("ADMIN")

                        // Packs - GET public, autres protégés
                        .requestMatchers(HttpMethod.GET, "/api/packs/**").permitAll()
                        .requestMatchers("/api/packs/**").hasRole("ADMIN")

                        // Variantes - GET public, autres protégés
                        .requestMatchers(HttpMethod.GET, "/api/variantes/**").permitAll()
                        .requestMatchers("/api/variantes/**").hasAnyRole("ADMIN", "ARTISTE")

                        // Designs - Consultation pour tous, gestion admin
                        .requestMatchers(HttpMethod.GET, "/api/designs/**").permitAll()
                        .requestMatchers("/api/designs/**").hasRole("ADMIN")

                        // Frais de livraison - Consultation pour tous, gestion admin
                        .requestMatchers(HttpMethod.GET, "/api/frais-livraison/**").permitAll()
                        .requestMatchers("/api/frais-livraison/**").hasRole("ADMIN")

                        // Commandes - Authentifié requis (gestion dans controller)
                        .requestMatchers("/api/commandes/**").authenticated()

                        // Produits personnalisés - Authentifié requis (gestion dans controller)
                        .requestMatchers("/api/produits-personnalises/**").authenticated()

                        // Utilisateurs - Authentifié requis (gestion dans controller)
                        .requestMatchers("/api/utilisateurs/**").authenticated()

                        // Files - GET public, POST/DELETE protégé
                        .requestMatchers("/api/files/**").permitAll()

                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}