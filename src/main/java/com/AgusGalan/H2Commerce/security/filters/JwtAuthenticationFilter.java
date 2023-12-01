package com.AgusGalan.H2Commerce.security.filters;

import com.AgusGalan.H2Commerce.persistence.entities.UserEntity;
import com.AgusGalan.H2Commerce.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter { // SE EXTIENDE DE ESTA CLASE QUE TIENE UN /LOGIN POR DEFAULT

    private JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UserEntity userEntity = null;
        String username = "";
        String password = "";

        try{
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class); // lee el cuerpo de la solicitud Http y mapea los datos a un objeto UserEntity
            username = userEntity.getUsername(); // se obtiene el usuario y contraseña
            password = userEntity.getPassword();

        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // con las credenciales obtenidas crea este objeto que representa la autenticacion basada en el nombre de usuario y contraseña
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal(); // se obtienen los datos del usuario
        String token = jwtUtils.generateAccessToken(user.getUsername()); // se genera token de acceso

        response.addHeader("Authorization", token); // agrega el token al encabezado http Authorization

        Map<String, Object> httpResponse = new HashMap<>(); // crea un Map con detalles del token
        httpResponse.put("token", token);
        httpResponse.put("message", "Autenticacion correcta");
        httpResponse.put("Username", user.getUsername());

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse)); // se pasa la respueta a JSON y demas
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult); // se encarga de la respuesta si es que fue valida
    }
}
