package com.AgusGalan.H2Commerce.security;

import com.AgusGalan.H2Commerce.security.filters.JwtAuthenticationFilter;
import com.AgusGalan.H2Commerce.security.filters.JwtAuthorizationFilter;
import com.AgusGalan.H2Commerce.security.jwt.JwtUtils;
import com.AgusGalan.H2Commerce.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // INDICA QUE LA CLASE CONTIENE METODOS ANOTADOS CON @BEAN
@EnableGlobalMethodSecurity(prePostEnabled = true) // PARA UTILIZAR @PreAuthorize y demás
public class SecurityConfig {

    @Autowired // Sirve para la inyección automatica de dependencias en esta clase de la API
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    @Bean // CONFIGURACION ACCESO A ENDPOINTS. CONFIGURAR LA CADENA DE FILTROS DE SEGURIDAD PARA GESTIONAR LAS SOLICITUDES HTTP ENTRANTES
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);


        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/hello").permitAll();
                    auth.requestMatchers("/users").permitAll();
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers("/usersWeb").permitAll();
                    auth.requestMatchers("/users").permitAll();
                    auth.requestMatchers("/categorias.js").permitAll();
                    auth.requestMatchers("/productos.js").permitAll();
                    auth.requestMatchers("/pedidos.js").permitAll();
                    auth.requestMatchers("/scripts.js").permitAll();
                    auth.requestMatchers("/users.js").permitAll();
                    auth.requestMatchers("/styles.css").permitAll();
                    auth.requestMatchers("/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**").permitAll();
                    auth.requestMatchers("/pedidosWeb").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/pedidos/todos").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/pedidos/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/pedidos/guardar").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/pedidos/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/pedidos/").hasRole("ADMIN");
                    auth.requestMatchers("/categoriasWeb").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/categorias/todos").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/categorias/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/categorias/guardar").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/categorias/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/categorias/").hasRole("ADMIN");
                    auth.requestMatchers("/productosWeb").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/productos/todos").permitAll();
                    auth.requestMatchers(HttpMethod.GET,"/productos/{id}").permitAll();
                    auth.requestMatchers(HttpMethod.POST, "/productos/guardar").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/productos/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/productos/").hasRole("ADMIN");
                    auth.requestMatchers("/createUser").hasRole("ADMIN");
                    auth.requestMatchers("/deleteUser").hasRole("ADMIN");
                    auth.requestMatchers("/accesoAdmin").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class) // se ejecuta primero que esta clase
                .build();
    }

    /*
    @Bean // USUARIO EN MEMORIA
    UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("agus")
                .password("1234")
                .roles()
                .build());

        return manager;
    }
*/

    @Bean // ENCRIPTACION CONTRASEÑA
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean // Crea un bean para administrar la autenticación en la API
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

}
