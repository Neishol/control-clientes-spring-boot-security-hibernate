package com.example.HolaSpring_1.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build, @Lazy BCryptPasswordEncoder passwordEncoder) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    //Estandar
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authorize -> authorize
//                .anyRequest().authenticated())
//            .httpBasic(withDefaults());
//        return http.build();
//    }
    //Configuracion propia
    //No olvidar usar anotaciones @PreAuthorize("hasRole('ADMIN')") y agregarlas
    //al servlet en los path pra agregar esa restriccion
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/resources/**", "/static/**", "/public/**", "/webui/**", "/h2-console/**", "/api/**", "/errores/**")
                .permitAll()
                .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .permitAll())
                .logout(logout -> logout
                .logoutSuccessUrl("/login"))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/errores/403"));

    // Configuración moderna de excepciones con manejo de 'lang' en la URL
    http.exceptionHandling(exception -> 
        exception.authenticationEntryPoint((request, response, authException) -> {
            // Obtener el parámetro 'lang' si existe en la URL
            String lang = request.getParameter("lang");
            if (lang != null && !lang.isEmpty()) {
                // Redirigir al login manteniendo el parámetro 'lang'
                response.sendRedirect("/login?lang=" + lang);
            } else {
                // Si no hay parámetro 'lang', redirigir normalmente
                response.sendRedirect("/login");
            }
        })
        .accessDeniedPage("/errores/403")  // Página personalizada de acceso denegado
    );

    return http.build();
}
}
