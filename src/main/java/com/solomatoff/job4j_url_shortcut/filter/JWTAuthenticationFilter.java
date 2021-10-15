package com.solomatoff.job4j_url_shortcut.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solomatoff.job4j_url_shortcut.dto.UserDTO;
import com.solomatoff.job4j_url_shortcut.jwt.TokenAuthenticationService;
import com.solomatoff.job4j_url_shortcut.model.Site;
import com.solomatoff.job4j_url_shortcut.service.ISiteService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.solomatoff.job4j_url_shortcut.jwt.TokenAuthenticationService.HEADER_STRING;
import static com.solomatoff.job4j_url_shortcut.jwt.TokenAuthenticationService.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final ISiteService siteService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   TokenAuthenticationService tokenAuthenticationService, ISiteService siteService) {
        this.authenticationManager = authenticationManager;
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.siteService = siteService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            UserDTO.Request.User user = new ObjectMapper()
                    .readValue(req.getInputStream(), UserDTO.Request.User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        String token = tokenAuthenticationService.generateToken(authentication);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        // Сохраним созданный token в таблицу site (для удобства)
        String userName = ((UserDetails) authentication.getPrincipal()).getUsername();
        var optionalSite = siteService.findByLogin(userName);
        if (optionalSite.isPresent()) {
            Site site = optionalSite.get();
            site.setCurrentToken(TOKEN_PREFIX + token);
            siteService.saveOrUpdate(site);
        }
    }

}